
package org.gogoup.utilities.misc;

public class NoSuchEntityFoundException extends RuntimeException {

    private String entityName;
    private String[] ids;
    
    public NoSuchEntityFoundException(Class<?> clazz, String id, String message) {
        this(clazz, new String[]{id}, message);
    }
    
    public NoSuchEntityFoundException(Class<?> clazz, String[] ids, String message) {
        super(message);
        this.entityName = clazz.getName();
        this.ids = ids;
    }

    public String getEntityName() {
        return entityName;
    }

    public String[] getIds() {
        return ids;
    }

}
