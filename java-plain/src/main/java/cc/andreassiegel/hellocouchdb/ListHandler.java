package cc.andreassiegel.hellocouchdb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ektorp.support.CouchDbRepositorySupport;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collection;

public class ListHandler implements HttpHandler {

  private final CouchDbRepositorySupport<HumanBeing> repository;
  private final Gson gson;

  public ListHandler(CouchDbRepositorySupport<HumanBeing> repository) {
    this.repository = repository;
    this.gson = new GsonBuilder().create();
  }

  public void handle(HttpExchange httpExchange) throws IOException {
    String requestMethod = httpExchange.getRequestMethod();

    if (requestMethod.equalsIgnoreCase("GET")) {
      httpExchange.getResponseHeaders().set("Content-Type", "application/json");
      httpExchange.sendResponseHeaders(200, 0);
      OutputStream responseBody = httpExchange.getResponseBody();

      responseBody.write(listPeople());
      responseBody.close();
    } else {
      httpExchange.sendResponseHeaders(500, 0);
    }
  }

  private byte[] listPeople() {

    Collection<HumanBeing> results = repository.getAll();
    String json = gson.toJson(results);
    System.out.println(json);
    return json.getBytes();
  }
}
