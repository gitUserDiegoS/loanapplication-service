package co.com.crediya.validatorauth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Getter
@Setter
@ConfigurationProperties(prefix = "whitelist")
public class Whitelist {

    private List<String> paths;


}
