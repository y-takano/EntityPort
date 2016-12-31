package jp.gr.java_conf.ke.entityport.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import jp.gr.java_conf.ke.entityport.exception.UnsetConnectionFactoryException;

public class ConnectionManager {

	private static class Holder {
		private static final ConnectionManager INSTANCE = new ConnectionManager();
	}

	public static ConnectionManager getInstance() {
		return Holder.INSTANCE;
	}

	private final Map<String, StorageConnection> DB_SCHEMA = new ConcurrentHashMap<String, StorageConnection>();
	private final Map<String, StorageConnection> FILE_SCHEMA = new ConcurrentHashMap<String, StorageConnection>();
	private final Map<String, StorageConnection> MEM_SCHEMA = new HashMap<String, StorageConnection>();

	private ConnectionFactory connectionFactory;

	private ConnectionManager() {
	}

	public void setFactory(ConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}

	public StorageConnection getDBConnection(String schemaName) {
		if (!DB_SCHEMA.containsKey(schemaName)) {
			if (connectionFactory == null) throw new UnsetConnectionFactoryException("先にsetFactoryを利用してFactoryを設定してください。");
			StorageConnection conn = connectionFactory.createDBConnection(schemaName);
			DB_SCHEMA.put(schemaName, conn);
		}
		return DB_SCHEMA.get(schemaName);
	}

	public StorageConnection getFileConnection(String schemaName) {
		if (!FILE_SCHEMA.containsKey(schemaName)) {
			if (connectionFactory == null) throw new UnsetConnectionFactoryException("先にsetFactoryを利用してFactoryを設定してください。");
			StorageConnection conn = connectionFactory.createFileConnection(schemaName);
			FILE_SCHEMA.put(schemaName, conn);
		}
		return FILE_SCHEMA.get(schemaName);
	}

	public StorageConnection getMemConnection(String schemaName) {
		if (!MEM_SCHEMA.containsKey(schemaName)) {
			if (connectionFactory == null) throw new UnsetConnectionFactoryException("先にsetFactoryを利用してFactoryを設定してください。");
			StorageConnection conn = connectionFactory.createMemoryConnection(schemaName);
			MEM_SCHEMA.put(schemaName, conn);
		}
		return MEM_SCHEMA.get(schemaName);
	}

}
