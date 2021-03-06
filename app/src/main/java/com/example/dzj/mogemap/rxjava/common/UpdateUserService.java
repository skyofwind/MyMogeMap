package com.example.dzj.mogemap.rxjava.common;

import com.example.dzj.mogemap.modle.Mogemap_user;
import com.example.dzj.mogemap.utils.HttpUtil;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface UpdateUserService {
    @Streaming
    @POST(HttpUtil.UPDATE_USER)
    Observable<Mogemap_user> updateUser(@Body Mogemap_user user);
}
