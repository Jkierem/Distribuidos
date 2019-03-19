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
			has = has || voter.equals(name);
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
				break;
			case MEDIUM:
				result = true;
				this.state[1]++;
				break;
			case HIGH:
				result = true;
				this.state[2]++;
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

}
