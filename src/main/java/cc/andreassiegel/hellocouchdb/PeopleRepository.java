package cc.andreassiegel.hellocouchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PeopleRepository extends CouchDbRepositorySupport<HumanBeing> {

  @Autowired
  public PeopleRepository(CouchDbConnector db) {
    super(HumanBeing.class, db);
  }
}
