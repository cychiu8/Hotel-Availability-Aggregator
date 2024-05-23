package hotels.search.service;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import hotels.search.model.SearchCondition;

@Repository
public interface SearchConditionRepository extends JpaRepository<SearchCondition, UUID> {
    // You can define additional query methods here
}