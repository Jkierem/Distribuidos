package server;

import java.time.Duration;
import java.time.Instant;
import java.util.Vector;
import java.util.concurrent.LinkedBlockingQueue;

import shared.model.Proposal;
import shared.utils.ConditionalLogger;

public class PetitionDiscoveryService extends Thread {
	
	private LinkedBlockingQueue<Proposal> waitList;
	private Vector<Proposal> activeProposals;
	private ConditionalLogger logger;
	private boolean open;
	private Instant before;
	
	public PetitionDiscoveryService(LinkedBlockingQueue<Proposal> waitList, Vector<Proposal> activeProposals) {
		this.waitList = waitList;
		this.activeProposals = activeProposals;
		this.before = Instant.now();
		this.logger = new ConditionalLogger(true,"PetitionService: ");
		this.open = true;
	}
	
	@Override
	public void run() {
		while(this.open) {
			Instant after = Instant.now();
			long ellapsed = Duration.between(before, after).toMinutes();
			if( ellapsed >= 1 ) {
				try {
					if( !this.waitList.isEmpty() ) {						
						Proposal p = this.waitList.take();
						this.logger.log("Adding "+p.getName()+" petition...");
						this.activeProposals.add(p);
						this.before = after;
					} else {
						this.close();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public void close() {
		this.open = false;
	}

}
