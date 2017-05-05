package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;
import cn.edu.ecust.faceaccesscontrol.manage.MyDatabaseHelper;

public class ResultActivity extends Toolbar2Activity {

    private TextView textViewSuccess;
    private Button buttonSuccessBack;

    private ImageView imageViewIcon;

    private TextView textViewFailure;
    private Button buttonFailureAgain;
    private Button buttonFailurePassword;

    private ImageView imageViewTest;
    private ImageView imageViewTrain;

    private TextView textViewUserNo;
    private TextView textViewUserName;
    private TextView textViewUserCellphone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        Toolbar toolbarCommon=(Toolbar)findViewById(R.id.resultactivity_toolbar_common);//找到工具条
        setSupportActionBar(toolbarCommon);//激活工具条

        //绑定控件
        textViewSuccess=(TextView)findViewById(R.id.resultactivity_textview_success);
        buttonSuccessBack=(Button)findViewById(R.id.resultactivity_button_successback);

        imageViewIcon=(ImageView)findViewById(R.id.resultactivity_imageview_icon);

        textViewFailure=(TextView)findViewById(R.id.resultactivity_textview_failure);
        buttonFailureAgain=(Button)findViewById(R.id.resultactivity_button_failureagain);
        buttonFailurePassword=(Button)findViewById(R.id.resultactivity_button_failurepassword);

        imageViewTest=(ImageView)findViewById(R.id.resultactivity_imageview_test);
        imageViewTrain=(ImageView)findViewById(R.id.resultactivity_imageview_train);

        textViewUserNo=(TextView)findViewById(R.id.resultactivity_textview_userno);
        textViewUserName=(TextView)findViewById(R.id.resultactivity_textview_username);
        textViewUserCellphone=(TextView)findViewById(R.id.resultactivity_textview_usercellphone);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent=getIntent();
        Boolean isOnePersonFacepp=intent.getBooleanExtra("isOnePersonFacepp",false);//是否识别成功
        String resultNo=intent.getStringExtra("resultNo");//识别出的人的工号
        String testFaceName=intent.getStringExtra("testFaceName");//测试图片的文件名

        if(isOnePersonFacepp==true){//识别成功
            textViewSuccess.setText("识别成功！门禁已开启");
            buttonSuccessBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
            imageViewIcon.setImageResource(R.drawable.ic_ok);
            textViewFailure.setVisibility(View.INVISIBLE);
            buttonFailureAgain.setVisibility(View.INVISIBLE);
            buttonFailurePassword.setVisibility(View.INVISIBLE);

            File testfaceDir = getDir("testface", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
            File faceDir = getDir("face", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
            Glide.with(ResultActivity.this).load(testfaceDir.getAbsolutePath()+"/"+testFaceName+".jpg").into(imageViewTest);
            Glide.with(ResultActivity.this).load(faceDir.getAbsolutePath()+"/"+resultNo+"_1.jpg").into(imageViewTrain);

            MyDatabaseHelper dbHelper=new MyDatabaseHelper(ResultActivity.this,"Face.db",null,1);
            SQLiteDatabase db=dbHelper.getWritableDatabase();
            Cursor cursor=db.rawQuery("select * from User where no = ? ",new String[]{""+resultNo});
            if(cursor.moveToFirst()){
                do {
                    textViewUserNo.setText("工号： "+resultNo);
                    textViewUserName.setText("姓名： "+cursor.getString(cursor.getColumnIndex("name")));
                    textViewUserCellphone.setText("手机： "+cursor.getString(cursor.getColumnIndex("cellphone")));
                }while (cursor.moveToNext());
            }
            cursor.close();
        }else{
            //识别失败
            textViewSuccess.setVisibility(View.INVISIBLE);
            buttonSuccessBack.setVisibility(View.INVISIBLE);
            imageViewIcon.setImageResource(R.drawable.ic_refuse);
            textViewFailure.setText("识别失败！请重试或使用密码");
            buttonFailureAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentAgain=new Intent(ResultActivity.this,CameraRecognizeActivity.class);
                    startActivity(intentAgain);//重试
                    finish();
                }
            });
            buttonFailurePassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //使用工号和密码登录
                    Intent intentPassword=new Intent(ResultActivity.this,PasswordLoginActivity.class);
                    startActivity(intentPassword);
                    finish();
                }
            });
            File testfaceDir = getDir("testface", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
            File faceDir = getDir("face", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
            Glide.with(ResultActivity.this).load(testfaceDir.getAbsolutePath()+"/"+testFaceName+".jpg").into(imageViewTest);
            Glide.with(ResultActivity.this).load(faceDir.getAbsolutePath()+"/"+resultNo+"_1.jpg").into(imageViewTrain);
            textViewUserNo.setVisibility(View.INVISIBLE);
            textViewUserName.setVisibility(View.INVISIBLE);
            textViewUserCellphone.setVisibility(View.INVISIBLE);
        }
    }
}
