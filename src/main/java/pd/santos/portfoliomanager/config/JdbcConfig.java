package pd.santos.portfoliomanager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.core.convert.JdbcCustomConversions;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import pd.santos.portfoliomanager.asset.model.AssetEventTypeConverter;

import java.util.Arrays;

/**
 * Configuration class for Spring Data JDBC.
 */
@Configuration
@EnableJdbcRepositories(basePackages = "pd.santos.portfoliomanager")
public class JdbcConfig {

    /**
     * Register custom converters for Spring Data JDBC.
     *
     * @return the JdbcCustomConversions bean
     */
    @Bean
    public JdbcCustomConversions jdbcCustomConversions() {
        return new JdbcCustomConversions(Arrays.asList(
                new AssetEventTypeConverter.AssetEventTypeToStringConverter(),
                new AssetEventTypeConverter.StringToAssetEventTypeConverter()
        ));
    }
}