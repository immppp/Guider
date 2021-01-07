package cn.imppp.guider.ui.activity.start;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.tbruyelle.rxpermissions2.RxPermissions;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.imppp.guider.R;
import cn.imppp.guider.app.App;
import cn.imppp.guider.ui.activity.guide.GuideActivity;
import cn.imppp.guider.ui.activity.map.MapRouteActivity;
import cn.imppp.guider.ui.activity.recyclerview.RecyclerViewActivity;

public class StartUpActivity extends AppCompatActivity {

    @SuppressLint("CheckResult")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        // 申请权限
        new RxPermissions(StartUpActivity.this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_NETWORK_STATE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_WIFI_STATE,
                        Manifest.permission.CHANGE_WIFI_STATE,
                        Manifest.permission.WAKE_LOCK,
                        Manifest.permission.BLUETOOTH)
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        startActivity(new Intent(App.getContext(), RecyclerViewActivity.class));
                    }
                    finish();
                });
    }
}
