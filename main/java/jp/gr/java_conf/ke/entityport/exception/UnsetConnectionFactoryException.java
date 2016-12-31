package jp.gr.java_conf.ke.entityport.exception;

public class UnsetConnectionFactoryException extends RuntimeException {
    public UnsetConnectionFactoryException(String msg) {
        super(msg);
    }
}
