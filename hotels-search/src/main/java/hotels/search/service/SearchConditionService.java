package hotels.search.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import hotels.search.model.SearchCondition;

public class SearchConditionService {

    public void createAccommodationSearch(SearchCondition search) throws Exception {
        String sql = "INSERT INTO search_condition (dest, checkin, checkout, group_adults, group_children, no_rooms) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, search.getDest());
            stmt.setString(2, search.getCheckin());
            stmt.setString(3, search.getCheckout());
            stmt.setInt(4, search.getGroupAdults());
            stmt.setInt(5, search.getGroupChildren());
            stmt.setInt(6, search.getNoRooms());

            stmt.executeUpdate();
        }
    }    

    public List<SearchCondition> getAccommodationSearches() throws Exception {
        List<SearchCondition> searches = new ArrayList<>();

        Connection conn = DatabaseConnection.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM search_condition");

        while (rs.next()) {
            SearchCondition search = new SearchCondition();
            search.setDest(rs.getString("dest"));
            search.setCheckin(rs.getString("checkin"));
            search.setCheckout(rs.getString("checkout"));
            search.setGroupAdults(rs.getInt("group_adults"));
            search.setGroupChildren(rs.getInt("group_children"));
            search.setNoRooms(rs.getInt("no_rooms"));
            searches.add(search);
        }

        rs.close();
        stmt.close();
        conn.close();

        return searches;
    }

    public void updateAccommodationSearch(SearchCondition search) throws Exception {
        String sql = "UPDATE search_condition SET dest = ?, checkin = ?, checkout = ?, group_adults = ?, group_children = ?, no_rooms = ? WHERE id = CAST(? AS UUID)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, search.getDest());
            stmt.setString(2, search.getCheckin());
            stmt.setString(3, search.getCheckout());
            stmt.setInt(4, search.getGroupAdults());
            stmt.setInt(5, search.getGroupChildren());
            stmt.setInt(6, search.getNoRooms());
            stmt.setObject(7, search.getId()); // Use setObject to set UUID value

            stmt.executeUpdate();
        }
    }

    public void deleteAccommodationSearch(int id) throws Exception {
        String sql = "DELETE FROM search_condition WHERE id = CAST(? AS UUID)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setObject(1, id, java.sql.Types.OTHER);

            stmt.executeUpdate();
        }
    }
}