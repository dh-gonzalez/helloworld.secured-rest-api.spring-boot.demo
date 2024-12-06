package helloworld.secured_rest_api.spring_boot.demo.controller;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import helloworld.secured_rest_api.spring_boot.demo.dto.GreetingDTO;
import helloworld.secured_rest_api.spring_boot.demo.service.GreetingService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Unit test class of controller GreetingController
 */
@ExtendWith(MockitoExtension.class)
public class GreetingControllerTest {

    // The services to be mocked
    @Mock
    private GreetingService greetingService;

    // The controller to be tested
    @InjectMocks
    private GreetingController greetingController;

    @Test
    public void test_anonymousGreeting_Nominal() {

        // Test data
        GreetingDTO greetingDTO = new GreetingDTO();
        greetingDTO.setGreeting("Hello, World!");

        // Mocks behaviour
        when(greetingService.greeting()).thenReturn(greetingDTO);

        // Call service method to test
        Mono<GreetingDTO> response = greetingController.greeting();
        // Retrieve asynchronous result and check expected result
        StepVerifier.create(response).expectNext(greetingDTO).verifyComplete();

        // Check mocks have been correctly called
        verify(greetingService).greeting();
    }

    @Test
    public void test_securedGreeting_Nominal() {

        // Test data
        GreetingDTO greetingDTO = new GreetingDTO();
        greetingDTO.setGreeting("Hello, David!");

        // Mocks behaviour
        when(greetingService.greeting("David")).thenReturn(greetingDTO);

        // Call service method to test
        Mono<GreetingDTO> response = greetingController.greeting("David");
        // Retrieve asynchronous result and check expected result
        StepVerifier.create(response).expectNext(greetingDTO).verifyComplete();

        // Check mocks have been correctly called
        verify(greetingService).greeting("David");
    }
}
