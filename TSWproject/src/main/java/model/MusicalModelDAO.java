package model;

import java.sql.SQLException;
import java.util.Collection;

public interface MusicalModelDAO<T> {
	public void doSave(T product) throws SQLException;

	public boolean doDelete(int code) throws SQLException;

	public T doRetrieveByKey(int code) throws SQLException;
	
	public Collection<T> doRetrieveAll(String order) throws SQLException;

}
