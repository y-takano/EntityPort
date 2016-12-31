package jp.gr.java_conf.ke.entityport.storage;

import jp.gr.java_conf.ke.json.databind.annotation.JsonBean;

@JsonBean
public class StorageRecord {

	private final String className;
	private final String classVersion;
	private final String primaryKey;
	private final String data;
	private final boolean persistent;

	StorageRecord(String className, String classVersion, String primaryKey, String data, boolean persistent) {
		this.className = className;
		this.classVersion = className;
		this.primaryKey = primaryKey;
		this.data = data;
		this.persistent = persistent;
	}

	public String getClassName() {
		return className;
	}

	public String getClassVersion() {
		return classVersion;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public String getData() {
		return data;
	}

	public boolean isPersistent() {
		return persistent;
	}

}
