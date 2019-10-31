package com.example.evaluacionapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.evaluacionapp.Adapters.HeroesAdapter;
import com.example.evaluacionapp.Fragments.PreloadFragment;
import com.example.evaluacionapp.Models.ListHeroes;
import com.example.evaluacionapp.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private TextView editTextUserHeader;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private RecyclerView recyclerView;

    private List<ListHeroes> list;
    private RecyclerView.LayoutManager layoutManager;

    RequestQueue queue;
    PreloadFragment preloadFragment;
    private SharedPreferences preferences = null;
    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Bind();
        Init();
    }

    private void Init() {
        //se inicializa toolbar
        setToolbar();
        //instncia firebase
        firebaseAuth = FirebaseAuth.getInstance();
        //se obtienes las putextras de el intent de login
        Bundle extras = getIntent().getExtras();
        if (extras != null) { // se obtiene el nombre del usuario desde las putextra o desde las sharedpreferences
            editTextUserHeader.setText(extras.getString("name"));
        }else {
            editTextUserHeader.setText(getPreferencesUser());
        }
        //evento para el menu
        navigationView.setNavigationItemSelectedListener(this);
        //se obtiene el listado de los heroes
        getHeroes();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logOff) {
            removeShared();
            firebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        return false;
    }
    //se envian los datos al adapter y se llena el recyclerview
    private void getData() {
        HeroesAdapter adapter = new HeroesAdapter(list, R.layout.hero_list_layout, new HeroesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailHeroActivity.class);
                intent.putExtra("image", list.get(position).getImage());
                intent.putExtra("description", list.get(position).getDetail());
                startActivity(intent);
                overridePendingTransition(R.anim.transition, R.anim.transitionout);
            }
        });
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            layoutManager = new GridLayoutManager(this, 3);
        }else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            layoutManager = new GridLayoutManager(this, 2);
        }
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        preloadFragment.dismiss();
    }
    //obtiene la informacion del api de los superheroes
    private void getHeroes() {
        //muestra el preload
        Preload();
        final String url = "https://gateway.marvel.com/v1/public/characters?limit=35&ts=1&apikey=f77f0d0cae9203fd42f6cfa1788844bb&hash=a6031e18752391d9b586837f605c838a";
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            //objetos para ingresar a la informacion que se necesita
                            JSONObject data = response.getJSONObject("data");
                            JSONArray results = data.getJSONArray("results");
                            //se crea un nuevo json con los datos convinados de "data" y el path que se obtiene de "thumbnail"
                            JSONArray json = new JSONArray();
                            //se recorre el json de response para obtener la informacion y convinarla en un json
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject jsonResults = results.getJSONObject(i);
                                JSONObject jsonThumbnail = results.getJSONObject(i).getJSONObject("thumbnail");
                                String path = jsonThumbnail.getString("path");
                                JSONObject jsonParse = new JSONObject();
                                jsonParse.put("id", jsonResults.getString("id"));
                                jsonParse.put("name", jsonResults.getString("name"));
                                jsonParse.put("description", jsonResults.getString("description"));
                                jsonParse.put("path", path);
                                json.put(jsonParse);
                            }
                            //el json generado se inserta a la lista por medio de gson
                            Type listHeroes = new TypeToken<List<ListHeroes>>() {
                            }.getType();
                            list = new Gson().fromJson(String.valueOf(json), listHeroes);
                            //se llama el metodo para que llene el recyclerview
                            getData();

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(MainActivity.this, "" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error.Response", error.toString());
                        Toast.makeText(MainActivity.this, "" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        );
// add it to the RequestQueue
        queue.add(getRequest);
    }
    //elimina las sharedprefrences para la validacion en splashscreen
    private void removeShared() {// borra el cache para indicar que cerro sesion
        preferences.edit().clear().apply();
    }
    //obtiene el nombre del usuario
    private String getPreferencesUser() { // obtiene el nombre del usuario
        return preferences.getString("user", null);
    }

    private void Preload() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        preloadFragment = new PreloadFragment();
        preloadFragment.show(fragmentManager, "PreloadFragment");
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(R.string.app_name);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void Bind() {
        preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        queue = Volley.newRequestQueue(this);
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.navview);
        recyclerView = findViewById(R.id.recyclerHeroes);
        list = new ArrayList<>();
        View navigationHeader = navigationView.getHeaderView(0);
        editTextUserHeader = navigationHeader.findViewById(R.id.nameUserHeader);
    }
}
