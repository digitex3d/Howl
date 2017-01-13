package tpdev.megaphone;

import android.net.Uri;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import tpdev.megaphone.db.Message;
import tpdev.megaphone.db.MessageCollection;
import tpdev.megaphone.db.MessageFactory;

import static android.R.attr.name;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static tpdev.megaphone.db.MessageFactory.generateMessage;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ArrayList<MarkerOptions> listMarker;
    private GoogleMap mMap;


    EditText message_editText;
    Button shout_button;

    FirebaseDatabase database;
    DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Database
        database = FirebaseDatabase.getInstance();
        databaseRef = database.getReference();

        // Ref to EditText
        message_editText = (EditText) findViewById(R.id.message_editText);

        // Init to shout button
        shout_button = (Button) findViewById(R.id.shout_button);

    }


    /**
     * Fonction pour afficher un message sur la map
     */
    public void shout_message(View v){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            String email = user.getEmail();

            String msg = this.message_editText.getText().toString();

            // Get push key
            String key = databaseRef.child("messages").push().getKey();

            // Create message
            Message db_message=MessageFactory.generateMessage(msg,"48.862725", "2.287592000000018", email  );

            // Get map value
            Map<String, Object> messageValues = db_message.toMap();

            Map<String, Object> childUpdates = new HashMap<>();
            childUpdates.put("/messages/" + key, messageValues);
            databaseRef.updateChildren(childUpdates);

            add_message_marker(email, msg);

        }



    }

    public void add_message_marker(String user, String msg){
        // Add a marker in Sydney and move the camera
        LatLng jussieu = new LatLng(48.862725, 2.287592000000018);

        MarkerOptions shout_marker = new MarkerOptions().position(jussieu)
                .title(user)
                .snippet(msg);

        mMap.addMarker(shout_marker);
        //float zoom = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jussieu, 7));

        listMarker.add(shout_marker);


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                marker.showInfoWindow();
                //marker.remove();

                return true;
            }
        });

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                MarkerOptions newPoint = new MarkerOptions().position(latLng).title("lat : "+latLng.latitude+", long :"+latLng.longitude);
                listMarker.add(newPoint);
                mMap.addMarker(newPoint);
            }
        });

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

    }

    //add a timer to the Marker to delete them (map + list) when timer is reached
    /*private Marker addMarker(MarkerOptions m, int timer){



    }*/

    private boolean inRadius(LatLng l1, LatLng l2){
        return (Math.abs(l1.latitude-l2.latitude) > 0.05);
    }
}
