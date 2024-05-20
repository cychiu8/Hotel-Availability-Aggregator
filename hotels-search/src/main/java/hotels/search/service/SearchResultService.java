package hotels.search.service;

import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotels.search.model.SearchResult;

@Service
public class SearchResultService {

    private final SearchResultRepository searchResultRepository;

    @Autowired
    public SearchResultService(SearchResultRepository searchResultRepository) {
        this.searchResultRepository = searchResultRepository;
    }

    @Transactional
    public void saveSearchResult(SearchResult searchResult) {
        searchResultRepository.save(searchResult);
    }

    @Transactional(readOnly = true)
    public List<SearchResult> getAllSearchResults() {
        return searchResultRepository.findAll();
    }

    @Transactional
    public SearchResult updateSearchResult(SearchResult updatedSearchResult) {
        return searchResultRepository.save(updatedSearchResult);
    }

    @Transactional
    public void deleteSearchResult(UUID id) {
        searchResultRepository.deleteById(id);
    }
}
