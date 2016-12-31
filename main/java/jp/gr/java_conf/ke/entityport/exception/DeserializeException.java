package jp.gr.java_conf.ke.entityport.exception;

public class DeserializeException extends RuntimeException {

	public DeserializeException(Exception e) {
		super(e);
	}

	public DeserializeException(String msg) {
		super(msg);
	}

}
