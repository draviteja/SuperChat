package info.supercode.superchat.config;

import info.supercode.superchat.functons.MockAzureAppRegistration;
import info.supercode.superchat.functons.MockPaymentService;
import info.supercode.superchat.functons.MockWeatherService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.function.Function;

@Configuration
public class FunctionConfiguration {

    @Bean
    public Function<MockAzureAppRegistration.Request, MockAzureAppRegistration.Response> createAzureApp() {
        return new MockAzureAppRegistration();
    }

    @Bean
    public Function<MockPaymentService.Transaction, MockPaymentService.Status> paymentStatus() {
        return new MockPaymentService();
    }

    @Bean
    public Function<MockWeatherService.Request, MockWeatherService.Response> currentWeather() {
        return new MockWeatherService();
    }

}
