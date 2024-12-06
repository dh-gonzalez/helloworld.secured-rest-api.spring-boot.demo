package helloworld.secured_rest_api.spring_boot.demo.dao;

import org.springframework.stereotype.Service;

/**
 * This is an example of DAO layer
 */
@Service
public interface GreetingDAO {

    /**
     * Get greeting template
     * 
     * @return the greeting template
     */
    String getGreetingTemplate();
}
