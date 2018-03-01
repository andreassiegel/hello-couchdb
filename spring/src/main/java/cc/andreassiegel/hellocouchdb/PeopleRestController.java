package cc.andreassiegel.hellocouchdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class PeopleRestController {

  @Autowired
  private PeopleRepository repo;

  @PostMapping("people/add")
  public ResponseEntity addHuman(@RequestBody HumanBeing human) {
    repo.add(human);
    return ResponseEntity.ok(human);
  }

  @GetMapping("people")
  public Collection<HumanBeing> listHumans() {
    return repo.getAll();
  }
}
