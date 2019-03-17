package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

import shared.logic.AbstractClient;
import shared.model.Credentials;
import shared.model.Result;
import shared.model.ServerReference;
import shared.utils.ConditionalLogger;

public class Consumer extends AbstractClient {
	
	private Scanner input;
	private ServerReference auth;
	private ServerReference dpd;
	private ServerReference proxy;
	private ConditionalLogger logger;

	public Consumer(Integer name , ServerReference auth, ServerReference dpd, Scanner input) {
		super();
		this.auth = auth;
		this.dpd = dpd;
		this.proxy = ServerReference.empty();
		this.logger = new ConditionalLogger(true,"From Client " + name.toString() + ": ");
		this.input = input;
	}
	
	public Consumer setAuth(ServerReference auth) {
		this.auth = auth;
		return this;
	}

	public Consumer setDpd(ServerReference dpd) {
		this.dpd = dpd;
		return this;
	}

	@Override
	public void onRun() {
		this.tryConnect(this.auth);
		this.tryConnect(this.dpd);
		if( !this.proxy.isNullRef() ) {
			this.tryConnect(this.proxy);
		} else {
			this.logger.log("Failed at receiving proxy server");
		}
	}
	
	@Override
	public void onFailed( ServerReference server , IOException e) {
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
			this.onConnectToDNS(socket);
		} else if( info.equals(this.proxy) ) {
			this.onConnectToProxy(socket);
		}
	}
	
	public Credentials askForCredentials() {
		this.logger.log("Provide user: ");
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
			while( result != Result.SUCCESS ) {				
				out.writeUTF(this.askForCredentials().toCSVString());
				in = new DataInputStream(socket.getInputStream());
				result = Result.fromString(in.readUTF());
				this.logger.log(result.toString());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public void onConnectToDNS(Socket socket) {
		DataInputStream in;
		try {
			in = new DataInputStream(socket.getInputStream());
			String info = in.readUTF();
			ServerReference proxyInfo = ServerReference.fromCSVString(info);
			this.logger.log(proxyInfo.toCSVString());
			this.proxy = proxyInfo;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onConnectToProxy(Socket socket) {
		
	}

}
