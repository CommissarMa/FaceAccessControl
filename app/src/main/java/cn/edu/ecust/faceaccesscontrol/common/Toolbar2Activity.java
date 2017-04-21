package cn.edu.ecust.faceaccesscontrol.common;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.activity.NoAdminMainActivity;
import cn.edu.ecust.faceaccesscontrol.activity.WelcomeActivity;

/**
 * 作为父类使用
 * 继承自ExitDialogActivity
 * 布局Toolbar
 * Toolbar上添加三个图标按钮
 * 添加了对应事件
 * 2017年4月21日10:50:46
 */
public class Toolbar2Activity extends ExitDialogActivity {
    public DrawerLayout mDrawerLayout;//滑动菜单

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar,menu);//将工具条填充到活动中
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.toolbar_back://点击工具栏上的返回按钮
                this.finish();
                break;
            case R.id.toolbar_home://点击工具栏上的起始页按钮
                //Toast.makeText(this,"Clicked home!",Toast.LENGTH_SHORT).show();
                Intent intent_home=new Intent(Toolbar2Activity.this,WelcomeActivity.class);
                startActivity(intent_home);
                break;
            case R.id.toolbar_tutor://点击工具栏上的帮助按钮
                //Toast.makeText(this,"Clicked tutor!",Toast.LENGTH_SHORT).show();
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            default:
        }
        return true;
    }
}
