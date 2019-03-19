package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import shared.functional.Effect;
import shared.functional.Transition;
import shared.logic.AbstractClient;
import shared.model.ApprovalRate;
import shared.model.Credentials;
import shared.model.Operation;
import shared.model.Result;
import shared.model.ServerReference;
import shared.model.Vote;
import shared.utils.ConditionalLogger;

public class Consumer extends AbstractClient {
	
	private enum State {
		INITIAL,
		ASKING_FOR_PROXY,
		VOTING,
		END;
	}
	
	private State state;
	private Scanner input;
	private ServerReference auth;
	private ServerReference dpd;
	private ServerReference proxy;
	private ConditionalLogger logger;
	private boolean authenticated;
	private boolean dpdConnect;
	private boolean proxyConnect;
	private int retries;
	private int maxRetries;
	private String userId;

	public Consumer(Integer name , ServerReference auth, ServerReference dpd, Scanner input) {
		super();
		this.auth = auth;
		this.dpd = dpd;
		this.proxy = ServerReference.empty();
		this.logger = new ConditionalLogger(true,"From Client " + name.toString() + ": ");
		this.input = input;
		this.authenticated = false;
		this.dpdConnect = false;
		this.proxyConnect = false;
		this.maxRetries = 10;
		this.retries = this.maxRetries;
		this.state = State.INITIAL;
		this.userId = "";
	}
	
	public Consumer setAuth(ServerReference auth) {
		this.auth = auth;
		return this;
	}

	public Consumer setDpd(ServerReference dpd) {
		this.dpd = dpd;
		return this;
	}
	
	public void checkRetries() {
		if( this.retries <= 0) {
			this.logger.log("Maximun amount of retries reached. Restart client.");
		}
	}
	
	public void resetCounter() {
		this.retries = this.maxRetries;
	}
	
	public Effect<Consumer> createStateChange( State nextState ) {
		return x -> {
			x.resetCounter();
			x.state = nextState;
		};
	}

	@Override
	public void onRun() {
		Transition<Consumer> retryFail = new Transition<>( x -> x.retries <= 0 );
		Transition<Consumer> success = new Transition<>( x -> true );
		Effect<Consumer> endExecution = x -> {
			x.logger.log("Could not connect. Please restart client.");
			x.state = State.END; 
		};
		
		Transition<Consumer> loginSuccess = success
				.setCondition( x -> x.authenticated == true )
				.setEffect(this.createStateChange(State.ASKING_FOR_PROXY));
		Transition<Consumer> loginFail = retryFail
				.setEffect(endExecution);
		
		Transition<Consumer> proxySuccess = success
				.setCondition( x -> x.dpdConnect == true )
				.setEffect(this.createStateChange(State.VOTING));
		Transition<Consumer> proxyFail = retryFail
				.setEffect(endExecution);
		
		Transition<Consumer> votingSuccess = success
				.setCondition( x -> x.proxyConnect == true )
				.setEffect(endExecution);
		Transition<Consumer> votingFail = retryFail
				.setEffect(this.createStateChange(State.ASKING_FOR_PROXY));
		
		while( this.state != State.END ) {
			switch (this.state) {
			case INITIAL:
				this.tryConnectWithRetry(this.auth, loginSuccess , loginFail );
				break;
			case ASKING_FOR_PROXY:
				this.tryConnectWithRetry(this.dpd, proxySuccess, proxyFail);
				break;
			case VOTING:
				this.tryConnectWithRetry(this.proxy, votingSuccess, votingFail );
				break;
			default:
				break;
			}
		}
	}
	
	private void tryConnectWithRetry( ServerReference server , Transition<Consumer> success, Transition<Consumer> failure ) {
		while( !success.holds(this) && !failure.holds(this) ) {
			this.tryConnect(server);
		}
		if( success.holds(this) ) {
			success.apply(this);
		} else {
			failure.apply(this);
		}
	}
	
