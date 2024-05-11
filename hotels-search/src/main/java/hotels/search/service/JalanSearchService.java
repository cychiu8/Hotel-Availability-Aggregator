package hotels.search.service;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
public class JalanSearchService extends SearchAbstractService{
    @Override
    public String getSearchResult() {

        String url = "https://www.jalan.net/020000/LRG_020200/?stayYear=2024&stayMonth=5&stayDay=12&stayCount=1&roomCount=1&adultNum=1&minPrice=0&maxPrice=999999&mealType=&kenCd=020000&lrgCd=020200&rootCd=04&distCd=01&roomCrack=100000&reShFlg=1&mvTabFlg=0&listId=0&screenId=UWW1402";
        String response = "Jalan search result";
        try {
            Document doc = Jsoup.connect(url).get();
            String title = doc.title(); // get the title of the page
            response = doc.text(); // get the text of the page
        } catch (IOException e) {
            // Handle the exception
            e.printStackTrace();
        }
        
        return response;
    }
}
