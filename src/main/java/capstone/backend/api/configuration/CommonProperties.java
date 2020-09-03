package capstone.backend.api.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Data
@Component
@PropertySource("classpath:CommonProperties.properties")
@ConfigurationProperties(prefix = "information")
public class CommonProperties {
    private String secret;
    private Long expiration;
    private int codeSize;
    private String lidiEmail;
}
