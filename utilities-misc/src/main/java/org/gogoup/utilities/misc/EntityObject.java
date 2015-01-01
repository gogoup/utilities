package org.gogoup.utilities.misc;

public abstract class EntityObject<T> extends DirtyMark {
    
    private T objectSegment;
    
    public EntityObject(T objectSegment) {
        this.objectSegment = objectSegment;
    }

    public T getObjectSegment() {
        return objectSegment;
    }
    
    public void checkForTheSameParent(Object entity) {
        if (this != entity) {
            throw new NotTheSameParentEntityException(this, entity);
        }
    }
    
}
