package user.service.controller;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import user.service.model.SearchCondition;
import user.service.service.SearchConditionService;



@RestController
@RequestMapping("/api/v1/searchcondition")
public class SearchConditionController {

    @Autowired
    SearchConditionService searchConditionService;

    @RequestMapping("/")
    public String home() {
        return "Hello World! user-service is up and running.";
    }

    @PostMapping("/condition")
    public ResponseEntity<SearchCondition> createSearchCondition(@Validated @RequestBody SearchCondition newSearchCondition) {
        try {
            SearchCondition createdSearchCondition = searchConditionService.createSearchCondition(newSearchCondition);
            return new ResponseEntity<>(createdSearchCondition, HttpStatus.CREATED);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/conditions")
    public ResponseEntity<?> searchConditions(@RequestParam(required = false) String filter) {
        try {
            List<SearchCondition> results = searchConditionService.getAllSearchConditions();
            // Implement the logic to search accommodations, possibly using the filter
            return new ResponseEntity<>(results, HttpStatus.OK);
        } catch (Exception e) {
            // Handle the exception here
            System.err.println(e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SearchCondition> updateSearchCondition(@PathVariable UUID id, @Validated @RequestBody SearchCondition updatedSearchCondition) {
        try {
            updatedSearchCondition.setId(id);
            searchConditionService.updateSearchCondition(updatedSearchCondition);
            return new ResponseEntity<>(updatedSearchCondition, HttpStatus.OK);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSearchCondition(@PathVariable UUID id) {
        try {
            searchConditionService.deleteSearchCondition(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        // Log the exception, you can use any logging framework
        System.err.println(e.getMessage());

        // Return a response entity with a status of 500 (Internal Server Error) and the exception message as the body
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
