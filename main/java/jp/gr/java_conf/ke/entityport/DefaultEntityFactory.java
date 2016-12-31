package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.annotation.PrimaryKey;
import jp.gr.java_conf.ke.entityport.extention.EntityFactory;
import jp.gr.java_conf.ke.entityport.exception.ConstructException;
import jp.gr.java_conf.ke.entityport.exception.WrongEntityClassDef;
import android.util.Log;

class DefaultEntityFactory<E> implements EntityFactory<E> {

	@Override
	public E construct(Class<E> clazz) {
		E ret = null;
		try {
			ret = clazz.newInstance();
		} catch (InstantiationException e) {
			Log.e(EntityPort.TAG, "Exception occurred in " + clazz
					+ " constructor.");
			throw new WrongEntityClassDef(e);
		} catch (IllegalAccessException e) {
			Log.e(EntityPort.TAG, clazz + "is not public class.");
			throw new WrongEntityClassDef(e);
		}
		return ret;
	}

	@Override
	public E reconstruct(E oldInstance, E newInstance) {
		if (oldInstance== null) throw new ConstructException("シーケンス相違: インスタンスが生成されていません。");
		String oldPkey = ReflectUtils.getPrimaryKey(oldInstance, PrimaryKey.class);
		String newPkey = ReflectUtils.getPrimaryKey(newInstance, PrimaryKey.class);
		if (!oldPkey.equals(newPkey)) throw new ConstructException("主キー相違 old=" + oldPkey + ", new=" + newPkey);
		return newInstance;
	}

	@Override
	public void destruct(E instance) {
		// NOP
	}

}