	@Override
	public void onFailed( ServerReference server , IOException e) {
		this.retries--;
		String failed = "";
		if( server.equals(this.auth) ) {
			failed = "Authentication";
		} else if( server.equals(this.dpd) ) {
			failed = "Distributed Proxy Directory";
		} else if( server.equals(this.proxy) ) {
			failed = "Proxy";
		}
		this.logger.log("Error on connecting to "+ failed + ":" + server.toCSVString());
	}
	
	@Override
	public void onConnect(Socket socket, ServerReference info) {
		try {
			if( info.equals(this.auth) ) {
				this.onConnectToAuth(socket);
			} else if( info.equals(this.dpd) ) {
				this.onConnectToDPD(socket);
			} else if( info.equals(this.proxy) ) {
				this.onConnectToProxy(socket);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Credentials askForCredentials() {
		this.logger.log("\nProvide user: ");
		String user = input.nextLine();
		this.logger.log("Provide password: ");
		String password = input.nextLine();
		return new Credentials()
				.setUser(user)
				.setPassword(password);
	}
	
	public void onConnectToAuth( Socket socket ) {
		DataInputStream in;
		DataOutputStream out;
		try {
			out = new DataOutputStream(socket.getOutputStream());
			Result result = null;
			Credentials creds = new Credentials();
			while( result != Result.SUCCESS ) {
				creds = this.askForCredentials();
				out.writeUTF(creds.toAuthMessage());
				in = new DataInputStream(socket.getInputStream());
				result = Result.fromString(in.readUTF());
				this.logger.log(result.toString());
			}
			this.userId = creds.getUser();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.authenticated = true;
	}
	
	public void onConnectToDPD(Socket socket) {
		DataInputStream in;
		try {
			in = new DataInputStream(socket.getInputStream());
			String info = in.readUTF();
			ServerReference proxyInfo = ServerReference.fromCSVString(info);
			this.logger.log(proxyInfo.toCSVString());
			this.proxy = proxyInfo;
			this.dpdConnect = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String askForPetition( String[] data ) {
		int chosen = -1;
		boolean chose = false;
		String pList = "";
		for( int i = 0 ; i< data.length ; i++ ) {
			pList += String.valueOf(i) + " - " + data[i] + "\n";
		}
		while( !chose ) {
			this.logger.log("Choose one of the following by the number: \n" + pList);
			String in = this.input.nextLine();
			int index = Integer.parseInt(in);
			boolean exists = index >= 0 && index < data.length;
			if( exists ) {
				chosen = index;
				chose = true;
			} else {
				this.logger.log("Please choose a valid one");
			}
		}
		return data[chosen];
	}
	
	public ApprovalRate askForApproval() {
		ApprovalRate res = ApprovalRate.NONE;
		boolean chosen = false;
		while(!chosen) {
			this.logger.log("Choose an approval rate from the number: \n"
					+ "0 - Low\n" + "1 - Medium\n" + "2 - High\n");
			String in = this.input.nextLine();
			int value = Integer.parseInt(in);
			switch (value) {
			case 0:
				res = ApprovalRate.LOW;
				chosen = true;
				break;
			case 1:
				res = ApprovalRate.MEDIUM;
				chosen = true;
				break;
			case 2:
				res = ApprovalRate.HIGH;
				chosen = true;
				break;
			default:
				this.logger.log("Choose a valid value\n");
				break;
			}
		}
		return res;
	}
	
	public void onConnectToProxy(Socket socket) {
		try {
			DataInputStream in = new DataInputStream(socket.getInputStream());
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeUTF(Operation.LIST.toString()+ ":please");
			String reply = in.readUTF();
			String[] tokens = reply.split(":");
			String[] data = tokens[1].split(",");
			String topic = this.askForPetition(data);
			ApprovalRate ar = this.askForApproval();
			Vote vote = new Vote(this.userId, topic, ar);
			out.writeUTF(vote.toVoteMessage());
			String res = in.readUTF();
			this.logger.log(res);
			this.state = State.END;
		} catch (IOException e) {
			this.state = State.ASKING_FOR_PROXY;
		}
	}

}
