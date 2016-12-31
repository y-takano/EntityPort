package jp.gr.java_conf.ke.entityport.extention;

import jp.gr.java_conf.ke.entityport.storage.StorageType;

public interface Repository<E> {

	/**
	 *
	 * @return
	 */
	StorageType storageType();

	/**
	 * 復元する
	 *
	 * @return
	 */
	void find();

	/**
	 * 格納する
	 *
	 * @return
	 */
	void store();

	/**
	 * 削除する
	 *
	 * @return
	 */
	void delete();
}