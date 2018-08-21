package com.example.dzj.mogemap.rxjava.common;

import com.example.dzj.mogemap.modle.Mogemap_run_record;
import com.example.dzj.mogemap.utils.HttpUtil;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecordService {
    @GET(HttpUtil.GET_RECORD)
    Observable<Mogemap_run_record> getRecord(@Path("id") int id);
}
