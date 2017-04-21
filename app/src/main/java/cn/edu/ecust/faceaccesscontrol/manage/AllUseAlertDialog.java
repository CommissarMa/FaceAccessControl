package cn.edu.ecust.faceaccesscontrol.manage;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

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
}
