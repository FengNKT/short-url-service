package io.github.fengnkt.shorturlresolve;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("io.github.fengnkt.shorturlresolve.mapper")
public class ShortUrlResolveApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortUrlResolveApplication.class, args);
    }

}
