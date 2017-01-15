package tpdev.megaphone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import tpdev.megaphone.R;
import tpdev.megaphone.db.Message;

/**
 * Created by Giuseppe Federico on 15/01/2017.
 */

public class MessageAdapter extends ArrayAdapter<Message>{
    private final int resource;

    public MessageAdapter(Context context, int resource, List<Message> objects) {
        super(context, resource, objects);
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(resource, null);
        }

        final Message item = getItem(position);

        TextView tv = (TextView)convertView.findViewById(R.id.textViewMessageItem);
        tv.setText(item.getText());


        return convertView;
    }
}

