package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.extention.ClassVersionSelector;
import jp.gr.java_conf.ke.entityport.extention.EntityFactory;
import jp.gr.java_conf.ke.entityport.extention.Serializer;
import jp.gr.java_conf.ke.entityport.storage.StorageType;

class EntityPortRecipe<E> {

	private final Class<E> clazz;

	private String className;

	private StorageType storage;

	private String tableName;

	private String schemaName;

	private Serializer<E> serializer;

	private ClassVersionSelector vselector;

	private String restorationPrimaryKey;

	private EntityFactory<E> entityFactory;

	public EntityPortRecipe(Class<E> clazz) {
		this.clazz = clazz;
		this.className = clazz.getSimpleName();
		this.storage = StorageType.Memory;
		this.schemaName = EntityPort.DEFAULT_SCHEMA_NAME;
		this.tableName = EntityPort.DEFAULT_TABLE_NAME;
		this.serializer = new DefaultSerializer<E>();
		this.vselector = new DefaultSelector();
		this.entityFactory = new DefaultEntityFactory<E>();
	}

	public EntityPortRecipe(Class<E> clazz, EntityPortRecipe<E> recipe) {
		this.clazz = clazz;
		this.className = recipe.getClassName();
		this.storage = recipe.getStorage();
		this.schemaName = recipe.getSchemaName();
		this.tableName = recipe.getTableName();
		this.serializer = recipe.getSerializer();
		this.vselector = recipe.getVerSelector();
		this.entityFactory = recipe.getEntityFactory();
	}

	public Class<E> getClazz() {
		return clazz;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public StorageType getStorage() {
		return storage;
	}

	public void setStorage(StorageType storage) {
		this.storage = storage;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String scemaName) {
		this.schemaName = scemaName;
	}

	public Serializer<E> getSerializer() {
		return serializer;
	}

	public void setSerializer(Serializer<E> serializer) {
		this.serializer = serializer;
	}

	public ClassVersionSelector getVerSelector() {
		return vselector;
	}

	public void setVerSelector(ClassVersionSelector vselector) {
		this.vselector = vselector;
	}

	public String getRestorationPrimaryKey() {
		return restorationPrimaryKey;
	}

	public void setRestorationPrimaryKey(String restorationPrimaryKey) {
		this.restorationPrimaryKey = restorationPrimaryKey;
	}

	public EntityFactory<E> getEntityFactory() {
		return entityFactory;
	}

	public void setEntityFactory(EntityFactory<E> entityFactory) {
		this.entityFactory = entityFactory;
	}

}
