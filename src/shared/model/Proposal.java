package shared.model;

import java.util.ArrayList;
import java.util.List;

public class Proposal {
	
	private String name;
	private List<String> voters;
	private int[] state;

	public Proposal(String name) {
		this.name = name;
		this.voters = new ArrayList<>();
		this.state = new int[3];
		for( int i = 0 ; i < 3 ; i++ ) {
			this.state[i] = 0;
		}
	}
	
	private int getState(int i) {
		return this.state[i];
	}
	
	private boolean hasVoted( String name ) {
		boolean has = false;
		for( String voter : this.voters ) {
			if( voter.compareTo(name) == 0) {
				has = true;
			}
		}
		return has;
	}
	
	public boolean receiveVote(Vote v) {
		boolean result = false;
		if( !v.isEmpty() && !this.hasVoted(v.getId()) ) {			
			switch (v.getApprovalRate()) {
			case LOW:
				result = true;
				this.state[0]++;
				this.voters.add(v.getId());
				break;
			case MEDIUM:
				result = true;
				this.state[1]++;
				this.voters.add(v.getId());
				break;
			case HIGH:
				result = true;
				this.state[2]++;
				this.voters.add(v.getId());
				break;
			default:
				break;
			}
		} 
		return result;
	}
	
	public int getHigh() {
		return this.getState(0);
	}
	
	public int getMedium() {
		return this.getState(1);
	}
	
	public int getLow() {
		return this.getState(2);
	}
	
	public String getName() {
		return this.name;
	}
	
	public String toString() {
		return this.name + ":\n" 
					+ " - Low: " + String.valueOf(this.getLow()) + "\n"
					+ " - Medium: " + String.valueOf(this.getMedium()) + "\n"
					+ " - High: " + String.valueOf(this.getHigh()) + "\n";
	}

}
