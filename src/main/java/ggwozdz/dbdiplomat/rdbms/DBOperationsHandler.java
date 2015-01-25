package ggwozdz.dbdiplomat.rdbms;

import ggwozdz.dbdiplomat.rdbms.operations.DBOperation;
import ggwozdz.dbdiplomat.rdbms.operations.DBOperationException;

import java.sql.ResultSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;


public interface DBOperationsHandler {
	public <T> List<T> select(String query, Function<ResultSet, T> mapper, List<?> params) throws DBOperationException;
	public <T> List<T> select(String query, Function<ResultSet, T> mapper, Object... params) throws DBOperationException;
	
	public <T> Optional<T> selectOne(String query, Function<ResultSet, T> mapper, List<?> params) throws DBOperationException;
	public <T> Optional<T> selectOne(String query, Function<ResultSet, T> mapper, Object... params) throws DBOperationException;
	
	public int insertOrUpdate(String query, List<?> params) throws DBOperationException;
	public int insertOrUpdate(String query, Object... params) throws DBOperationException;
	
	public <T> int insertOrUpdate(String query, T input, Function<T, List<?>> inputMapper) throws DBOperationException;

	public <T> T execute(DBOperation<T> doOperation) throws DBOperationException;
}
