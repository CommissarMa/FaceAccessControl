package cn.edu.ecust.faceaccesscontrol.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import cn.edu.ecust.faceaccesscontrol.manage.ActivityCollector;

public class FullScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//取消状态栏
        ActivityCollector.addActivity(this);//添加当前活动到活动集合管理类中
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);//从活动集合管理类中删除当前活动
    }
}
