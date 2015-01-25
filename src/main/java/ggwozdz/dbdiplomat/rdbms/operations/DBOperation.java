package ggwozdz.dbdiplomat.rdbms.operations;

import java.sql.Connection;
import java.sql.SQLException;

@FunctionalInterface
public interface DBOperation<T> {
	T execute(Connection connection) throws SQLException;
}
