package hotels.search.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class DestinationMappingService {
    private Map<String, String> bookingDestinationMap;
    private Map<String, String[]> jalanDestinationMap;

    public DestinationMappingService() {
        // Initialize the maps with the destination codes or IDs for each platform
        bookingDestinationMap = new HashMap<>();
        bookingDestinationMap.put("Aomori", "-225562");

        jalanDestinationMap = new HashMap<>();
        jalanDestinationMap.put("Aomori", new String[]{"020000", "020200"});
    }

    public String getBookingDestinationId(String destination) {
        return bookingDestinationMap.get(destination);
    }

    public String[] getJalanDestinationCodes(String destination) {
        return jalanDestinationMap.get(destination);
    }
}