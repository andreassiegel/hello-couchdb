package cc.andreassiegel.hellocouchdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public class HumanBeing {

  @JsonProperty("name")
  private String name;

  @JsonProperty("origin")
  private String origin = "Java";

  @JsonProperty("_id")
  private String id;

  @JsonProperty("_rev")
  private String revision;
}
