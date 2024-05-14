package hotels.search.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.GenericGenerator;

@Entity
public class SearchResult {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
        name = "UUID",
        strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "execute_time")
    private LocalDateTime executeTime;

    @Column(name = "response_time")
    private long responseTime;

    @Column(name = "number_of_results")
    private int numberOfResults;

    @Column(name = "min_price")
    private int minPrice;

    @Column(name = "max_price")
    private int maxPrice;

    @Column(name = "execute_url")
    private String executeUrl;

    @ManyToOne
    @JoinColumn(name = "search_condition_id")
    private SearchCondition searchCondition;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(LocalDateTime executeTime) {
        this.executeTime = executeTime;
    }

    public long getResponseTime() {
        return responseTime;
    }

    public void setResponseTime(long responseTime) {
        this.responseTime = responseTime;
    }

    public int getNumberOfResults() {
        return numberOfResults;
    }

    public void setNumberOfResults(int numberOfResults) {
        this.numberOfResults = numberOfResults;
    }

    public int getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(int minPrice) {
        this.minPrice = minPrice;
    }

    public int getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(int maxPrice) {
        this.maxPrice = maxPrice;
    }

    public String getExecuteUrl() {
        return executeUrl;
    }

    public void setExecuteUrl(String executeUrl) {
        this.executeUrl = executeUrl;
    }

    public SearchCondition getSearchCondition() {
        return searchCondition;
    }

    public void setSearchCondition(SearchCondition searchCondition) {
        this.searchCondition = searchCondition;
    }
    
    @Override
    public String toString() {
        return "SearchResult{" +
            "executeTime=" + executeTime +
            ", responseTime=" + responseTime +
            ", numberOfResults=" + numberOfResults +
            ", minPrice=" + minPrice +
            ", maxPrice=" + maxPrice +
            ", executeUrl='" + executeUrl + '\'' +
            '}';
    }
}