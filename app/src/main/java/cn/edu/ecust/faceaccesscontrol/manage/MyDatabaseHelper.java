package cn.edu.ecust.faceaccesscontrol.manage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by CommissarMa on 2017/4/25.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    /**
     * 创建通知表
     */
    public static final String CREATE_NOTICE="create table Notice ("
            +"id integer primary key, "
            +"noticetext text, "
            +"time text)";

    /**
     * 创建用户表
     */
    public static final String CREATE_USER="create table User ("
            +"no text primary key, "
            +"name text, "
            +"password text, "
            +"cellphone text, "
            +"grant text)";

    /**
     * 创建识别记录表
     */
    public static final String CREATE_TEST="create table Test ("
            +"testname text primary key)";

    //上下文对象：说明是哪个上下文（活动是其中一种）调用数据库
    private Context mContext;

    /**
     * 构造方法
     */
    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }

    /**
     * 当数据库的version不变时，只会在第一次时创建
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTICE);
        db.execSQL(CREATE_USER);
        db.execSQL(CREATE_TEST);
        Toast.makeText(mContext,"创建数据库成功！",Toast.LENGTH_SHORT).show();
    }

    /**
     * 数据库的升级方法，当新version大于旧version时被调用
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
