package cn.edu.ecust.faceaccesscontrol.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.FullScreenActivity;

/**
 * Camera视图，采集人脸
 * 调用Opencv Android中的JavaCameraView显示Camera视图
 * 横屏的效果相比竖屏好，这也是程序设计为横屏的原因
 */
public class CameraRecognizeActivity extends FullScreenActivity implements CameraBridgeViewBase.CvCameraViewListener2{

    private CameraBridgeViewBase cameraBridgeViewBaseOpencv;//Camera视图
    private CascadeClassifier cascadeClassifierOpencv;//级联分类器
    private Mat matRgba;//Camera帧，RGBA颜色空间
    private Mat matGray;//Camera帧，灰度图

    private int intFrameCount=0;//已阅的帧数
    private int intFaceCount=0;//已经保存的人脸数

    private BaseLoaderCallback mLoaderCallback=new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status){
                case LoaderCallbackInterface.SUCCESS:{
                    //加载级联分类器
                    try{
                        InputStream cascadeInputStream=getResources().openRawResource(R.raw.haarcascade_frontalface_alt2);//从项目资源中读入级联分类器是，输入流
                        File cascadeDir=getDir("cascade", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
                        File cascadeFile=new File(cascadeDir,"cascade.xml");//新建一个cascade.xml文件
                        FileOutputStream cascadeOutputStream=new FileOutputStream(cascadeFile);//创建一个输出流
                        byte[] buffer=new byte[4096];//字节数组
                        int bytesRead;//待会用来记录输入流返回的字节数
                        while((bytesRead=cascadeInputStream.read(buffer))!=-1){//也就是读进字符数组的字符个数，范围为0到字符数组长度的最大值
                            cascadeOutputStream.write(buffer,0,bytesRead);//写入到cascade.xml文件
                        }
                        cascadeInputStream.close();//关闭输入流
                        cascadeOutputStream.close();//关闭输出流
                        //从android设备的文件系统中载入cascade.xml
                        cascadeClassifierOpencv=new CascadeClassifier(cascadeFile.getAbsolutePath());
                        if(cascadeClassifierOpencv.empty()){
                            Toast.makeText(CameraRecognizeActivity.this,"级联分类器加载失败！",Toast.LENGTH_SHORT).show();
                            cascadeClassifierOpencv=null;
                        }
                    }
                    catch (Exception e){
                        Toast.makeText(CameraRecognizeActivity.this,"级联分类器加载失败！",Toast.LENGTH_SHORT).show();
                    }
                    //开启Camera
                    cameraBridgeViewBaseOpencv.enableView();
                }break;
                default:{
                    super.onManagerConnected(status);
                    Toast.makeText(CameraRecognizeActivity.this,"Opencv管理器连接失败，请确认已正确安装！",Toast.LENGTH_SHORT).show();
                }break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_recognize);

        cameraBridgeViewBaseOpencv=(CameraBridgeViewBase)findViewById(R.id.java_surface_view);//绑定控件
    }

    @Override
    protected void onResume() {
        super.onResume();
        //连接opencvManager
        OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_2_0, this, mLoaderCallback);

        //设置java_surface_view的一些属性（CameraBridgeViewBase）
        cameraBridgeViewBaseOpencv.setCvCameraViewListener(this);//设置监听器
        cameraBridgeViewBaseOpencv.setCameraIndex(CameraBridgeViewBase.CAMERA_ID_FRONT);//设置摄像头索引，前置还是后置
        cameraBridgeViewBaseOpencv.enableFpsMeter();//允许在View中显示fps信息
    }

    /**
     * CameraView开始工作时
     * @param width -  the width of the frames that will be delivered
     * @param height - the height of the frames that will be delivered
     */
    @Override
    public void onCameraViewStarted(int width, int height) {
        matRgba=new Mat(height,width, CvType.CV_8UC4);
    }

    /**
     * CameraView结束工作时
     */
    @Override
    public void onCameraViewStopped() {
        //Log.e(TAG, "Camera应该停下来了");
    }

    /**
     * CameraView工作中
     * @param inputFrame：Camera捕捉到的帧
     * @return matRgba: 返回Mat格式的图像用来显示在View上
     */
    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        intFrameCount++;
        matGray=inputFrame.gray();//转灰度
        matRgba=inputFrame.rgba();//转彩色

        //在帧中检测人脸
        MatOfRect matOfRectFace=new MatOfRect();
        if(cascadeClassifierOpencv!=null){
            cascadeClassifierOpencv.detectMultiScale(matGray,matOfRectFace,1.1,2,2,new Size(200,200),new Size());//检测人脸
        }
        Rect[] rectFacesArray=matOfRectFace.toArray();
        Rect rectMaxFace=null;//最大的人脸
        for(int i=0;i<rectFacesArray.length;i++){
            if(i==0) {
                rectMaxFace=rectFacesArray[0];
            }else {
                if(rectFacesArray[i].height*rectFacesArray[i].width>rectMaxFace.height*rectMaxFace.width){
                    rectMaxFace=rectFacesArray[i];
                }
            }
        }
        if(rectMaxFace!=null) {
            Imgproc.rectangle(matRgba, rectMaxFace.tl(), rectMaxFace.br(), new Scalar(255, 0, 0, 0), 3);//红色，最大的人脸
        }
        //Imgproc.rectangle(mRgba,new Point(500,80),new Point(1420,1000),new Scalar(0,255,0),3);//绿色，事先设定引导框
        Core.flip(matRgba,matRgba,1);//对称变化，类似平面镜成像

        //我挑出第一个人脸进行保存
        if(rectMaxFace!=null && intFrameCount>20 && ((int)rectMaxFace.br().x-(int)rectMaxFace.tl().x>=500)) {//有人脸，超过20帧，并且人脸大小大于500*500
            int rs = (int) rectMaxFace.tl().x;//人脸外接矩形左上顶点x坐标
            int cs = (int) rectMaxFace.tl().y;
            int re = (int) rectMaxFace.br().x;//右下
            int ce = (int) rectMaxFace.br().y;
            Rect r = new Rect(new Point(rs, cs), new Point(re, ce));//填充成Rect
            Mat faceMat = matGray.submat(r);//截取人脸区域，保存到faceMat
            int width = faceMat.width();
            int height = faceMat.height();
            Bitmap bitmapFace = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            Utils.matToBitmap(faceMat, bitmapFace);//转换到Bitmap格式
            String facePath="";
            String faceTime="";
            try {
                File faceDir = getDir("testface", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
                faceTime=getCharacterAndNumber();
                File faceFile = new File(faceDir, faceTime+".jpg");//新建一个jpg文件，以时间命名如：yyyyMMddHHmmss.jpg
                facePath=faceFile.getAbsolutePath();
                FileOutputStream out = new FileOutputStream(faceFile);
                bitmapFace.compress(Bitmap.CompressFormat.JPEG, 100, out);//bitmap写入文件
                out.flush();
                out.close();//关闭流
            }catch (Exception e){
                e.printStackTrace();
            }
            //启动人脸识别计算活动
            Intent intentCalculateActivity=new Intent(CameraRecognizeActivity.this,CalculateActivity.class);
            intentCalculateActivity.putExtra("faceTime",faceTime);
            startActivity(intentCalculateActivity);
            finish();
        }

        if(intFrameCount>200){
            finish();
            Toast.makeText(CameraRecognizeActivity.this,"未检测到人，请重试",Toast.LENGTH_SHORT).show();
        }

        return matRgba;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(cameraBridgeViewBaseOpencv!=null)
        {
            cameraBridgeViewBaseOpencv.disableView();
        }
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(cameraBridgeViewBaseOpencv!=null)
        {
            cameraBridgeViewBaseOpencv.disableView();
        }
        finish();
    }

    //获取当前年份月份日期小时秒，可以当作图像的名字，不会重复
    public static String getCharacterAndNumber() {
        String rel="";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        rel = formatter.format(curDate);
        return rel;
    }
}
