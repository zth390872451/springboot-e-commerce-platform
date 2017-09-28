package com.svlada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.util.HashSet;
import java.util.Set;


@SpringBootApplication
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SpringBootSecurityJwtApplication {

	@Bean(name="conversionService")
	public ConversionServiceFactoryBean serviceFactoryBean() {
		ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
		Set<Converter> converters = new HashSet<>();
		converters.add(new StringToDateConverter());
		bean.setConverters(converters);
		return bean;
	}

	/*@Bean
	public Converter<String, Date> StringToDateConverter() {
		return new Converter<String, Date>() {
			@Override
			public Date convert(String source) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Date date = null;
				try {
					date = sdf.parse((String) source);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return date;
			}
		};
	}*/
    public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}
}
