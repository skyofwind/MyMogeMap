package com.example.dzj.mogemap.rxjava.common;

import com.example.dzj.mogemap.modle.Mogemap_run_record;
import com.example.dzj.mogemap.modle.Mogemap_user;
import com.example.dzj.mogemap.utils.HttpUtil;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface AddRecordService {
    @Streaming
    @POST(HttpUtil.ADD_RECORD)
    Observable<Mogemap_run_record> addUser(@Body Mogemap_run_record record);
}
