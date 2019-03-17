package shared.model;

import java.util.HashMap;

public enum Result {
	SUCCESS("Success"),
	FAILURE("Failure");
	
	private String value;
	 
    Result(String value) {
        this.value = value;
    }
 
    public String toString() {
        return value;
    }
    
    private static final HashMap<String, Result> lookup = new HashMap<>();
    
    static
    {
        for(Result res : Result.values())
        {
            lookup.put(res.toString(), res);
        }
    }
    
    public static Result fromString(String value) {
    	return lookup.get(value);
    }
}
