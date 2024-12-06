package helloworld.secured_rest_api.spring_boot.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import helloworld.secured_rest_api.spring_boot.demo.dao.GreetingDAO;
import helloworld.secured_rest_api.spring_boot.demo.service.impl.GreetingServiceImpl;

/**
 * Unit test class of service GreetingService
 */
@ExtendWith(MockitoExtension.class)
public class GreetingServiceTest {

    // The service to be mocked
    @Mock
    private GreetingDAO greetingDAO;

    // The service to be tested
    @InjectMocks
    private GreetingServiceImpl greetingService;

    @Test
    public void test_anonymousGreeting_Nominal() {

        // Mock behaviour
        Mockito.when(greetingDAO.getGreetingTemplate()).thenReturn("Bonjour, %s!");

        // Test nominal case
        assertEquals("Bonjour, World!", greetingService.greeting().getGreeting());
    }

    @Test
    public void test_securedGreeting_Nominal() {

        // Mock behaviour
        Mockito.when(greetingDAO.getGreetingTemplate()).thenReturn("Bonjour, %s!");

        // Test nominal case
        assertEquals("Bonjour, David!", greetingService.greeting("David").getGreeting());
    }
}
