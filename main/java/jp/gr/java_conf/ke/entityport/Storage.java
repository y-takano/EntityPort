package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.annotation.Lifecycle;
import jp.gr.java_conf.ke.entityport.extention.Serializer;
import jp.gr.java_conf.ke.entityport.exception.DeserializeException;
import jp.gr.java_conf.ke.entityport.exception.SerializeException;
import jp.gr.java_conf.ke.entityport.storage.StorageConnection;
import jp.gr.java_conf.ke.entityport.storage.StorageRecord;
import jp.gr.java_conf.ke.entityport.storage.StorageType;

import android.util.Log;

class Storage<E> {

	private final StorageConnection conn;
	private final StorageType type;
	private final Serializer<E> serializer;

	public Storage(StorageConnection conn, StorageType type, Serializer<E> serializer, Lifecycle lifeCycle) {
		this.type = type;
		this.serializer = serializer;
		this.conn = conn;
	}

	public void update(String primaryKey, ClassMetadata<E> meta, E instance) {
		String tableName = meta.getTableName();
		String className = meta.getClassName();
		String classVersion = meta.getClassVersion();
		String data = serialize(instance);
		boolean persistence = meta.getLifecycle().equals(Lifecycle.PERSISTENT);
		conn.insertOrUpdate(tableName, className, classVersion, primaryKey, data, persistence);
	}

	public void clear(String primaryKey, ClassMetadata<E> meta) {
		String tableName = meta.getTableName();
		conn.deleteIfExists(tableName, primaryKey);
	}

	public E take(String primaryKey, ClassMetadata<E> meta) {
		String tableName = meta.getTableName();
		StorageRecord record = conn.findByPrimaryKey(tableName, primaryKey);
		String data = record.getData();
		return deserialize(data, meta.getMetaClass());
	}

	public StorageRecord select(String primaryKey, ClassMetadata<E> meta) {
		String tableName = meta.getTableName();
		return conn.findByPrimaryKey(tableName, primaryKey);
	}

	public StorageType getType() {
		return type;
	}

	private String serialize(E instance) {
		try {
			return serializer.serilize(instance);
		} catch (Exception e) {
			Log.e(EntityPort.TAG, "Exception occurred in serialize class", e);
			throw new SerializeException(e);
		}
	}

	private E deserialize(String serial, Class<E> clazz) {
		try {
			return serializer.deserialize(serial, clazz);
		} catch (Exception e) {
			Log.e(EntityPort.TAG, "Exception occurred in deserialize class", e);
			throw new DeserializeException(e);
		}
	}
}
