package jp.gr.java_conf.ke.entityport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import jp.gr.java_conf.ke.entityport.exception.AnnotationOverlap;
import jp.gr.java_conf.ke.entityport.exception.WrongEntityClassDef;
import android.util.Log;

class ReflectUtils {

	private static final String TAG = "EntityPort";

	public static <A, E> A getClassAnnotation(Class<E> entity,
			Class<A> annotateClass) {
		if (entity == null)
			throw new NullPointerException();
		Annotation[] annos = entity.getDeclaredAnnotations();
		return selectAnnotation(entity, annotateClass, annos);
	}

	@SuppressWarnings("unchecked")
	public static <A, E> String getPrimaryKey(E entity, Class<A> annotateClass) {
		if (entity == null)
			throw new NullPointerException();
		@SuppressWarnings("rawtypes")
		Class clazz = entity.getClass();
		String primaryKey = null;
		int cnt = 0;
		for (Field field : clazz.getDeclaredFields()) {
			A pkAnno = (A) selectAnnotation(clazz, annotateClass,
					field.getDeclaredAnnotations());
			if (pkAnno != null) {
				cnt++;
				field.setAccessible(true);

				if (!field.getType().equals(String.class)) {
					throw new WrongEntityClassDef(
							"@PrimaryKey annotation need to define String class. but defined "
									+ field.getType().getCanonicalName());
				}
				try {
					primaryKey = (String) field.get(entity);
				} catch (Exception e) {
					Log.e(TAG, "Internal Error ocurred.");
					throw new RuntimeException(e);
				}
			}
		}

		if (0 == cnt)
			throw new WrongEntityClassDef(
					"Entity class need to define @PrimaryKey.");
		if (1 < cnt)
			throw new AnnotationOverlap("@" + annotateClass.getSimpleName()
					+ " is overlaped. class:" + clazz.getCanonicalName());
		return primaryKey;
	}

	@SuppressWarnings("unchecked")
	private static <A, E> A selectAnnotation(Class<E> entity,
			Class<A> annotateClass, Annotation[] annos) {
		int cnt = 0;
		A ret = null;
		for (Annotation anno : annos) {
			if (annotateClass.equals(anno.getClass())) {
				ret = (A) anno;
				cnt++;
			}
		}
		if (1 < cnt)
			throw new AnnotationOverlap("@" + annotateClass.getSimpleName()
					+ " is overlaped. class:" + entity.getCanonicalName());
		return ret;
	}

}
