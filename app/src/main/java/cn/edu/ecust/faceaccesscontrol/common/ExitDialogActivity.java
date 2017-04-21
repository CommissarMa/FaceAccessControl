package cn.edu.ecust.faceaccesscontrol.common;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;

import cn.edu.ecust.faceaccesscontrol.manage.ActivityCollector;
import cn.edu.ecust.faceaccesscontrol.manage.AllUseAlertDialog;

/**
 * 作为其他Activity的父类使用
 * 取消状态栏
 * 能弹出退出警告框
 * 能对活动进行管理，包括添加一个活动，删除一个活动，删除所有活动
 */
public class ExitDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//取消状态栏
        ActivityCollector.addActivity(this);//添加当前活动到活动集合管理类中
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityCollector.removeActivity(this);//从活动集合管理类中删除当前活动
    }

    /**
     * 按下Android物理按键时触发
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){//按下返回键
            AllUseAlertDialog.showAlertDialogToExit(this);//弹出警告框
        }
        return super.onKeyDown(keyCode, event);
    }
}
