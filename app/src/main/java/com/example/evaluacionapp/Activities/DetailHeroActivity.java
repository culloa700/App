package com.example.evaluacionapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.evaluacionapp.Models.ListHeroes;
import com.example.evaluacionapp.R;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

public class DetailHeroActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageButton buttonClose;
    private ImageView imageViewDetail;
    private TextView textViewDetail;

    RequestQueue queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hero);
        Bind();
        buttonClose.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            setData(extras.getString("image"), extras.getString("descriptions"), extras.getString("ext"));
        }
    }

    @Override
    public void onClick(View view) {
        onBackPressed();
    }

    private void setData(String image, String description, String ext){
        Picasso.get().load(image + "." + ext).fit().centerCrop().into(imageViewDetail);
        if(description.isEmpty())
            textViewDetail.setText(R.string.emptyDescription);
        else
            textViewDetail.setText(description);

    }

    private void Bind() {
        queue = Volley.newRequestQueue(this);
        buttonClose = findViewById(R.id.buttonCloseDetail);
        imageViewDetail = findViewById(R.id.imageViewDetailActivity);
        textViewDetail = findViewById(R.id.textViewDetailHeroActivity);
    }
}
