package hotels.search.model;

import java.util.UUID;

public class SearchCondition {

    private UUID id;
    private String dest;
    private String checkin;
    private String checkout;
    private int groupAdults;
    private int groupChildren;
    private int noRooms;

    // Getters and setters for each field

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getCheckin() {
        return checkin;
    }

    public void setCheckin(String checkin) {
        this.checkin = checkin;
    }

    public String getCheckout() {
        return checkout;
    }

    public void setCheckout(String checkout) {
        this.checkout = checkout;
    }

    public int getGroupAdults() {
        return groupAdults;
    }

    public void setGroupAdults(int groupAdults) {
        this.groupAdults = groupAdults;
    }

    public int getGroupChildren() {
        return groupChildren;
    }

    public void setGroupChildren(int groupChildren) {
        this.groupChildren = groupChildren;
    }

    public int getNoRooms() {
        return noRooms;
    }

    public void setNoRooms(int noRooms) {
        this.noRooms = noRooms;
    }
}