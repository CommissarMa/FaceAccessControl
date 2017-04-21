package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.ExitDialogActivity;
import cn.edu.ecust.faceaccesscontrol.manage.ActivityCollector;
import cn.edu.ecust.faceaccesscontrol.manage.AllUseAlertDialog;

public class WelcomeActivity extends ExitDialogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);//注意先取消状态栏再加载布局文件
        Button welcomeStartButton=(Button)findViewById(R.id.welcomeStartButton);//开始使用按钮
        Button welcomeTutorButton=(Button)findViewById(R.id.welcomeTutorButton);//使用说明按钮
        welcomeStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击开始使用按钮
                Intent intent=new Intent(WelcomeActivity.this,NoAdminMainActivity.class);
                startActivity(intent);
            }
        });
        welcomeTutorButton.setOnClickListener(new View.OnClickListener() {//点击使用说明按钮
            @Override
            public void onClick(View v) {

            }
        });
    }

}
