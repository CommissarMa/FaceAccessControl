package cn.edu.ecust.faceaccesscontrol.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;

public class AdminMainActivity extends Toolbar2Activity {

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
        NavigationView navView=(NavigationView)findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch(item.getItemId()){
                    case R.id.nav_config:
                        break;
                    case R.id.nav_close:
                        Toast.makeText(AdminMainActivity.this,"Clicked home!",Toast.LENGTH_SHORT).show();
                        break;
                    default:
                }
                return true;
            }
        });
    }

}
