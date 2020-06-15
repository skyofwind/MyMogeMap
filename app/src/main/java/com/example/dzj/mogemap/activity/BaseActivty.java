package com.example.dzj.mogemap.activity;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dzj.mogemap.R;

import java.util.ArrayList;

/**
 * Created by dzj on 2018/2/28.
 */

public class BaseActivty extends AppCompatActivity {
    private ImageView icon, iconRight;
    private TextView title;
    protected final int SDK_PERMISSION_REQUEST = 127;
    protected final int LOCATION_PERMISSION_REQUEST = 128;
    protected final int SMS_PERMISSION_REQUEST = 129;
    protected final int STORAGE_PERMISSION_REQUEST = 130;
    protected String permissionInfo;
    //定时器相关
    private Dialog progressDialog;
    private boolean progress = false;

    public void initTitle() {
        icon = (ImageView) findViewById(R.id.icon);
        iconRight = (ImageView) findViewById(R.id.icon_right);
        title = (TextView) findViewById(R.id.titleText);
    }

    public void setIcon(int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            icon.setImageDrawable(getDrawable(drawable));
        } else {
            icon.setImageDrawable(getResources().getDrawable(drawable));
        }
    }

    public void setIconRight(int drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iconRight.setImageDrawable(getDrawable(drawable));
        } else {
            iconRight.setImageDrawable(getResources().getDrawable(drawable));
        }
    }

    public void setTitle(String s) {
        title.setText(s);
    }

    public void setIconListener(View.OnClickListener listener) {
        icon.setOnClickListener(listener);
    }

    public void seticonRightListener(View.OnClickListener listener) {
        iconRight.setOnClickListener(listener);
    }

    public void statrProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new Dialog(this, R.style.progress_dialog);
            progressDialog.setContentView(R.layout.dialog);
            progressDialog.setCancelable(true);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msg = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msg.setText("正在加载中");
        }
        progress = true;
        progressDialog.show();
    }

    public void statrProgressDialog(String msg) {
        if (progressDialog == null) {
            progressDialog = new Dialog(this, R.style.progress_dialog);
            progressDialog.setContentView(R.layout.dialog);
            progressDialog.setCancelable(true);
            progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            TextView msgTv = (TextView) progressDialog.findViewById(R.id.id_tv_loadingmsg);
            msgTv.setText(msg);
        }
        progress = true;
        progressDialog.show();
    }

    public void cancelDialog() {
        if (progress) {
            progress = false;
            progressDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelDialog();
    }

    //初次权限申请权限相关
    @TargetApi(23)
    public void getPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.READ_PHONE_STATE);
            }
            if (addPermission(permissions, Manifest.permission.RECEIVE_SMS)) {
                permissionInfo += "Manifest.permission.RECEIVE_SMS Deny \n";
            }
            if (addPermission(permissions, Manifest.permission.SEND_SMS)) {
                permissionInfo += "Manifest.permission.SEND_SMS Deny \n";
            }
            // 读写权限
            /*if (addPermission(permissions, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                permissionInfo += "Manifest.permission.WRITE_EXTERNAL_STORAGE Deny \n";
            }*/
            // 读取电话状态权限
            /*if (addPermission(permissions, Manifest.permission.READ_PHONE_STATE)) {
                permissionInfo += "Manifest.permission.READ_PHONE_STATE Deny \n";
            }*/

            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SDK_PERMISSION_REQUEST);
            }
        }
    }

    //位置权限申请
    @TargetApi(23)
    public void getLocationPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
            }
            if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), LOCATION_PERMISSION_REQUEST);
            }
        }
    }

    //短信权限申请
    @TargetApi(23)
    public void getSMSPersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            if (checkSelfPermission(Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.RECEIVE_SMS);
            }
            if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.SEND_SMS);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), SMS_PERMISSION_REQUEST);
            }
        }
    }

    //短信权限申请
    @TargetApi(23)
    public void getStoragePersimmions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ArrayList<String> permissions = new ArrayList<String>();
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            }
            if (permissions.size() > 0) {
                requestPermissions(permissions.toArray(new String[permissions.size()]), STORAGE_PERMISSION_REQUEST);
            }
        }
    }


    @TargetApi(23)
    public boolean addPermission(ArrayList<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) { // 如果应用没有获得对应权限,则添加到列表中,准备批量申请
            if (shouldShowRequestPermissionRationale(permission)) {
                return true;
            } else {
                permissionsList.add(permission);
                return false;
            }

        } else {
            return true;
        }
    }
}
