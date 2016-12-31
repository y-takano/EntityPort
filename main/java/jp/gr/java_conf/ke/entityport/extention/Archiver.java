package jp.gr.java_conf.ke.entityport.extention;

import jp.gr.java_conf.ke.entityport.storage.StorageType;

public interface Archiver<E> {

	/**
	 *
	 * @return
	 */
	StorageType storageType();

	/**
	 *
	 * @return
	 */
	void restorePermanent();

	/**
	 *
	 */
	void perpetuate();

	/**
	 * 削除する
	 */
	void delete();
}