package pd.santos.portfoliomanager;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestDatabaseConfiguration.class)
@SpringBootTest
@EnableAutoConfiguration(exclude = KafkaAutoConfiguration.class)
class PortfolioManagerApplicationTests {

    @Test
    void contextLoads() {
        System.out.println("[DEBUG_LOG] Running contextLoads test");
    }

}
