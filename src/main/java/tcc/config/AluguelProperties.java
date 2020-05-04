package tcc.config;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties
@Component
public class AluguelProperties {

    private float percentualSinal;

}

