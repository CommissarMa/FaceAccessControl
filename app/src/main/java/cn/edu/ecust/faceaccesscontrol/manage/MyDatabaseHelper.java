package cn.edu.ecust.faceaccesscontrol.manage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

/**
 * Created by CommissarMa on 2017/4/25.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_NOTICE="create table Notice ("
            +"id integer primary key, "
            +"noticetext text, "
            +"time text)";

    private Context mContext;

    public MyDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version){
        super(context,name,factory,version);
        mContext=context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_NOTICE);
        Toast.makeText(mContext,"创建数据库成功！",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
