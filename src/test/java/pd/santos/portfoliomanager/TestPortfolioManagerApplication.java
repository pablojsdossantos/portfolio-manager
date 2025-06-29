package pd.santos.portfoliomanager;

import org.springframework.boot.SpringApplication;

public class TestPortfolioManagerApplication {

    public static void main(String[] args) {
        SpringApplication.from(PortfolioManagerApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
