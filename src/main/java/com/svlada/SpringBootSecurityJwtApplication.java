package com.svlada;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Sample application for demonstrating security with JWT Tokens
 *
 * @author vladimir.stankovic
 *
 * Aug 3, 2016
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
//@EnableAutoConfiguration(exclude = {MultipartAutoConfiguration.class})
public class SpringBootSecurityJwtApplication {

   /* @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize("128KB");
        factory.setMaxRequestSize("128KB");
        return factory.createMultipartConfig();
    }*/

    /*@Bean(name = "multipartResolver")
    public MultipartResolver multipartResolver()
    {
        CommonsMultipartResolver resolver = new CommonsMultipartResolver();
        // resolver.setDefaultEncoding("UTF-8");
        // resolver.setResolveLazily(true);// resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
        // resolver.setMaxInMemorySize(40960);
        resolver.setMaxUploadSize(10 * 1024 * 1024);// 上传文件大小 5M 5*1024*1024
        return resolver;
    }
*/
    public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}
}
