package cc.andreassiegel.hellocouchdb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.ektorp.support.CouchDbRepositorySupport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.stream.Collectors;

public class AddHandler implements HttpHandler {

  private final CouchDbRepositorySupport<HumanBeing> repository;
  private final Gson gson;

  public AddHandler(CouchDbRepositorySupport<HumanBeing> repository) {
    this.repository = repository;
    this.gson = new GsonBuilder().create();
  }

  public void handle(HttpExchange httpExchange) throws IOException {
    String requestMethod = httpExchange.getRequestMethod();

    if (requestMethod.equalsIgnoreCase("POST")) {
      String body = new BufferedReader(new InputStreamReader(httpExchange.getRequestBody())).lines().collect(Collectors.joining("\n"));

      HumanBeing human = gson.fromJson(body, HumanBeing.class);
      repository.add(human);

      httpExchange.getResponseHeaders().set("Content-Type", "application/json");
      httpExchange.sendResponseHeaders(200, 0);
      OutputStream responseBody = httpExchange.getResponseBody();

      responseBody.write(gson.toJson(human).getBytes());
      responseBody.close();
    } else {
      httpExchange.sendResponseHeaders(500, 0);
    }
  }
}
