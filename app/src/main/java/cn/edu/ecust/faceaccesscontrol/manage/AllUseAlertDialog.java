package cn.edu.ecust.faceaccesscontrol.manage;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.activity.AdminMainActivity;
import cn.edu.ecust.faceaccesscontrol.activity.AdminNoticeActivity;
import cn.edu.ecust.faceaccesscontrol.activity.AdminNoticeEditActivity;
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
        //dialog_exit.setCancelable(false);
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
        //dialog_exit.setCancelable(false);
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

    public static void showNoticeOptionsAlertDialog(final Activity activity, final int index){//index为id在数据库中的实际值
        AlertDialog.Builder dialog_exit=new AlertDialog.Builder(activity);
        //dialog_exit.setTitle(R.string.dialogexit_title);
        //dialog_exit.setMessage(R.string.dialogexit_message);
        //dialog_exit.setCancelable(false);
        dialog_exit.setPositiveButton(R.string.adminnotice_delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//删除通知按钮
                Toast.makeText(activity,index+"",Toast.LENGTH_SHORT).show();
                MyDatabaseHelper dbHelper=new MyDatabaseHelper(activity,"Face.db",null,1);
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.delete("Notice","id = ?",new String[]{""+index});
                db.execSQL("update Notice set id = id-1 where id > ?",new String[]{""+index});
            }
        });
        dialog_exit.setNegativeButton(R.string.adminnotice_edit, new DialogInterface.OnClickListener() {//编辑通知按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent_newNoticeEditActivity=new Intent(activity, AdminNoticeEditActivity.class);
                intent_newNoticeEditActivity.putExtra("idIndexInDB",index);//把id的index传过去
                activity.startActivity(intent_newNoticeEditActivity);
            }
        });
        dialog_exit.show();
    }

    public static void showUserDeleteAlertDialog(final Activity activity,final String userNo,final String userName){
        AlertDialog.Builder dialog_exit=new AlertDialog.Builder(activity);
        dialog_exit.setTitle("删除");
        dialog_exit.setMessage("确认删除"+userNo+" "+userName+"用户吗？");
        //dialog_exit.setCancelable(false);
        dialog_exit.setNegativeButton("删除", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//删除通知按钮
                MyDatabaseHelper dbHelper=new MyDatabaseHelper(activity,"Face.db",null,1);
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.delete("User","no = ?",new String[]{userNo});//数据库User表中删除该用户

                //获取当前通知数量
                Cursor cursor=db.rawQuery("select * from Notice",null);
                int countNotice=0;
                if(cursor.moveToFirst()){
                    do {
                        countNotice++;
                    }while (cursor.moveToNext());
                }
                cursor.close();

                //插入新通知
                ContentValues values=new ContentValues();
                values.put("id",countNotice+1);
                values.put("noticetext","用户"+userNo+userName+"被移除");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String timeString = formatter.format(curDate);//时间字符串
                values.put("time",timeString);
                db.insert("Notice",null,values);

                Toast.makeText(activity,"删除成功",Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
        dialog_exit.setPositiveButton("取消", new DialogInterface.OnClickListener() {//编辑通知按钮
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_exit.show();
    }

    /**
     * 审批同意注册对话框
     * @param activity
     * @param userNo
     * @param userName
     */
    public static void showApproveOkAlertDialog(final Activity activity,final String userNo,final String userName){
        AlertDialog.Builder dialog_exit=new AlertDialog.Builder(activity);
        dialog_exit.setTitle("审批");
        dialog_exit.setMessage("确认通过"+userNo+" "+userName+"的注册？");
        //dialog_exit.setCancelable(false);
        dialog_exit.setNegativeButton("同意", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//同意注册按钮
                MyDatabaseHelper dbHelper=new MyDatabaseHelper(activity,"Face.db",null,1);
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.execSQL("update User set grant = 1  where no = ?",new String[]{userNo});

                //获取当前通知数量
                Cursor cursor=db.rawQuery("select * from Notice",null);
                int countNotice=0;
                if(cursor.moveToFirst()){
                    do {
                        countNotice++;
                    }while (cursor.moveToNext());
                }
                cursor.close();

                //插入新通知
                ContentValues values=new ContentValues();
                values.put("id",countNotice+1);
                values.put("noticetext",userNo+userName+"的注册已审核通过");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String timeString = formatter.format(curDate);//时间字符串
                values.put("time",timeString);
                db.insert("Notice",null,values);

                Toast.makeText(activity,"已同意",Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
        dialog_exit.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_exit.show();
    }

    public static void showApproveRefuseAlertDialog(final Activity activity,final String userNo,final String userName){
        AlertDialog.Builder dialog_exit=new AlertDialog.Builder(activity);
        dialog_exit.setTitle("审批");
        dialog_exit.setMessage("确认拒绝"+userNo+" "+userName+"的注册？");
        //dialog_exit.setCancelable(false);
        dialog_exit.setNegativeButton("拒绝", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {//同意注册按钮
                MyDatabaseHelper dbHelper=new MyDatabaseHelper(activity,"Face.db",null,1);
                SQLiteDatabase db=dbHelper.getWritableDatabase();
                db.execSQL("delete from User where no = ?",new String[]{userNo});

                //获取当前通知数量
                Cursor cursor=db.rawQuery("select * from Notice",null);
                int countNotice=0;
                if(cursor.moveToFirst()){
                    do {
                        countNotice++;
                    }while (cursor.moveToNext());
                }
                cursor.close();

                //插入新通知
                ContentValues values=new ContentValues();
                values.put("id",countNotice+1);
                values.put("noticetext","已拒绝"+userNo+userName+"的注册");
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());
                String timeString = formatter.format(curDate);//时间字符串
                values.put("time",timeString);
                db.insert("Notice",null,values);

                Toast.makeText(activity,"已拒绝",Toast.LENGTH_SHORT).show();
                activity.finish();
            }
        });
        dialog_exit.setPositiveButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog_exit.show();
    }
}
