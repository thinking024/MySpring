package org.springframework.core.convert.support;

import org.springframework.core.convert.converter.ConverterRegistry;

/**
 * 默认的转换服务，提供了一些默认的转换器，包括String到Number
 */
public class DefaultConversionService extends GenericConversionService {

	public DefaultConversionService() {
		addDefaultConverters(this);
	}

	public static void addDefaultConverters(ConverterRegistry converterRegistry) {
		converterRegistry.addConverterFactory(new StringToNumberConverterFactory());
		// TODO 添加其他ConverterFactory
	}
}