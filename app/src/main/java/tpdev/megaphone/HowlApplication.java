package tpdev.megaphone;

import android.app.Application;

import tpdev.megaphone.tpdev.megaphone.db.DataBase;

/**
 * Created by giuseppe on 09/01/17.
 */

public class HowlApplication extends Application {

    private DataBase db;

    public DataBase getDataBase() {
        return this.db;
    }

    public void setSomeVariable( DataBase db) {
        this.db = db;
    }
}
