package com.example.dzj.mogemap.activity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.dzj.mogemap.R;
import com.example.dzj.mogemap.adapter.RankingListViewAdapter;
import com.example.dzj.mogemap.modle.MogeLeaderboards;
import com.example.dzj.mogemap.utils.RetrofitUtils;
import com.example.dzj.mogemap.utils.ToastUtil;
import com.example.dzj.mogemap.utils.UserManager;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by dzj on 2018/3/5.
 */

public class RankingActivity extends BaseActivty {

    private TextView dayRanking, weekRanking, monthRanking;
    private ListView listView;
    private RankingListViewAdapter adapter;
    private MogeLeaderboards leaderboards;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leaderboards_layout);
        setMyTitle();
        initView();
        getData(UserManager.getInstance().getUser().getPhone(), 0);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            switch (m.what) {
                case 0x01:
                    updateAdapter();
                    break;
                case 0x02:
                    statrProgressDialog();
                    break;
                case 0x03:
                    cancelDialog();
                    break;
            }
        }
    };

    private void setMyTitle() {
        initTitle();
        setTitle("排行榜");
        setIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initView() {
        dayRanking = (TextView) findViewById(R.id.day_ranking);
        weekRanking = (TextView) findViewById(R.id.week_ranking);
        monthRanking = (TextView) findViewById(R.id.month_ranking);
        listView = (ListView) findViewById(R.id.list);

        dayRanking.setOnClickListener(listener);
        weekRanking.setOnClickListener(listener);
        monthRanking.setOnClickListener(listener);
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.day_ranking:
                    dayRanking.setBackground(getMyDrawable(R.drawable.day_text_bg_choose));
                    weekRanking.setBackground(getMyDrawable(R.drawable.week_text_bg_normal));
                    monthRanking.setBackground(getMyDrawable(R.drawable.month_text_bg_normal));
                    getData(UserManager.getInstance().getUser().getPhone(), 0);
                    break;
                case R.id.week_ranking:
                    dayRanking.setBackground(getMyDrawable(R.drawable.day_text_bg_normal));
                    weekRanking.setBackground(getMyDrawable(R.drawable.week_text_bg_choose));
                    monthRanking.setBackground(getMyDrawable(R.drawable.month_text_bg_normal));
                    getData(UserManager.getInstance().getUser().getPhone(), 1);
                    break;
                case R.id.month_ranking:
                    dayRanking.setBackground(getMyDrawable(R.drawable.day_text_bg_normal));
                    weekRanking.setBackground(getMyDrawable(R.drawable.week_text_bg_normal));
                    monthRanking.setBackground(getMyDrawable(R.drawable.month_text_bg_choose));
                    getData(UserManager.getInstance().getUser().getPhone(), 2);
                    break;
            }
        }
    };

    private void getData(String phone, final int type) {
        if (!phone.equals("")) {
            RetrofitUtils.getInstance()
                .getGetLeaderBoardsService()
                .getLeaderBoards(phone, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MogeLeaderboards>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        statrProgressDialog();
                    }

                    @Override
                    public void onNext(MogeLeaderboards mogeLeaderboards) {
                        leaderboards = mogeLeaderboards;
                    }

                    @Override
                    public void onError(Throwable e) {
                        cancelDialog();
                        ToastUtil.tip(RankingActivity.this, "请求出错", 1);
                    }

                    @Override
                    public void onComplete() {
                        cancelDialog();
                        updateAdapter();
                    }
                });
        } else {
            ToastUtil.tip(this, "请先授权登录", 0);
        }
    }

    private void updateAdapter() {
        adapter = new RankingListViewAdapter(this, leaderboards.getItems());
        listView.setAdapter(adapter);
    }

    private Drawable getMyDrawable(int id) {
        Drawable drawable;
        if (Build.VERSION.SDK_INT >= 23) {
            drawable = this.getDrawable(id);
        } else {
            drawable = this.getResources().getDrawable(id);
        }
        return drawable;
    }
}
