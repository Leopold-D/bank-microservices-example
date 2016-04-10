package garage.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
/**
 * 
 * @author Leopold Dauvergne
 *
 */
@SpringBootApplication
@EnableDiscoveryClient
public class GarageServiceAPIAdminFacadeApplication {
    public static void main(String[] args) {
        SpringApplication.run(GarageServiceAPIAdminFacadeApplication.class, args);
    }
}
