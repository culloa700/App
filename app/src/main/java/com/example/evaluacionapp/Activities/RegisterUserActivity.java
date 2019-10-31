package com.example.evaluacionapp.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.evaluacionapp.Fragments.PreloadFragment;
import com.example.evaluacionapp.Models.AppHelper;
import com.example.evaluacionapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout textInputEditTextUser;
    private TextInputLayout textInputEditTextPassword;
    private TextInputLayout textInputEditTextConfirmPassword;
    private MaterialButton materialButtonRegister;
    private TextView textViewLoginRegister;

    private FirebaseAuth firebaseAuth;
    PreloadFragment preloadFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        Bind();
        //eventos de click
        materialButtonRegister.setOnClickListener(this);
        textViewLoginRegister.setOnClickListener(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.buttonAcceptRegister:
                registerUser();
                break;
            case R.id.textViewLoginRegister:
                Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }

    }
    //valida y registra los datos del usuario
    private void registerUser(){
        //se muestra el dialogdragment de preload
        Preload();
        //se eliminan los errores generados
        textInputEditTextUser.setErrorEnabled(false);
        textInputEditTextPassword.setErrorEnabled(false);
        textInputEditTextConfirmPassword.setErrorEnabled(false);
        //se obtiene el valor de los inputs
        final String user =  textInputEditTextUser.getEditText().getText().toString();
        String password =  textInputEditTextPassword.getEditText().getText().toString();
        String confirmPassword =  textInputEditTextConfirmPassword.getEditText().getText().toString();
        //validacion de inputs
        if(TextUtils.isEmpty(user)){
            textInputEditTextUser.setError(getText(R.string.msgInputEmpty));
            //se cierra el dialogfragment del preload
            preloadFragment.dismiss();
            return;
        }else if(!AppHelper.validateEmail(user)){
            textInputEditTextUser.setError(getText(R.string.msgEnterEmail));
            preloadFragment.dismiss();
            return;
        }

        if(TextUtils.isEmpty(password)){
            textInputEditTextPassword.setError(getText(R.string.msgInputEmpty));
            preloadFragment.dismiss();
            return;
        }
        if(TextUtils.isEmpty(confirmPassword)){
            textInputEditTextConfirmPassword.setError(getText(R.string.msgInputEmpty));
            preloadFragment.dismiss();
            return;
        }
        //se validan las contraseñas
        if(!password.contains(confirmPassword)){
            Toast.makeText(this, R.string.msgMatchPassword, Toast.LENGTH_SHORT).show();
            preloadFragment.dismiss();
            return;
        }
        //evento para registrarse en firebase mediante el email
        firebaseAuth.createUserWithEmailAndPassword(user,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterUserActivity.this, R.string.msgSuccssefulRegister, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterUserActivity.this, LoginActivity.class);
                    intent.putExtra("user",user);
                    startActivity(intent);
                    preloadFragment.dismiss();
                }else{
                    Toast.makeText(RegisterUserActivity.this, R.string.msgErrorRegister, Toast.LENGTH_SHORT).show();
                    preloadFragment.dismiss();
                }
            }
        });
    }

    private void Preload(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        preloadFragment = new PreloadFragment();
        preloadFragment.show(fragmentManager, "PreloadFragment");
    }
    //referencias
    private void Bind(){
        textInputEditTextUser = findViewById(R.id.textInputLayoutUserRegister);
        textInputEditTextPassword = findViewById(R.id.textInputLayoutPasswordRegister);
        textInputEditTextConfirmPassword = findViewById(R.id.textInputLayoutConfirmRegister);
        materialButtonRegister = findViewById(R.id.buttonAcceptRegister);
        textViewLoginRegister = findViewById(R.id.textViewLoginRegister);
    }
}
