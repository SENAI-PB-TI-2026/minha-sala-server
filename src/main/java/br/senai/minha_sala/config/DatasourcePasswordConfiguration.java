package br.senai.minha_sala.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

@Configuration
@Profile("!dev")
public class DatasourcePasswordConfiguration {
    public DatasourcePasswordConfiguration(Environment environment) {
        if (!StringUtils.hasText(environment.getRequiredProperty("DB_PASSWORD"))) {
            throw new IllegalStateException("DB_PASSWORD deve conter uma senha fora do perfil dev.");
        }
    }
}
