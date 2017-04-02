package org.gogoup.utilities.misc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ruisun on 2016-10-29.
 */
public abstract class JDBCTransactionalService implements TransactionalService {

    private static final String TRANSACTION_COUNT = "_TRANSACTION_COUNT_";
    private static final String TRANSACTION_COMMIT_COUNT = "_TRANSACTION_COMMIT_COUNT_";
    private static final String TRANSACTION_ROLLBACK_COUNT = "_TRANSACTION_ROLLBACK_COUNT_";
    private static final String TRANSACTION_CONNECTION = "_TRANSACTION_CONNECTION_";

    private String name;
    private DataSource dataSource;
    private Connection connection; //cache connection as local transaction started.
    private TransactionContext txContext;
    private boolean isLocalTransactionStarted;

    public JDBCTransactionalService(String name, DataSource dataSource) {
        this.name = name;
        this.dataSource = dataSource;
        this.connection = null;
        this.txContext = null;
        this.isLocalTransactionStarted = false;
    }

    private boolean isGlobalTransactionStarted() {
        return (txContext != null);
    }

    private boolean isLocalTransactionStarted() {
        return isLocalTransactionStarted;
    }

    protected Connection getConnection() {
        try {
            if (!isGlobalTransactionStarted()) {
                if (isLocalTransactionStarted()) {
                    return getLocalTransactionConnection();
                }
                return getNoneTransactionConnection();
            }
            return getGlobalTransactionConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getLocalTransactionConnection() throws SQLException {
        if (null == connection) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
        }
        return connection;
    }

    private Connection getNoneTransactionConnection() throws SQLException {
        Connection conn =  dataSource.getConnection();
        conn.setAutoCommit(true);
        return conn;
    }

    private Connection getGlobalTransactionConnection() throws SQLException {
        Connection conn = (Connection) txContext.getProperty(TRANSACTION_CONNECTION);
        if (null == conn) {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);
            txContext.setProperty(TRANSACTION_CONNECTION, conn);
        }
        return conn;
    }

    private void checkForGlobalTransactionNotStarted() {
        if (!isGlobalTransactionStarted()) {
            throw new IllegalStateException("Need to start a transaction first!");
        }
    }

    private void checkForLocalTransactionNotStarted() {
        if (!isLocalTransactionStarted()) {
            throw new IllegalStateException("Need to start a nested transaction first!");
        }
    }

    protected void startLocalTransaction() {
        isLocalTransactionStarted = true;
    }

    protected void commit() {
        checkForLocalTransactionNotStarted();
        if (isGlobalTransactionStarted()) {
            return;
        }
        try {
            connection.commit();
            cleanLocalTransaction();
        } catch (SQLException e) {
            //TODO: log this error
            throw new RuntimeException(e);
        }
    }

    protected void rollback() {
        checkForLocalTransactionNotStarted();
        if (isGlobalTransactionStarted()) {
            throw new RuntimeException("Require rollback!"); //trigger global transaction rollback.
        }
        try {
            connection.rollback();
            cleanLocalTransaction();
        } catch (SQLException e) {
            //TODO: log this error
            throw new RuntimeException(e);
        }
    }

    private void cleanLocalTransaction() throws SQLException {
        connection.close();
        connection = null;
        isLocalTransactionStarted = false;
    }

    protected void closeResultSet(ResultSet rs) {
        try {
            if (null != rs) {
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void closePreparedStatement(PreparedStatement stmt) {
        try {
            if (null != stmt) {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    protected void closeConnection(Connection conn) {
        if (!isGlobalTransactionStarted()
                && !isLocalTransactionStarted()) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void startTransaction(TransactionContext context, TransactionState transactionState)
            throws TransactionalServiceException {
        checkForLocalTransactionStarted();
        this.txContext = context;
        increaseTransactionCount();
    }

    private void checkForLocalTransactionStarted() {
        if (isLocalTransactionStarted()) {
            throw new IllegalStateException("Global transaction need started before a local transaction");
        }
    }

    private void increaseTransactionCount() {
        Integer count = (Integer) txContext.getProperty(TRANSACTION_COUNT);
        if (null == count) {
            count = 1;
        } else {
            count ++;
        }
        txContext.setProperty(TRANSACTION_COUNT, count);
    }

    private void markTransactionAsCommit() {
        Integer count = (Integer) txContext.getProperty(TRANSACTION_COMMIT_COUNT);
        if (null == count) {
            count = 1;
        } else {
            count ++;
        }
        txContext.setProperty(TRANSACTION_COMMIT_COUNT, count);
    }

    private void markTransactionAsRollback() {
        Integer count = (Integer) txContext.getProperty(TRANSACTION_ROLLBACK_COUNT);
        if (null == count) {
            count = 1;
        } else {
            count ++;
        }
        txContext.setProperty(TRANSACTION_ROLLBACK_COUNT, count);
    }

    private boolean isLastTransactionCall() {
        Integer count = (Integer) txContext.getProperty(TRANSACTION_COUNT);
        Integer commitCount = (Integer) txContext.getProperty(TRANSACTION_COMMIT_COUNT);
        Integer rollbackCount = (Integer) txContext.getProperty(TRANSACTION_ROLLBACK_COUNT);
        return ( count == (commitCount + 1) || count == (rollbackCount + 1) );
    }

    @Override
    public void commit(TransactionContext context, TransactionState transactionState)
            throws TransactionalServiceException {
        checkForGlobalTransactionNotStarted();
        try {
            if (isLastTransactionCall()) {
                Connection conn = getConnection();
                conn.commit();
                conn.close();
                txContext = null;
                markTransactionAsCommit();
            }
        } catch (SQLException e) {
            //TODO: log this error
            throw new TransactionalServiceException(e);
        }
    }

    @Override
    public void rollback(TransactionContext context, TransactionState transactionState)
            throws TransactionalServiceException {
        checkForGlobalTransactionNotStarted();
        try {
            if (isLastTransactionCall()) {
                Connection conn = getConnection();
                conn.rollback();
                conn.close();
                txContext = null;
                markTransactionAsRollback();
            }
        } catch (SQLException e) {
            //TODO: log this error
            throw new TransactionalServiceException(e);
        }
    }

    @Override
    public void onError(TransactionContext transactionContext, TransactionState transactionState, Exception e) {

    }

}
