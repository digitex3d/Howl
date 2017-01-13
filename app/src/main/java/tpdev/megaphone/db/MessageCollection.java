package tpdev.megaphone.db;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by giuseppe on 13/01/17.
 */

public class MessageCollection {
    Map<String, Message> messagesCollection;

    public MessageCollection(Map<String, Message> messagesCollection) {
        this.messagesCollection = new HashMap<>();

    }

    public Map<String, Message> getMessagesCollection() {
        return messagesCollection;
    }

    public void add_message(String msg, String lat, String lon, String user){
        messagesCollection.put(user, MessageFactory.generateMessage(msg, lat, lon, user));

    }
}
