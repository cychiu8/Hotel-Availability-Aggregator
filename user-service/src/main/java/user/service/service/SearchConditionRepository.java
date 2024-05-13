package user.service.service;

import user.service.model.SearchCondition;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchConditionRepository extends JpaRepository<SearchCondition, UUID> {
    // You can define additional query methods here
}
