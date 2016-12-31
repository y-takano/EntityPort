package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.extention.Serializer;
import jp.gr.java_conf.ke.json.JsonFactory;

class DefaultSerializer<E> implements Serializer<E> {

	@Override
	public String serilize(E instance) {
		return JsonFactory.toJson(instance);
	}

	@Override
	public E deserialize(String serialData, Class<E> clazz) {
		return JsonFactory.toObject(serialData, clazz);
	}

}
