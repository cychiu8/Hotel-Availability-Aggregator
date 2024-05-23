package notification.service.model;

import java.util.List;

public class LineMessageBody {
    private List<LineMessage> messages;

    public List<LineMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<LineMessage> messages) {
        this.messages = messages;
    }
}
