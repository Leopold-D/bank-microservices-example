package helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HelloWorldCompositeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldCompositeServiceApplication.class, args);
    }
}
