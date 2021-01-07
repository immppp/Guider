package cn.imppp.guider.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.imppp.guider.R;
import cn.imppp.guider.app.App;

import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        App.getContext().getString(R.string.app_name);
    }
}
