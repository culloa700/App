package com.example.evaluacionapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.evaluacionapp.Fragments.PreloadFragment;
import com.example.evaluacionapp.Models.AppHelper;
import com.example.evaluacionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout textInputLayoutUser;
    private TextInputLayout textInputLayoutPassword;
    private MaterialButton buttonLogin;
    private MaterialTextView textViewRegister;

    private FirebaseAuth firebaseAuth;
    PreloadFragment preloadFragment;
    private SharedPreferences preferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bind();
        firebaseAuth = FirebaseAuth.getInstance();
        buttonLogin.setOnClickListener(this);
        textViewRegister.setOnClickListener(this);
        Bundle extras = getIntent().getExtras();
        if(extras != null){
            textInputLayoutUser.getEditText().setText(extras.getString("user"));
            textInputLayoutPassword.requestFocus();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonLogin:
                LoginUser();
                break;
            case R.id.textViewRegisterLogin:
                changeActivity(new RegisterUserActivity());
        }
    }

    private void LoginUser(){
        preload();

        final String user =  textInputLayoutUser.getEditText().getText().toString().trim();
        String password =  textInputLayoutPassword.getEditText().getText().toString().trim();

        if(TextUtils.isEmpty(user)){
            textInputLayoutUser.setError(getText(R.string.msgInputEmpty));
            //se cierra el dialogfragment del preload
            preloadFragment.dismiss();
            return;
        }else if(!AppHelper.validateEmail(user)){
            textInputLayoutUser.setError(getText(R.string.msgEnterEmail));
            preloadFragment.dismiss();
            return;
        }

        if(TextUtils.isEmpty(password)){
            textInputLayoutPassword.setError(getText(R.string.msgInputEmpty));
            preloadFragment.dismiss();
            return;
        }

        firebaseAuth.signInWithEmailAndPassword(user,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    changeActivity(new MainActivity());
                    saveOnPreference(true, user);
                    preloadFragment.dismiss();
                }else{
                    Toast.makeText(LoginActivity.this, R.string.msgLoginError, Toast.LENGTH_SHORT).show();
                    preloadFragment.dismiss();
                }
            }
        });
    }

    private void saveOnPreference(boolean log, String userName){
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("Log",log); // se envia si inicio sesion
        editor.putString("user",userName);
        editor.apply();
    }

    private void preload(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        preloadFragment = new PreloadFragment();
        preloadFragment.show(fragmentManager, "PreloadFragment");
    }

    private void changeActivity(Activity activity){
        Intent intent = new Intent(LoginActivity.this, activity.getClass());
        intent.putExtra("name", textInputLayoutUser.getEditText().getText().toString());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void Bind(){
        preferences = getSharedPreferences("Login", Context.MODE_PRIVATE);
        textInputLayoutUser = findViewById(R.id.textInputLayoutUserLogin);
        textInputLayoutPassword = findViewById(R.id.textInputLayoutPasswordLogin);
        buttonLogin = findViewById(R.id.buttonLogin);
        textViewRegister = findViewById(R.id.textViewRegisterLogin);
    }
}
