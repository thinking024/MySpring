package org.springframework.core.convert.support;

import cn.hutool.core.util.StrUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

public class StringToNumberConverterFactory implements ConverterFactory<String, Number> {

    @Override
    public <T extends Number> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToNumber<>(targetType);
    }

    private static final class StringToNumber<T extends Number> implements Converter<String, T> {
        private final Class<T> targetType;

        public StringToNumber(Class<T> targetType) {
            this.targetType = targetType;
        }

        @Override
        public T convert(String source) {
            if (StrUtil.isEmpty(source)) {
                return null;
            }

            if (targetType.equals(Integer.class)) {
                return (T) Integer.valueOf(source);
            } else if (targetType.equals(Long.class)) {
                return (T) Long.valueOf(source);
            } else if (targetType.equals(Double.class)) {
                return (T) Double.valueOf(source);
            } else if (targetType.equals(Float.class)) {
                return (T) Float.valueOf(source);
            } else if (targetType.equals(Short.class)) {
                return (T) Short.valueOf(source);
            } else if (targetType.equals(Byte.class)) {
                return (T) Byte.valueOf(source);
            }

            throw new IllegalArgumentException("Unsupported target type: " + this.targetType.getName());

        }
    }
}
