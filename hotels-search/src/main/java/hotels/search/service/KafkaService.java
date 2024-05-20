package hotels.search.service;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import javax.annotation.PostConstruct;
import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.ListTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class KafkaService {

    @Autowired
    private AdminClient adminClient;

    @Value("${kafka.topic.name}")
    private String topicName;

    @PostConstruct
    public void createTopic() {
        try {
            ListTopicsResult listTopics = adminClient.listTopics();
            Set<String> topicNames = listTopics.names().get();

            if (!topicNames.contains(topicName)) {
                NewTopic newTopic = new NewTopic(topicName, 1, (short) 1);
                adminClient.createTopics(List.of(newTopic));
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

}
