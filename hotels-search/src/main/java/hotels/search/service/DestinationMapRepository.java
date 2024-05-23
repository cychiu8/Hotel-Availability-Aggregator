package hotels.search.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import hotels.search.model.DestinationMap;

@Repository
public interface DestinationMapRepository extends JpaRepository<DestinationMap, String> {

}
