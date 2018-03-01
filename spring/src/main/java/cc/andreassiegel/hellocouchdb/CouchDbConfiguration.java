package cc.andreassiegel.hellocouchdb;

import lombok.extern.slf4j.Slf4j;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.MalformedURLException;

@Slf4j
@Configuration
@EnableConfigurationProperties(CouchDbProperties.class)
public class CouchDbConfiguration {

  private final CouchDbProperties properties;

  @Autowired
  public CouchDbConfiguration(CouchDbProperties properties) {
    this.properties = properties;
  }

  @Bean
  public CouchDbConnector couchDbConnector() {

    log.info("Creating CouchDB connector bean");

    CouchDbConnector dbConnector = new StdCouchDbConnector(properties.getDbName(), dbInstance());
    dbConnector.createDatabaseIfNotExists();
    return dbConnector;
  }

  @Bean
  public CouchDbInstance dbInstance() {
    return new StdCouchDbInstance(couchDbHttpClient());
  }

  @Bean
  public HttpClient couchDbHttpClient() {
    try {
      return new StdHttpClient.Builder()
          .url(String.format("https://%s:%d", properties.getHost(), properties.getPort()))
          .build();
    } catch (MalformedURLException e) {
      throw new RuntimeException("Unable to read URL from CouchDB configuration", e);
    }
  }
}
