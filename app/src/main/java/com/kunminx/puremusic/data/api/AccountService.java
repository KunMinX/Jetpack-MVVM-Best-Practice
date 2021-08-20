package com.kunminx.puremusic.data.api;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Create by KunMinX at 2021/6/3
 */
public interface AccountService {

    @POST("xxx/login")
    @FormUrlEncoded
    Call<String> login(
        @Field("username") String username,
        @Field("password") String password
    );

}
