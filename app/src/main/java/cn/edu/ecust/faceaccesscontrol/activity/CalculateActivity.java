package cn.edu.ecust.faceaccesscontrol.activity;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;

public class CalculateActivity extends Toolbar2Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        Toolbar toolbarCommon=(Toolbar)findViewById(R.id.calculateactivity_toolbar_common);//找到工具条
        setSupportActionBar(toolbarCommon);//激活工具条
    }
}
