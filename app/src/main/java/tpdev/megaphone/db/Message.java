package tpdev.megaphone.db;

/**
 * Created by giuseppe on 12/01/17.
 */

/**
 * Les informations contenues dans un message
 */
public class Message {
    private String text;
    private String lat;
    private String lon;
    private String user;

    public Message(String text, String lat, String lon, String user) {
        this.text = text;
        this.lat = lat;
        this.lon = lon;
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getUser() {
        return user;
    }
}
