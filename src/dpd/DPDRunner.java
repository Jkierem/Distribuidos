package dpd;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import dpd.services.DPDRegister;
import dpd.services.DPDService;
import shared.model.ServerReference;

public class DPDRunner {

	public static void main(String[] args) {
		int registryPort = 7777;
		int servicePort = 7778;
		BlockingQueue<ServerReference> proxyList = new LinkedBlockingQueue<>(10);
		DPDRegister<BlockingQueue<ServerReference>> registry = new DPDRegister<>("Registry", registryPort, proxyList, true);
		DPDService<BlockingQueue<ServerReference>> service = new DPDService<>("Service" , servicePort, proxyList, true);
		registry.start();
		service.start();
	}

}
