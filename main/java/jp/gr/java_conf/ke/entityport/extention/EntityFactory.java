package jp.gr.java_conf.ke.entityport.extention;

public interface EntityFactory<E> {

	E construct(Class<E> clazz);

	E reconstruct(E oldInstance, E newInstance);

	void destruct(E instance);
}
