package com.svlada;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.env.Environment;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private static final Logger log = LoggerFactory.getLogger(SpringApplication.class);
    public static void main(String[] args) throws UnknownHostException {
//        ConfigurableApplicationContext app = SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
        SpringApplication app = new SpringApplication(SpringBootSecurityJwtApplication.class,args);
        Environment env = app.run(args).getEnvironment();
        log.info("Access URLs:\n----------------------------------------------------------\n\t" +
                "Local: \t\thttp://127.0.0.1:{}\n\t" +
                "External: \thttp://{}:{}\n----------------------------------------------------------",
            env.getProperty("server.port"),
            InetAddress.getLocalHost().getHostAddress(),
            env.getProperty("server.port"));
	}
}
