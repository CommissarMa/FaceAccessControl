package cn.edu.ecust.faceaccesscontrol.manage;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.activity.AdminMainActivity;
import cn.edu.ecust.faceaccesscontrol.activity.NoAdminMainActivity;

/**
 * Created by CommissarMa on 2017/4/20.
 */

public class AllUseAlertDialog {
    /**
     * 显示退出程序对话框
     * @param activity
     */
    public static void showAlertDialogToExit(Activity activity){
        AlertDialog.Builder dialog_exit=new AlertDialog.Builder(activity);
        dialog_exit.setTitle(R.string.dialogexit_title);
        dialog_exit.setMessage(R.string.dialogexit_message);
        dialog_exit.setCancelable(false);
        dialog_exit.setPositiveButton(R.string.dialogexit_exit_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCollector.finishAll();
            }
        });
        dialog_exit.setNegativeButton(R.string.dialogexit_noexit_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_exit.show();
    }

    public static void adminPasswordConfirmDialogshow(final Activity activity){
        LayoutInflater inflater = LayoutInflater.from(activity);
        View layout = inflater.inflate(R.layout.alert_password, null);
        final AlertDialog.Builder dialog_exit=new AlertDialog.Builder(activity);
        dialog_exit.setTitle(R.string.dialogpassword_title);
        dialog_exit.setView(layout);
        dialog_exit.setCancelable(false);
        final EditText passwordText=(EditText)layout.findViewById(R.id.alert_password_text);
        dialog_exit.setPositiveButton(R.string.dialogpassword_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_exit.setNegativeButton(R.string.dialogpassword_login_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String pw=passwordText.getText().toString();//输入的密码
                SharedPreferences pref=activity.getSharedPreferences("admin_data", Context.MODE_PRIVATE);
                String pw_sp=pref.getString("admin_password","0000");//默认是0000，但如果管理员修改了密码，则会返回相应密码
                if(pw.equals(pw_sp)){
                    Intent admin_intent=new Intent(activity,AdminMainActivity.class);
                    activity.startActivity(admin_intent);
                }
                else{
                    Toast.makeText(activity,"密码错误，请重试!",Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog_exit.show();
    }
}
