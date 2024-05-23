package hotels.search.service;

import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import hotels.search.model.DestinationMap;

@Service
public class DestinationMapService {

    private final DestinationMapRepository destinationMapRepository;


    public DestinationMapService(DestinationMapRepository destinationMapRepository) {
        this.destinationMapRepository = destinationMapRepository;
    }

    @Transactional
    public void saveDestinationMap(DestinationMap destinationMap) {
        destinationMapRepository.save(destinationMap);
    }

    @Transactional(readOnly = true)
    public List<DestinationMap> getAllDestinationMaps() {
        return destinationMapRepository.findAll();
    }

    @Transactional(readOnly = true)
    public DestinationMap getDestinationMapById(String dest) {
        return destinationMapRepository.findById(dest).orElse(null);
    }

    @Transactional
    public DestinationMap updateSearchResult(DestinationMap updatedDestinationMap) {
        return destinationMapRepository.save(updatedDestinationMap);
    }

    @Transactional
    public void deleteSearchResult(String dest) {
        destinationMapRepository.deleteById(dest);
    }
}
