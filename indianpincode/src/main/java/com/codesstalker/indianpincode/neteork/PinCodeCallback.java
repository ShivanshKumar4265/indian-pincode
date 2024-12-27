package com.codesstalker.indianpincode.neteork;

import java.util.List;

public interface PinCodeCallback {
    void onSuccess(String state, String district, List<String> cities);
    void onFailure(String errorMessage);
}
