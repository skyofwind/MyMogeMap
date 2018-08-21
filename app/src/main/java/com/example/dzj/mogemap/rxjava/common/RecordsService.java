package com.example.dzj.mogemap.rxjava.common;

import com.example.dzj.mogemap.modle.RunRecords;
import com.example.dzj.mogemap.utils.HttpUtil;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecordsService {
    @GET(HttpUtil.GET_RECORDS)
    Observable<RunRecords> getRecords(@Path("phone") String phone);
}
