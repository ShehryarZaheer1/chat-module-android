package com.xevensolutions.chat.networking;


import com.xevensolutions.baseapp.utils.CacheManager;
import com.xevensolutions.chat.utils.Constants;

import java.util.concurrent.TimeUnit;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebServicesHandler {

    public static WebServices webServices;


    private static WebServicesHandler instance = new WebServicesHandler();

    public static WebServicesHandler getInstance() {
        return instance;
    }

    private WebServicesHandler() {

        try {
//            initInstance(SharedData.getInstance().getCurrentUser().get);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public WebServices initInstance(String baseUrl) {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.readTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS);
        httpClient.connectTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS);
        httpClient.writeTimeout(Constants.REQUEST_TIMEOUT, TimeUnit.SECONDS);
        httpClient.addInterceptor(chain -> {

            Request.Builder newRequest = chain.request().newBuilder();
            String isAuthorizableString = chain.request().header("isAuthorizable");
            boolean shouldAddAuthHeaders = isAuthorizableString == null || isAuthorizableString.equalsIgnoreCase("true");

/*
                if (!chain.request().url().toString().contains("/api/property/getProperties"))
*/

            if (shouldAddAuthHeaders) {
                newRequest.addHeader("Authorization", "Bearer " + com.xevensolutions.chat.utils.CacheManager.getCurrentUser().getAuthToken());
                newRequest.addHeader("Accept-Language", CacheManager.getAppLanguage() != null ? CacheManager.getAppLanguage() : "en-us");
            }


            newRequest.removeHeader("isAuthorizable");
            return chain.proceed(newRequest.build());
        });


        String url = baseUrl;
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(url)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build());

        Retrofit retrofit = builder.build();
        webServices = retrofit.create(WebServices.class);
        return webServices;


    }


    public static WebServices getWebServices() {
        return webServices;
    }
}

 