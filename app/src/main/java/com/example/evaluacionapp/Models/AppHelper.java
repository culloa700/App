package com.example.evaluacionapp.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Patterns;

import java.util.regex.Pattern;

public class AppHelper {

    //validacion para email
    public static boolean validateEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}
