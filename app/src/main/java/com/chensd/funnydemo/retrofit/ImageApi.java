package com.chensd.funnydemo.retrofit;


import com.chensd.funnydemo.model.ImageInfo;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by chen on 2017/1/13.
 */
public interface ImageApi {

    String BASE_URL = "http://www.zhuangbi.info/";

    @GET("search")
    Observable<List<ImageInfo>> search(@Query("q") String key);
}
