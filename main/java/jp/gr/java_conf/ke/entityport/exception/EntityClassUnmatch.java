package jp.gr.java_conf.ke.entityport.exception;

public class EntityClassUnmatch extends Exception {

	public EntityClassUnmatch(String msg, ClassCastException e) {
		super(msg, e);
	}

}
