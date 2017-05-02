package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;
import cn.edu.ecust.faceaccesscontrol.manage.AllUseAlertDialog;
import cn.edu.ecust.faceaccesscontrol.manage.MyDatabaseHelper;

public class AdminNoticeActivity extends Toolbar2Activity {

    private MyDatabaseHelper dbHelper;
    private int noticeCount;

    private AppCompatEditText addNoticeEditText;
    private ListView showListView;
    private Button addNoticeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notice);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);//找到工具条
        setSupportActionBar(toolbar);//激活工具条

        addNoticeEditText=(AppCompatEditText)findViewById(R.id.adminnoticepage_addnotice_edittext);
        addNoticeButton=(Button)findViewById(R.id.adminnoticepage_addnotice_button);
        showListView=(ListView)findViewById(R.id.adminnoticepage_listview);

        dbHelper=new MyDatabaseHelper(this,"Face.db",null,1);
        final SQLiteDatabase db=dbHelper.getWritableDatabase();
        String[] noticeArray=getNoticeListViewFromDB(db);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(AdminNoticeActivity.this,android.R.layout.simple_list_item_1,noticeArray);
        showListView.setAdapter(adapter);

        addNoticeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!addNoticeEditText.getText().toString().equals("")){
                    //获取当前通知数量
                    Cursor cursor=db.rawQuery("select * from Notice",null);
                    int countNotice=0;
                    if(cursor.moveToFirst()){
                        do {
                            countNotice++;
                        }while (cursor.moveToNext());
                    }
                    Log.e("count", ""+countNotice );
                    cursor.close();

                    //插入新通知
                    ContentValues values=new ContentValues();
                    values.put("id",countNotice+1);
                    values.put("noticetext",addNoticeEditText.getText().toString());
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    String timeString = formatter.format(curDate);
                    Log.e("时间", timeString);
                    values.put("time",timeString);
                    db.insert("Notice",null,values);

                    addNoticeEditText.setText("");

                    String[] noticeArray=getNoticeListViewFromDB(db);
                    ArrayAdapter<String> adapter=new ArrayAdapter<String>(AdminNoticeActivity.this,android.R.layout.simple_list_item_1,noticeArray);
                    showListView.setAdapter(adapter);
                }
            }
        });

        showListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {//position是从0开始的，数据库的自增长id从1开始
                AllUseAlertDialog.showNoticeOptionsAlertDialog(AdminNoticeActivity.this,noticeCount-position);
            }
        });
    }

    public String[] getNoticeListViewFromDB(SQLiteDatabase db){
        Cursor cursor=db.rawQuery("select * from Notice order by id desc",null);
        ArrayList<String> noticeArrayList=new ArrayList<String>();
        int max=0;
        if(cursor.moveToFirst()){
            do {
                int id = cursor.getInt(cursor.getColumnIndex("id"));
                if(id>max){
                    max=id;
                }
                String noticeText = cursor.getString(cursor.getColumnIndex("noticetext"));
                noticeArrayList.add(noticeText);
                Log.e("id:", id+"" );
            }while (cursor.moveToNext());
        }
        cursor.close();
        noticeCount=max;
        String[] noticeArray=noticeArrayList.toArray(new String[noticeArrayList.size()]);
        return  noticeArray;
    }

    @Override
    protected void onResume() {
        super.onResume();

        dbHelper=new MyDatabaseHelper(this,"Face.db",null,1);
        final SQLiteDatabase db=dbHelper.getWritableDatabase();
        String[] noticeArray=getNoticeListViewFromDB(db);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(AdminNoticeActivity.this,android.R.layout.simple_list_item_1,noticeArray);
        showListView.setAdapter(adapter);
    }
}
