package org.springframework.core.convert.support;

import org.junit.Assert;
import org.junit.Test;


public class DefaultConversionServiceTest {

    @Test
    public void testStringToNumber() {
        DefaultConversionService conversionService = new DefaultConversionService();
        Integer result = conversionService.convert("123", Integer.class);
        Assert.assertEquals(Integer.valueOf(123), result);
    }
}