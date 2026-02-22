package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EshopApplicationTests {

    @Test
    void contextLoads() {
    }

    @Test
    void mainStartsApplicationInNonWebMode() {
        CapturingApplicationContextInitializer.reset();

        EshopApplication.main(new String[]{
                "--spring.main.web-application-type=none",
                "--spring.main.banner-mode=off",
                "--context.initializer.classes=id.ac.ui.cs.advprog.eshop.CapturingApplicationContextInitializer"
        });

        ConfigurableApplicationContext context = CapturingApplicationContextInitializer.getContext();
        assertNotNull(context);
        assertTrue(context.isActive());

        context.close();
        assertFalse(context.isActive());
    }

}

class CapturingApplicationContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final AtomicReference<ConfigurableApplicationContext> CONTEXT = new AtomicReference<>();

    static void reset() {
        ConfigurableApplicationContext previous = CONTEXT.getAndSet(null);
        if (previous != null) {
            previous.close();
        }
    }

    static ConfigurableApplicationContext getContext() {
        return CONTEXT.get();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        CONTEXT.set(applicationContext);
    }
}
