package ggwozdz.dbdiplomat.rdbms.mappings;

import ggwozdz.dbdiplomat.rdbms.operations.DBOperationException;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.function.Function;

public final class Mappers {
	public static <T> Function<ResultSet, T> mapperForClass(Class<T> mappedTo){
		final Field[] declaredFields = mappedTo.getDeclaredFields();
		return new Function<ResultSet, T>(){

			@Override
			public T apply(ResultSet t) {
				try {
					T mappedToInstance = mappedTo.newInstance();
					for (Field field : declaredFields) {
						Object value = t.getObject(field.getName(), field.getType());
						field.set(mappedToInstance, value);
					}
					return mappedToInstance;
				} catch (InstantiationException | IllegalAccessException e) {
					throw new IllegalStateException("Cannot create instance of "+mappedTo.getName()+": "+e.getMessage());
				} catch (SQLException e) {
					throw new DBOperationException("Cannot map db results to "+mappedTo.getName(), e);
				}
			}
		};
	}
}
