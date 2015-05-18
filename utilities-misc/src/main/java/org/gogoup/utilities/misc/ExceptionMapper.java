package org.gogoup.utilities.misc;

public interface ExceptionMapper<T> {

    public T toResult(Throwable t);

}
