package com.example.core.testing.annotation;

import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

public class FlywayTestExecutionListener extends AbstractTestExecutionListener {

    @Override
    public void beforeTestMethod(TestContext testContext) {
        ApplicationContext applicationContext = testContext.getApplicationContext();
        Flyway flyway = applicationContext.getBean(Flyway.class);
        flyway.clean();
        flyway.migrate();
    }
}
