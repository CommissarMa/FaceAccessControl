package cn.edu.ecust.faceaccesscontrol.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Mat;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.ExitDialogActivity;
import cn.edu.ecust.faceaccesscontrol.manage.ActivityCollector;
import cn.edu.ecust.faceaccesscontrol.manage.AllUseAlertDialog;

/**
 * 欢迎界面
 * 继承自ExitDialogActivity类，返回键的事件改为弹出lertdialog
 * 加载Opencv，采用OpencvManager的方式，手机端需要安装OpencvManager（在OpencvAndroid包下的apk目录中，需要根据手机cpu架构进行选择）
 * 功能：展示欢迎界面
 *       开始使用按钮——跳转到用户模式界面
 *       使用说明按钮——跳转到使用说明界面
 * 2017年5月2日09:23:29
 */
public class WelcomeActivity extends ExitDialogActivity {

    /**
     * 活动创建时进行：
     * 界面的布局
     * UI的初始化，事件的绑定
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);//注意先取消状态栏再加载布局文件

        //第一次启动应用的时候动态申请权限
        if(ContextCompat.checkSelfPermission(WelcomeActivity.this, Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){//若没有权限
            ActivityCompat.requestPermissions(WelcomeActivity.this,new String[]{Manifest.permission.CAMERA},1);//动态申请权限
        }

        //开始使用按钮
        Button buttonStart=(Button)findViewById(R.id.welcomeacticity_button_start);
        //使用说明按钮
        Button buttonTutor=(Button)findViewById(R.id.welcomeactivity_button_tutor);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {//点击开始使用按钮
                Intent intent=new Intent(WelcomeActivity.this,NoAdminMainActivity.class);
                startActivity(intent);
            }
        });
        buttonTutor.setOnClickListener(new View.OnClickListener() {//点击使用说明按钮
            @Override
            public void onClick(View v) {

            }
        });
    }

    /**
     * 动态申请权限的回调函数
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){//若用户同意授权Camera
                    Toast.makeText(WelcomeActivity.this,"成功获得照相机权限！",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this,"你拒绝授予Camera权限，请进入应用设置页面手动打开权限",Toast.LENGTH_LONG).show();
                    //进入应用设置界面
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package","com.edu.ecust.faceaccesscontrol",null);
                    intent.setData(uri);
                    startActivity(intent);
                    //关闭当前活动
                    finish();
                }
                break;
            default:
                break;
        }
    }
}
