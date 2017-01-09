package tpdev.megaphone.tpdev.megaphone.db;

/**
 * Created by giuseppe on 09/01/17.
 */

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a database object.
 */
public class DataBase {
    // Users collection
    protected Map<String, User> collection_users;


    public DataBase() {
        this.collection_users = new HashMap<String, User>();

    }

    public HashMap<String, User> getCollection_users() {
        return (HashMap<String, User>) collection_users;

    }

}
