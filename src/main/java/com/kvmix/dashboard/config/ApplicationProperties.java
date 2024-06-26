package com.kvmix.dashboard.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Dashboard.
 *
 * <p>Properties are configured in the {@code application.yml} file.</p>
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

}
