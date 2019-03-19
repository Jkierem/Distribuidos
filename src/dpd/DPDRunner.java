package dpd;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import dpd.services.DPDRegister;
import dpd.services.DPDService;
import shared.model.ServerReference;

public class DPDRunner {

	public static void main(String[] args) {
		if( args[0].equals("help") ) {
			System.out.println("Usage: DPD <proxy registry port> <service port>");
		} else {			
			//int registryPort = 7777;
			//int servicePort = 7778;
			int registryPort = Integer.parseInt(args[0]);
			int servicePort = Integer.parseInt(args[1]);
			BlockingQueue<ServerReference> proxyList = new LinkedBlockingQueue<>(10);
			DPDRegister<BlockingQueue<ServerReference>> registry = new DPDRegister<>("Registry", registryPort, proxyList, true);
			DPDService<BlockingQueue<ServerReference>> service = new DPDService<>("Service" , servicePort, proxyList, true);
			registry.start();
			service.start();
		}
	}

}
