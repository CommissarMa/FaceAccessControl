
package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;
import cn.edu.ecust.faceaccesscontrol.manage.MyDatabaseHelper;

public class AdminNoticeEditActivity extends Toolbar2Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice_edit);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);//找到工具条
        setSupportActionBar(toolbar);//激活工具条

        final AppCompatEditText noticeEditText=(AppCompatEditText)findViewById(R.id.adminnoticeeditpage_addnotice_edittext);//修改编辑框
        Button noticeEditButton=(Button)findViewById(R.id.adminnoticeeditpage_addnotice_button);//修改按钮

        Intent intent_lastActvity=getIntent();
        final int index=intent_lastActvity.getIntExtra("idIndexInDB",0);

        MyDatabaseHelper dbHelper=new MyDatabaseHelper(AdminNoticeEditActivity.this,"Face.db",null,1);
        final SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from Notice where id= ? ",new String[]{""+index});
        if(cursor.moveToFirst()){
            do {
                noticeEditText.setText(cursor.getString(cursor.getColumnIndex("noticetext")));
            }while (cursor.moveToNext());
        }
        cursor.close();

        noticeEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!noticeEditText.getText().toString().equals("")){
                    db.execSQL("update Notice set noticetext = ? where id = ? ",new String[]{noticeEditText.getText().toString(),index+""});
                    Intent intent_restartAdminNoticeActivity=new Intent(AdminNoticeEditActivity.this,AdminNoticeActivity.class);
                    startActivity(intent_restartAdminNoticeActivity);
                    finish();
                }else{
                    Toast.makeText(AdminNoticeEditActivity.this,"通知内容不能为空",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
