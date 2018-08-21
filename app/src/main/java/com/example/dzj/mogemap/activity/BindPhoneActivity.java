package com.example.dzj.mogemap.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.example.dzj.mogemap.R;
import com.example.dzj.mogemap.modle.PhoneType;
import com.example.dzj.mogemap.utils.HttpUtil;
import com.example.dzj.mogemap.utils.RetrofitUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;

/**
 * Created by dzj on 2018/2/22.
 */

public class BindPhoneActivity extends BaseActivty {

    //private static final int MY_CODE = 20001;

    private EditText phone;
    private TextView getCode;
    private boolean btn = false;
    private PhoneType pt;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x2:
                    statrProgressDialog();
                    break;
                case 0x03:
                    cancel();
                    break;
            }
        }
    };
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bind_phone_layout);
        setMyTitle();
        phone = (EditText)findViewById(R.id.phone);
        getCode = (TextView)findViewById(R.id.getCode);

        phone.addTextChangedListener(new TextChange());
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if(checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
                            getSMSPersimmions();
                        }else {
                            getPhoneUseMessage(phone.getText().toString());
                        }
                    }else {
                        getPhoneUseMessage(phone.getText().toString());
                    }
                }
            }
        });
    }
    private void setMyTitle(){
        initTitle();
        setTitle("");
        setIconListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void onDestroy() {
        super.onDestroy();
        //用完回调要注销掉，否则可能会出现内存泄露
        SMSSDK.unregisterAllEventHandler();
    };
    class TextChange implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(phone.getText().toString().length() == 11){
                changeBtn(true);
            }else {
                changeBtn(false);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }

    }
    private void changeBtn(boolean type){
        btn = type;
        if (type){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getCode.setBackground(getDrawable(R.drawable.button_on));
            }
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getCode.setBackground(getDrawable(R.drawable.button_off));
            }
        }
    }
    private void sendCode(final String country, final String phone) {
        // 注册一个事件回调，用于处理发送验证码操作的结果
        mHandler.sendEmptyMessage(0x02);
        SMSSDK.registerEventHandler(new EventHandler() {
            public void afterEvent(int event, int result, Object data) {
                //org.json.JSONObject jsonObject = new org.json.JSONObject(o.toString());
                Log.d("mysms"," event="+event+" result="+result+" data"+data.toString());
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // TODO 处理成功得到验证码的结果
                    // 请注意，此时只是完成了发送验证码的请求，验证码短信还需要几秒钟之后才送达
                    Log.d("bindmysms","sendCode+成功"+" "+country+" "+phone);
                    sendData(country, phone);
                } else{
                    // TODO 处理错误的结果
                    Log.d("mysms","sendCode+失败");
                    tip("请求验证码失败");
                }

            }
        });
        // 触发操作
        SMSSDK.getVerificationCode(country, phone);
    }
    private void getPhoneUseMessage(final String phone){
        RetrofitUtils.getInstance()
                .getCheckPhoneService()
                .getCheck(phone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<PhoneType>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        statrProgressDialog();
                    }

                    @Override
                    public void onNext(PhoneType phoneType) {
                        pt = phoneType;
                    }

                    @Override
                    public void onError(Throwable e) {
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        cancel();
                        if(pt.getUse().equals("0")){
                            sendCode("86", phone);
                        }else {
                            tip("此手机号已被绑定");
                        }
                    }
                });
    }
    private void sendData(String country, String phone){
        mHandler.sendEmptyMessage(0x03);
        String name = getIntent().getStringExtra("name");
        String type = getIntent().getStringExtra("type");
        String head = getIntent().getStringExtra("head");
        String id = getIntent().getStringExtra("id");
        Log.d("data:",name+" "+type+" head="+head+" id="+id);
        Intent intent = new Intent(this, SubmitCodeActivity.class);
        intent.putExtra("name",name);
        intent.putExtra("type",type);
        intent.putExtra("head",head);
        intent.putExtra("id", id);
        intent.putExtra("country", country);
        intent.putExtra("phone", phone);
        BindPhoneActivity.this.startActivity(intent);
    }
    private void tip(String s){
        Toast.makeText(BindPhoneActivity.this, s, Toast.LENGTH_SHORT).show();
    }
    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        // TODO Auto-generated method stub
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case SMS_PERMISSION_REQUEST:
                for(int i = 0; i < permissions.length; i++){
                    if (permissions[i].equals(Manifest.permission.RECEIVE_SMS) && grantResults[i] == 0) {
                        getPhoneUseMessage(phone.getText().toString());
                    }
                }
                break;
        }
    }
}
