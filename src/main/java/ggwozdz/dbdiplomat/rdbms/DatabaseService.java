package ggwozdz.dbdiplomat.rdbms;

import ggwozdz.dbdiplomat.rdbms.mappings.DBOperationException;
import ggwozdz.dbdiplomat.rdbms.operations.DBOperation;

import java.sql.Connection;
import java.sql.SQLException;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DatabaseService extends AbstractDBOperationHandler {
	private final Logger LOG = LoggerFactory.getLogger(DatabaseService.class);
	
	private final DataSource dataSource;

	@Inject
	public DatabaseService(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	@Override
	public <T> T execute(DBOperation<T> doOperation) throws DBOperationException{
		Connection connection = null;
		try {
			connection = this.dataSource.getConnection();
			return doOperation.execute(connection);
		} catch (SQLException e) {
			throw new DBOperationException("Cannot execute DBOperation: "+e.getMessage(), e);
		} finally {
			closeConnection(connection);
		}
	}

	public Transaction getTransaction() throws DBOperationException {
		try {
			Connection connection = this.dataSource.getConnection();
			return new Transaction(connection);
		} catch (SQLException e) {
			throw new DBOperationException("Cannot get connection to the database: "+e.getMessage(), e);
		}
		
	}
	
	private void closeConnection(Connection connection){
		try {
			if(connection != null){
				connection.close();	
			}
		} catch (SQLException e) {
			throw new IllegalStateException("Cannot close SQL connection!", e);
		}
	}

	
}
