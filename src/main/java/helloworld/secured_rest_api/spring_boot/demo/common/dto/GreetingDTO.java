package helloworld.secured_rest_api.spring_boot.demo.common.dto;

/**
 * Greeting DTO
 */
public class GreetingDTO {

    /**
     * Greeting message
     */
    private String greeting;

    /**
     * Constructor
     */
    public GreetingDTO() {
        // nothing to do
    }

    /**
     * Constructor
     * 
     * @param greeting greeting message
     */
    public GreetingDTO(String greeting) {
        this.greeting = greeting;
    }

    public void setGreeting(String greeting) {
        this.greeting = greeting;
    }

    public String getGreeting() {
        return this.greeting;
    }
}
