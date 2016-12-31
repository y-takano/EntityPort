package jp.gr.java_conf.ke.entityport.exception;

public class FileAccessException extends RuntimeException {

	public FileAccessException(Exception e) {
		super(e);
	}

}
