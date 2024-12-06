package helloworld.secured_rest_api.spring_boot.demo.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import helloworld.secured_rest_api.spring_boot.demo.dao.GreetingDAO;

/**
 * This is an dummy example of DAO implementation
 */
@Service
public class GreetingDAOImpl implements GreetingDAO {

    /** Logger for this class */
    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Override
    public String getGreetingTemplate() {
        LOGGER.debug("Greeting template has been requested");

        return "Hello, %s!";
    }
}
