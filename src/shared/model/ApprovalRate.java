package shared.model;

public enum ApprovalRate {
	NONE(0),
	HIGH(1),
	MEDIUM(2),
	LOW(3);
	
	private int value;
	
	private ApprovalRate(int value) {
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
	
	@Override
	public String toString() {
		return this.name();
	}
}
