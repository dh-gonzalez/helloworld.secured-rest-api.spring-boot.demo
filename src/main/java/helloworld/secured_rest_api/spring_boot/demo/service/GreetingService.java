package helloworld.secured_rest_api.spring_boot.demo.service;

import org.springframework.stereotype.Service;
import helloworld.secured_rest_api.spring_boot.demo.common.dto.GreetingDTO;

/**
 * This is an example of service layer
 */
@Service
public interface GreetingService {

    /**
     * Greeting
     * 
     * @return a greeting message
     */
    GreetingDTO greeting();

    /**
     * Greeting a person
     * 
     * @param name name of the person
     * @return a greeting message for that person
     */
    GreetingDTO greeting(String name);
}
