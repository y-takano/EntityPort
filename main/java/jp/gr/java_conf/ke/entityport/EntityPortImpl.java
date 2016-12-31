package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.annotation.PrimaryKey;
import jp.gr.java_conf.ke.entityport.extention.Archiver;
import jp.gr.java_conf.ke.entityport.extention.EntityFactory;
import jp.gr.java_conf.ke.entityport.extention.Repository;

class EntityPortImpl<E> implements EntityPort<E> {

	private final ClassMetadata<E> meta;
	private final EntityFactory<E> entityFactory;
	private final Archiver<E> archiver;
	private final Repository<E> repository;

	private E instance;
	private String primaryKey;

	EntityPortImpl(EntityFactory<E> entityFactory, ClassMetadata<E> meta, Archiver<E> archiver, Repository<E> repository) {
		this.meta = meta;
		this.entityFactory = entityFactory;
		this.archiver = archiver;
		this.repository = repository;
	}

	@Override
	public ClassMetadata<E> metadata() {
		return meta;
	}

	@Override
	public String primaryKey() {
		return primaryKey;
	}

	@Override
	public E getEntity() {
		return instance;
	}

	@Override
	public void destruct() {
		instance = null;
	}

	@Override
	public void construct() {
		instance = entityFactory.construct(meta.getMetaClass());
		primaryKey = ReflectUtils
				.getPrimaryKey(instance, PrimaryKey.class);
	}

	@Override
	public Archiver<E> achiver() {
		return archiver;
	}

	@Override
	public Repository<E> repository() {
		return repository;
	}

	@SuppressWarnings("rawtypes")
	public boolean equals(Object o) {
		if (o == null)
			return false;
		else if (o instanceof EntityPort) {
			if (primaryKey == null) {
				if (((EntityPort) o).primaryKey() == null)
					return true;
			} else if (primaryKey.equals(((EntityPort) o).primaryKey())) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		String primaryKey = primaryKey();
		if (primaryKey == null)
			return super.hashCode();
		return primaryKey.hashCode();
	}

	boolean hasInstance() {
		return instance != null;
	}

	E reconstruct(E instance) {
		this.instance = entityFactory.reconstruct(this.instance, instance);
		return this.instance;
	}
}
