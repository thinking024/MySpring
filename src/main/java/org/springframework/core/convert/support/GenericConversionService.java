package org.springframework.core.convert.support;

import cn.hutool.core.convert.BasicType;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;
import org.springframework.core.convert.converter.ConverterRegistry;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.core.convert.converter.GenericConverter.ConvertiblePair;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

public class GenericConversionService implements ConversionService, ConverterRegistry {
    private Map<GenericConverter.ConvertiblePair, GenericConverter> converters = new HashMap<>();

    @Override
    public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
        return getConverter(sourceType, targetType) != null;
    }

    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        Class<?> sourceType = source.getClass();
        targetType = (Class<T>) BasicType.wrap(targetType);
        GenericConverter converter = getConverter(sourceType, targetType);
        return (T) converter.convert(source, sourceType, targetType);
    }


    @Override
    public void addConverter(GenericConverter converter) {
        for (ConvertiblePair convertibleType : converter.getConvertibleTypes()) {
            converters.put(convertibleType, converter);
        }
    }

    /**
     * 将普通的converter转换为GenericConverter，然后保存
     */
    @Override
    public void addConverter(Converter<?, ?> converter) {
        ConvertiblePair typeInfo = getRequiredTypeInfo(converter);
        ConverterAdapter converterAdapter = new ConverterAdapter(typeInfo, converter);
        this.addConverter(converterAdapter);
    }


    /**
     * 将ConverterFactory转换为GenericConverter，然后保存
     */
    @Override
    public void addConverterFactory(ConverterFactory<?, ?> converterFactory) {
        ConvertiblePair typeInfo = getRequiredTypeInfo(converterFactory);
        ConverterFactoryAdapter converterFactoryAdapter = new ConverterFactoryAdapter(typeInfo, converterFactory);
        this.addConverter(converterFactoryAdapter);
    }

    /**
     * 双重循环遍历sourceType和targetType的父类组合pair，找到对应的converter
     */
    protected GenericConverter getConverter(Class<?> sourceType, Class<?> targetType) {
        List<Class<?>> targetCandidates = getClassHierarchy(targetType);
        List<Class<?>> sourceCandidates = getClassHierarchy(sourceType);

        for (Class<?> targetCandidate : targetCandidates) {
            for (Class<?> sourceCandidate : sourceCandidates) {
                ConvertiblePair convertiblePair = new ConvertiblePair(sourceCandidate, targetCandidate);
                GenericConverter converter = converters.get(convertiblePair);
                if (converter != null) {
                    return converter;
                }
            }
        }
        return null;
    }

    /**
     * 获取类的继承层次
     */
    private List<Class<?>> getClassHierarchy(Class<?> clazz) {
        // 原始类转为包装类
        clazz = BasicType.wrap(clazz);

        List<Class<?>> hierarchy = new ArrayList<>();
        while (clazz != null) {
            hierarchy.add(clazz);
            clazz = clazz.getSuperclass();
        }
        return hierarchy;
    }

    /**
     * 获取指定对象的接口的两个参数的converter pair
     */
    private ConvertiblePair getRequiredTypeInfo(Object object) {
        Type[] types = object.getClass().getGenericInterfaces();
        if (types.length == 0) {
            throw new IllegalArgumentException("Expected a GenericConverter with at least one ConvertiblePair.");
        }
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        return new ConvertiblePair((Class<?>) actualTypeArguments[0], (Class<?>) actualTypeArguments[1]);
    }


    private final class ConverterAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final Converter<Object, Object> converter;

        public ConverterAdapter(ConvertiblePair typeInfo, Converter<?, ?> converter) {
            this.typeInfo = typeInfo;
            this.converter = (Converter<Object, Object>) converter;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converter.convert(source);
        }
    }

    private final class ConverterFactoryAdapter implements GenericConverter {

        private final ConvertiblePair typeInfo;

        private final ConverterFactory<Object, Object> converterFactory;

        public ConverterFactoryAdapter(ConvertiblePair typeInfo, ConverterFactory<?, ?> converterFactory) {
            this.typeInfo = typeInfo;
            this.converterFactory = (ConverterFactory<Object, Object>) converterFactory;
        }

        @Override
        public Set<ConvertiblePair> getConvertibleTypes() {
            return Collections.singleton(typeInfo);
        }

        @Override
        public Object convert(Object source, Class sourceType, Class targetType) {
            return converterFactory.getConverter(targetType).convert(source);
        }
    }
}
