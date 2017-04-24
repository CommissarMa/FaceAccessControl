package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;

public class AdminConfigActivity extends Toolbar2Activity {

    //5个输入框
    private AppCompatEditText nameConfigEdit;
    private AppCompatEditText cellphoneConfigEdit;
    private AppCompatEditText locationConfigEdit;
    private AppCompatEditText mailConfigEdit;
    private AppCompatEditText pwConfigEdit;
    private AppCompatEditText confirmpwConfigEdit;
    //保存修改按钮
    private Button saveConfigButton;
    //5个验证详情文本框
    private AppCompatTextView nameTextDetail;
    private AppCompatTextView cellphoneTextDetail;
    private AppCompatTextView locationTextDetail;
    private AppCompatTextView mailTextDetail;
    private AppCompatTextView pwTextDetail;
    private AppCompatTextView confirmpwTextDetail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_config);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);//找到工具条
        setSupportActionBar(toolbar);//激活工具条

        initialUI();//初始化UI
    }

    private void initialUI(){
        //绑定5个EditView控件
        nameConfigEdit=(AppCompatEditText)findViewById(R.id.admin_config_name_text);
        cellphoneConfigEdit=(AppCompatEditText)findViewById(R.id.admin_config_cellphone_text);
        locationConfigEdit=(AppCompatEditText)findViewById(R.id.admin_config_location_text);
        mailConfigEdit=(AppCompatEditText)findViewById(R.id.admin_config_mail_text);
        pwConfigEdit=(AppCompatEditText)findViewById(R.id.admin_config_password_text);
        confirmpwConfigEdit=(AppCompatEditText)findViewById(R.id.admin_config_confirmpassword_text);
        saveConfigButton=(Button)findViewById(R.id.admin_config_save_button);//绑定保存修改按钮
        //绑定验证信息详情文本控件
        nameTextDetail=(AppCompatTextView)findViewById(R.id.admin_config_name_detail);
        cellphoneTextDetail=(AppCompatTextView)findViewById(R.id.admin_config_cellphone_detail);
        locationTextDetail=(AppCompatTextView)findViewById(R.id.admin_config_location_detail);
        mailTextDetail=(AppCompatTextView)findViewById(R.id.admin_config_mail_detail);
        pwTextDetail=(AppCompatTextView)findViewById(R.id.admin_config_password_detail);
        confirmpwTextDetail=(AppCompatTextView)findViewById(R.id.admin_config_confirmpassword_detail);

        SharedPreferences pref=getSharedPreferences("admin_data",MODE_PRIVATE);//SharedPreferences的文件名是admin_data
        nameConfigEdit.setText(pref.getString("admin_name",""));
        cellphoneConfigEdit.setText(pref.getString("admin_cellphone",""));
        locationConfigEdit.setText(pref.getString("admin_location",""));
        mailConfigEdit.setText(pref.getString("admin_mail",""));
        pwConfigEdit.setText(pref.getString("admin_password",""));
        confirmpwConfigEdit.setText(pref.getString("admin_password",""));

        saveConfigButton.setOnClickListener(new View.OnClickListener() {//点击保存修改事件
            @Override
            public void onClick(View v) {
                boolean isAllRight=false;//是否一切正确
                boolean isPwRight=false;
                boolean isConfirmPwRight=false;
                if(pwConfigEdit.getText().toString().equals("")){
                    pwTextDetail.setText("密码不能为空");
                }else{
                    pwTextDetail.setText("");
                    isPwRight=true;
                }
                if(!pwConfigEdit.getText().toString().equals(confirmpwConfigEdit.getText().toString())){
                    confirmpwTextDetail.setText("两次密码输入不同");
                }else{
                    confirmpwTextDetail.setText("");
                    isConfirmPwRight=true;
                }
                if(isPwRight && isConfirmPwRight){
                    isAllRight=true;
                }

                if(isAllRight==true) {
                    SharedPreferences.Editor editor = getSharedPreferences("admin_data", MODE_PRIVATE).edit();//编辑键值对存储文件
                    editor.putString("admin_name", nameConfigEdit.getText().toString());
                    editor.putString("admin_cellphone", cellphoneConfigEdit.getText().toString());
                    editor.putString("admin_location",locationConfigEdit.getText().toString());
                    editor.putString("admin_mail", mailConfigEdit.getText().toString());
                    editor.putString("admin_password", pwConfigEdit.getText().toString());
                    editor.apply();//将添加的键值对应用到键值对存储文件中去
                    setResult(RESULT_OK);//返回成功给上一个
                    finish();
                }
            }
        });
    }
}
