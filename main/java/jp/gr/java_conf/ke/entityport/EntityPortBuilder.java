package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.annotation.Lifecycle;
import jp.gr.java_conf.ke.entityport.extention.ClassVersionSelector;
import jp.gr.java_conf.ke.entityport.extention.EntityFactory;
import jp.gr.java_conf.ke.entityport.extention.Serializer;
import jp.gr.java_conf.ke.entityport.storage.ConnectionManager;
import jp.gr.java_conf.ke.entityport.storage.StorageConnection;
import jp.gr.java_conf.ke.entityport.storage.StorageType;

public class EntityPortBuilder<E> {

	private EntityPortRecipe<E> recipe;

	public EntityPortBuilder(Class<E> clazz) {
		if (clazz == null)
			throw new NullPointerException();
		this.recipe = new EntityPortRecipe<E>(clazz);
	}

	public EntityPortBuilder(Class<E> clazz, EntityPortBuilder<E> prototype) {
		if (clazz == null)
			throw new NullPointerException();
		this.recipe = new EntityPortRecipe<E>(clazz, prototype.recipe);
	}

	public EntityPortBuilder<E> storage(StorageType storage) {
		if (storage == null)
			throw new NullPointerException();
		recipe.setStorage(storage);
		return this;
	}

	public EntityPortBuilder<E> serializer(Serializer<E> serializer) {
		if (serializer == null)
			throw new NullPointerException();
		recipe.setSerializer(serializer);
		return this;
	}

	public EntityPortBuilder<E> versionSelector(ClassVersionSelector verSlector) {
		if (verSlector == null)
			throw new NullPointerException();
		recipe.setVerSelector(verSlector);
		return this;
	}

	public EntityPortBuilder<E> schemaName(String name) {
		if (name == null)
			throw new NullPointerException();
		recipe.setSchemaName(name);
		return this;
	}

	public EntityPortBuilder<E> tableName(String name) {
		if (name == null)
			throw new NullPointerException();
		recipe.setTableName(name);
		return this;
	}

	public EntityPortBuilder<E> className(String name) {
		if (name == null)
			throw new NullPointerException();
		recipe.setClassName(name);
		return this;
	}

	public EntityPortBuilder<E> restorationPrimaryKey(String primaryKey) {
		if (primaryKey == null)
			throw new NullPointerException();
		recipe.setRestorationPrimaryKey(primaryKey);
		return this;
	}

	public EntityPortBuilder<E> entityFactory(EntityFactory<E> entityFactory) {
		if (entityFactory == null)
			throw new NullPointerException();
		recipe.setEntityFactory(entityFactory);
		return this;
	}

	public EntityPort<E> build() {
		EntityFactory<E> entityFactory = recipe.getEntityFactory();
		ClassMetadata<E> meta = createMetadata();
		ArchiverImpl<E> archiver = new ArchiverImpl<E>();
		RepositoryImpl<E> repository = new RepositoryImpl<E>();
		EntityPortImpl<E> portal = new EntityPortImpl<E>(entityFactory, meta, archiver, repository);

		Serializer<E> serializer = recipe.getSerializer();
		StorageType type = recipe.getStorage();
		Storage<E> storage = createStorage(
				type,
				meta.getLifecycle(),
				serializer,
				meta.getSchemaName());
		repository.init(storage, portal);

		ClassVersionSelector selector = recipe.getVerSelector();
		Storage<E> database = createStorage(
				StorageType.Database,
				Lifecycle.PERSISTENT,
				new DefaultSerializer<E>(),
				EntityPort.DEFAULT_SCHEMA_NAME);
		String oldPkey = recipe.getRestorationPrimaryKey();
		archiver.init(database, selector, oldPkey, serializer, portal);

		return portal;
	}

	private ClassMetadata<E> createMetadata() {
		 String schemaName = recipe.getSchemaName();
		 String tableName = recipe.getTableName();
		 String className = recipe.getClassName();
		 Class<E> clazz = recipe.getClazz();
		return new ClassMetadata<E>(clazz, schemaName,
				 tableName, className);
	}

	private Storage<E> createStorage(StorageType type, Lifecycle lifecycle,
			Serializer<E> serializer, String schemaName) {
		StorageConnection conn = null;
		ConnectionManager connManager = ConnectionManager.getInstance();
		switch (type) {
		case Database:
			conn = connManager.getDBConnection(schemaName);
			break;

		case File:
			conn = connManager.getFileConnection(schemaName);
			break;

		case Memory:
			conn = connManager.getMemConnection(schemaName);
			break;

		default:
			throw new InternalError();
		}
		return new Storage<E>(conn, type, serializer, lifecycle);
	}

}
