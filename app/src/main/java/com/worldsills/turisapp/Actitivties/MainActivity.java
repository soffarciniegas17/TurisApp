package com.worldsills.turisapp.Actitivties;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.worldsills.turisapp.Fragments.InicioFragment;
import com.worldsills.turisapp.Fragments.ListaFragment;
import com.worldsills.turisapp.Interfaces.ComunicaFragment;
import com.worldsills.turisapp.R;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ComunicaFragment {


    private MenuItem menuItem;
    private boolean vista;
    private String tipoLugar;
    private int fragmentActivo, itemPresionado;
    private FloatingActionButton fab;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        permisos();

         fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getApplicationContext(), Mapa.class);
                intent.putExtra("FRAGMENT_ACTIVO",fragmentActivo);
                intent.putExtra("CATEG",tipoLugar);
                intent.putExtra("ITEM",itemPresionado);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(fragmentActivo==2) {
            cargaFragment(1, tipoLugar);
        }else {
                super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        menuItem=menu.findItem(R.id.action_vista);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
        }else if(id==R.id.action_vista){
            if (vista){
                vista=false;
                menuItem.setIcon(R.drawable.icono_grid);
            }else{
                vista=true;
                menuItem.setIcon(R.drawable.icon_list);

            }
        }
        cargaFragment(fragmentActivo,tipoLugar);

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            fragmentActivo=0;

        } else if (id == R.id.nav_sitios) {
            cargaFragment(1,"sitios");

        } else if (id == R.id.nav_hoteles) {
            cargaFragment(1,"hotel");

        } else if (id == R.id.nav_restaurantes) {
            cargaFragment(1,"restaurante");

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void cargaFragment(int fragment, String tipoL){
        fragmentActivo=fragment;
        tipoLugar=tipoL;
        FragmentManager manager=getFragmentManager();
        FragmentTransaction transaction=manager.beginTransaction();
        Fragment frag=getFragment(fragmentActivo);

        Bundle datos=new Bundle();
        datos.putBoolean("VISTA",vista);
        datos.putString("CATEG", tipoLugar);
        datos.putInt("ITEM", itemPresionado);
        frag.setArguments(datos);


        if (getResources().getConfiguration().orientation== Configuration.ORIENTATION_LANDSCAPE){
            LinearLayout contenedor1;
            contenedor1=findViewById(R.id.contenedor_fragment_1);
            switch (fragmentActivo){
                case 0:

                    contenedor1.getLayoutParams().width=getResources().getDisplayMetrics().widthPixels;
                    transaction.replace(R.id.contenedor_fragment_1,frag);
                    break;
                case 1:

                    contenedor1.getLayoutParams().width=(int)((getResources().getDisplayMetrics().widthPixels)/1.7);
                    itemPresionado=0;
                    cargaFragment(2,tipoLugar);
                    break;
                case 2:
                    transaction.replace(R.id.contenedor_fragment_2,frag);
                    break;

            }


        }else{

            transaction.replace(R.id.contenedor_fragment_1,frag);

        }
        transaction.commit();

        actualizaPantalla();

    }
    public void actualizaPantalla(){
        if (getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE)
            if (menuItem!=null)menuItem.setVisible(false);

        switch (fragmentActivo){
            case 0:
                toolbar.setTitle("Inicio");
                try{
                    menuItem.setVisible(false);
                }catch (Exception e){}
                fab.setVisibility(View.INVISIBLE);

                break;
            case 1:
                toolbar.setTitle(tipoLugar);
                fab.setImageResource(R.drawable.icono_marcador);
                break;
            case 2:
                toolbar.setTitle("Detalle");
                fab.setImageResource(R.drawable.icono_car);
                try{
                    menuItem.setVisible(false);
                }catch (Exception e){}
                break;
        }
    }

    public Fragment getFragment(int tipo){
        switch (tipo){
            case 0: return new Fragment();
            case 1: return new ListaFragment();
            default: return new Fragment();
        }
    }

    @Override
    public void itemPresionado(int posicion) {
        itemPresionado=posicion;
        cargaFragment(2,tipoLugar);

    }
    public void onResume(){
        super.onResume();
        SharedPreferences datos=PreferenceManager.getDefaultSharedPreferences(this);

        vista=datos.getBoolean("VISTA",true);
        fragmentActivo=datos.getInt("FRAG_ACTIVO",0);
        itemPresionado=datos.getInt("ITEM",0);
        tipoLugar=datos.getString("CATEG","sitios");

        if (getResources().getConfiguration().orientation==Configuration.ORIENTATION_LANDSCAPE && fragmentActivo==2){
            cargaFragment(1,tipoLugar);
        }
        cargaFragment(fragmentActivo,tipoLugar);
    }
    public void onPause(){
        super.onPause();
        SharedPreferences datos= PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor guarda=datos.edit();

        guarda.putBoolean("VISTA",vista);
        guarda.putInt("FRAG_ACTIVO",fragmentActivo);
        guarda.putInt("ITEM",itemPresionado);
        guarda.putString("CATEG",tipoLugar);


        guarda.apply();
    }
    public void permisos(){
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
        ,Manifest.permission.INTERNET},2);

    }
}
