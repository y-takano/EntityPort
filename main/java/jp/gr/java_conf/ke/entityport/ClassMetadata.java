package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.annotation.Lifecycle;
import jp.gr.java_conf.ke.entityport.annotation.RootEntity;

public class ClassMetadata<E> {

	private final Class<E> clazz;
	private final String schemaName;
	private final String tableName;
	private final String className;

	private final String classVersion;
	private final Lifecycle lifecycle;
	private final boolean restoration;

	ClassMetadata(Class<E> clazz, String schemaName, String tableName, String className) {
		this.clazz = clazz;
		this.schemaName = schemaName;
		this.tableName = tableName;
		this.className = className;
		RootEntity anno = ReflectUtils.getClassAnnotation(clazz, RootEntity.class);
		 classVersion = anno.classVersion();
		 lifecycle = anno.lifecycle();
		 restoration = anno.restoration();
	}

	public String getSchemaName() {
		return schemaName;
	}
	public String getTableName() {
		return tableName;
	}
	public String getClassName() {
		return className;
	}
	public Class<E> getMetaClass() {
		return clazz;
	}
	public String getClassVersion() {
		return classVersion;
	}
	public Lifecycle getLifecycle() {
		return lifecycle;
	}
	public boolean isRestoration() {
		return restoration;
	}
}
