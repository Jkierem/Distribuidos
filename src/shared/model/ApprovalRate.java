package shared.model;

public enum ApprovalRate {
	NONE,
	HIGH,
	MEDIUM,
	LOW;
	
	@Override
	public String toString() {
		return this.name();
	}
}
