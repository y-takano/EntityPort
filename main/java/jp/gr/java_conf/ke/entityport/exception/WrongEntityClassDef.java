package jp.gr.java_conf.ke.entityport.exception;

public class WrongEntityClassDef extends RuntimeException {

	public WrongEntityClassDef(Exception e) {
		super(e);
	}

	public WrongEntityClassDef(String string) {
		super(string);
	}

}
