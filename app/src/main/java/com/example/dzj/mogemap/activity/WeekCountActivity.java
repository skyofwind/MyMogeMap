package com.example.dzj.mogemap.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dzj.mogemap.R;
import com.example.dzj.mogemap.adapter.RecordCountAdapter;
import com.example.dzj.mogemap.modle.Mogemap_run_record;
import com.example.dzj.mogemap.modle.RunRecords;
import com.example.dzj.mogemap.utils.OtherUtil;
import com.example.dzj.mogemap.utils.RetrofitUtils;
import com.example.dzj.mogemap.utils.ToastUtil;
import com.example.dzj.mogemap.utils.UserManager;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dzj on 2018/2/28.
 */

public class WeekCountActivity extends BaseActivty {
    private final static String TAG = "WeekCountActivity";
    private List<Mogemap_run_record> records = new ArrayList<>();
    private TextView distanceCount, timeCount;
    private ListView list;
    private RecordCountAdapter adapter;

    private int times = 0;
    private double distance = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_count_layout);
        initView();
        setMyTitle();
        getData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void setMyTitle() {
        initTitle();
        setTitle("周统计");
        setIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        distanceCount = (TextView) findViewById(R.id.distance_count);
        timeCount = (TextView) findViewById(R.id.time_count);
        list = (ListView) findViewById(R.id.list);
    }

    private void getData() {
        if (!UserManager.getInstance().getUser().getPhone().equals("")) {
            RetrofitUtils.getInstance()
                .getWeekRecordsService()
                .getRecords(UserManager.getInstance().getUser().getPhone())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<RunRecords>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        statrProgressDialog();
                    }

                    @Override
                    public void onNext(RunRecords runRecords) {
                        records = runRecords.getRecords();
                    }

                    @Override
                    public void onError(Throwable e) {
                        cancelDialog();
                        ToastUtil.tip(WeekCountActivity.this, "请求出错", 1);
                    }

                    @Override
                    public void onComplete() {
                        cancelDialog();
                        setDate();
                    }
                });
        } else {
            ToastUtil.tip(this, "请先授权登录", 1);
        }

    }

    private void setDate() {
        times = records.size();
        for (Mogemap_run_record record : records) {
            distance += record.getDistance();
        }
        distanceCount.setText(OtherUtil.getKM(distance));
        timeCount.setText(times + "");

        adapter = new RecordCountAdapter(this, records);
        list.setAdapter(adapter);
    }

    private void log(String s) {
        Log.d(TAG, s);
    }
}
