package com.chensd.funnydemo.retrofit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by chen on 2017/1/13.
 */
public class RetrofitApi {

    private static volatile ImageApi imageApi = null;
    private static GsonConverterFactory converterFactory = GsonConverterFactory.create();
    private static RxJavaCallAdapterFactory callAdapterFactory = RxJavaCallAdapterFactory.create();
    private static OkHttpClient okHttpClient = new OkHttpClient();

    public static ImageApi getImageApi(){
        if (imageApi == null){
            synchronized (RetrofitApi.class){
                if (imageApi == null){
                    Retrofit retrofit = new Retrofit.Builder()
                            .client(okHttpClient)
                            .baseUrl(imageApi.BASE_URL)
                            .addConverterFactory(converterFactory)
                            .addCallAdapterFactory(callAdapterFactory)
                            .build();
                    imageApi = retrofit.create(ImageApi.class);
                }
            }
        }
        return imageApi;
    }
}
