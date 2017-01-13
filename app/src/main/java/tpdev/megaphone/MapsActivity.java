package tpdev.megaphone;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private ArrayList<MarkerOptions> listMarker;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
        LatLng jussieu = new LatLng(48.862725, 2.287592000000018);
        MarkerOptions juss = new MarkerOptions().position(jussieu).title("lat : "+jussieu.latitude+", long : "+jussieu.longitude).snippet("Jussieu");
        mMap.addMarker(juss);
        //float zoom = 16;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(jussieu, 7));

        LatLng point1 = new LatLng(50, 3);
        MarkerOptions p1 = new MarkerOptions().position(point1).title("lat : "+point1.latitude+", long : "+point1.longitude).snippet("Point1");
        mMap.addMarker(p1);

        listMarker.add(juss);
        listMarker.add(p1);


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

    //add a timer to the Marker to delete them (map + list) when timer is reached
    /*private Marker addMarker(MarkerOptions m, int timer){



    }*/

    private boolean inRadius(LatLng l1, LatLng l2){
        return (Math.abs(l1.latitude-l2.latitude) > 0.05);
    }
}
