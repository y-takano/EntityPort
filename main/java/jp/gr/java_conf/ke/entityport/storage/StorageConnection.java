package jp.gr.java_conf.ke.entityport.storage;

public interface StorageConnection {

	void insertOrUpdate(String tableName, String className,
						String classVersion, String primaryKey, String data,
						boolean persistance);

	void deleteIfExists(String tableName, String primaryKey);

	StorageRecord findByPrimaryKey(String tableName, String primaryKey);

}
