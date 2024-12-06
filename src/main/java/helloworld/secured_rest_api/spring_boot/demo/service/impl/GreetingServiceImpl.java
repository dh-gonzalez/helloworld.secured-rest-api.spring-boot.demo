package helloworld.secured_rest_api.spring_boot.demo.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import helloworld.secured_rest_api.spring_boot.demo.dao.GreetingDAO;
import helloworld.secured_rest_api.spring_boot.demo.dto.GreetingDTO;
import helloworld.secured_rest_api.spring_boot.demo.service.GreetingService;

/**
 * This is an implementation example of service
 */
@Service
public class GreetingServiceImpl implements GreetingService {

    /** Logger for this class */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    /** Greeting DAO service */
    private final GreetingDAO _greetingDAO;

    /**
     * Constructor
     * 
     * @param greetingDAO greeting DAO
     */
    public GreetingServiceImpl(GreetingDAO greetingDAO) {
        _greetingDAO = greetingDAO;
    }

    @Override
    public GreetingDTO greeting() {
        return greeting("World");
    }

    @Override
    public GreetingDTO greeting(String name) {
        LOGGER.debug("Greeting has been requested for {}", name);

        String greetingTemplate = _greetingDAO.getGreetingTemplate();
        return new GreetingDTO(String.format(greetingTemplate, name));
    }
}
