package org.gogoup.utilities.misc;

public class NotTheSameParentEntityException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = -4626514216819032561L;
    
    private Object thisEntity;
    private Object thatEntity;
    
    public NotTheSameParentEntityException(Object thisEntity, Object thatEntity) {
        this.thisEntity = thisEntity;
        this.thatEntity = thatEntity;
    }

    public Object getThisEntity() {
        return thisEntity;
    }

    public Object getThatEntity() {
        return thatEntity;
    }

}
