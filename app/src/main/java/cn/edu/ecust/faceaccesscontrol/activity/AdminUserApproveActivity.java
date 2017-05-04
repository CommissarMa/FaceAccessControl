package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;
import cn.edu.ecust.faceaccesscontrol.manage.MyDatabaseHelper;
import cn.edu.ecust.faceaccesscontrol.userrecycler.ApproveUserCardAdapter;
import cn.edu.ecust.faceaccesscontrol.userrecycler.UserCard;
import cn.edu.ecust.faceaccesscontrol.userrecycler.UserCardAdapter;

public class AdminUserApproveActivity extends Toolbar2Activity {

    private List<UserCard> userCardList=new ArrayList<>();
    private ApproveUserCardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user_approve);
        Toolbar toolbarCommon=(Toolbar)findViewById(R.id.adminuserapproveactivity_toolbar_common);//找到工具条
        setSupportActionBar(toolbarCommon);//激活工具条
    }

    @Override
    protected void onResume() {
        super.onResume();

        //清空列表
        userCardList=new ArrayList<>();
        //先从数据库中获取所有用户的工号
        getAllUserNoFromDB();//这时候userCardList就有数据了

        //填充RecyclerView
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.adminuserapproveactivity_recyclerview_main);
        GridLayoutManager layoutManager=new GridLayoutManager(this,4);//设置每行显示的card数量
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ApproveUserCardAdapter(userCardList);
        recyclerView.setAdapter(adapter);
    }

    private void getAllUserNoFromDB(){
        File faceDir = getDir("face", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
        MyDatabaseHelper dbHelper=new MyDatabaseHelper(AdminUserApproveActivity.this,"Face.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select no from User where grant = 0",null);
        if(cursor.moveToFirst()){
            do{
                String userNo=cursor.getString(cursor.getColumnIndex("no"));
                UserCard userCard=new UserCard(userNo,faceDir.getAbsolutePath()+"/"+userNo+"_1.jpg");
                userCardList.add(userCard);
            }while (cursor.moveToNext());
        }
    }
}
