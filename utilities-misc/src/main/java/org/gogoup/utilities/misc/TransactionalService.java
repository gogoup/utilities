package org.gogoup.utilities.misc;

/**
 * Created by ruisun on 2015-11-08.
 */
public interface TransactionalService {

    public String getName();

    public void startTransaction(TransactionState state);

    public void commit(TransactionState state);

    public void rollback(TransactionState state);

}
