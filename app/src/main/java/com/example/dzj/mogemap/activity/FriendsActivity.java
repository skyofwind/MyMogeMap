package com.example.dzj.mogemap.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.dzj.mogemap.R;
import com.example.dzj.mogemap.adapter.FriendsListViewAdapter;
import com.example.dzj.mogemap.modle.Mogemap_user;
import com.example.dzj.mogemap.utils.HttpUtil;
import com.example.dzj.mogemap.utils.ToastUtil;
import com.example.dzj.mogemap.utils.UserManager;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by dzj on 2018/2/23.
 */

public class FriendsActivity extends BaseActivty {

    private EditText editText;
    private Button button;
    private ListView listView;
    private FriendsListViewAdapter adapter;
    boolean clcik = false, isLogin = false;
    List<Mogemap_user> userList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friends_layout);
        if (UserManager.getInstance().getUser().getPhone() != null) {
            isLogin = true;
        }
        setMyTitle();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (isLogin) {
            getFriends(UserManager.getInstance().getUser().getPhone());
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message m) {
            switch (m.what) {
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
        setTitle("我的好友");
        setIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void init() {
        editText = (EditText) findViewById(R.id.searchEdit);
        button = (Button) findViewById(R.id.searchBtn);
        listView = (ListView) findViewById(R.id.friends_list);
        editText.addTextChangedListener(new TextChange());
        button.setOnClickListener(clickListener);
    }

    View.OnClickListener clickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (isLogin) {
                if (clcik) {
                    addFriend(UserManager.getInstance().getUser().getPhone(), editText.getText().toString());
                } else {
                    tip("请输入正确的手机号");
                }
            } else {
                tip("请先登录帐号");
            }

        }
    };

    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (editText.getText().toString().length() == 11) {
                clcik = true;
            } else {
                clcik = false;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }

    private void addFriend(final String mPhone, String fPhone) {
        if (!UserManager.getInstance().getUser().getPhone().equals("")) {
            handler.sendEmptyMessage(0x02);
            OkHttpUtils
                .get()
                .url(HttpUtil.ADD_FRIEND + mPhone + "/add/" + fPhone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        tip("请求发送失败");
                        handler.sendEmptyMessage(0x03);
                        Log.d("response:", e.toString() + "  call=" + call.toString() + " id=" + id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("response:", response);
                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                        String type = jsonObject.getString("type");
                        handler.sendEmptyMessage(0x03);
                        if (type.equals("0")) {
                            tip("此用户不存在");
                        } else if (type.equals("1")) {
                            tip("添加好友成功");
                            getFriends(mPhone);
                        } else if (type.equals("2")) {
                            tip("添加好友失败");
                        } else if (type.equals("3")) {
                            tip("您与此帐号已经是好友了");
                        }
                    }
                });
        } else {
            ToastUtil.tip(this, "请先授权登录", 1);
        }
    }

    private void getFriends(String phone) {
        if (!UserManager.getInstance().getUser().getPhone().equals("")) {
            handler.sendEmptyMessage(0x02);
            OkHttpUtils
                .get()
                .url(HttpUtil.GET_FRIENDS + phone)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        //tip("请求发送失败");
                        e.printStackTrace();
                        handler.sendEmptyMessage(0x03);
                        Log.d("response:", e.toString() + "  call=" + call.toString() + " id=" + id);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.d("response:", response);
                        handler.sendEmptyMessage(0x03);
                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                        String array = jsonObject.getString("friends");
                        userList = JSON.parseArray(array, Mogemap_user.class);
                        System.out.println(userList.size());
                        adapter = new FriendsListViewAdapter(FriendsActivity.this, userList);
                        listView.setAdapter(adapter);
                    }
                });
        } else {
            ToastUtil.tip(this, "请先授权登录", 1);
        }
    }

    private void tip(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    public void deleteItem(int position) {
        if (userList != null) {
            userList.remove(position);
            adapter = new FriendsListViewAdapter(FriendsActivity.this, userList);
            listView.setAdapter(adapter);
        }
    }
}
