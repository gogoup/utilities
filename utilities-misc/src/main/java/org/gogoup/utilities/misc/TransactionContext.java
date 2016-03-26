package org.gogoup.utilities.misc;

/**
 * Created by ruisun on 2016-01-04.
 */
public interface TransactionContext {

    public void setProperty(String name, Object property);

    public Object getProperty(String name);

    public Object removeProperty(String name);

}
