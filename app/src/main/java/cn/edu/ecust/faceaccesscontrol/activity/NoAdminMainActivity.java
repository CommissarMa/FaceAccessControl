package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.ExitDialogActivity;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;
import cn.edu.ecust.faceaccesscontrol.manage.ActivityCollector;
import cn.edu.ecust.faceaccesscontrol.manage.AllUseAlertDialog;

/**
 * 没有管理员登录时的活动
 */
public class NoAdminMainActivity extends Toolbar2Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_admin_main);//加载布局文件
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);//找到工具条
        setSupportActionBar(toolbar);//激活工具条

        Button button_register=(Button)findViewById(R.id.noadminmain_register);//注册按钮
        Button button_recognize=(Button)findViewById(R.id.noadminmain_recognize);//识别按钮
        Button button_adminlogin=(Button)findViewById(R.id.noadminmain_adminlogin);//管理员登录按钮
        button_register.setOnClickListener(new View.OnClickListener() {//点击注册按钮
            @Override
            public void onClick(View v) {
                Intent intent_register=new Intent(NoAdminMainActivity.this,RegisterNewUserActivity.class);
                startActivity(intent_register);
            }
        });
        button_recognize.setOnClickListener(new View.OnClickListener() {//点击识别按钮
            @Override
            public void onClick(View v) {

            }
        });
        button_adminlogin.setOnClickListener(new View.OnClickListener() {//点击管理员登录按钮
            @Override
            public void onClick(View v) {//点击管理员登录按钮
                AllUseAlertDialog.adminPasswordConfirmDialogshow(NoAdminMainActivity.this);
            }
        });

        final AppCompatTextView adminInfoTextView=(AppCompatTextView)findViewById(R.id.admininfo_text);
        SharedPreferences pref=getSharedPreferences("admin_data",MODE_PRIVATE);//SharedPreferences的文件名是admin_data
        String adminInfo="管理员联系方式"+(pref.getString("admin_name","").equals("")?" 姓名：":" 姓名："+pref.getString("admin_name",""))
                +" \n "
                +(pref.getString("admin_cellphone","").equals("")?" 手机：":" 手机："+pref.getString("admin_cellphone",""))
                +" \n "
                +(pref.getString("admin_location","").equals("")?" 地址：":" 地址："+pref.getString("admin_location",""))
                +" \n "
                +(pref.getString("admin_mail","").equals("")?" 邮箱：":" 邮箱："+pref.getString("admin_mail",""));
        adminInfoTextView.setText(adminInfo);
        adminInfoTextView.setVisibility(View.INVISIBLE);

        FloatingActionButton fbtnAdminInfo=(FloatingActionButton)findViewById(R.id.floatbutton_admininfo);//悬浮按钮
        fbtnAdminInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(adminInfoTextView.getVisibility()==View.INVISIBLE){
                    adminInfoTextView.setVisibility(View.VISIBLE);
                }else{
                    adminInfoTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
    }



}
