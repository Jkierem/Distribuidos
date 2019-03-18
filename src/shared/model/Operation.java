package shared.model;

import java.util.HashMap;

public enum Operation {
	AUTH("AUTH"),
	VOTE("VOTE"),
	PING("PING"),
	LIST("LIST");
	
	private String value;
	 
    private Operation(String value) {
        this.value = value;
    }
 
    public String toString() {
        return value;
    }
    
    private static final HashMap<String, Operation> lookup = new HashMap<>();
    
    static
    {
        for(Operation res : Operation.values())
        {
            lookup.put(res.toString(), res);
        }
    }
    
    public static Operation fromString(String value) {
    	return lookup.get(value);
    }
}
