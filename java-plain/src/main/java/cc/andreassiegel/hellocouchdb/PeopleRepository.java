package cc.andreassiegel.hellocouchdb;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class PeopleRepository extends CouchDbRepositorySupport<HumanBeing> {
  public PeopleRepository(CouchDbConnector connector) {
    super(HumanBeing.class, connector);
  }
}
