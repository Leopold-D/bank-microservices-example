package garage.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
/**
 * 
 * @author Leopold Dauvergne
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GarageServiceAPIFacadeApplication {
    public static void main(String[] args) {
        SpringApplication.run(GarageServiceAPIFacadeApplication.class, args);
    }
}
