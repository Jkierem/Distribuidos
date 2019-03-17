package auth;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

import shared.logic.AbstractConnection;
import shared.model.Credentials;
import shared.model.Result;
import shared.utils.ConditionalLogger;

public class AuthConnection extends AbstractConnection {
	
	private ConcurrentHashMap<String, String> userList;
	private ConditionalLogger logger;
	
	public AuthConnection(Socket socket , ConcurrentHashMap<String, String> userList, ConditionalLogger logger) {
		super(socket);
		this.userList = userList;
		this.logger = logger;
	}
	
	@Override
	public void onReceive(String msg, DataOutputStream out) {
		Result result = null;
		Credentials creds = Credentials.fromCSVString(msg);
		String user = creds.getUser();
		String password = creds.getPassword();
		if( this.userList.containsKey(user) ) {
			if( this.userList.get(user).compareTo(password) == 0 ) {
				result = Result.SUCCESS;
			} else {
				this.logger.log("Attemptedto login with '"+user+"' Password does not match");
				result = Result.FAILURE;
			}
		} else {
			this.logger.log(user + " does not exist");
			result = Result.FAILURE;
		}
		try {
			out.writeUTF(result.toString());
			if( result == Result.SUCCESS ) {
				this.logger.log("Succesful login with "+user+"!");
				super.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
