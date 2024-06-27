package com.kvmix.dashboard.config;

import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.cors.CorsConfiguration;

/**
 * Properties specific to Dashboard.
 *
 * <p>Properties are configured in the {@code application.yml} file.
 */
@ConfigurationProperties(prefix = "kvmix", ignoreUnknownFields = true)
@Setter
public class ApplicationProperties {

  private final Cache cache = new Cache();
  private final Mail mail = new Mail();
  private final CorsConfiguration cors = new CorsConfiguration();

  public Cache getCache() {
    return cache;
  }

  public Mail getMail() {
    return mail;
  }

  public CorsConfiguration getCors() {
    return cors;
  }


  public static class Cache {

    private final Ehcache ehcache = new Ehcache();

    public Ehcache getEhcache() {
      return ehcache;
    }

    public static class Ehcache {

      private int timeToLiveSeconds = 3600; // 1 hour
      private long maxEntries = 100;

      public int getTimeToLiveSeconds() {
        return timeToLiveSeconds;
      }

      public void setTimeToLiveSeconds(int timeToLiveSeconds) {
        this.timeToLiveSeconds = timeToLiveSeconds;
      }

      public long getMaxEntries() {
        return maxEntries;
      }

      public void setMaxEntries(long maxEntries) {
        this.maxEntries = maxEntries;
      }
    }

  }

  public static class Mail {

    boolean enabled = false;
    String from = "";
    String baseUrl = "";

    public boolean isEnabled() {
      return enabled;
    }

    public String getFrom() {
      return from;
    }

    public String getBaseUrl() {
      return baseUrl;
    }

    public void setEnabled(boolean enabled) {
      this.enabled = enabled;
    }

    public void setFrom(String from) {
      this.from = from;
    }

    public void setBaseUrl(String baseUrl) {
      this.baseUrl = baseUrl;
    }
  }
}
