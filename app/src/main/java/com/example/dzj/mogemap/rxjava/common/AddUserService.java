package com.example.dzj.mogemap.rxjava.common;

import com.example.dzj.mogemap.modle.Mogemap_user;
import com.example.dzj.mogemap.modle.RunRecords;
import com.example.dzj.mogemap.utils.HttpUtil;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface AddUserService {
    @Streaming
    @POST(HttpUtil.ADD_USER_URL)
    Observable<Mogemap_user> addUser(@Body Mogemap_user user);
}
