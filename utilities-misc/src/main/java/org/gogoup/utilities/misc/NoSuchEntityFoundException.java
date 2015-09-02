
package org.gogoup.utilities.misc;

public class NoSuchEntityFoundException extends RuntimeException {

    private String entityName;
    private String[] ids;

    public NoSuchEntityFoundException(Class<?> clazz, String message, String... ids) {
        this(clazz.getName(), message, ids);
    }

    public NoSuchEntityFoundException(String entityName, String message, String... ids) {
        super(message);
        this.entityName = entityName;
        this.ids = ids;
    }

    public String getEntityName() {
        return entityName;
    }

    public String[] getIds() {
        return ids;
    }

}
