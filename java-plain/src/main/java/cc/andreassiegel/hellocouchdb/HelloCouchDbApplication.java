package cc.andreassiegel.hellocouchdb;


import com.sun.net.httpserver.HttpServer;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.support.CouchDbRepositorySupport;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.util.concurrent.Executors;

public class HelloCouchDbApplication {

  private static final int SERVER_PORT = 8090;
  private static final String COUCH_DB_HOST = "localhost";
  private static final int COUCH_DB_PORT = 5987;
  private static final String COUCH_DB_NAME = "people";

  public static void main(String[] args) throws IOException {
    CouchDbConnector connector = couchDbConnector();

    CouchDbRepositorySupport<HumanBeing> repository = new PeopleRepository(connector);

    HttpServer server = HttpServer.create(new InetSocketAddress(SERVER_PORT), 0);
    server.createContext("/people", new ListHandler(repository));
    server.createContext("/people/add", new AddHandler(repository));

    server.setExecutor(Executors.newCachedThreadPool());
    server.start();

    System.out.println("Server started on port " + SERVER_PORT);
  }

  private static CouchDbConnector couchDbConnector() {
    System.out.println("Creating CouchDB connector bean");

    CouchDbConnector dbConnector = new StdCouchDbConnector(COUCH_DB_NAME, dbInstance());
    dbConnector.createDatabaseIfNotExists();
    return dbConnector;
  }

  private static CouchDbInstance dbInstance() {
    return new StdCouchDbInstance(couchDbHttpClient());
  }

  private static HttpClient couchDbHttpClient() {
    try {
      return new StdHttpClient.Builder()
          .url(String.format("http://%s:%d", COUCH_DB_HOST, COUCH_DB_PORT))
          .build();
    } catch (MalformedURLException e) {
      throw new RuntimeException("Unable to read URL from CouchDB configuration", e);
    }
  }
}
