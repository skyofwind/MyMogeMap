package com.example.dzj.mogemap.utils;

import com.example.dzj.mogemap.rxjava.common.AddRecordService;
import com.example.dzj.mogemap.rxjava.common.AddUserService;
import com.example.dzj.mogemap.rxjava.common.CheckPhoneService;
import com.example.dzj.mogemap.rxjava.common.GetLeaderBoardsService;
import com.example.dzj.mogemap.rxjava.common.MonthRecordsService;
import com.example.dzj.mogemap.rxjava.common.MyHttpServices;
import com.example.dzj.mogemap.rxjava.common.RecordService;
import com.example.dzj.mogemap.rxjava.common.RecordsService;
import com.example.dzj.mogemap.rxjava.common.UpdateUserService;
import com.example.dzj.mogemap.rxjava.common.WeekRecordsService;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.fastjson.FastJsonConverterFactory;

public class RetrofitUtils extends MyHttpServices {
    private static RetrofitUtils instance;
    private Retrofit retrofit;

    private RetrofitUtils() {
        initRetrofit();
        initOkhttp();
    }

    public static RetrofitUtils getInstance() {
        if (instance == null) {
            synchronized (RetrofitUtils.class) {
                instance = new RetrofitUtils();
            }
        }
        return instance;
    }

    private void initRetrofit() {
        retrofit = new Retrofit.Builder()
            .baseUrl(HttpUtil.ROOT_URL)
            .addConverterFactory(FastJsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build();
    }

    private void initOkhttp() {
        // 创建 OKHttpClient
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(30, TimeUnit.SECONDS);//连接超时时间
        builder.writeTimeout(60, TimeUnit.SECONDS);//写操作 超时时间
        builder.readTimeout(30, TimeUnit.SECONDS);//读操作超时时间
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }

    @Override
    public RecordsService getRecordsService() {
        if (recordsService == null) {
            recordsService = retrofit.create(RecordsService.class);
        }
        return recordsService;
    }

    @Override
    public WeekRecordsService getWeekRecordsService() {
        if (weekRecordsService == null) {
            weekRecordsService = retrofit.create(WeekRecordsService.class);
        }
        return weekRecordsService;
    }

    @Override
    public MonthRecordsService getMonthRecordsService() {
        if (monthRecordsService == null) {
            monthRecordsService = retrofit.create(MonthRecordsService.class);
        }
        return monthRecordsService;
    }

    @Override
    public CheckPhoneService getCheckPhoneService() {
        if (checkPhoneService == null) {
            checkPhoneService = retrofit.create(CheckPhoneService.class);
        }
        return checkPhoneService;
    }

    @Override
    public AddUserService getAddUserService() {
        if (addUserService == null) {
            addUserService = retrofit.create(AddUserService.class);
        }
        return addUserService;
    }

    @Override
    public RecordService getRecordService() {
        if (recordService == null) {
            recordService = retrofit.create(RecordService.class);
        }
        return recordService;
    }

    @Override
    public AddRecordService getAddRecordService() {
        if (addRecordService == null) {
            addRecordService = retrofit.create(AddRecordService.class);
        }
        return addRecordService;
    }

    @Override
    public UpdateUserService getUpdateUserService() {
        if (updateUserService == null) {
            updateUserService = retrofit.create(UpdateUserService.class);
        }
        return updateUserService;
    }

    @Override
    public GetLeaderBoardsService getGetLeaderBoardsService() {
        if (getLeaderBoardsService == null) {
            getLeaderBoardsService = retrofit.create(GetLeaderBoardsService.class);
        }
        return getLeaderBoardsService;
    }
}
