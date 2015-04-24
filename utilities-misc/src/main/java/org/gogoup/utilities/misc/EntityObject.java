package org.gogoup.utilities.misc;

public abstract class EntityObject<T> extends DirtyMark {
    
    private T objectSegment;
    private T dirtyObjectSegment;
    
    public EntityObject(T objectSegment) {
        this.objectSegment = objectSegment;
    }

    protected T getObjectSegment() {
        return objectSegment;
    }
    
}
