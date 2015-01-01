package org.gogoup.utilities.misc;

public class IllegalAttributeFormatException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 2749001004004092287L;
    
    private String name;
    private String attribute;
    
    public IllegalAttributeFormatException(String name, String attribute, String message) {
        super(message);
        this.name = name;
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public String getAttribute() {
        return attribute;
    }
    
}

