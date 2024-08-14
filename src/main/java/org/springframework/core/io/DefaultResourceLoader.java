package org.springframework.core.io;

import java.net.MalformedURLException;
import java.net.URL;

public class DefaultResourceLoader implements ResourceLoader {

	public static final String CLASSPATH_URL_PREFIX = "classpath:";

	/**
	 * 工厂方法，根据location字符创建对应的Resource对象
	 */
	@Override
	public Resource getResource(String location) {
		if (location.startsWith(CLASSPATH_URL_PREFIX)) {
			// classpath下的资源，去除classpath:前缀
			return new ClassPathResource(location.substring(CLASSPATH_URL_PREFIX.length()));
		} else {
			try {
				// 尝试当成url来处理
				URL url = new URL(location);
				return new UrlResource(url);
			} catch (MalformedURLException ex) {
				// 当成文件系统下的资源处理
				return new FileSystemResource(location);
			}
		}
	}
}