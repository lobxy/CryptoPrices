package com.lobxy.cryptoprices.Controller;

import com.lobxy.cryptoprices.Model.CryptoData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RetrofitInterface {
    @GET("latest")
    Call<CryptoData> getCryptoData(@Query("CMC_PRO_API_KEY") String apiKey, @Query("start") int start, @Query("limit") int limit, @Query("sort") String param, @Query("sort_dir") String order);

}
