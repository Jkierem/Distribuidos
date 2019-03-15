package dns;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import dns.services.DNSRegister;
import dns.services.DNSService;
import shared.model.ServerReference;

public class DNSRunner {

	public static void main(String[] args) {
		int registryPort = 7777;
		int servicePort = 7778;
		BlockingQueue<ServerReference> proxyList = new LinkedBlockingQueue<>(10);
		DNSRegister<BlockingQueue<ServerReference>> registry = new DNSRegister<>("Registry", registryPort, proxyList, true);
		DNSService<BlockingQueue<ServerReference>> service = new DNSService<>("Service" , servicePort, proxyList, true);
		registry.start();
		service.start();
	}

}
