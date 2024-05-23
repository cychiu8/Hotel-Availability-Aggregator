package hotels.search.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class DestinationMap {

    @Id
    @Column(name = "dest")
    private String dest;

    @Column(name = "booking_id")
    private String bookingId;

    @Column(name = "jalan_ken_id")
    private String jalanKenId;

    @Column(name = "jalan_lrg_id")
    private String jalanLrgId;

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getJalanKenId() {
        return jalanKenId;
    }

    public void setJalanKenId(String jalanKenId) {
        this.jalanKenId = jalanKenId;
    }

    public String getJalanLrgId() {
        return jalanLrgId;
    }

    public void setJalanLrgId(String jalanLrgId) {
        this.jalanLrgId = jalanLrgId;
    }

    // Getters and setters for each field

}
