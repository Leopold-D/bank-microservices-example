package currencyconverter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class CurrencyConverterCompositeServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CurrencyConverterCompositeServiceApplication.class, args);
    }
}
