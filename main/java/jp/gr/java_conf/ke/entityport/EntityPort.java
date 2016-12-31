package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.extention.Archiver;
import jp.gr.java_conf.ke.entityport.extention.Repository;

public interface EntityPort<E> {

	static final String TAG = "EntityPort";

	static final String DEFAULT_SCHEMA_NAME = "APP_LOCAL";

	static final String DEFAULT_TABLE_NAME = "CENTRAL_REPOSITORY";

	ClassMetadata<E> metadata();

	String primaryKey();

	void construct();

	void destruct();

	E getEntity();

	Repository<E> repository();

	Archiver<E> achiver();

	boolean equals(Object entity);

	int hashCode();
}
