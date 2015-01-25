package ggwozdz.dbdiplomat.rdbms.mappings;

import ggwozdz.dbdiplomat.rdbms.operations.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public final class SelectQuery<T> implements DBOperation<List<T>>{
	private static final Logger LOG = LoggerFactory.getLogger(SelectQuery.class);
	
	private final Function<ResultSet, T> mapper;
	private final String query;
	private List<?> params;
	
	private SelectQuery(Builder<T> builder) {
		this.query = builder.query;
		this.params = builder.params;
		this.mapper = builder.mapper;
				
	}
	
	public static <T> Builder<T> newBuilder(){
		return new Builder<T>();
	}
	
	@Override
	public List<T> execute(Connection connection) throws SQLException {
		PreparedStatement select = connection.prepareStatement(query, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
		this.applyParams(select, params);
		
		ResultSet resultSet = select.executeQuery();
		return mapResultSet(resultSet, mapper);
	}
	
	private void applyParams(PreparedStatement preparedStatement, List<?> params) throws SQLException{
		for(int i=0; i<params.size(); ++i){
			LOG.trace("param {} => {}", i+1, params.get(i));
			preparedStatement.setObject(i+1, params.get(i));
		}
	}
	
	private List<T> mapResultSet(ResultSet resultSet, Function<ResultSet, T> mapper) throws SQLException{
		List<T> results = Lists.newArrayList();
		
		while(resultSet.next()){
			T mappedResultRow = mapper.apply(resultSet);
			results.add(mappedResultRow);
		}
		
		return results;
	}
	
	
	public static final class Builder<U> {
		private List<?> params;
		private String query;
		private Function<ResultSet, U> mapper;

		private Builder() {
		}

		public Builder<U> setParams(List<?> params) {
			this.params = params;
			return this;
		}

		public Builder<U> setQuery(String query) {
			this.query = query;
			return this;
		}

		public Builder<U> setMapper(Function<ResultSet, U> mapper) {
			this.mapper = mapper;
			return this;
		}

		public SelectQuery<U> build() {
			return new SelectQuery<U>(this);
		}

	}

}
