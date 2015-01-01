package org.gogoup.utilities.misc;

public class IllegalAttributeLengthException extends RuntimeException {
    
    /**
     * 
     */
    private static final long serialVersionUID = -162080378831391799L;
    
    private String name;
    private String attribute;
    private int minLength;
    private int maxLength;
    
    public IllegalAttributeLengthException(String name, String attribute, 
            int minLength, int maxLength, String message) {
        super(message);
        this.attribute = attribute;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public String getName() {
        return name;
    }

    public String getAttribute() {
        return attribute;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }
    
}

