package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import java.util.ArrayList;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.ExitDialogActivity;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;
import cn.edu.ecust.faceaccesscontrol.manage.ActivityCollector;
import cn.edu.ecust.faceaccesscontrol.manage.AllUseAlertDialog;
import cn.edu.ecust.faceaccesscontrol.manage.MyDatabaseHelper;

/**
 * 没有管理员登录时的活动（用户模式）
 */
public class NoAdminMainActivity extends Toolbar2Activity {

    private Button buttonRegister;
    private Button buttonRecognize;
    private Button buttonAdminlogin;

    private TextView textViewAdmininfo;
    private FloatingActionButton floatingActionButtonAdminInfo;

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_admin_main);//加载布局文件
        Toolbar toolbarCommon=(Toolbar)findViewById(R.id.noadminmainactivity_toolbar_common);//找到工具条
        setSupportActionBar(toolbarCommon);//激活工具条

        buttonRegister=(Button)findViewById(R.id.noadminmainactivity_button_register);//注册按钮
        buttonRecognize=(Button)findViewById(R.id.noadminmainactivity_button_recognize);//识别按钮
        buttonAdminlogin=(Button)findViewById(R.id.noadminmainactivity_button_adminlogin);//管理员登录按钮
        //点击注册按钮
        buttonRegister.setOnClickListener(new View.OnClickListener() {//点击注册按钮
            @Override
            public void onClick(View v) {
                Intent intentRegister=new Intent(NoAdminMainActivity.this,RegisterNewUserActivity.class);
                startActivity(intentRegister);
            }
        });
        //点击识别按钮
        buttonRecognize.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //点击管理员登录按钮
        buttonAdminlogin.setOnClickListener(new View.OnClickListener() {//点击管理员登录按钮
            @Override
            public void onClick(View v) {
                AllUseAlertDialog.adminPasswordConfirmDialogshow(NoAdminMainActivity.this);
            }
        });

        textViewAdmininfo=(TextView)findViewById(R.id.noadminmainactivity_textview_admininfo);
        SharedPreferences pref=getSharedPreferences("admin_data",MODE_PRIVATE);//SharedPreferences的文件名是admin_data
        String adminInfo="管理员联系方式"+(pref.getString("admin_name","").equals("")?" 姓名：":" 姓名："+pref.getString("admin_name",""))
                +" \n "
                +(pref.getString("admin_cellphone","").equals("")?" 手机：":" 手机："+pref.getString("admin_cellphone",""))
                +" \n "
                +(pref.getString("admin_location","").equals("")?" 地址：":" 地址："+pref.getString("admin_location",""))
                +" \n "
                +(pref.getString("admin_mail","").equals("")?" 邮箱：":" 邮箱："+pref.getString("admin_mail",""));
        textViewAdmininfo.setText(adminInfo);
        textViewAdmininfo.setVisibility(View.INVISIBLE);

        floatingActionButtonAdminInfo=(FloatingActionButton)findViewById(R.id.noadminmainactivity_floatingactionbutton_admininfo);//悬浮按钮
        floatingActionButtonAdminInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewAdmininfo.getVisibility()==View.INVISIBLE){
                    textViewAdmininfo.setVisibility(View.VISIBLE);
                    floatingActionButtonAdminInfo.setImageResource(R.drawable.ic_close);
                }else{
                    textViewAdmininfo.setVisibility(View.INVISIBLE);
                    floatingActionButtonAdminInfo.setImageResource(R.drawable.ic_admin);
                }
            }
        });

        MyDatabaseHelper dbHelper=new MyDatabaseHelper(NoAdminMainActivity.this,"Face.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from Notice order by id desc",null);
        ArrayList<String> noticeArrayList=new ArrayList<String>();
        if(cursor.moveToFirst()){
            do {
                String noticeText = cursor.getString(cursor.getColumnIndex("noticetext"));
                noticeArrayList.add(noticeText);
            }while (cursor.moveToNext());
        }
        cursor.close();
        String[] noticeArray=noticeArrayList.toArray(new String[noticeArrayList.size()]);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(NoAdminMainActivity.this,android.R.layout.simple_list_item_1,noticeArray);
        listView=(ListView)findViewById(R.id.noadminmainactivity_listview_notice);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        textViewAdmininfo=(TextView)findViewById(R.id.noadminmainactivity_textview_admininfo);
        SharedPreferences pref=getSharedPreferences("admin_data",MODE_PRIVATE);//SharedPreferences的文件名是admin_data
        String adminInfo="管理员联系方式"+(pref.getString("admin_name","").equals("")?" 姓名：":" 姓名："+pref.getString("admin_name",""))
                +" \n "
                +(pref.getString("admin_cellphone","").equals("")?" 手机：":" 手机："+pref.getString("admin_cellphone",""))
                +" \n "
                +(pref.getString("admin_location","").equals("")?" 地址：":" 地址："+pref.getString("admin_location",""))
                +" \n "
                +(pref.getString("admin_mail","").equals("")?" 邮箱：":" 邮箱："+pref.getString("admin_mail",""));
        textViewAdmininfo.setText(adminInfo);
        textViewAdmininfo.setVisibility(View.INVISIBLE);

        floatingActionButtonAdminInfo=(FloatingActionButton)findViewById(R.id.noadminmainactivity_floatingactionbutton_admininfo);//悬浮按钮
        floatingActionButtonAdminInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(textViewAdmininfo.getVisibility()==View.INVISIBLE){
                    textViewAdmininfo.setVisibility(View.VISIBLE);
                    floatingActionButtonAdminInfo.setImageResource(R.drawable.ic_close);
                }else{
                    textViewAdmininfo.setVisibility(View.INVISIBLE);
                    floatingActionButtonAdminInfo.setImageResource(R.drawable.ic_admin);
                }
            }
        });

        MyDatabaseHelper dbHelper=new MyDatabaseHelper(NoAdminMainActivity.this,"Face.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from Notice order by id desc",null);
        ArrayList<String> noticeArrayList=new ArrayList<String>();
        if(cursor.moveToFirst()){
            do {
                String noticeText = cursor.getString(cursor.getColumnIndex("noticetext"));
                noticeArrayList.add(noticeText);
            }while (cursor.moveToNext());
        }
        cursor.close();
        String[] noticeArray=noticeArrayList.toArray(new String[noticeArrayList.size()]);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(NoAdminMainActivity.this,android.R.layout.simple_list_item_1,noticeArray);
        listView=(ListView)findViewById(R.id.noadminmainactivity_listview_notice);
        listView.setAdapter(adapter);
    }
}
