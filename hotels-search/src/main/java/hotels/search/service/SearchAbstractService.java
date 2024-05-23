package hotels.search.service;
import java.util.List;
import org.springframework.stereotype.Service;
import hotels.search.model.SearchCondition;
import hotels.search.model.SearchResult;

@Service
public abstract class SearchAbstractService {
    public abstract SearchResult getSearchResultAndSave(SearchCondition search);
    public abstract List<SearchResult> getAllSearchResult();
}
