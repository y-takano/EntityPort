package jp.gr.java_conf.ke.entityport.extention;

public interface Serializer<E> {

	String serilize(E instance);

	E deserialize(String serialData, Class<E> clazz);

}
