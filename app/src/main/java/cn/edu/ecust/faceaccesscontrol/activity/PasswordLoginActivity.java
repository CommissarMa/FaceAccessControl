package cn.edu.ecust.faceaccesscontrol.activity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;
import cn.edu.ecust.faceaccesscontrol.manage.MyDatabaseHelper;

public class PasswordLoginActivity extends Toolbar2Activity {

    private EditText editTextNo;
    private EditText editTextPassword;
    private Button buttonLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_login);
        Toolbar toolbarCommon=(Toolbar)findViewById(R.id.passwordloginactivity_toolbar_common);//找到工具条
        setSupportActionBar(toolbarCommon);//激活工具条

        editTextNo=(EditText)findViewById(R.id.passwordloginactivity_edittext_no);
        editTextPassword=(EditText)findViewById(R.id.passwordloginactivity_edittext_password);
        buttonLogin=(Button)findViewById(R.id.passwordloginactivity_button_login);
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper dbHelper=new MyDatabaseHelper(PasswordLoginActivity.this,"Face.db",null,1);
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                Cursor cursor=db.rawQuery("select * from User where no = ? and grant = 1",new String[]{editTextNo.getText().toString()});
                if(cursor.moveToFirst()){
                    do {
                        if(editTextPassword.getText().toString().equals(cursor.getString(cursor.getColumnIndex("password")))){
                            Toast.makeText(PasswordLoginActivity.this,"验证成功，门禁已开启！",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                        else {
                            Toast.makeText(PasswordLoginActivity.this,"密码错误！",Toast.LENGTH_SHORT).show();
                        }
                    }while (cursor.moveToNext());
                }else{
                    Toast.makeText(PasswordLoginActivity.this,"用户不存在！",Toast.LENGTH_SHORT).show();
                }
                cursor.close();
            }
        });
    }
}
