package org.gogoup.utilities.misc;

/**
 * Created by ruisun on 2016-02-15.
 */
public interface TransactionManager {

    public void register(TransactionalService service);
    
    public TransactionalService deregister(String name);

    public void startTransaction() throws TransactionalServiceException;

    public void commit() throws TransactionalServiceException;

    public void rollback() throws TransactionalServiceException;

}
