package jp.gr.java_conf.ke.entityport.extention;

public interface ClassVersionSelector {

	boolean select(String className, String oldClassVersion, String newClassVersion);

}
