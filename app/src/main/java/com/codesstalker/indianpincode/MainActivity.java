package com.codesstalker.indianpincode;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.codesstalker.indianpincode.neteork.PinCodeCallback;
import com.codesstalker.indianpincode.neteork.PinCodeResponse;
import com.codesstalker.indianpincode.network.PinCodeService;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PinCodeService.fetchPincodeDetails("226020", new PinCodeCallback() {
            @Override
            public void onSuccess(String state, String district, List<String> cities) {
                // Handle the response
                Log.d("PinCodeDetails", "State: " + state);
                Log.d("PinCodeDetails", "District: " + district);

                // Join the cities list into a single string and log it
                if (cities != null && !cities.isEmpty()) {
                    String citiesString = String.join(", ", cities);  // Java 8+ way
                    Log.d("PinCodeDetails", "Cities: " + citiesString);
                } else {
                    Log.d("PinCodeDetails", "No cities found.");
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                // Handle the error
                Log.e("PinCodeDetails", "Error: " + errorMessage);
                // Optionally show an error message to the user
            }
        });



    }
}