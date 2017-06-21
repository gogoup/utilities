package org.gogoup.utilities.misc;

/**
 * Created by ruisun on 2015-11-08.
 */
public interface TransactionalService {

    String getName();

    void doStartTransaction(TransactionContext context, TransactionState state) throws TransactionalServiceException;

    void doCommit(TransactionContext context, TransactionState state) throws TransactionalServiceException;

    void doRollback(TransactionContext context, TransactionState state) throws TransactionalServiceException;

    void onError(TransactionContext context, TransactionState state, Exception exception);

}
