package cc.andreassiegel.hellocouchdb;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("couchdb")
@Data
public class CouchDbProperties {

  private String host = "localhost";

  private int port = 5984;

  private String dbName = "mydatabase";
}
