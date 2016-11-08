package org.gogoup.utilities.misc;

/**
 * Created by ruisun on 2016-11-08.
 */
public class TransactionalServiceException extends Exception {

    public TransactionalServiceException(String message) {
        super(message);
    }

    public TransactionalServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public TransactionalServiceException(Throwable cause) {
        super(cause);
    }
}
