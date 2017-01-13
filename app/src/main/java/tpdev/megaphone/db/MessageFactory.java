package tpdev.megaphone.db;

import static android.os.Build.VERSION_CODES.M;

/**
 * Created by giuseppe on 12/01/17.
 */

public class MessageFactory {
    public static Message generateMessage(String text, String lat, String lon, String user) {
        boolean flag = true;

        // Validation paramètres
        if (text.length() == 0) flag = false;

        if (lat.length() == 0) flag = false;

        if (lon.length() == 0) flag = false;

        // Validation user
        if (user.length() == 0) flag = false;

        if (flag)
            return new Message(text, lat, lon, user);

        return null;

    }

}
