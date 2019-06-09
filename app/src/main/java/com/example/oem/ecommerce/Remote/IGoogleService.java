package com.example.oem.ecommerce.Remote;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by avneesh jaiswal on 23-Mar-18.
 */

public interface IGoogleService {
    @GET
    Call<String> getAddressName(@Url String url);
}
