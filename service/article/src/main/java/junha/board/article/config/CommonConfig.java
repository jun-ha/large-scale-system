package junha.board.article.config;

import junha.board.common.snowflake.Snowflake;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonConfig {

    @Bean
    public Snowflake snowflake() {
        return new Snowflake();
    }
}
