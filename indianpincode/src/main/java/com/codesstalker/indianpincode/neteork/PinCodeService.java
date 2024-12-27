package com.codesstalker.indianpincode.network;

import com.codesstalker.indianpincode.neteork.PinCodeCallback;
import com.codesstalker.indianpincode.neteork.PinCodeResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.ArrayList;
import java.util.List;

public class PinCodeService {

    private static final String BASE_URL = "https://api.postalpincode.in/";

    public interface ApiInterface {
        @GET("pincode/{pincode}")
        Call<List<PinCodeResponse>> getPincodeDetails(@Path("pincode") String pincode);
    }

    private static Retrofit getRetrofitInstance() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    // This method fetches pincode details and provides the data through the callback
    public static void fetchPincodeDetails(String pincode, final PinCodeCallback callback) {
        if (!isValidPincode(pincode)) {
            callback.onFailure("Invalid pincode. It must be a 6-digit number.");
            return;
        }

        ApiInterface apiInterface = getRetrofitInstance().create(ApiInterface.class);
        Call<List<PinCodeResponse>> call = apiInterface.getPincodeDetails(pincode);
        call.enqueue(new Callback<List<PinCodeResponse>>() {
            @Override
            public void onResponse(Call<List<PinCodeResponse>> call, Response<List<PinCodeResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<PinCodeResponse> pinCodeResponses = response.body();
                    if (pinCodeResponses.get(0).getStatus().equalsIgnoreCase("success")) {
                        PinCodeResponse pincodeResponse = pinCodeResponses.get(0);

                        // Extract the state, district, and cities
                        if (pincodeResponse.getPostOffices() != null && !pincodeResponse.getPostOffices().isEmpty()) {
                            String state = pincodeResponse.getPostOffices().get(0).getState();
                            String district = pincodeResponse.getPostOffices().get(0).getDistrict();
                            List<String> cities = new ArrayList<>();
                            for (PinCodeResponse.PostOffice postOffice : pincodeResponse.getPostOffices()) {
                                cities.add(postOffice.getName());
                            }

                            // Pass the extracted data to the callback
                            callback.onSuccess(state, district, cities);
                        } else {
                            callback.onFailure("No post offices found for this pincode.");
                        }
                    } else {
                        callback.onFailure("Failed to fetch pincode details. Status: " + pinCodeResponses.get(0).getStatus());
                    }
                } else {
                    callback.onFailure("Response was not successful. Code: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<PinCodeResponse>> call, Throwable t) {
                callback.onFailure("Request failed: " + t.getMessage());
            }
        });
    }

    private static boolean isValidPincode(String pincode) {
        return pincode != null && pincode.matches("\\d{6}");
    }
}
