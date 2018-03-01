package cc.andreassiegel.hellocouchdb;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HumanBeing {

  @JsonProperty("_id")
  private String id;

  @JsonProperty("_rev")
  private String revision;

  private String name;

  private String origin = "Spring";
}
