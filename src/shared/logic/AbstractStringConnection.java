package shared.logic;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class AbstractStringConnection extends AbstractConnection<String> {

	public AbstractStringConnection(Socket socket) {
		super(socket);
	}

	@Override
	public String listen(DataInputStream in, DataOutputStream out) throws IOException {
		return in.readUTF();
	}

}
