package ggwozdz.dbdiplomat.rdbms;

import ggwozdz.dbdiplomat.rdbms.mappings.DBOperationException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public class ResultSetUtils {
	public static Integer getInteger(ResultSet rs, String columnName) throws SQLException{
		int nValue = rs.getInt(columnName);
        return rs.wasNull() ? null : nValue;
	}
	
	public static Function<ResultSet, Long> getCountMapper(){
		return new CountMapper();
	}
	
	private static final class CountMapper implements Function<ResultSet, Long> {

		@Override
		public Long apply(ResultSet input) {
			try {
				return input.getLong(1);
			} catch (SQLException e) {
				throw new DBOperationException("Cannot map count query result to long:" +e.getMessage(), e);
			}
		}
		
	}
}
