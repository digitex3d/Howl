package tpdev.megaphone;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tpdev.megaphone.db.Message;
import tpdev.megaphone.db.MessageCollection;
import tpdev.megaphone.db.MessageFactory;

import static android.R.attr.name;
import static android.R.id.message;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static tpdev.megaphone.db.MessageFactory.generateMessage;

public class MapsActivity extends FragmentActivity implements
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        MessageListFragment.Listener{

    private static final int MY_PERMISSIONS_REQUEST_READ_LOCATION = 0;
    private static final String TAG = "DEBUG";
    private ArrayList<Marker> listMarker;
    private GoogleMap mMap;


    EditText message_editText;
    Button shout_button;

    FirebaseDatabase database;
    DatabaseReference databaseRef;

    // The system Location
    LocationService locationService;


    // Google API
    private GoogleApiClient mGoogleApiClient;
    // Adapter
    private MessageAdapter message_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        this.locationService = LocationService.getLocationManager(this.getApplicationContext());

        // Database
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        // Ref to EditText
        message_editText = (EditText) findViewById(R.id.message_editText);

        // Init to shout button
        shout_button = (Button) findViewById(R.id.shout_button);



        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)) {



            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_READ_LOCATION);


            }
        }


        // DB change event listener
        ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.getKey());

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.getKey());

                databaseRef.child("messages").orderByKey().limitToLast(1).
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                for (DataSnapshot eventSnapshot : dataSnapshot.getChildren()) {

                                    Message m = eventSnapshot.getValue(Message.class);
                                    addMessToHist(m);
                                    add_message_marker(m);
                                    Log.d(TAG, "onDataChanged:" + dataSnapshot.getKey());
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.getKey());


            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.getKey());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "postComments:onCancelled", databaseError.toException());

            }
        };

        databaseRef.addChildEventListener(childEventListener);



    }



    /**
     * Fonction pour afficher un message sur la map
     */
    public void shout_message(View v) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String email = user.getEmail();

            String msg = this.message_editText.getText().toString();

            this.message_editText.setText("");

            // Get push key
            String key = databaseRef.child("messages").push().getKey();

            double lat = locationService.getLatitude();
            double lon = locationService.getLongitude();

            // Create message
            Message db_message = MessageFactory.generateMessage(
                    msg,
                    lat,
                    lon,
                    email);

            // Get map value
            Map<String, Object> messageValues = db_message.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/messages/" + key, messageValues);
            databaseRef.updateChildren(childUpdates);

        }


    }


    /**
     * Add a message on the map
     */
    public void add_message_marker(Message msg_object) {

        LatLng latLng = new LatLng(
                msg_object.getLat(),
                msg_object.getLon()
        );

        String user = msg_object.getUser();
        String msg = msg_object.getText();

        MarkerOptions shout_marker = new MarkerOptions().position(latLng)
                .title(user)
                .snippet(msg);

        final Marker marker = mMap.addMarker(shout_marker);
        marker.showInfoWindow();


        listMarker.add(marker);

        Log.w(TAG, "ADDING MARKER");

        // Remove marker thread
        RemoveMarker runner = new RemoveMarker();
        runner.execute(marker);

    }

    public List<Message> getMessagesList(){
        Log.w(TAG, "Getting list from application");
        return ((MegaphoneApplication) getApplication()).getMessagesList();
    }



    public void addMessToHist(Message msg) {
        // Ajoute msg à l'adapteur
        this.message_adapter.add( msg);
        Log.v(TAG, "Ajoute à l'adapter le msg " + msg.getText());
    }


    @Override
    public void registerAdapter(MessageAdapter adapter) {
        this.message_adapter = adapter;
        Log.v(TAG, "Adapter defini " + this.message_adapter);
    }

    private class RemoveMarker extends AsyncTask<Marker, Void, Marker> {

        @Override
        protected Marker doInBackground(Marker... markers) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return markers[0];
        }



        @Override
        protected void onPreExecute() {

        }


        protected void onPostExecute(Marker result) {
            if( result != null) {
                result.remove();
                Log.w(TAG, "Marked removed");
            }
            else
                Log.w(TAG, "Cant remove marker, it's null");
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        listMarker = new ArrayList<>();

        double lat = locationService.getLatitude();
        double lon = locationService.getLongitude();


        /*double lat = 48.8464111;
        double lg = 2.3548468;*/

        //move the camera to the user's location, with an appropriate zoom
        //googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lg), 7));

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), 7));

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                //marker.remove();

                return true;
            }
        });


    }

    //add a timer to the Marker to delete them (map + list) when timer is reached
    /*private Marker addMarker(MarkerOptions m, int timer){



    }*/

    private boolean inRadius(LatLng l1, LatLng l2) {
        return (Math.abs(l1.latitude - l2.latitude) > 0.05);
    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {


    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnectionFailed(@NonNull     ConnectionResult connectionResult) {

    }
}