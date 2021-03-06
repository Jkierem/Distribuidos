package shared.model;

public class Vote {
	
	private String id;
	private String topic;
	private ApprovalRate approvalRate;

	public Vote( String id, String topic, ApprovalRate ar) {
		this.id = id;
		this.topic = topic;
		this.approvalRate = ar;
	}
	
	public String toCSVString() {
		return this.id + "," + this.topic + "," + this.approvalRate.toString();
	}
	
	public static Vote fromCSVString(String csv) {
		String[] tokens = csv.split(",");
		if( tokens.length != 3 ) {
			return Vote.empty();
		}
		return new Vote( tokens[0] , tokens[1] , ApprovalRate.valueOf(tokens[2]) );
	}
	
	public static Vote empty() {
		return new Vote("", "", ApprovalRate.NONE);
	}
	
	public String toVoteMessage() {
		return Operation.VOTE + ":" + this.toCSVString();
	}

	public String getId() {
		return id;
	}

	public String getTopic() {
		return topic;
	}

	public ApprovalRate getApprovalRate() {
		return approvalRate;
	}
	
	public int compare(Vote v) {
		return id.compareTo(v.id) + topic.compareTo(v.topic) + (approvalRate.getValue() - v.approvalRate.getValue());
	}
	
	public boolean equals(Vote v) {
		return this.compare(v) == 0;
	}
	
	public boolean isEmpty() {
		return this.equals(Vote.empty());
	}
}
