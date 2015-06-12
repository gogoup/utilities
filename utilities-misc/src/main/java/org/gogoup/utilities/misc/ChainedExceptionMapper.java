package org.gogoup.utilities.misc;

public abstract class ChainedExceptionMapper<T> implements ExceptionMapper {

    private ChainedExceptionMapper<T> successor;

    public ChainedExceptionMapper(ChainedExceptionMapper successor) {
        this.successor = successor;
    }

    public T toResult(Throwable t) {
        if (isMatch(t)) {
            return getResult(t);
        }
        if (null == successor) {
            throw new NoExceptionMapperMatchException(t);
        }
        return successor.toResult(t);
    }

    protected abstract boolean isMatch(Throwable t);

    protected abstract T getResult(Throwable t);

}
