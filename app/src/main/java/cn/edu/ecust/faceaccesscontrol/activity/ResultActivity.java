package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;

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

        }
    }
}
