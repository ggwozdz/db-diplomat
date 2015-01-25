package ggwozdz.dbdiplomat.rdbms;

import ggwozdz.dbdiplomat.rdbms.operations.DBOperation;
import ggwozdz.dbdiplomat.rdbms.operations.DBOperationException;

import java.sql.Connection;
import java.sql.SQLException;

public final class Transaction extends AbstractDBOperationHandler implements AutoCloseable{

	private final Connection connection;
	private boolean autoCommit;
	
	public Transaction(Connection connection) {
		this.connection = connection;
	}

	public Transaction begin() throws SQLException{
		this.autoCommit = this.connection.getAutoCommit();
		this.connection.setAutoCommit(false);
		return this;
	}
	
	@Override
	public <T> T execute(DBOperation<T> doOperation) throws DBOperationException{
		try {
			return doOperation.execute(connection);
		} catch (SQLException e) {
			throw new DBOperationException("Cannot execute DBOperation: "+e.getMessage(), e);
		}
	}
	
	@Override
	public void close() throws Exception {
		connection.commit();
		connection.setAutoCommit(autoCommit);
		connection.close();
	}
}
