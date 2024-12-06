package helloworld.secured_rest_api.spring_boot.demo.e2e;

import org.junit.jupiter.api.Tag;
import com.intuit.karate.junit5.Karate;

/**
 * End2End tests for application helloworld.secured-rest-api.spring-boot.demo
 */
@Tag("e2e")
public class AllE2ETest {

    @Karate.Test
    Karate testAll() {
        return Karate.run("classpath:e2e").configDir("classpath:e2e").relativeTo(getClass());
    }
}
