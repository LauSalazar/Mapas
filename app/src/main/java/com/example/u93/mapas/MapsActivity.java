package com.example.u93.mapas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.example.u93.mapas.models.Cinema;
import com.example.u93.mapas.models.CinemaType;
import com.example.u93.mapas.models.Location;
import com.example.u93.mapas.services.Repository;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.ArrayList;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Repository repository;

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

        mMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(this,R.raw.style_map)
        );

        // Add a marker in Sydney and move the camera
        LatLng cedesistemas = new LatLng(6.2092069, -75.5751823);
        mMap.addMarker(new MarkerOptions().position(cedesistemas).title("Cedesistemas"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(cedesistemas));

        createMarkers();
        //getCinemas();
    }

    private void getCinemas() {
        repository = new Repository();
        try{
            ArrayList<CinemaType> cinemaTypes = repository.getCinemas();

            if (cinemaTypes.size() > 0){
                ArrayList<Cinema> cinemas = cinemaTypes.get(0).getCinemas();
                if (cinemas.size()>0){
                    Location location = cinemas.get(0).getLocation();
                    ArrayList<String> coordinates = location.getCordinates();
                    final LatLng cinemaPoint = new LatLng(Double.valueOf(coordinates.get(1)), Double.valueOf(coordinates.get(0)));
                    mMap.addMarker(new MarkerOptions().position(cinemaPoint).title(cinemas.get(0).getName()));

                    centerPoints(new ArrayList<LatLng>() {{add(cinemaPoint);}});
                }
            }

        } catch (IOException e){

        }
    }

    private void createMarkers() {
        final LatLng primerPunto = new LatLng(6.2487758,-75.5761394);
        mMap.addMarker(new MarkerOptions().position(primerPunto).title("Mi Casa")
        .icon(bitmapDescriptorFromVector(this,R.drawable.ic_audiotrack_black_24dp)));

        final LatLng segundoPunto = new LatLng(6.266501, -75.559917);
        mMap.addMarker(new MarkerOptions().position(segundoPunto).title("La casa de mi mama")
        .icon(bitmapDescriptorFromVector(this,R.drawable.ic_attachment_black_24dp)));

        final LatLng tercerPunto = new LatLng(6.2469414,-75.5684576);
        mMap.addMarker(new MarkerOptions().position(tercerPunto).title("Tercer punto"));

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(primerPunto,18));

        //mMap.addPolyline(new PolylineOptions().add(primerPunto, segundoPunto).width(4).color(Color.BLUE));

        ArrayList<LatLng> points = new ArrayList<LatLng>(){{add(primerPunto); add(segundoPunto); add(tercerPunto);}};
        centerPoints(points);
        calculateRoute(points);
    }

    private void centerPoints(ArrayList<LatLng> puntos){
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(int i=0; i< puntos.size(); i++){
            builder.include(puntos.get(i));
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds,50);
        mMap.animateCamera(cameraUpdate);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context,vectorId);
        vectorDrawable.setBounds(0,0,vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight());

        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(),vectorDrawable.getIntrinsicHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private void calculateRoute(ArrayList<LatLng> puntos){
        Routing routing = new Routing.Builder().travelMode(AbstractRouting.TravelMode.DRIVING)
                .waypoints(puntos)
                .key(getString(R.string.google_maps_key))
                .optimize(true)
                .withListener(routingListener)
                .build();
        routing.execute();
    }

    RoutingListener routingListener = new RoutingListener() {
        @Override
        public void onRoutingFailure(RouteException e) {
            Log.e("RoutingListener",e.getMessage());
        }

        @Override
        public void onRoutingStart() {
            Log.i("RoutingListener","Iniciando ruta");
        }

        @Override
        public void onRoutingSuccess(ArrayList<Route> routes, int index) {
            ArrayList polyLines = new ArrayList();
            for (int i = 0; i<routes.size();i++){
                PolylineOptions polylineOptions = new PolylineOptions();
                polylineOptions.color(ContextCompat.getColor(getApplicationContext(),R.color.colorPrimary));
                polylineOptions.width(5);
                polylineOptions.addAll(routes.get(i).getPoints());
                Polyline polyline = mMap.addPolyline(polylineOptions);
                polyLines.add(polyline);

                int distance = routes.get(i).getDistanceValue();
                int duration = routes.get(i).getDurationValue();
                Log.i("onRoutingSucess","Distancia: "+distance+", Duracion: "+duration);
            }

        }

        @Override
        public void onRoutingCancelled() {

        }
    };


}
