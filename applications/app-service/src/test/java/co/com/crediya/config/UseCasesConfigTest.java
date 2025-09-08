package co.com.crediya.config;

import co.com.crediya.model.loanapplication.gateways.LoanApplicationRepository;
import co.com.crediya.model.loanapplication.gateways.LoanTypeRepository;
import co.com.crediya.model.loanapplication.gateways.UserGatewayRepository;
import co.com.crediya.usecase.loanapplication.LoanApplicationUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertTrue;

class UseCasesConfigTest {


    @Test
    void testUseCaseBeansExist() {
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class)) {
            String[] beanNames = context.getBeanDefinitionNames();

            boolean useCaseBeanFound = false;
            for (String beanName : beanNames) {
                if (beanName.endsWith("UseCase")) {
                    useCaseBeanFound = true;
                    break;
                }
            }

            assertTrue(useCaseBeanFound, "No beans ending with 'Use Case' were found");
        }
    }

    @Configuration
    @Import(UseCasesConfig.class)
    static class TestConfig {

        @Bean
        public UserGatewayRepository userGatewayRepository() {
            return Mockito.mock(UserGatewayRepository.class);
        }

        @Bean
        public LoanApplicationRepository loanApplicationRepository() {
            return Mockito.mock(LoanApplicationRepository.class);
        }

        @Bean
        public LoanTypeRepository loanTypeRepository() {
            return Mockito.mock(LoanTypeRepository.class);
        }

        @Bean
        public MyUseCase myUseCase() {
            return new MyUseCase();
        }


        static class MyUseCase {
            public String execute() {
                return "MyUseCase Test";
            }
        }

        @Bean
        public LoanApplicationUseCase loanApplicationUseCase(
                UserGatewayRepository userGatewayRepository,
                LoanApplicationRepository loanApplicationRepository,
                LoanTypeRepository loanTypeRepository
        ) {
            return new LoanApplicationUseCase(userGatewayRepository, loanApplicationRepository, loanTypeRepository);
        }

    }
}
