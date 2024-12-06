package helloworld.secured_rest_api.spring_boot.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import helloworld.secured_rest_api.spring_boot.demo.common.dto.GreetingDTO;
import helloworld.secured_rest_api.spring_boot.demo.dao.GreetingDAO;
import helloworld.secured_rest_api.spring_boot.demo.service.GreetingService;

/**
 * This is an implementation example of service
 */
@Component
public class GreetingServiceImpl implements GreetingService {

    /** Logger for this class */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /** Greeting DAO service */
    private final GreetingDAO greetingDAO;

    /**
     * Constructor
     * 
     * @param greetingDAO
     */
    public GreetingServiceImpl(GreetingDAO greetingDAO) {
        this.greetingDAO = greetingDAO;
    }

    @Override
    public GreetingDTO greeting() {
        return greeting("World");
    }

    @Override
    public GreetingDTO greeting(String name) {
        LOGGER.debug("Greeting has been requested for {}", name);

        String greetingTemplate = greetingDAO.getGreetingTemplate();
        return new GreetingDTO(String.format(greetingTemplate, name));
    }
}
