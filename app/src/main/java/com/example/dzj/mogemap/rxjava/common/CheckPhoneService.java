package com.example.dzj.mogemap.rxjava.common;

import com.example.dzj.mogemap.modle.PhoneType;
import com.example.dzj.mogemap.modle.RunRecords;
import com.example.dzj.mogemap.utils.HttpUtil;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CheckPhoneService {
    @GET(HttpUtil.CHECK_PHONE_URL)
    Observable<PhoneType> getCheck(@Path("phone") String phone);
}
