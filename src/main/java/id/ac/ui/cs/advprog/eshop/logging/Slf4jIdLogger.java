package id.ac.ui.cs.advprog.eshop.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class Slf4jIdLogger implements IdLogger {
    private static final Logger log = LoggerFactory.getLogger(Slf4jIdLogger.class);

    @Override
    public void log(String id) {
        log.info("id={}", id);
    }
}

