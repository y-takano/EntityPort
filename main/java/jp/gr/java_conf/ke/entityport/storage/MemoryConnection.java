package jp.gr.java_conf.ke.entityport.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

class MemoryConnection implements StorageConnection {

	private final String schemaName;
	private final Map<String, Map<String, StorageRecord>> schema;

	MemoryConnection(String schemaName) {
		this.schemaName = schemaName;
		this.schema = new HashMap<String, Map<String, StorageRecord>>();
	}

	@Override
	public void deleteIfExists(String tableName, String primaryKey) {
		if (schema.containsKey(tableName)) {
			Map<String, StorageRecord> table = schema.get(tableName);
			if (table.containsKey(primaryKey)) {
				table.remove(primaryKey);
			}
		}
	}

	@Override
	public void insertOrUpdate(String tableName, String className,
							   String classVersion, String primaryKey, String data,
							   boolean persistance) {
		Map<String, StorageRecord> table;
		if (schema.containsKey(tableName)) {
			table = schema.get(tableName);
		} else {
			table = new ConcurrentHashMap<String, StorageRecord>();
			schema.put(tableName, table);
		}

		StorageRecord record = new StorageRecord(className,
				classVersion, primaryKey, data, persistance);
		table.put(primaryKey, record);

	}

	@Override
	public StorageRecord findByPrimaryKey(String tableName,
										  String primaryKey) {
		StorageRecord ret = null;
		if (schema.containsKey(tableName)) {
			Map<String, StorageRecord> table = schema.get(tableName);
			if (table.containsKey(primaryKey)) {
				ret = table.get(primaryKey);
			}
		}
		return ret;
	}

	public String toString() {
		return new StringBuilder(schemaName).append(":").append(schema).toString();
	}
}
