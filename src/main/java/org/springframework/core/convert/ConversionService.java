package org.springframework.core.convert;

/**
 * 执行类型转换操作的接口
 */
public interface ConversionService {

	boolean canConvert(Class<?> sourceType, Class<?> targetType);

	<T> T convert(Object source, Class<T> targetType);
}