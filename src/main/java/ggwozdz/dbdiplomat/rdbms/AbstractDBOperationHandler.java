package ggwozdz.dbdiplomat.rdbms;

import ggwozdz.dbdiplomat.rdbms.operations.DBOperation;
import ggwozdz.dbdiplomat.rdbms.operations.DBOperationException;
import ggwozdz.dbdiplomat.rdbms.operations.SelectQuery;
import ggwozdz.dbdiplomat.rdbms.operations.UpdateQuery;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


abstract class AbstractDBOperationHandler implements DBOperationsHandler{
	private final Logger LOG = LoggerFactory.getLogger(AbstractDBOperationHandler.class);
	
	@Override
	public <T> List<T> select(String query, Function<ResultSet, T> mapper,  Object... params) throws DBOperationException{
		return this.select(query, mapper, Arrays.asList(params));
	}
	
	@Override
	public <T> List<T> select(String query, Function<ResultSet, T> mapper,  List<?> params) throws DBOperationException{
		LOG.debug("Running query {} with params {}", query, params);
		
		DBOperation<List<T>> doOperation = SelectQuery.<T>newBuilder()
			.setQuery(query)
			.setParams(params)
			.setMapper(mapper)
			.build();
		
		return this.execute(doOperation);
	}
	
	@Override
	public <T> Optional<T> selectOne(String query, Function<ResultSet, T> mapper, Object... params) throws DBOperationException{
		return this.selectOne(query, mapper, Arrays.asList(params));
	}
	
	@Override
	public <T> Optional<T> selectOne(String query, Function<ResultSet, T> mapper, List<?> params) throws DBOperationException{
		List<T> results = this.select(query, mapper, params);
		if(results.isEmpty()){
			return Optional.empty();
		}else{
			return Optional.of(results.get(0));
		}
	}
	
	@Override
	public <T> int insertOrUpdate(String query, T input, Function<T, List<?>> inputMapper) throws DBOperationException {
		List<?> params = inputMapper.apply(input);
		return this.insertOrUpdate(query, params);
	}
	
	@Override
	public int insertOrUpdate(String query, Object... params) throws DBOperationException {
		return this.insertOrUpdate(query, Arrays.asList(params));
	}
	
	@Override
	public int insertOrUpdate(String query, List<?> params) throws DBOperationException {
		LOG.debug("Running query {} with params {}", query, params);
		
		DBOperation<Integer> doOperation = UpdateQuery.newBuilder()
			.setQuery(query)
			.setParams(params)
			.build();
		
		return this.execute(doOperation);
	}
	
}
