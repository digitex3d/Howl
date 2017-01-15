package tpdev.megaphone;

import android.app.Application;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import tpdev.megaphone.db.Message;

/**
 * Created by giuseppe on 15/01/17.
 */
public class MegaphoneApplication extends Application {

    private List<Message> messageList;

    public void onCreate(){
        Log.i("Application", "onCreate");
        messageList = new ArrayList<>();
    }

    public List<Message> getMessagesList(){
        return messageList;
    }

}
