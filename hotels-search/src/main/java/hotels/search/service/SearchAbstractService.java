package hotels.search.service;
import org.springframework.stereotype.Service;

@Service
public abstract class SearchAbstractService {
    public abstract String getSearchResult();
}
