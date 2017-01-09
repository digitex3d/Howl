package tpdev.megaphone;

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
    protected Map collection_users;


    public DataBase() {
        this.collection_users = new HashMap();

    }

    public Map getCollection_users() {
        return collection_users;

    }

}
