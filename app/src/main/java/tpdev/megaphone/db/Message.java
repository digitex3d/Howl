package tpdev.megaphone.db;

/**
 * Created by giuseppe on 12/01/17.
 */

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

import static android.R.attr.author;

/**
 * Les informations contenues dans un message
 */
public class Message {
    private String text;
    private double lat;
    private double lon;
    private String user;
    private boolean visible;

    public Message() {
        // Default constructor required for calls to DataSnapshot.getValue(Message.class)
    }

    public Message(String text, double lat, double lon, String user, boolean visible) {
        this.text = text;
        this.lat = lat;
        this.lon = lon;
        this.user = user;
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getText() {
        return text;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getUser() {
        return user;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("text", text);
        result.put("lat", lat);
        result.put("lon", lon);
        result.put("user", user);


        return result;
    }
}
