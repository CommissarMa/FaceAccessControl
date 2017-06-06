package cn.edu.ecust.faceaccesscontrol.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.edu.ecust.faceaccesscontrol.R;
import cn.edu.ecust.faceaccesscontrol.common.Toolbar2Activity;
import cn.edu.ecust.faceaccesscontrol.manage.MyDatabaseHelper;
import libsvm.svm;
import libsvm.svm_model;
import libsvm.svm_node;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CalculateActivity extends Toolbar2Activity {

    private String testFaceName;//测试人脸的文件名
    private int peopleCount;//库中的人数
    private String[] userNoArray;//用户工号数组
    private List<String> userNoList=new ArrayList<>();//用户工号列表

    private Mat subMatTrainPcaFace;//训练数据矩阵
    private Mat subMatTestPcaFace;//测试数据矩阵

    private String pcaSvmResultNo;//pca+libsvm得到的分类
    private double faceppZxd;//置信度，face++
    private double faceppYz;//阈值，face++
    private boolean isOnePersonFacepp;//face++判断是不是一个人的结果

    private ProgressDialog progressDialog;//进度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculate);
        Toolbar toolbarCommon=(Toolbar)findViewById(R.id.calculateactivity_toolbar_common);//找到工具条
        setSupportActionBar(toolbarCommon);//激活工具条

        Intent intentCamera=getIntent();
        testFaceName=intentCamera.getStringExtra("faceTime");//获取照片按照时间生成的名字，记得用的时候要加.jpg

        //显示进度对话框
        progressDialog=new ProgressDialog(CalculateActivity.this);
        progressDialog.setTitle("进度对话框");
        progressDialog.setMessage("正在识别...");
        progressDialog.setCancelable(true);//设置是否可以手动关闭这个对话框

        //先从数据库中读取所有用户（grant=1）
        readUserNoFromDB();

        //判断人数，少于5个进行分支
        if(peopleCount==0){
            Toast.makeText(CalculateActivity.this,"当前没有用户！请先注册！",Toast.LENGTH_SHORT).show();
        }else if(peopleCount<5){
            progressDialog.show();
            //Opencv中的Pca
            //pca(peopleCount*5);
            pca(5);
            //libsvm
            //libsvm(peopleCount*5);
            libsvm(5);
        }else if(peopleCount>=5){
            progressDialog.show();
            //Opencv中的Pca
            pca(25);
            //libsvm
            libsvm(25);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        //face++
        new Thread(new Runnable() {
            @Override
            public void run() {
                facepp(pcaSvmResultNo);
            }
        }).start();
    }

    public void readUserNoFromDB(){
        MyDatabaseHelper dbHelper=new MyDatabaseHelper(this,"Face.db",null,1);
        SQLiteDatabase db=dbHelper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select * from User where grant = 1",null);
        peopleCount=0;
        if(cursor.moveToFirst()){
            do{
                String no=cursor.getString(cursor.getColumnIndex("no"));
                userNoList.add(no);
                peopleCount++;//得到用户的数量
            }while(cursor.moveToNext());
        }
        cursor.close();
    }

    public int pow(int value,int n){
        int result=1;
        for(int i=0;i<n;i++){
            result=result*value;
        }
        return result;
    }

    public Mat lbp(Mat src){
        Mat dst=new Mat(src.height()-2,src.width()-2,CvType.CV_32FC1);//实际应该为8位二进制数

        for(int i=1;i<src.height()-1;i++){
            for(int j=1;j<src.width()-1;j++){
                //3*3的范围进行判断
                double center=src.get(i, j)[0];
                int result=0;

                double[] d=new double[8];
                int[] r=new int[8];

                d[0]=src.get(i-1, j-1)[0];
                d[1]=src.get(i-1, j)[0];
                d[2]=src.get(i-1, j+1)[0];
                d[3]=src.get(i, j+1)[0];
                d[4]=src.get(i+1, j+1)[0];
                d[5]=src.get(i+1, j)[0];
                d[6]=src.get(i+1, j-1)[0];
                d[7]=src.get(i, j-1)[0];

                for(int start=0;start<8;start++){
                    if(d[start]>center){
                        r[start]=1;
                    }else{
                        r[start]=0;
                    }
                }

                for(int start=0;start<8;start++){
                    result=result+r[start]*pow(2,start);
                }

                dst.put(i-1, j-1, result);
            }
        }
        return dst;
    }

    public void pca(int pcaN){
        //总的数据矩阵
        Mat allDataMat=new Mat(peopleCount*5+1,100*100, CvType.CV_32FC1);

        File faceDir = getDir("face", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
        File testfaceDir = getDir("testface", Context.MODE_PRIVATE);//有此目录就获取，没有就创建

        int start=0;
        for(int i=0;i<peopleCount;i++){//第1个人到第n个人，每人5张照片读入，并resize到100*100
            String no=userNoList.get(i);
            for(int j=1;j<=5;j++){
                Mat m=Imgcodecs.imread(faceDir.getAbsolutePath()+"/"+no+"_"+j+".jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
                Imgproc.resize(m,m,new Size(102,102));//20*20的人脸图像,尺寸标准化
                m=lbp(m);
                Mat temp=m.reshape(1, 100*100);//change image's col & row，92*112行1列
                for(int k=0;k<100*100;k++){
                    double[] data=new double[1];
                    data=temp.get(k, 0);
                    allDataMat.put(start, k, data);
                }
                start++;
            }
        }
        //塞入测试的人脸
        Mat m=Imgcodecs.imread(testfaceDir.getAbsolutePath()+"/"+testFaceName+".jpg", Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
        Imgproc.resize(m,m,new Size(102,102));//20*20的人脸图像,尺寸标准化
        m=lbp(m);
        Mat temp=m.reshape(1, 100*100);//change image's col & row，92*112行1列
        for(int k=0;k<100*100;k++){
            double[] data=new double[1];
            data=temp.get(k, 0);
            allDataMat.put(start, k, data);
        }

        //PCA
        Mat Mean = new Mat();//特征值
        Mat Vectors = new Mat();//特征向量
        //用30落到92%
        Core.PCACompute(allDataMat.t(), Mean, Vectors,pcaN);//Mat.t()是矩阵转置
        Mat allPcaData=Vectors.t();//400*20的矩阵，每行对应一张人脸的特征向量
        //train:0-199行
        subMatTrainPcaFace=allPcaData.submat(0, peopleCount*5,0, pcaN);
        //test:200-399行
        subMatTestPcaFace=allPcaData.submat(peopleCount*5, peopleCount*5+1,0,pcaN);
    }

    public void libsvm(int pcaN){
        //Libsvm部分
        int trainRow=peopleCount*5;
        svm_node node;//每个节点（索引：特征值）
        svm_node[] nodeArray;//n维节点构成向量
        svm_node[][] nodeDataSet=new svm_node[trainRow][];//m个向量构成数据集
        for(int i=0;i<trainRow;i++){
            nodeArray=new svm_node[pcaN];
            for(int j=0;j<pcaN;j++){
                node=new svm_node();
                node.index=j;
                node.value=subMatTrainPcaFace.get(i, j)[0];
                nodeArray[j]=node;
            }
            nodeDataSet[i]=nodeArray;
        }
        nodeArray=new svm_node[pcaN];
        double[] labels=new double[trainRow];
        for(int i=0;i<peopleCount;i++){
            for(int j=0;j<5;j++){
                labels[5*i+j]=i;
            }
        }

        //定义svm_problem对象
        svm_problem problem = new svm_problem();
        problem.l = trainRow; //向量个数
        problem.x = nodeDataSet; //训练集向量表
        problem.y = labels; //对应的lable数组

        //定义svm_parameter对象
        svm_parameter param = new svm_parameter();
        param.svm_type = svm_parameter.C_SVC;
        param.kernel_type = svm_parameter.LINEAR;
        //param.cache_size = 100;
        param.eps = 0.000001;
        param.C = 1;
        //param.probability=1;
        param.gamma=0.5;
        param.nu=1;

        //训练SVM分类模型
        if(svm.svm_check_parameter(problem, param)==null) {//如果参数没有问题，则svm.svm_check_parameter()函数返回null,否则返回error描述。

        }
        svm_model model = svm.svm_train(problem, param); //svm.svm_train()训练出SVM分类模型

        //定义测试数据 tnodeArray
        svm_node tnode;//每个节点（索引：特征值）
        svm_node[] tnodeArray;//n维节点构成向量
        tnodeArray=new svm_node[pcaN];
        for(int j=0;j<pcaN;j++){
            tnode=new svm_node();
            tnode.index=j;
            tnode.value=subMatTestPcaFace.get(0, j)[0];
            tnodeArray[j]=tnode;
        }

        int nr=peopleCount;
        double[] dec_values=new double[nr*(nr-1)/2];
        double result=svm.svm_predict_values(model, tnodeArray,dec_values);
        //Toast.makeText(CalculateActivity.this,""+userNoList.get((int)result),Toast.LENGTH_SHORT).show();
        pcaSvmResultNo=userNoList.get((int)result);
    }

    public void facepp(String resultNo){
        try {
            File faceDir = getDir("face", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
            File testfaceDir = getDir("testface", Context.MODE_PRIVATE);//有此目录就获取，没有就创建
            File image1=new File(testfaceDir.getAbsolutePath()+"/"+testFaceName+".jpg");
            File image2=new File(faceDir.getAbsolutePath()+"/"+resultNo+"_1.jpg");

            OkHttpClient client=new OkHttpClient();
            MultipartBody.Builder requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM);
            requestBody.addFormDataPart("api_key", "XJ9dUEAPQc8Fsh_uMg_f5a_vR8QzjJOs");
            requestBody.addFormDataPart("api_secret", "jJvXMwYhdoszwvaVEBFNXfyJUfJpDUZN");

            RequestBody body1 = RequestBody.create(MediaType.parse("image/*"), image1);
            requestBody.addFormDataPart("image_file1", image1.getName(), body1);
            RequestBody body2 = RequestBody.create(MediaType.parse("image/*"), image2);
            requestBody.addFormDataPart("image_file2", image2.getName(), body2);

            Request request = new Request.Builder().url("https://api-cn.faceplusplus.com/facepp/v3/compare").post(requestBody.build()).build();

            Response response=client.newCall(request).execute();
            String responseData=response.body().string();
            //Log.e("返回的结果：", responseData );
            try{
                JSONObject jsonobject=new JSONObject(responseData);
                double confidence=jsonobject.getDouble("confidence");
                faceppZxd=confidence;
                JSONObject thOj=jsonobject.getJSONObject("thresholds");
                double thresholds=thOj.getDouble("1e-4");
                faceppYz=thresholds;
                if(confidence>=thresholds){
                    isOnePersonFacepp=true;
                }else{
                    isOnePersonFacepp=false;
                }
                showResponse(isOnePersonFacepp);
                //Log.e("置信度：", ""+confidence+"阈值："+thresholds);
            }catch (JSONException e){
                Log.e("Json失败！", "Json失败！ " );
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showResponse(final Boolean b){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
                //Toast.makeText(CalculateActivity.this,isOnePersonFacepp+"",Toast.LENGTH_SHORT).show();
                Intent intentResult=new Intent(CalculateActivity.this,ResultActivity.class);
                intentResult.putExtra("isOnePersonFacepp",isOnePersonFacepp);//是否识别出人
                intentResult.putExtra("resultNo",pcaSvmResultNo);//识别出的用户工号
                intentResult.putExtra("testFaceName",testFaceName);//测试人脸的文件名
                startActivity(intentResult);
                finish();//关闭这个为了计算的过渡活动
            }
        });
    }
}
