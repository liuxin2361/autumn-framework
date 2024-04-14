package com.javastar.autumn.utils;

import com.javastar.autumn.exception.BeanDefinitionException;

import java.lang.annotation.Annotation;

public class ClassUtils {

    /**
     *
     * @param target
     * @param annoClass
     * @return
     * @param <A>
     */
    public static <A extends Annotation> A findAnnotation(Class<?> target, Class<A> annoClass) {
        A a = target.getAnnotation(annoClass);
        for (Annotation annotation : target.getAnnotations()) {
            Class<? extends Annotation> annoType = annotation.annotationType();
            if (!annoType.getPackageName().equals("java.lang.annotation")) {
                A foundAnno = findAnnotation(annoType, annoClass);
                if (foundAnno != null) {
                    if (a != null) {
                        throw new BeanDefinitionException("Duplicate @" + annoClass.getSimpleName() + " found on " +
                                "class " + target.getSimpleName());
                    }
                    a = foundAnno;
                }
            }
        }
        return a;
    }
}
