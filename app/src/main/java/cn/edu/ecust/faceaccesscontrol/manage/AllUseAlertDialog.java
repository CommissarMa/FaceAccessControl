package cn.edu.ecust.faceaccesscontrol.manage;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import cn.edu.ecust.faceaccesscontrol.R;

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

    public static void adminPasswordConfirmDialogshow(Activity activity){
        final AlertDialog.Builder dialog_exit=new AlertDialog.Builder(activity);
        final EditText passwordText=new EditText(activity);
        dialog_exit.setTitle(R.string.dialogpassword_title);
        dialog_exit.setView(passwordText);
        dialog_exit.setCancelable(false);
        dialog_exit.setPositiveButton(R.string.dialogpassword_cancel_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_exit.setNegativeButton(R.string.dialogpassword_login_button, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.e("ISISIS", passwordText.getText().toString() );
            }
        });
        dialog_exit.show();
    }
}
