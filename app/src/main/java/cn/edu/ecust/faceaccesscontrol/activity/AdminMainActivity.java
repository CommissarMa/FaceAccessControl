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
import android.widget.Button;
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
        Toolbar toolbarCommon=(Toolbar)findViewById(R.id.adminmainactivity_toolbar_common);//找到工具条
        setSupportActionBar(toolbarCommon);//激活工具条
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
        nameText.setText(pref.getString("admin_name","").equals("")?"管理员":pref.getString("admin_name",""));
        navView.getMenu().findItem(R.id.nav_call).setTitle(pref.getString("admin_cellphone","").equals("")?"手机":pref.getString("admin_cellphone",""));
        navView.getMenu().findItem(R.id.nav_location).setTitle(pref.getString("admin_location","").equals("")?"地址":pref.getString("admin_location",""));
        navView.getMenu().findItem(R.id.nav_mail).setTitle(pref.getString("admin_mail","").equals("")?"邮箱":pref.getString("admin_mail",""));

        Button buttonNoticeManage=(Button)findViewById(R.id.adminmainactivity_button_noticemanage);//通知管理按钮
        Button buttonUserManage=(Button)findViewById(R.id.adminmainactivity_button_usermanage);//用户管理按钮
        Button buttonApproveRegister=(Button)findViewById(R.id.adminmainactivity_button_approveregister);//审批注册按钮
        buttonNoticeManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdminNoticeActivity=new Intent(AdminMainActivity.this,AdminNoticeActivity.class);
                startActivity(intentAdminNoticeActivity);
            }
        });
        buttonUserManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentAdminUserManageActivity=new Intent(AdminMainActivity.this,AdminUserManageActivity.class);
                startActivity(intentAdminUserManageActivity);
            }
        });
        buttonApproveRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case RESULTACTIVITY_CONFIG:
                if(resultCode==RESULT_OK){
                    SharedPreferences pref=getSharedPreferences("admin_data",MODE_PRIVATE);//SharedPreferences的文件名是admin_data
                    View headerLayout = navView.getHeaderView(0); // 0-index header
                    TextView nameText=(TextView)headerLayout.findViewById(R.id.nav_adminname_text);
                    nameText.setText(pref.getString("admin_name","").equals("")?"管理员":pref.getString("admin_name",""));
                    navView.getMenu().findItem(R.id.nav_call).setTitle(pref.getString("admin_cellphone","").equals("")?"手机":pref.getString("admin_cellphone",""));
                    navView.getMenu().findItem(R.id.nav_location).setTitle(pref.getString("admin_location","").equals("")?"地址":pref.getString("admin_location",""));
                    navView.getMenu().findItem(R.id.nav_mail).setTitle(pref.getString("admin_mail","").equals("")?"邮箱":pref.getString("admin_mail",""));
                }
                break;
            default:
        }
    }
}
