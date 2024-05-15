package hotels.search.service;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import hotels.search.model.SearchResult;

@Repository
public interface SearchResultRepository extends JpaRepository<SearchResult, UUID>{
    
    // SearchResult findByConditionId(UUID conditionId);
}
