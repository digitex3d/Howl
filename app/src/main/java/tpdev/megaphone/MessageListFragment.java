package tpdev.megaphone;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import tpdev.megaphone.db.Message;
import tpdev.megaphone.db.MessageFactory;

import static com.google.android.gms.wearable.DataMap.TAG;


/**
 * A fragment representing a list of Items.
 * <p/>

 * interface.
 */
public class MessageListFragment extends Fragment {


    private Listener mListener;
    private MessageAdapter adapter;


    public MessageListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ListView lv = (ListView)view.findViewById(R.id.messages_listView);

        Log.w(TAG, "INIT ADAPTER");
        adapter = new MessageAdapter(getContext(), R.layout.message_item,
                mListener.getMessagesList());
        lv.setAdapter(adapter);


        adapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();

            }
        });


        mListener.registerAdapter(adapter);
    }



    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        if (context instanceof Listener) {
            mListener = (Listener) context;

        } else {
            throw new RuntimeException(context.toString()
                    + " must implement Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface Listener {
        List<Message> getMessagesList();

        void registerAdapter(MessageAdapter adapter);
    }
}
