package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.ExitDialogActivity;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;
import cn.edu.ecust.faceaccesscontrol.manage.ActivityCollector;

public class RegisterNewUserActivity extends Toolbar2Activity {

    private EditText textViewUserNo;//用户工号
    private EditText textViewUserName;//用户姓名
    private EditText textViewUserPassword;//用户密码
    private EditText textViewUserConfirmPassword;//用户确认密码
    private EditText textViewUserCellphone;//用户手机号码
    private Button buttonNextStep;//下一步按钮

    private TextView tUserNo;
    private TextView tUserName;
    private TextView tUserPassword;
    private TextView tUserConfirmPassword;
    private TextView tUserCellphone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_user);
        Toolbar toolbarCommon=(Toolbar)findViewById(R.id.registernewuseractivity_toolbar_common);//找到工具条
        setSupportActionBar(toolbarCommon);//激活工具条

        //绑定控件
        textViewUserNo=(EditText)findViewById(R.id.registernewuseractivity_edittext_userno);
        textViewUserName=(EditText)findViewById(R.id.registernewuseractivity_edittext_username);
        textViewUserPassword=(EditText)findViewById(R.id.registernewuseractivity_edittext_userpassword);
        textViewUserConfirmPassword=(EditText)findViewById(R.id.registernewuseractivity_edittext_userconfirmpassword);
        textViewUserCellphone=(EditText)findViewById(R.id.registernewuseractivity_edittext_usercellphone);
        buttonNextStep=(Button)findViewById(R.id.registernewuseractivity_button_nextstep);

        tUserNo=(TextView)findViewById(R.id.registernewuseractivity_textview_userno);
        tUserName=(TextView)findViewById(R.id.registernewuseractivity_textview_username);
        tUserPassword=(TextView)findViewById(R.id.registernewuseractivity_textview_userpassword);
        tUserConfirmPassword=(TextView)findViewById(R.id.registernewuseractivity_textview_userconfirmpassword);
        tUserCellphone=(TextView)findViewById(R.id.registernewuseractivity_textview_usercellphone);

        //点击下一步按钮触发事件
        buttonNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tUserNo.setText("");
                tUserName.setText("");
                tUserPassword.setText("");
                tUserConfirmPassword.setText("");
                tUserCellphone.setText("");
                boolean b=true;
                //判断各个输入框的输入是否正确
                if(textViewUserNo.getText().toString().equals("")){
                    tUserNo.setText("不能为空");
                    b=false;
                }
                if(textViewUserName.getText().toString().equals("")){
                    tUserName.setText("不能为空");
                    b=false;
                }
                if(textViewUserPassword.getText().toString().equals("")){
                    tUserPassword.setText("不能为空");
                    b=false;
                }
                if(!textViewUserConfirmPassword.getText().toString().equals(textViewUserPassword.getText().toString())){
                    tUserConfirmPassword.setText("两次密码输入不同");
                    b=false;
                }
                if(textViewUserCellphone.getText().toString().equals("")){
                    tUserCellphone.setText("不能为空");
                    b=false;
                }

                if(b==true) {
                    Intent intentCameraRegister = new Intent(RegisterNewUserActivity.this, CameraRegisterActivity.class);
                    intentCameraRegister.putExtra("userNo", textViewUserNo.getText().toString());
                    intentCameraRegister.putExtra("userName", textViewUserName.getText().toString());
                    intentCameraRegister.putExtra("userPassword", textViewUserPassword.getText().toString());
                    intentCameraRegister.putExtra("userCellphone", textViewUserCellphone.getText().toString());
                    startActivity(intentCameraRegister);
                }
            }
        });
    }

}
