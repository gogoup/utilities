package org.gogoup.utilities.misc;

/**
 * Created by ruisun on 2016-02-15.
 */
public interface TransactionManager {

    public void register(TransactionalService service);
    
    public TransactionalService deregister(String name);

    public void startTransaction();

    public void commit();

    public void rollback();

}
