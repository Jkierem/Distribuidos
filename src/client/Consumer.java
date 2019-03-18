package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import shared.functional.Effect;
import shared.functional.Transition;
import shared.logic.AbstractClient;
import shared.model.Credentials;
import shared.model.Result;
import shared.model.ServerReference;
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
		if( info.equals(this.auth) ) {
			this.onConnectToAuth(socket);
		} else if( info.equals(this.dpd) ) {
			this.onConnectToDPD(socket);
		} else if( info.equals(this.proxy) ) {
			this.onConnectToProxy(socket);
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
	
	public void onConnectToProxy(Socket socket) {
		this.logger.log(this.userId);
		//TODO: Implement this
	}

}
