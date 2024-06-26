package com.kvmix.dashboard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.kvmix.dashboard.config.AsyncSyncConfiguration;
import com.kvmix.dashboard.config.EmbeddedSQL;
import com.kvmix.dashboard.config.JacksonConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Base composite annotation for integration tests.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@SpringBootTest(classes = {DashboardApplication.class, JacksonConfiguration.class, AsyncSyncConfiguration.class})
@EmbeddedSQL
public @interface IntegrationTest {
}
