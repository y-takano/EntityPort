package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.extention.Archiver;
import jp.gr.java_conf.ke.entityport.extention.ClassVersionSelector;
import jp.gr.java_conf.ke.entityport.extention.Serializer;
import jp.gr.java_conf.ke.entityport.exception.DeserializeException;
import jp.gr.java_conf.ke.entityport.storage.StorageRecord;
import jp.gr.java_conf.ke.entityport.storage.StorageType;

class ArchiverImpl<E> implements Archiver<E> {

	private Storage<E> storage;
	private ClassVersionSelector selector;
	private String oldPkey;
	private Serializer<E> serializer;
	private EntityPortImpl<E> driver;

	void init(Storage<E> storage, ClassVersionSelector selector, String oldPkey, Serializer<E> serializer, EntityPortImpl<E> driver) {
		this.storage = storage;
		this.selector = selector;
		this.oldPkey = oldPkey;
		this.serializer = serializer;
		this.driver = driver;
	}

	@Override
	public StorageType storageType() {
		return storage.getType();
	}

	@Override
	public void restorePermanent() {
		ClassMetadata<E> meta = driver.metadata();
		if (!meta.isRestoration())
			throw new DeserializeException(
					"復元可能オブジェクトではありません。@RootEntity(restoration=true)で設定してください。");
		if (oldPkey == null)
			throw new DeserializeException(
					"復元キーが設定されていません。restorationPrimaryKey(String)で設定してください。");

		StorageRecord record = storage.select(oldPkey, meta);
		String oldClassVersion = record.getClassVersion();
		String newClassVersion = meta.getClassVersion();
		if (selector.select(oldPkey, oldClassVersion, newClassVersion)) {
			String serialdata = record.getData();
			E instance = serializer.deserialize(serialdata, meta.getMetaClass());
			driver.reconstruct(instance);
		} else {
			driver.construct();
		}
	}

	@Override
	public void perpetuate() {
		String primaryKey = driver.primaryKey();
		ClassMetadata<E> meta = driver.metadata();
		E instance = driver.getEntity();
		storage.update(primaryKey, meta, instance);
		driver.repository().delete();
	}

	@Override
	public void delete() {
		String primaryKey = driver.primaryKey();
		ClassMetadata<E> meta = driver.metadata();
		storage.clear(primaryKey, meta);
	}
}
