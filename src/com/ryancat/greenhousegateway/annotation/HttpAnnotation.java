package com.ryancat.greenhousegateway.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.ryancat.greenhousegateway.util.Constants;

/**
 * Http的注解，标注这个字段是请求还是响应
 * @author RyanHu
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface HttpAnnotation {
	String HttpType() default Constants.HTTP_REQUEST ;
}
