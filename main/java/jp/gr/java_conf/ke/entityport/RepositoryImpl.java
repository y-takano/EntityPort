package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.extention.Repository;
import jp.gr.java_conf.ke.entityport.storage.StorageType;

class RepositoryImpl<E> implements Repository<E> {

	private EntityPortImpl<E> driver;
	private Storage<E> storage;

	void init(Storage<E> storage, EntityPortImpl<E> driver) {
		this.driver = driver;
		this.storage = storage;
	}

	@Override
	public StorageType storageType() {
		return storage.getType();
	}

	@Override
	public void find() {
		String primaryKey = driver.primaryKey();
		ClassMetadata<E> meta = driver.metadata();
		E instance = storage.take(primaryKey, meta);
		driver.reconstruct(instance);
	}

	@Override
	public void store() {
		String primaryKey = driver.primaryKey();
		ClassMetadata<E> meta = driver.metadata();
		storage.update(primaryKey, meta, driver.getEntity());
	}

	@Override
	public void delete() {
		String primaryKey = driver.primaryKey();
		ClassMetadata<E> meta = driver.metadata();
		storage.clear(primaryKey, meta);
		driver.reconstruct(null);
	}
}
