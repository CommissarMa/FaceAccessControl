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
import cn.edu.ecust.faceaccesscontrol.manage.AllUseAlertDialog;
import cn.edu.ecust.faceaccesscontrol.manage.MyDatabaseHelper;

public class AdminUserDetialActivity extends Toolbar2Activity {

    private TextView textViewUserNo;
    private TextView textViewUserName;
    private TextView textViewUserCellphone;

    private ImageView imageViewFace1;
    private ImageView imageViewFace2;
    private ImageView imageViewFace3;
    private ImageView imageViewFace4;
    private ImageView imageViewFace5;

    private Button buttonDelete;

    private String stringUserNo;
    private String stringUserName;
    private String stringUserCellphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_detial);
        Toolbar toolbarCommon=(Toolbar)findViewById(R.id.adminuserdetailactivity_toolbar_common);//找到工具条
        setSupportActionBar(toolbarCommon);//激活工具条

        textViewUserNo=(TextView)findViewById(R.id.adminuserdetialactivity_textview_userno);
        textViewUserName=(TextView)findViewById(R.id.adminuserdetialactivity_textview_username);
        textViewUserCellphone=(TextView)findViewById(R.id.adminuserdetialactivity_textview_usercellphone);

        imageViewFace1=(ImageView)findViewById(R.id.adminuserdetailactivity_imageview_face1);
        imageViewFace2=(ImageView)findViewById(R.id.adminuserdetailactivity_imageview_face2);
        imageViewFace3=(ImageView)findViewById(R.id.adminuserdetailactivity_imageview_face3);
        imageViewFace4=(ImageView)findViewById(R.id.adminuserdetailactivity_imageview_face4);
        imageViewFace5=(ImageView)findViewById(R.id.adminuserdetailactivity_imageview_face5);

        buttonDelete=(Button)findViewById(R.id.adminuserdetialactivity_button_delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllUseAlertDialog.showUserDeleteAlertDialog(AdminUserDetialActivity.this,stringUserNo,stringUserName);
            }
        });

        //得到用户工号
        Intent intent=getIntent();
        stringUserNo=intent.getStringExtra("userNo");

        //填充5个ImageView
        fillFiveImageView();

        //从数据库中读取用户其他的数据然后输出
        fillUserInformationText();
    }

    private void fillFiveImageView(){
        File faceDir = getDir("face", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
        Glide.with(AdminUserDetialActivity.this).load(faceDir.getAbsolutePath()+"/"+stringUserNo+"_1.jpg").into(imageViewFace1);
        Glide.with(AdminUserDetialActivity.this).load(faceDir.getAbsolutePath()+"/"+stringUserNo+"_2.jpg").into(imageViewFace2);
        Glide.with(AdminUserDetialActivity.this).load(faceDir.getAbsolutePath()+"/"+stringUserNo+"_3.jpg").into(imageViewFace3);
        Glide.with(AdminUserDetialActivity.this).load(faceDir.getAbsolutePath()+"/"+stringUserNo+"_4.jpg").into(imageViewFace4);
        Glide.with(AdminUserDetialActivity.this).load(faceDir.getAbsolutePath()+"/"+stringUserNo+"_5.jpg").into(imageViewFace5);
    }

    private void fillUserInformationText(){
        MyDatabaseHelper dbHelper=new MyDatabaseHelper(this,"Face.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from User where no = ?",new String[]{""+stringUserNo});
        if(cursor.moveToFirst()){
            do{
                stringUserName=cursor.getString(cursor.getColumnIndex("name"));
                stringUserCellphone=cursor.getString(cursor.getColumnIndex("cellphone"));
            }while (cursor.moveToNext());
        }
        cursor.close();

        textViewUserNo.setText(stringUserNo);
        textViewUserName.setText(stringUserName);
        textViewUserCellphone.setText(stringUserCellphone);
    }
}
