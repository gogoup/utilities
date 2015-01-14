
package org.gogoup.utilities.misc;

public class NoSuchEntityFoundException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -5676880192596730035L;
    
    private Class<?> clazz;
    private String[] ids;
    
    public NoSuchEntityFoundException(Class<?> clazz, String id, String message) {
        this(clazz, new String[]{id}, message);
    }
    
    public NoSuchEntityFoundException(Class<?> clazz, String[] ids, String message) {
        super(message);
        this.clazz = clazz;
        this.ids = ids;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public String[] getIds() {
        return ids;
    }

}
