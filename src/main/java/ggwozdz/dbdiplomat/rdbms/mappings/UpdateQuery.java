package ggwozdz.dbdiplomat.rdbms.mappings;

import ggwozdz.dbdiplomat.rdbms.operations.DBOperation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class UpdateQuery implements DBOperation<Integer> {
	private static final Logger LOG = LoggerFactory.getLogger(UpdateQuery.class);
	

	private final String query;
	private List<?> params;
	
	private UpdateQuery(Builder builder) {
		this.query = builder.query;
		this.params = builder.params;
	}
	
	public static Builder newBuilder(){
		return new Builder ();
	}
	
	@Override
	public Integer execute(Connection connection) throws SQLException {
		PreparedStatement select = connection.prepareStatement(query);
		this.applyParams(select, params);
		
		return select.executeUpdate();
	}
	
	private void applyParams(PreparedStatement preparedStatement, List<?> params) throws SQLException{
		for(int i=0; i<params.size(); ++i){
			LOG.trace("param {} => {}", i+1, params.get(i));
			preparedStatement.setObject(i+1, params.get(i));
		}
	}
	
	public static final class Builder {
		private List<?> params;
		private String query;

		private Builder() {
		}

		public Builder setParams(List<?> params) {
			this.params = params;
			return this;
		}

		public Builder setQuery(String query) {
			this.query = query;
			return this;
		}

		public UpdateQuery build() {
			return new UpdateQuery(this);
		}

	}

}
