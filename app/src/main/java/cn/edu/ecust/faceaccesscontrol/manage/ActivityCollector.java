package cn.edu.ecust.faceaccesscontrol.manage;

import android.app.Activity;
import android.support.v7.app.ActionBar;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动集合管理类
 * 添加活动；删除活动；结束所有活动
 * 主要用到List存储活动
 * Created by CommissarMa on 2017/4/20.
 */

public class ActivityCollector {
    public static List<Activity> activities=new ArrayList<>();

    public static void addActivity(Activity activity){
        activities.add(activity);
    }

    public static void removeActivity(Activity activity){
        activities.remove(activity);
    }

    public static void finishAll(){
        for(Activity activity:activities){
            if(!activity.isFinishing()){
                activity.finish();
            }
        }
        activities.clear();
    }
}
