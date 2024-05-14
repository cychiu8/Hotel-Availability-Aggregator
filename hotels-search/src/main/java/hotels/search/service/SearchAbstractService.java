package hotels.search.service;
import org.springframework.stereotype.Service;
import hotels.search.model.SearchCondition;

@Service
public abstract class SearchAbstractService {
    public abstract String getSearchResult(SearchCondition search);

    public abstract String getAllSearchResult();
}
