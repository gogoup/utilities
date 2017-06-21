package org.gogoup.utilities.misc;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by ruisun on 2016-10-29.
 */
public class JDBCConnectionHelper {

    private DataSource dataSource;
    private Connection connection; //cache connection as local transaction started.
    private boolean isTransactionStarted;

    public JDBCConnectionHelper(DataSource dataSource) {
        this.dataSource = dataSource;
        this.connection = null;
    }

    public boolean isTransactionStarted() {
        return isTransactionStarted;
    }

    public Connection getConnection() {
        try {
            if (isTransactionStarted()) {
                return getTransactionalConnection();
            }
            return getNormalConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private Connection getTransactionalConnection() throws SQLException {
        if (null == connection) {
            connection = dataSource.getConnection();
            connection.setAutoCommit(false);
        }
        return connection;
    }

    private Connection getNormalConnection() throws SQLException {
        Connection conn =  dataSource.getConnection();
        conn.setAutoCommit(true);
        return conn;
    }

    private void checkForTransactionNotStarted() {
        if (!isTransactionStarted()) {
            throw new IllegalStateException("Need to start a transaction first!");
        }
    }

    public void startTransaction() {
        isTransactionStarted = true;
    }

    public void commit() {
        checkForTransactionNotStarted();
        try {
            connection.commit();
            cleanLocalTransaction();
        } catch (SQLException e) {
            //TODO: log this error
            throw new RuntimeException(e);
        }
    }

    public void rollback() {
        checkForTransactionNotStarted();
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
        isTransactionStarted = false;
    }

    public void closeResultSet(ResultSet rs) {
        try {
            if (null != rs) {
                rs.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closePreparedStatement(PreparedStatement stmt) {
        try {
            if (null != stmt) {
                stmt.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection(Connection conn) {
        if (!isTransactionStarted()) {
            try {
                conn.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
