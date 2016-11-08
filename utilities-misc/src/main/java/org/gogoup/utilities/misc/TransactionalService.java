package org.gogoup.utilities.misc;

/**
 * Created by ruisun on 2015-11-08.
 */
public interface TransactionalService {

    public String getName();

    public void startTransaction(TransactionContext context, TransactionState state) throws TransactionalServiceException;

    public void commit(TransactionContext context, TransactionState state) throws TransactionalServiceException;

    public void rollback(TransactionContext context, TransactionState state) throws TransactionalServiceException;

    public void onError(TransactionContext context, TransactionState state, Exception exception);

}
