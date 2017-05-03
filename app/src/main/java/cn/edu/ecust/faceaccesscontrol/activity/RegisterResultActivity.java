package cn.edu.ecust.faceaccesscontrol.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;
import cn.edu.ecust.faceaccesscontrol.manage.MyDatabaseHelper;

public class RegisterResultActivity extends Toolbar2Activity {

    private ImageView imageViewIcon;//显示注册结果的图标
    private TextView textViewMessage;//显示注册结果的文字信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_result);
        Toolbar toolbarCommon=(Toolbar)findViewById(R.id.registerresultactivity_toolbar_common);//找到工具条
        setSupportActionBar(toolbarCommon);//激活工具条

        //绑定控件
        imageViewIcon=(ImageView)findViewById(R.id.registerresultactivity_imageview_icon);
        textViewMessage=(TextView)findViewById(R.id.registerresultactivity_textview_message);

        Intent intentFromCameraRegisterActivity=getIntent();
        if(intentFromCameraRegisterActivity.getBooleanExtra("isRegisterSuccess",false)==true){
            //把该用户的信息插入数据库
            insertIntoTable(RegisterResultActivity.this,intentFromCameraRegisterActivity);
            //更改UI
            imageViewIcon.setImageResource(R.drawable.ic_success);
            textViewMessage.setText("注册成功！请等待管理员审批！");
        }else{
            imageViewIcon.setImageResource(R.drawable.ic_failure);
            textViewMessage.setText("注册失败！请重试！");
        }
    }

    public void insertIntoTable(Activity activity,Intent intent){
        String stringUserNo=intent.getStringExtra("userNo");
        String stringUserName=intent.getStringExtra("userName");
        String stringUserPassword=intent.getStringExtra("userPassword");
        String stringUserCellphone=intent.getStringExtra("userCellphone");

        MyDatabaseHelper dbHelper=new MyDatabaseHelper(activity,"Face.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        db.execSQL("insert into User (no, name, password, cellphone, grant) values (?,?,?,?,?)",
                new String[]{stringUserNo,stringUserName,stringUserPassword,stringUserCellphone,0+""});
    }
}
