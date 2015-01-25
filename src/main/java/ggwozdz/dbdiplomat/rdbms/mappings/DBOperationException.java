package ggwozdz.dbdiplomat.rdbms.mappings;

import java.sql.SQLException;

public class DBOperationException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public DBOperationException(String message, SQLException cause) {
		super(message, cause);
	}
	
	public SQLException getSQLException() {
		return (SQLException) this.getCause();
	}
}
