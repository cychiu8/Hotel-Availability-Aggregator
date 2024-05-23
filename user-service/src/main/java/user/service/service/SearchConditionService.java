package user.service.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import user.service.model.SearchCondition;

@Service
public class SearchConditionService {

    private final SearchConditionRepository searchConditionRepository;

    @Autowired
    public SearchConditionService(SearchConditionRepository searchConditionRepository) {
        this.searchConditionRepository = searchConditionRepository;
    }

    @Transactional
    public SearchCondition createSearchCondition(SearchCondition newSearchCondition) {
        return searchConditionRepository.save(newSearchCondition);
    }


    @Transactional(readOnly = true)
    public List<SearchCondition> getAllSearchConditions() {
        return searchConditionRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<SearchCondition> getSearchConditionById(UUID id) {
        return searchConditionRepository.findById(id);
    }

    @Transactional
    public SearchCondition updateSearchCondition(SearchCondition updatedSearchCondition) {
        return searchConditionRepository.save(updatedSearchCondition);
    }

    @Transactional
    public void deleteSearchCondition(UUID id) {
        searchConditionRepository.deleteById(id);
    }
}