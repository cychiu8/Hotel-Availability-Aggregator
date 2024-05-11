package hotels.search.service;

import org.springframework.stereotype.Service;
import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

@Service
public class BookingSearchService extends SearchAbstractService {
    @Override
    public String getSearchResult(){

        String url = "https://www.booking.com/searchresults.zh-tw.html?ss=%E9%9D%92%E6%A3%AE%E8%BB%8A%E7%AB%99%EF%BC%88Aomori+Station%EF%BC%89%2C+%E9%9D%92%E6%A3%AE%2C+%E9%9D%92%E6%A3%AE%E7%B8%A3%2C+%E6%97%A5%E6%9C%AC&map=1&efdco=1&label=gen173nr-1FCAEoggI46AdIM1gEaHWIAQGYATC4AQfIAQzYAQHoAQH4AQuIAgGoAgO4AuXl-7EGwAIB0gIkZjk2YzU4MWMtZWQ1MC00YWQ3LWJmZTctY2YyNmE0NTEwZGI12AIG4AIB&sid=7d982f20d955da37b5ed6b046464fddd&aid=304142&lang=zh-tw&sb=1&src_elem=sb&src=index&dest_id=900055309&dest_type=landmark&ac_position=1&ac_click_type=b&ac_langcode=xt&ac_suggestion_list_length=5&search_selected=true&search_pageview_id=5af31ef361410115&ac_meta=GhBiN2NkMWYwN2YxMjgwMDA5IAEoATICeHQ6BmFvbW9yaUAASgBQAA%3D%3D&checkin=2024-07-03&checkout=2024-07-04&group_adults=1&no_rooms=1&group_children=0"; // replace with the actual URL
        String response = "Booking search result";
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
