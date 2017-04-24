package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;

public class AdminMainActivity extends Toolbar2Activity {
    public static final int RESULTACTIVITY_CONFIG=1;//从修改管理员信息页面返回的请求码

    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);//找到工具条
        setSupportActionBar(toolbar);//激活工具条
        super.mDrawerLayout=(DrawerLayout)findViewById(R.id.drawer_layout_adminpage);
        ActionBar actionBar=getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_admin);
        }

        navView=(NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_config:
                        Intent intent_config=new Intent(AdminMainActivity.this,AdminConfigActivity.class);
                        startActivityForResult(intent_config,RESULTACTIVITY_CONFIG);
                        break;
                    case R.id.nav_close:
                        Toast.makeText(AdminMainActivity.this,"管理员已注销!",Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    default:
                }
                return true;
            }
        });
        SharedPreferences pref=getSharedPreferences("admin_data",MODE_PRIVATE);//SharedPreferences的文件名是admin_data
        View headerLayout = navView.getHeaderView(0); // 0-index header
        TextView nameText=(TextView)headerLayout.findViewById(R.id.nav_adminname_text);
        nameText.setText(pref.getString("admin_name","").equals("")?"用户名":pref.getString("admin_name",""));
        navView.getMenu().findItem(R.id.nav_call).setTitle(pref.getString("admin_cellphone","").equals("")?"手机":pref.getString("admin_cellphone",""));
        navView.getMenu().findItem(R.id.nav_location).setTitle(pref.getString("admin_location","").equals("")?"地址":pref.getString("admin_location",""));
        navView.getMenu().findItem(R.id.nav_mail).setTitle(pref.getString("admin_mail","").equals("")?"邮箱":pref.getString("admin_mail",""));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULTACTIVITY_CONFIG:
                if(resultCode==RESULT_OK){
                    SharedPreferences pref=getSharedPreferences("admin_data",MODE_PRIVATE);//SharedPreferences的文件名是admin_data
                    View headerLayout = navView.getHeaderView(0); // 0-index header
                    TextView nameText=(TextView)headerLayout.findViewById(R.id.nav_adminname_text);
                    nameText.setText(pref.getString("admin_name","").equals("")?"用户名":pref.getString("admin_name",""));
                    navView.getMenu().findItem(R.id.nav_call).setTitle(pref.getString("admin_cellphone","").equals("")?"手机":pref.getString("admin_cellphone",""));
                    navView.getMenu().findItem(R.id.nav_location).setTitle(pref.getString("admin_location","").equals("")?"地址":pref.getString("admin_location",""));
                    navView.getMenu().findItem(R.id.nav_mail).setTitle(pref.getString("admin_mail","").equals("")?"邮箱":pref.getString("admin_mail",""));
                }
                break;
            default:
        }
    }
}
