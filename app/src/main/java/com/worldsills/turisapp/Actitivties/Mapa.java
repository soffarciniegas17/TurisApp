package com.worldsills.turisapp.Actitivties;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.maps.android.PolyUtil;
import com.worldsills.turisapp.Interfaces.ConsumirServicios;
import com.worldsills.turisapp.ItemLugar;
import com.worldsills.turisapp.R;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Mapa extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private int fragmentActivo;
    private String tipoLugar;
    private int itemPresionado;
    private LatLng origin, destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Bundle datos = getIntent().getExtras();

        if (datos != null) {
            itemPresionado = datos.getInt("ITEM");
            fragmentActivo = datos.getInt("FRAGMENT_ACTIVO");
            tipoLugar = datos.getString("CATEG");
        } else {
            Toast.makeText(this, "null", Toast.LENGTH_SHORT).show();
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

        consumeServiciosLugares(tipoLugar);
    }

    public void consumeServiciosLugares(String tipoL) {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.url_base_lugares))
                .addConverterFactory(GsonConverterFactory.create()).build();

        ConsumirServicios servicios = retrofit.create(ConsumirServicios.class);

        Call<List<ItemLugar>> res = servicios.getLugares(tipoL);

        res.enqueue(new Callback<List<ItemLugar>>() {
            @Override
            public void onResponse(Call<List<ItemLugar>> call, Response<List<ItemLugar>> response) {
                if (fragmentActivo == 1) agregaMarcadoresLugares(response.body());
                else actualizaPantalla(response.body());
            }

            @Override
            public void onFailure(Call<List<ItemLugar>> call, Throwable t) {

            }
        });
    }

    public void agregaMarcadoresLugares(List<ItemLugar> lugares) {


        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (int i = 0; i < lugares.size(); i++) {
            ItemLugar lugar = lugares.get(i);

            mMap.addMarker(new MarkerOptions().title(lugar.getNombre())
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                    .position(new LatLng(lugar.getLatitud(), lugar.getLongitud())));

            builder.include(new LatLng(lugar.getLatitud(), lugar.getLongitud()));
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 0));

    }
    public void actualizaPantalla(List<ItemLugar> lugares){
        ItemLugar lugar = lugares.get(itemPresionado);
        destination = new LatLng(lugar.getLatitud(),lugar.getLongitud());
        origin = miPosicion();

        mMap.addMarker(new MarkerOptions().title(lugar.getNombre()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED))
        .position(destination));
        mMap.addMarker(new MarkerOptions().title("Mi posicion").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                .position(origin));

        LatLngBounds.Builder builder=new LatLngBounds.Builder();
        builder.include(origin);
        builder.include(destination);

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),0));


        rutaDeViaje(origin, destination);
    }

    public void rutaDeViaje(LatLng origen, LatLng destino) {

        String orig=origen.latitude+","+origen.longitude;
        String dest=destino.latitude+","+destino.longitude;

        Retrofit retrofit = new Retrofit.Builder().baseUrl(getResources().getString(R.string.url_base_rutas))
                .addConverterFactory(GsonConverterFactory.create()).build();

        ConsumirServicios servicios = retrofit.create(ConsumirServicios.class);


        Call<ResponseBody> res = servicios.getRuta(orig,dest);

        res.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                JsonObject obj=null;

                try {
                    obj=new Gson().fromJson(response.body().string(), JsonElement.class).getAsJsonObject();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String points=obj.get("routes").getAsJsonArray().get(0).getAsJsonObject()
                        .get("overview_polyline").getAsJsonObject()
                        .get("points").getAsString();



                mMap.addPolyline(new PolylineOptions().color(Color.BLACK)
                .addAll(PolyUtil.decode(points)));
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    public LatLng miPosicion() {



        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        return new LatLng(location.getLatitude(),location.getLongitude());
    }
}
