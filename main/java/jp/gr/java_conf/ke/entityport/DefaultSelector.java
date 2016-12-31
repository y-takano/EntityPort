package jp.gr.java_conf.ke.entityport;

import jp.gr.java_conf.ke.entityport.extention.ClassVersionSelector;

class DefaultSelector implements ClassVersionSelector {

	@Override
	public boolean select(String className, String oldClassVersion,
			String newClassVersion) {
		return true;
	}

}
