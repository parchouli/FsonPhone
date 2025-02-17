//package com.example.Fson.WorkClass;
//
//import android.annotation.TargetApi;
//import android.app.Activity;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.Color;
//import android.hardware.Camera;
//import android.media.CamcorderProfile;
//import android.media.MediaPlayer;
//import android.media.MediaRecorder;
//import android.net.ConnectivityManager;
//import android.net.NetworkInfo;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Environment;
//import android.os.Handler;
//import android.os.StatFs;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.SurfaceHolder;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.view.animation.LinearInterpolator;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.Fson.BeanClass.ItemBean;
//import com.example.Fson.BeanClass.LogBean;
//import com.example.Fson.BeanClass.RecordBean;
//import com.example.Fson.BeanClass.StepBean;
//import com.example.Fson.BeanClass.TitleBean;
//import com.example.Fson.ControlClass.WorkDb;
//import com.example.Fson.PreviewClass.ImageShow;
//import com.example.Fson.PreviewClass.VideoPlayer;
//import com.example.Fson.R;
//import com.example.Fson.ToolsClass.CameraHelper;
//import com.example.Fson.ToolsClass.FileUtil;
//import com.example.Fson.ToolsClass.NumberFormatClass;
//import com.example.Fson.ToolsClass.toResult;
//import com.example.Fson.WorkClass.Ftp.FTPClientFunctions;
//import com.example.Fson.WorkClass.Speech.SpeechCallBack;
//import com.example.Fson.WorkClass.Speech.SpeechRecognition;
//import com.example.Fson.WorkClass.asynchttp.HttpUtils;
//import com.example.Fson.WorkClass.asynchttp.NetCallBack;
//import com.example.Fson.WorkClass.camera.CameraInterface;
//import com.example.Fson.WorkClass.camera.CameraSurfaceView;
//import com.example.Fson.WorkClass.camera.FocusCallBack;
//import com.example.Fson.ToolsClass.iflytek.FucUtil;
//import com.loopj.android.http.RequestParams;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import org.json.JSONTokener;
//
//
//import java.io.BufferedReader;
//import java.io.BufferedWriter;
//import java.io.File;
//
//import java.io.FileReader;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Timer;
//import java.util.TimerTask;
//// 无图纸
//
//public class MainActivity extends Activity implements Camera.PictureCallback, MediaRecorder.OnErrorListener, MediaPlayer.OnCompletionListener, KeyEvent.Callback, SpeechCallBack, FocusCallBack {
//    private SpeechRecognition mSpeechRecognition;
//    private static String TAG = "TAG";
//    private LinearLayout showline, line_point;
//    private TextView showtext1, showtext2, showtext3, showtext4;
//    private boolean isRecording = false;    //是否在录制
//    private int minMB = 50; //视频录制要求最小的内存
//    private String cacheError = "内存不足" + minMB + "MB无法录制";
//    private CameraSurfaceView mySurfaceView;
//    private SurfaceHolder mySurfaceHolder;
//    private MediaRecorder mMediaRecorder;   //录像
//    private CamcorderProfile profile;
//    private File imgFile;        //图像文件
//    private File mOutputFile;   //视频录制的文件
//    private Handler mHanler = new Handler();
//    private long recordTime = 0;    //录像的时间
//    private TextView tvRecordTime;  //提示
//    private int delayCheckCacheTime = 1000; //视频录制时多长时间检测一次内存
//    private static final int REQUEST_CODE_SCAN = 1;     // 扫码
//    private static final String DECODED_CONTENT_KEY = "codedContent";
//    private static final String EXITACTION = "action.exit";
//    private String work_id = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()), timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
//    private int longEnter = 0;
//    private int sortEnter = 0;
//    private Handler longHandler = new Handler();
//    private Handler sortHandler = new Handler();
//    private WorkDb workDb;
//    private CameraInterface cameraInterface;
//    private ItemBean itemBean;
//    private StepBean stepBean;
//    private TitleBean titleBean;
//    private ArrayList<TitleBean> titleBeanArrayList = new ArrayList<>();
//    private ArrayList<StepBean> stepBeanArrayList = new ArrayList<>();
//    private String saying = "系统请求开始，请发口令！";
//    private int order = 0;
//    private int item_num;
//    private int step_column = 0;
//    private int step_order = 0;
//    private TextView stop, takepic, takevid, starttake, takeover, takenormal, takeunnormal, lookpic, strong;
//    private TextView opentouch, closetouch, big, small;
//    private ImageView focusImg1;
//    private ImageView focusImg2;
//    private ImageView point_icon1, point_icon2, point_icon3;
//    private Handler pointHander = new Handler();
//    private TextView wakeup;    //还存在问题-测试
//    private toResult toResult;
//    private boolean isBiger = false;
//    private String work_ids, nums;
//    private NumberFormatClass numberFormatClass;
//    private ArrayList<String> numStrings = new ArrayList<>();
//    private int isNormal = 0;
//    private boolean isStart = false;
//    private boolean isEntry = true;   // K34
//    //    private boolean isEntry = false;  // 5719
//    private Bitmap showImgBitmap = null;
//    private LinearLayout showImgLay;
//    private ImageView showImg;
//    private ArrayList<String> uploadFileStrings = new ArrayList<>();
//    private boolean isUploading = false;
//    private String user_id = "null";
//    private String uploadIp = "192.168.1.101";
//    private String OriginalUrl = "", insertlogUrl = "", updateimageUrl = "", insertrecordUrl = "";
//    private boolean hasBigTitle = false;
//    private boolean hasTitle = false;
//    private int image_order = 0;
//    private int op_sorder = 1;
//    private int step_sorder = 1;
//    private int substepid = 1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);//隐藏标题
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
//        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
//        setContentView(R.layout.activity_main);
//        initView();
//        initInfo();
//        initBulid();
//    }
//
//    /* 初始化view */
//    private void initView() {
//        wakeup = (TextView) findViewById(R.id.wakeup);
//        focusImg1 = (ImageView) findViewById(R.id.focusImg1);
//        focusImg2 = (ImageView) findViewById(R.id.focusImg2);
//        Animation animation = AnimationUtils.loadAnimation(this, R.anim.circular);
//        LinearInterpolator lin = new LinearInterpolator();
//        animation.setInterpolator(lin);
//        animation.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//                focusImg1.setVisibility(View.VISIBLE);
//                focusImg2.setVisibility(View.VISIBLE);
//                focusImg1.setTop(315);
//                focusImg2.setTop(315);
//            }
//
//            public void onAnimationEnd(Animation animation) {
//            }
//
//            public void onAnimationRepeat(Animation animation) {
//            }
//        });
//        focusImg1.startAnimation(animation);
//        focusImg2.startAnimation(animation);
//        focusImg1.setVisibility(View.INVISIBLE);
//        focusImg2.setVisibility(View.INVISIBLE);
//        showline = (LinearLayout) findViewById(R.id.showline);
//        showline.getBackground().setAlpha(60);
//        line_point = (LinearLayout) findViewById(R.id.line_point);
//        showtext1 = (TextView) findViewById(R.id.showtext1);
//        showtext2 = (TextView) findViewById(R.id.showtext2);
//        showtext3 = (TextView) findViewById(R.id.showtext3);
//        showtext4 = (TextView) findViewById(R.id.showtext4);
//        showtext4.setTextColor(Color.RED);
//        tvRecordTime = (TextView) findViewById(R.id.tv_record_time);
//        profile = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
//        mySurfaceView = (CameraSurfaceView) findViewById(R.id.mySurfaceView);
//        mySurfaceHolder = mySurfaceView.getSurfaceHolder(MainActivity.this);
//        stop = (TextView) findViewById(R.id.stop);
//        takepic = (TextView) findViewById(R.id.takepic);
//        takevid = (TextView) findViewById(R.id.takevid);
//        starttake = (TextView) findViewById(R.id.starttake);
//        takeover = (TextView) findViewById(R.id.takeover);
//        takenormal = (TextView) findViewById(R.id.takenormal);
//        takeunnormal = (TextView) findViewById(R.id.takeunnormal);
//        lookpic = (TextView) findViewById(R.id.lookpic);
//        strong = (TextView) findViewById(R.id.strong);
//
//        opentouch = (TextView) findViewById(R.id.opentouch);
//        closetouch = (TextView) findViewById(R.id.closetouch);
//        big = (TextView) findViewById(R.id.big);
//        small = (TextView) findViewById(R.id.small);
//        point_icon1 = (ImageView) findViewById(R.id.point_icon1);
//        point_icon2 = (ImageView) findViewById(R.id.point_icon2);
//        point_icon3 = (ImageView) findViewById(R.id.point_icon3);
//        showImgLay = (LinearLayout) findViewById(R.id.showImgLay);
//        showImg = (ImageView) findViewById(R.id.showImg);
//    }
//
//    /* 初始化信息 */
//    private void initInfo() {
//        /* 初始化SpeechRecognition接口类 */
//        mSpeechRecognition = SpeechRecognition.getInstance();
//        /* 初始化Camera接口类 */
//        cameraInterface = CameraInterface.getInstance();
//        cameraInterface.loadFocusing(this, this);
//        toResult = new toResult();
//        Bundle bundle = this.getIntent().getExtras();
//        item_num = bundle.getInt("item_num");
//        user_id = bundle.getString("user_id");
//        workDb = new WorkDb(this, "");
//        itemBean = workDb.getItem(item_num);
//        titleBeanArrayList = workDb.getTitles(item_num);
//        for (TitleBean titleBean : titleBeanArrayList) {
//            ArrayList<StepBean> stepBeens = workDb.getSteps(item_num, Integer.parseInt(titleBean.getNum()));
//            for (StepBean stepBean : stepBeens) {
//                stepBeanArrayList.add(stepBean);
//            }
//        }
//        /* 初始化定义转义字符 */
//        numberFormatClass = new NumberFormatClass();
//        String[] nativenum = {"零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "百"};
//        for (String num : nativenum) {
//            numStrings.add(num);
//        }
//        String readStorageTxt = FucUtil.readStorageTxt(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON/config.txt");
//        if (readStorageTxt.length() != 0) {
//            ArrayList<String> mLocalConfigs = new ArrayList<>();
//            String[] Keys = readStorageTxt.split("\\n|\\r");
//            for (String keys : Keys) {
//                if (keys.length() != 0) {
//                    mLocalConfigs.add(keys);
//                }
//            }
//            for (int i = 0; i < mLocalConfigs.size(); i++) {
//                uploadIp = mLocalConfigs.get(0).substring(3, mLocalConfigs.get(0).length());
//            }
//        }
//        Log.e("uploadIp", uploadIp);
//        insertlogUrl = "http://" + uploadIp + ":8080/FreshBooks/insertlog";
//        updateimageUrl = "http://" + uploadIp + ":8080/FreshBooks/updateimage";
//        insertrecordUrl = "http://" + uploadIp + ":8080/FreshBooks/insertrecord";
//        OriginalUrl = "http://" + uploadIp + ":8080/FreshBooks/";
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Thread() {
//            public void run() {
//                boolean isWifi = isWifiConnected(MainActivity.this);
//                if (isWifi) {
//                    getisload();
//                }
//            }
//        }, 10000);
//    }
//
//    /* 初始化绑定 */
//    private void initBulid() {
//        starttake.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!isStart) {
//                    /* 进入最开始的检查 */
//                    isStart = true;
//                    RecordBean recordBean = workDb.getWorkingRecord(item_num, "false", user_id);
//                    Log.e("getId", String.valueOf(recordBean.getId()));
//                    if (recordBean.getId() > 0) {
//                        work_id = recordBean.getWork_id();
//                        order = workDb.getStepOrder(item_num);
//                        mSpeechRecognition.freshPlayStrings("跳转工作节点。");
//                    } else {
//                        workDb.insertRecord(itemBean.getNum(), "", "", work_id, "false", timestamp, user_id);
//                        mSpeechRecognition.freshPlayStrings("检查开始，请按工单认真检查！");
//                    }
//                    getProcedure(order);
//                    order++;
//
//                    /* 后台添加record。传work_id，没有则添加 */
//                    RequestParams params = new RequestParams();
//                    params.put("work_id", work_id);
//                    params.put("user_id", user_id);
//                    params.put("item_id", item_num);
//                    params.put("timestamp", timestamp);
//                    HttpUtils.ClientPost(insertrecordUrl, params, new NetCallBack() {
//                        @Override
//                        public void onMySuccess(String result) {
//                            //Toast.makeText(MainActivity.this,result , Toast.LENGTH_LONG).show();
//                            Log.e("HttpUtils", "success");
//                        }
//
//                        @Override
//                        public void onMyFailure(Throwable throwable) {
////                            Toast.makeText(MainActivity.this, "请求失败！", Toast.LENGTH_LONG).show();
//                            Log.e("HttpUtils", "fail");
//                        }
//                    });
//                } else {
//                    if (hasBigTitle) {
//                        hasBigTitle = false;
//                        hasTitle = true;
//                        showtext1.setVisibility(View.GONE);
//                        showtext2.setVisibility(View.GONE);
//                        showtext3.setVisibility(View.GONE);
//                        showtext4.setVisibility(View.GONE);
//                        mSpeechRecognition.freshPlayStrings("第" + titleBean.getNum() + "步，" + titleBean.getTitle());
//                        getProcedure(order - 1);
//                    } else {
//                        isEntry = true;
//                        /* 跳过图册、进入检查 */
//                        showImgLay.setVisibility(View.GONE);
//                    }
//                }
//            }
//        });
//        takepic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isStart) {
//                    if (isEntry) {
//                        if (isNormal != 0) {
//                            if (cameraInterface.isPreviewing && !cameraInterface.isCapturing && !isRecording) {
//                                cameraInterface.doTakePicture(MainActivity.this);
//                            }
//                        } else {
//                            mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            }
//        });
//        takevid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isStart) {
//                    if (isEntry) {
//                        if (isNormal != 0) {
//                            mSpeechRecognition.startMediaPlay();
//                            if (!isRecording && !cameraInterface.isCapturing) {
//                                startVideoRecord();
//                            }
//                        } else {
//                            mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            }
//        });
//        takeover.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isStart) {
//                    if (isEntry) {
//                        if (isNormal == 0) {
//                            mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                        } else if (isNormal == 1) {
//                            /* 判断是否拍了照 */
//                            if (order == stepBeanArrayList.size()) {
//                                // 提示检查遗漏项
//
//                                // 修改工作状态为true
//                                workDb.updateRecordState("true", work_id);
//                                if (toResult.toConvert(itemBean.getTitle(), work_id, workDb)) {
//                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                    intent.setData(Uri.fromFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_DATA, itemBean.getTitle(), work_id)));
//                                    sendBroadcast(intent);
//
//                                    /* 新增：需要修改>>>提示最后一步，需要检查异常，不往下走，不需要更新本地test */
//
//                                }
//                                Toast.makeText(getApplicationContext(), "该项检查完毕！", Toast.LENGTH_LONG).show();
//                                finish();
//                            } else {
//                                isNormal = 0;
//                                mSpeechRecognition.freshPlayStrings("检查完成，进入下一项！");
//                                getProcedure(order);
//                                order++;
//                            }
////                            if (hasPicture(isNormal)){
////                                if (order == stepBeanArrayList.size()){
////                                    // 提示检查遗漏项
////
////                                    // 修改工作状态为true
////                                    workDb.updateRecordState("true", work_id);
////                                    if (toResult.toConvert(itemBean.getTitle(), work_id, workDb)){
////                                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
////                                        intent.setData(Uri.fromFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_DATA, itemBean.getTitle(), work_id)));
////                                        sendBroadcast(intent);
////
////                                        /* 新增：需要修改>>>提示最后一步，需要检查异常，不往下走，不需要更新本地test */
////
////                                    }
////                                    Toast.makeText(getApplicationContext(),"该项检查完毕！", Toast.LENGTH_LONG).show();
////                                    finish();
////                                }else {
////                                    isNormal = 0;
////                                    mSpeechRecognition.freshPlayStrings("检查完成，进入下一项！");
////                                    getProcedure(order);
////                                    order ++;
////                                }
////                            }else {
////                                if (stepBean.getImports() != null){
////                                    if (stepBean.getImports().length() > 0 && !stepBean.getImports().equals("null")){
////                                        mSpeechRecognition.freshPlayStrings(stepBean.getImports());
////                                    }else {
////                                        //mSpeechRecognition.freshPlayStrings("请拍照！");
////                                    }
////                                }else {
////                                    //mSpeechRecognition.freshPlayStrings("请拍照！");
////                                }
////                            }
//                        } else if (isNormal == -1) {
//                            /* 如果有记错照片 */
//                            if (order == stepBeanArrayList.size()) {
//                                // 修改工作状态为true
//                                workDb.updateRecordState("true", work_id);
//                                if (toResult.toConvert(itemBean.getTitle(), work_id, workDb)) {
//                                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                    intent.setData(Uri.fromFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_DATA, itemBean.getTitle(), work_id)));
//                                    sendBroadcast(intent);
//
//                                        /* 新增：更新远程数据库 */
//
//                                }
//                                Toast.makeText(getApplicationContext(), "该项检查完毕！", Toast.LENGTH_LONG).show();
//                                finish();
//                            } else {
//                                isNormal = 0;
//                                mSpeechRecognition.freshPlayStrings("进入下一项！");
//                                getProcedure(order);
//                                order++;
//                            }
////                            if (hasPicture(isNormal)) {
////                                if (order == stepBeanArrayList.size()){
////                                    // 修改工作状态为true
////                                    workDb.updateRecordState("true", work_id);
////                                    if (toResult.toConvert(itemBean.getTitle(), work_id, workDb)){
////                                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
////                                        intent.setData(Uri.fromFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_DATA, itemBean.getTitle(), work_id)));
////
////                                          sendBroadcast(intent);
////
////                                        /* 新增：更新远程数据库 */
////
////                                    }
////                                    Toast.makeText(getApplicationContext(),"该项检查完毕！", Toast.LENGTH_LONG).show();
////                                    finish();
////                                }else {
////                                    isNormal = 0;
////                                    mSpeechRecognition.freshPlayStrings("进入下一项！");
////                                    getProcedure(order);
////                                    order ++;
////                                }
////                            } else {
////                                if (stepBean.getImports() != null) {
////                                    if (stepBean.getImports().length() > 0 && !stepBean.getImports().equals("null")) {
////                                        mSpeechRecognition.freshPlayStrings(stepBean.getImports());
////                                    } else {
////                                        //mSpeechRecognition.freshPlayStrings("请拍照！");
////                                    }
////                                } else {
////                                    //mSpeechRecognition.freshPlayStrings("请拍照！");
////                                }
////                            }
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            }
//        });
//        takenormal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isStart) {
//                    if (isEntry) {
//                        isNormal = 1;
//                        /* 判断注意项是否为空，为空自动清拍摄现场。 */
//                        if (stepBean.getImports() != null) {
//                            if (stepBean.getImports().length() > 0 && !stepBean.getImports().equals("null")) {
//                                mSpeechRecognition.freshPlayStrings(changetext(stepBean.getImports()));
//                            } else {
//                                //mSpeechRecognition.freshPlayStrings("请拍照！");
//                            }
//                        } else {
//                            //mSpeechRecognition.freshPlayStrings("请拍照！");
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            }
//        });
//        takeunnormal.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isStart) {
//                    if (isEntry) {
//                        isNormal = -1;
//                        if (stepBean.getImports() != null) {
//                            if (stepBean.getImports().length() > 0 && !stepBean.getImports().equals("null")) {
//                                mSpeechRecognition.freshPlayStrings(changetext(stepBean.getImports()));
//                            } else {
//                                mSpeechRecognition.freshPlayStrings("请拍照！");
//                            }
//                        } else {
//                            mSpeechRecognition.freshPlayStrings("请拍照！");
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            }
//        });
//        lookpic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isStart) {
//                    mSpeechRecognition.startMediaPlay();
//                    if (hasPicture(0)) {
//                        targetPreview();
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            }
//        });
//
//        stop.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isRecording) {
//                    stopVideoRecord();
//                }
//            }
//        });
//
//        opentouch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!cameraInterface.openTorch(true)) {
//                    mSpeechRecognition.freshPlayStrings("打开失败！");
//                }
//            }
//        });
//
//        closetouch.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (!cameraInterface.openTorch(false)) {
//                    mSpeechRecognition.freshPlayStrings("关闭失败！");
//                }
//            }
//        });
//
//        strong.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                PackageManager packageManager = MainActivity.this.getPackageManager();
//                intent = packageManager.getLaunchIntentForPackage("com.wikitude.nativesdksampleapp");
//                if (intent != null) {
//                    ///设置外部apk返回标志
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                }
//            }
//        });
//
//        big.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isEntry) {
//                    mSpeechRecognition.startMediaPlay();
//                    cameraInterface.setZoom(1);
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            }
//        });
//
//        small.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isEntry) {
//                    mSpeechRecognition.startMediaPlay();
//                    cameraInterface.setZoom(-1);
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            }
//        });
//
//        wakeup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mSpeechRecognition.startSpeech();
//            }
//        });
//    }
//
//    public String changetext(String text) {
//        return text.replace("弹", "蛋");
//    }
//
//    @Override/* 获取识别文字 */
//    public void getResult(String result) {
//        switch (result) {
////            case "开始检查":
////                if (!isStart){
////                    /* 进入最开始的检查 */
////                    isStart = true;
////                    RecordBean recordBean = workDb.getWorkingRecord(item_num, "false");
////                    Log.e("getId", String.valueOf(recordBean.getId()));
////                    if (recordBean.getId() > 0){
////                        work_id = recordBean.getWork_id();
////                        order = workDb.getStepOrder(item_num);
////                        mSpeechRecognition.freshPlayStrings("跳转工作节点。");
////                    }else {
////                        workDb.insertRecord(itemBean.getNum(), "", "", work_id, "false", timestamp);
////                        mSpeechRecognition.freshPlayStrings("总检开始，请按工单认真检查！");
////                    }
////                    getProcedure(order);
////                    order ++;
////                    mSpeechRecognition.startSpeech();
////
////                    /* 后台添加record。传work_id，没有则添加 */
////                    RequestParams params = new RequestParams();
////                    params.put("work_id", work_id);
////                    params.put("item_id", item_num);
////                    params.put("timestamp", timestamp);
////                    HttpUtils.ClientPost(insertrecordUrl, params, new NetCallBack() {
////                        @Override
////                        public void onMySuccess(String result) {
////                            //Toast.makeText(MainActivity.this,result , Toast.LENGTH_LONG).show();
////                            Log.e("HttpUtils", "success");
////                        }
////                        @Override
////                        public void onMyFailure(Throwable throwable) {
////                            Toast.makeText(MainActivity.this, "请求失败！", Toast.LENGTH_LONG).show();
////                            Log.e("HttpUtils", "fail");
////                        }
////                    });
////                }else {
////                    /* 跳过图册、进入检查 */
////                    isEntry = true;
////                    showImgLay.setVisibility(View.GONE);
////                    mSpeechRecognition.startSpeech();
////                }
////                break;
////            case "打开闪光灯":
////                mSpeechRecognition.startMediaPlay();
////                if (!cameraInterface.openTorch(true)){
////                    mSpeechRecognition.freshPlayStrings("打开失败！");
////                }
////                mSpeechRecognition.startSpeech();
////                break;
////            case "关闭闪光灯":
////                mSpeechRecognition.startMediaPlay();
////                if (!cameraInterface.openTorch(false)){
////                    mSpeechRecognition.freshPlayStrings("关闭失败！");
////                }
////                mSpeechRecognition.startSpeech();
////                break;
////            case "缩小":
////                if (isEntry){
////                    mSpeechRecognition.startMediaPlay();
////                    cameraInterface.setZoom(-1);
////                    mSpeechRecognition.startSpeech();
////                }else {
////                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                    mSpeechRecognition.startSpeech();
////                }
////                break;
////            case "放大":
////                if (isEntry){
////                    mSpeechRecognition.startMediaPlay();
////                    cameraInterface.setZoom(1);
////                    mSpeechRecognition.startSpeech();
////                }else {
////                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                    mSpeechRecognition.startSpeech();
////                }
////                break;
////            case "拍照":
////                if (isStart){
////                    if (isEntry){
////                        if (isNormal != 0){
////                            if (cameraInterface.isPreviewing && !cameraInterface.isCapturing && !isRecording){
////                                cameraInterface.doTakePicture(MainActivity.this);
////                            }
////                            mSpeechRecognition.startSpeech();
////                        }else {
////                            mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
////                            mSpeechRecognition.startSpeech();
////                        }
////                    }else {
////                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                        mSpeechRecognition.startSpeech();
////                    }
////                }else {
////                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                    mSpeechRecognition.startSpeech();
////                }
////                break;
////            case "开始录像":
////                if (isStart){
////                    if (isEntry){
////                        if (isNormal != 0){
////                            mSpeechRecognition.startMediaPlay();
////                            if (!isRecording && !cameraInterface.isCapturing){
////                                startVideoRecord();
////                            }
////                        }else {
////                            mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
////                            mSpeechRecognition.startSpeech();
////                        }
////                    }else {
////                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                        mSpeechRecognition.startSpeech();
////                    }
////                }else {
////                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                    mSpeechRecognition.startSpeech();
////                }
////                break;
//            case "特写拍照":
//                if (isStart) {
//                    if (isEntry) {
//                        if (isNormal != 0) {
//                            mSpeechRecognition.startMediaPlay();
//                            isBiger = true;
//                            cameraInterface.setZoom(1);
//                            if (cameraInterface.isPreviewing && !cameraInterface.isCapturing && !isRecording) {
//                                cameraInterface.doTakePicture(MainActivity.this);
//                            }
//                            mSpeechRecognition.startSpeech();
//                        } else {
//                            mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                            mSpeechRecognition.startSpeech();
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                        mSpeechRecognition.startSpeech();
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    mSpeechRecognition.startSpeech();
//                }
//                break;
//            case "特写录像":
//                if (isStart) {
//                    if (isEntry) {
//                        if (isNormal != 0) {
//                            mSpeechRecognition.startMediaPlay();
//                            isBiger = true;
//                            cameraInterface.setZoom(1);
//                            if (!isRecording && !cameraInterface.isCapturing) {
//                                startVideoRecord();
//                            }
//                        } else {
//                            mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                            mSpeechRecognition.startSpeech();
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                        mSpeechRecognition.startSpeech();
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    mSpeechRecognition.startSpeech();
//                }
//                break;
////            case "正常":
////                if (isStart){
////                    if (isEntry){
////                        isNormal = 1;
////                        /* 判断注意项是否为空，为空自动清拍摄现场。 */
////                        if (stepBean.getImports() != null){
////                            if (stepBean.getImports().length() > 0 && !stepBean.getImports().equals("null")){
////                                mSpeechRecognition.freshPlayStrings(stepBean.getImports());
////                            }else {
////                                mSpeechRecognition.freshPlayStrings("请拍照！");
////                            }
////                        }else {
////                            mSpeechRecognition.freshPlayStrings("请拍照！");
////                        }
////                        mSpeechRecognition.startSpeech();
////                    }else {
////                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                        mSpeechRecognition.startSpeech();
////                    }
////                }else {
////                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                    mSpeechRecognition.startSpeech();
////                }
////                break;
////            case "异常":
////                if (isStart){
////                    if (isEntry){
////                        isNormal = -1;
////                        if (stepBean.getImports() != null){
////                            if (stepBean.getImports().length() > 0 && !stepBean.getImports().equals("null")){
////                                mSpeechRecognition.freshPlayStrings(stepBean.getImports());
////                            }else {
////                                mSpeechRecognition.freshPlayStrings("请拍照！");
////                            }
////                        }else {
////                            mSpeechRecognition.freshPlayStrings("请拍照！");
////                        }
////                        mSpeechRecognition.startSpeech();
////                    }else {
////                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                        mSpeechRecognition.startSpeech();
////                    }
////                }else {
////                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                    mSpeechRecognition.startSpeech();
////                }
////                break;
////            case "检查完毕":
////                if (isStart){
////                    if (isEntry){
////                        if (isNormal == 0){
////                            mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
////                            mSpeechRecognition.startSpeech();
////                        }else if (isNormal == 1){
////                            /* 判断是否拍了照 */
////                            if (hasPicture(isNormal)){
////                                if (order == stepBeanArrayList.size()){
////                                    // 提示检查遗漏项
////
////                                    // 修改工作状态为true
////                                    workDb.updateRecordState("true", work_id);
////                                    if (toResult.toConvert(itemBean.getTitle(), work_id, workDb)){
////                                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
////                                        intent.setData(Uri.fromFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_DATA, itemBean.getTitle(), work_id)));
////                                        sendBroadcast(intent);
////
////                                        /* 新增：需要修改>>>提示最后一步，需要检查异常，不往下走，不需要更新本地test */
////
////                                    }
////                                    Toast.makeText(getApplicationContext(),"该项检查完毕！", Toast.LENGTH_LONG).show();
////                                    finish();
////                                }else {
////                                    isNormal = 0;
////                                    mSpeechRecognition.freshPlayStrings("检查完成，进入下一项！");
////                                    getProcedure(order);
////                                    order ++;
////                                    mSpeechRecognition.startSpeech();
////                                }
////                            }else {
////                                if (stepBean.getImports() != null){
////                                    if (stepBean.getImports().length() > 0 && !stepBean.getImports().equals("null")){
////                                        mSpeechRecognition.freshPlayStrings(stepBean.getImports());
////                                    }else {
////                                        mSpeechRecognition.freshPlayStrings("请拍照！");
////                                    }
////                                }else {
////                                    mSpeechRecognition.freshPlayStrings("请拍照！");
////                                }
////                                mSpeechRecognition.startSpeech();
////                            }
////                        }else if (isNormal == -1) {
////                            /* 如果有记错照片 */
////                            if (hasPicture(isNormal)) {
////                                if (order == stepBeanArrayList.size()){
////
////                                    // 修改工作状态为true
////                                    workDb.updateRecordState("true", work_id);
////                                    if (toResult.toConvert(itemBean.getTitle(), work_id, workDb)){
////                                        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
////                                        intent.setData(Uri.fromFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_DATA, itemBean.getTitle(), work_id)));
////                                        sendBroadcast(intent);
////
////                                        /* 新增：更新远程数据库 */
////
////                                    }
////                                    Toast.makeText(getApplicationContext(),"该项检查完毕！", Toast.LENGTH_LONG).show();
////                                    finish();
////                                }else {
////                                    isNormal = 0;
////                                    mSpeechRecognition.freshPlayStrings("进入下一项！");
////                                    getProcedure(order);
////                                    order ++;
////                                    mSpeechRecognition.startSpeech();
////                                }
////                            } else {
////                                if (stepBean.getImports() != null) {
////                                    if (stepBean.getImports().length() > 0 && !stepBean.getImports().equals("null")) {
////                                        mSpeechRecognition.freshPlayStrings(stepBean.getImports());
////                                    } else {
////                                        mSpeechRecognition.freshPlayStrings("请拍照！");
////                                    }
////                                } else {
////                                    mSpeechRecognition.freshPlayStrings("请拍照！");
////                                }
////                                mSpeechRecognition.startSpeech();
////                            }
////                        }
////                    }else {
////                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                        mSpeechRecognition.startSpeech();
////                    }
////                }else {
////                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                    mSpeechRecognition.startSpeech();
////                }
////                break;
////            case "浏览图像":
////                if (isStart){
////                    mSpeechRecognition.startMediaPlay();
////                    if (hasPicture(0)) {
////                        targetPreview();
////                    }else {
////                        mSpeechRecognition.startSpeech();
////                    }
////                }else {
////                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
////                    mSpeechRecognition.startSpeech();
////                }
////                break;
//            case "查看异常":
//                if (isStart) {
//                    step_column = 1;
//                    workDb.updateStepOrder(order - 1, item_num);
//                    Intent intent = new Intent(MainActivity.this, WStepActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("work_id", work_id);
//                    bundle.putInt("item_num", item_num);
//                    bundle.putInt("order", order - 1);
//                    bundle.putString("keyword", "查错");
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    mSpeechRecognition.startSpeech();
//                }
//                break;
//            case "查看进度":
//                if (isStart) {
//                    mSpeechRecognition.startMediaPlay();
//                    step_column = 1;
//                    workDb.updateStepOrder(order - 1, item_num);
//                    Intent intent = new Intent(MainActivity.this, WStepActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putString("work_id", work_id);
//                    bundle.putInt("item_num", item_num);
//                    bundle.putInt("order", order - 1);
//                    bundle.putString("keyword", "null");
//                    intent.putExtras(bundle);
//                    startActivity(intent);
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    mSpeechRecognition.startSpeech();
//                }
//                break;
//            case "后退":
//                mSpeechRecognition.startMediaPlay();
//                this.finish();
//                break;
//            case "退出":
//                mSpeechRecognition.startMediaPlay();
//                TimerTask task = new TimerTask() {
//                    public void run() {
//                        Intent exitintent = new Intent(EXITACTION);
//                        sendBroadcast(exitintent);
//                    }
//                };
//                Timer timer = new Timer();
//                timer.schedule(task, 1000);
//                break;
//            default:
//                Log.e("Main-result", result);
//                Log.e("无匹配项，重新加载listening", result);
//                mSpeechRecognition.startSpeech();
//                break;
//        }
//    }
//
//    /* 键盘-按键 */
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent keyEvent) {
//        if (keyCode == KeyEvent.KEYCODE_1) {
//            // 初始状态
//            if (longEnter == 0) {
//                // 按住状态
//                longEnter = 1;
//                longHandler.postDelayed(longWait, 600);
//            }
//            return true;
//        }
//        if (keyCode == 11) {
//            // 单击录像
//            return true;
//        }
//        if (keyCode == 10) {
//            // 切换摄像头
//            return true;
//        }
//        if (keyCode == 66) {
//            // 拍照
//            return true;
//        }
//        if (keyCode == 25) {
//            // 上一步
//            return true;
//        }
//        if (keyCode == 24) {
//            // 下一步
//
//            return true;
//        }
//        return super.onKeyDown(keyCode, keyEvent);
//    }
//
//    @Override
//    public boolean onKeyUp(int keyCode, KeyEvent keyEvent) {
//        if (keyCode == 11) {
//            // 单击录像
//            if (isStart) {
//                if (isEntry) {
//                    if (isNormal != 0) {
//                        mSpeechRecognition.startMediaPlay();
//                        if (isRecording) {
//                            stopVideoRecord();
//                        } else if (!isRecording && !cameraInterface.isCapturing) {
//                            startVideoRecord();
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            } else {
//                mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//            }
//            return true;
//        }
//        if (keyCode == 10) {
//            // 切换摄像头
//            return true;
//        }
//        if (keyCode == 66) {
//            // 拍照
//            if (isStart) {
//                if (isEntry) {
//                    if (isNormal != 0) {
//                        if (cameraInterface.isPreviewing && !cameraInterface.isCapturing && !isRecording) {
//                            cameraInterface.doTakePicture(MainActivity.this);
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            } else {
//                mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//            }
//            return true;
//        }
//        if (keyCode == 25) {
//            // 上一步
//            return true;
//        }
//        if (keyCode == 24) {
//            // 下一步
//            if (isStart) {
//                if (isEntry) {
//                    if (isNormal == 0) {
//                        mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                    } else if (isNormal == 1) {
//                            /* 判断是否拍了照 */
//                        if (order == stepBeanArrayList.size()) {
//                            // 提示检查遗漏项
//
//                            // 修改工作状态为true
//                            workDb.updateRecordState("true", work_id);
//                            if (toResult.toConvert(itemBean.getTitle(), work_id, workDb)) {
//                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                intent.setData(Uri.fromFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_DATA, itemBean.getTitle(), work_id)));
//                                sendBroadcast(intent);
//                                /* 新增：需要修改>>>提示最后一步，需要检查异常，不往下走，不需要更新本地test */
//                            }
//                            Toast.makeText(getApplicationContext(), "该项检查完毕！", Toast.LENGTH_LONG).show();
//                            finish();
//                        } else {
//                            isNormal = 0;
//                            mSpeechRecognition.freshPlayStrings("检查完成，进入下一项！");
//                            getProcedure(order);
//                            order++;
//                        }
//                    } else if (isNormal == -1) {
//                            /* 如果有记错照片 */
//                        if (order == stepBeanArrayList.size()) {
//                            // 修改工作状态为true
//                            workDb.updateRecordState("true", work_id);
//                            if (toResult.toConvert(itemBean.getTitle(), work_id, workDb)) {
//                                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                intent.setData(Uri.fromFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_DATA, itemBean.getTitle(), work_id)));
//                                sendBroadcast(intent);
//                                /* 新增：更新远程数据库 */
//                            }
//                            Toast.makeText(getApplicationContext(), "该项检查完毕！", Toast.LENGTH_LONG).show();
//                            finish();
//                        } else {
//                            isNormal = 0;
//                            mSpeechRecognition.freshPlayStrings("进入下一项！");
//                            getProcedure(order);
//                            order++;
//                        }
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            } else {
//                mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//            }
//            return true;
//        }
//        if (keyCode == KeyEvent.KEYCODE_1) {
//            if (longEnter == 1) {
//                longEnter = 0;
//                longHandler.removeCallbacks(longWait);
//                // 单按 - 拍照
//                if (sortEnter == 0) {
//                    sortEnter = 1;
//                    sortHandler.postDelayed(sortWait, 350);
//                } else if (sortEnter == 1) {
//                    // 双击 - 下一步
//                    sortEnter = 0;
//                    sortHandler.removeCallbacks(sortWait);
//                    /* 下一步>>>检查完毕 */
//                    if (isStart) {
//                        if (isEntry) {
//                            if (isNormal == 0) {
//                                mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                            } else if (isNormal == 1) {
//                                /* 判断是否拍了照 */
//                                if (hasPicture(isNormal)) {
//                                    if (order == stepBeanArrayList.size()) {
//                                        // 提示检查遗漏项
//
//                                        // 修改工作状态为true
//                                        workDb.updateRecordState("true", work_id);
//                                        if (toResult.toConvert(itemBean.getTitle(), work_id, workDb)) {
//                                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                            intent.setData(Uri.fromFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_DATA, itemBean.getTitle(), work_id)));
//                                            sendBroadcast(intent);
//
//                                            /* 新增：需要修改>>>提示最后一步，需要检查异常，不往下走，不需要更新本地test */
//
//                                        }
//                                        Toast.makeText(getApplicationContext(), "该项检查完毕！", Toast.LENGTH_LONG).show();
//                                        finish();
//                                    } else {
//                                        isNormal = 0;
//                                        mSpeechRecognition.freshPlayStrings("检查完成，进入下一项！");
//                                        getProcedure(order);
//                                        order++;
//                                    }
//                                } else {
//                                    if (stepBean.getImports() != null) {
//                                        if (stepBean.getImports().length() > 0 && !stepBean.getImports().equals("null")) {
//                                            mSpeechRecognition.freshPlayStrings(changetext(stepBean.getImports()));
//                                        } else {
//                                            //mSpeechRecognition.freshPlayStrings("请拍照！");
//                                        }
//                                    } else {
//                                        //mSpeechRecognition.freshPlayStrings("请拍照！");
//                                    }
//                                }
//                            } else if (isNormal == -1) {
//                                /* 如果有记错照片 */
//                                if (hasPicture(isNormal)) {
//                                    if (order == stepBeanArrayList.size()) {
//
//                                        // 修改工作状态为true
//                                        workDb.updateRecordState("true", work_id);
//                                        if (toResult.toConvert(itemBean.getTitle(), work_id, workDb)) {
//                                            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                                            intent.setData(Uri.fromFile(CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_DATA, itemBean.getTitle(), work_id)));
//                                            sendBroadcast(intent);
//
//                                            /* 新增：更新远程数据库 */
//
//                                        }
//                                        Toast.makeText(getApplicationContext(), "该项检查完毕！", Toast.LENGTH_LONG).show();
//                                        finish();
//                                    } else {
//                                        isNormal = 0;
//                                        mSpeechRecognition.freshPlayStrings("进入下一项！");
//                                        getProcedure(order);
//                                        order++;
//                                    }
//                                } else {
//                                    if (stepBean.getImports() != null) {
//                                        if (stepBean.getImports().length() > 0 && !stepBean.getImports().equals("null")) {
//                                            mSpeechRecognition.freshPlayStrings(changetext(stepBean.getImports()));
//                                        } else {
//                                            //mSpeechRecognition.freshPlayStrings("请拍照！");
//                                        }
//                                    } else {
//                                        //mSpeechRecognition.freshPlayStrings("请拍照！");
//                                    }
//                                }
//                            }
//                        } else {
//                            mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    }
//                }
//            } else if (longEnter == -1) {
//                // 长按 -- 不算入单次点击 - 录像
//                longEnter = 0;
//                if (isStart) {
//                    if (isEntry) {
//                        if (isNormal != 0) {
//                            mSpeechRecognition.startMediaPlay();
//                            if (!isRecording) {
//                                if (!cameraInterface.isCapturing) {
//                                    startVideoRecord();
//                                } else {
//                                    stopVideoRecord();
//                                }
//                            }
//                        } else {
//                            mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            }
//            return true;
//        }
//        return super.onKeyUp(keyCode, keyEvent);
//    }
//
//    Thread longWait = new Thread() {
//        @Override
//        public void run() {
//            longEnter = -1;     // 已达到长按时间
//        }
//    };
//    Thread sortWait = new Thread() {
//        @Override
//        public void run() {
//            sortEnter = 0;
//            if (isStart) {
//                if (isEntry) {
//                    if (isNormal != 0) {
//                        mSpeechRecognition.startMediaPlay();
//                        if (cameraInterface.isPreviewing && !cameraInterface.isCapturing && !isRecording) {
//                            cameraInterface.doTakePicture(MainActivity.this);
//                        }
//                    } else {
//                        mSpeechRecognition.freshPlayStrings("该项还未作检查结论！");
//                    }
//                } else {
//                    mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//                }
//            } else {
//                mSpeechRecognition.freshPlayStrings("项目检查未开始，请发口令！");
//            }
//        }
//    };
//
//    /* 判断是否有图像浏览 */
//    public boolean hasPicture(int property) {
//        if (property == 1) {
//            String url = workDb.getLog(titleBean.getNum(), stepBean.getNum(), work_id).getUrl();
//            if (url.length() > 0) {
//                return true;
//            }
//        } else if (property == -1) {
//            String bad_url = workDb.getLog(titleBean.getNum(), stepBean.getNum(), work_id).getBad_url();
//            if (bad_url.length() > 0) {
//                return true;
//            }
//        } else if (property == 0) {
//            String bad_url = workDb.getLog(titleBean.getNum(), stepBean.getNum(), work_id).getBad_url();
//            String url = workDb.getLog(titleBean.getNum(), stepBean.getNum(), work_id).getUrl();
//            if (bad_url.length() > 0 || url.length() > 0) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    /* 跳转预览界面 */
//    public void targetPreview() {
//        String bad_url = workDb.getLog(titleBean.getNum(), stepBean.getNum(), work_id).getBad_url();
//        String url = workDb.getLog(titleBean.getNum(), stepBean.getNum(), work_id).getUrl();
//        if (bad_url.length() > 0) {
//            if (url.length() > 0) {
//                url = bad_url + "," + url;
//            } else {
//                url = bad_url;
//            }
//        }
//        String urls[] = url.split(",");
//        String dataPath = urls[urls.length - 1];
//            /* 第一张为图片 */
//        if (dataPath.substring(dataPath.length() - 3, dataPath.length()).equals("jpg") || dataPath.substring(dataPath.length() - 3, dataPath.length()).equals("png")) {
//            Intent intent = new Intent(MainActivity.this, ImageShow.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("foreign_title_id", titleBean.getNum());
//            bundle.putString("title", titleBean.getTitle());
//            bundle.putString("foreign_step_id", stepBean.getNum());
//            bundle.putString("work_id", work_id);
//            bundle.putString("dataPath", dataPath);
//            bundle.putInt("order", urls.length - 1);
//            bundle.putInt("item_num", item_num);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        } else if (dataPath.substring(dataPath.length() - 3, dataPath.length()).equals("mp4")) {
//            Intent intent = new Intent(MainActivity.this, VideoPlayer.class);
//            Bundle bundle = new Bundle();
//            bundle.putString("foreign_title_id", titleBean.getNum());
//            bundle.putString("title", titleBean.getTitle());
//            bundle.putString("foreign_step_id", stepBean.getNum());
//            bundle.putString("work_id", work_id);
//            bundle.putString("dataPath", dataPath);
//            bundle.putInt("order", urls.length - 1);
//            bundle.putInt("item_num", item_num);
//            intent.putExtras(bundle);
//            startActivity(intent);
//        }
//    }
//
//    /* 更新Log Url */
//    public void updateUrl(int property, String url) {
//        boolean isWifi = isWifiConnected(this);
//
//        if (isWifi) {
////            getisload();
//
//
//        /* 新增：更新远程数据库 */
//            RequestParams params = new RequestParams();
//            params.put("work_id", work_id);
//            params.put("property", String.valueOf(property));
//            params.put("item_id", item_num);
//            params.put("title_id", titleBean.getNum());
//            params.put("step_id", String.valueOf(substepid));
//            params.put("op_sorder", String.valueOf(op_sorder));
//            params.put("url", url);
//            HttpUtils.ClientPost(updateimageUrl, params, new NetCallBack() {
//                @Override
//                public void onMySuccess(String result) {
//                    //Toast.makeText(MainActivity.this,result , Toast.LENGTH_LONG).show();
//                    Log.e("HttpUtils", "success");
//                }
//
//                @Override
//                public void onMyFailure(Throwable throwable) {
////                Toast.makeText(MainActivity.this, "请求失败！", Toast.LENGTH_LONG).show();
//                    Log.e("HttpUtils", "fail");
//                }
//            });
//            if (property == -1) {
//                String old_url = workDb.getLog(titleBean.getNum(), stepBean.getNum(), work_id).getBad_url();
//                Log.e("old_url", old_url);
//                if (old_url.length() > 0) {
//                    url = old_url + "," + url;
//                }
//                workDb.updatebadurl(titleBean.getNum(), stepBean.getNum(), work_id, url);
//            } else {
//                String old_url = workDb.getLog(titleBean.getNum(), stepBean.getNum(), work_id).getUrl();
//                if (old_url.length() > 0) {
//                    url = old_url + "," + url;
//                }
//                workDb.updateurl(titleBean.getNum(), stepBean.getNum(), work_id, url);
//            }
//        } else {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("work_id", work_id);
//                jsonObject.put("property", String.valueOf(property));
//                jsonObject.put("item_id", item_num);
//                jsonObject.put("title_id", titleBean.getNum());
//                jsonObject.put("step_id", String.valueOf(substepid));
//                jsonObject.put("op_sorder", String.valueOf(op_sorder));
//                jsonObject.put("url", url);
//                boolean bool = CameraHelper.writenotwilf(jsonObject);
//                if (!bool) {
//                    return;
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            if (property == -1) {
//                String old_url = workDb.getLog(titleBean.getNum(), stepBean.getNum(), work_id).getBad_url();
//                Log.e("old_url", old_url);
//                if (old_url.length() > 0) {
//                    url = old_url + "," + url;
//                }
//                workDb.updatebadurl(titleBean.getNum(), stepBean.getNum(), work_id, url);
//            } else {
//                String old_url = workDb.getLog(titleBean.getNum(), stepBean.getNum(), work_id).getUrl();
//                if (old_url.length() > 0) {
//                    url = old_url + "," + url;
//                }
//                workDb.updateurl(titleBean.getNum(), stepBean.getNum(), work_id, url);
//            }
//
//        }
//    }
//
//    public void getisload() {
//        File mediaStorageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON" + File.separator + "isupload.txt");
//        if (mediaStorageDir.exists()) {
//            BufferedReader bufferedReader = null;
//            try {
//                bufferedReader = new BufferedReader(new FileReader(mediaStorageDir));
//                String line = "";
//                BufferedWriter bufferedWriter = null;
//                StringBuffer stringBuffer = new StringBuffer();
//                while ((line = bufferedReader.readLine()) != null) {
//                    stringBuffer.append(line);
//                }
//                String[] strings = stringBuffer.toString().split("&&");
//                for (int i = 0; i < strings.length; i++) {
//                    JSONTokener tokener = new JSONTokener(strings[i]);
//                    JSONObject joResult = new JSONObject(tokener);
//                    RequestParams params = new RequestParams();
//                    params.put("work_id", joResult.get("work_id"));
//                    params.put("property", joResult.get("property"));
//                    params.put("item_id", joResult.get("item_id"));
//                    params.put("title_id", joResult.get("title_id"));
//                    params.put("step_id", joResult.get("step_id"));
//                    params.put("op_sorder", joResult.get("op_sorder"));
//                    params.put("url", joResult.get("url"));
//                    HttpUtils.ClientPost(updateimageUrl, params, new NetCallBack() {
//                        @Override
//                        public void onMySuccess(String result) {
//                            Log.e("HttpUtils", "success");
//                        }
//                        @Override
//                        public void onMyFailure(Throwable throwable) {
//                            Log.e("HttpUtils", "fail");
//                        }
//                    });
//                }
//                File file = new File(mediaStorageDir.getPath());
//                file.delete();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public boolean isWifiConnected(Context context) {
//        if (context != null) {
//            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//            NetworkInfo mWiFiNetworkInfo = mConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//            if (mWiFiNetworkInfo != null) {
//                return mWiFiNetworkInfo.isAvailable();
//            }
//        }
//        return false;
//    }
//
//    /* 上传 */
//    private void ftpUpload() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                // TODO 可以首先去判断一下网络
//
//
//                isUploading = true;
//                FTPClientFunctions ftpClient = new FTPClientFunctions();
//                boolean connectResult = ftpClient.ftpConnect(uploadIp, "root", "123456", 888);
//                if (connectResult) {
//                    boolean changeDirResult = ftpClient.ftpChangeDir("data/");
//                    if (changeDirResult) {
//                        if (uploadFileStrings.size() > 0) {
//                            Log.w("FTP", "开始上传：" + uploadFileStrings.get(0));
//                            boolean uploadResult = ftpClient.ftpUpload(uploadFileStrings.get(0));
//                            if (uploadResult) {
//                                Log.w("FTP", "上传成功");
//                                uploadFileStrings.remove(0);
//                                if (uploadFileStrings.size() > 0) {
//                                    isUploading = true;
//                                    this.run();
//                                } else {
//                                    isUploading = false;
//                                    boolean disConnectResult = ftpClient.ftpDisconnect();
//                                    if (disConnectResult) {
//                                        Log.e("FTP", "关闭ftp连接成功");
//                                    } else {
//                                        Log.e("FTP", "关闭ftp连接失败");
//                                    }
//                                }
//                            } else {
//                                Log.w("FTP", "上传失败");
//                                uploadFileStrings.remove(0);
//                                if (uploadFileStrings.size() > 0) {
//                                    isUploading = true;
//                                    this.run();
//                                } else {
//                                    isUploading = false;
//                                    boolean disConnectResult = ftpClient.ftpDisconnect();
//                                    if (disConnectResult) {
//                                        Log.e("FTP", "关闭ftp连接成功");
//                                    } else {
//                                        Log.e("FTP", "关闭ftp连接失败");
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        isUploading = false;
//                        Log.w("FTP", "切换ftp目录失败");
//                    }
//                } else {
//                    isUploading = false;
//                    Log.w("FTP", "连接ftp服务器失败");
//                }
//            }
//        }).start();
//    }
//
//    /* 获取工序 */
//    public void getProcedure(int parameterOrder) {
//        if (stepBeanArrayList.size() > 0) {
//            try {
//                saying = "";
//                stepBean = stepBeanArrayList.get(parameterOrder);
//                titleBean = workDb.getTitle(item_num, Integer.parseInt(stepBean.getForeign_title_id()));
//                workDb.updateStepOrder(order, item_num);
//                /* 没有log则添加 */
//                /* 新增：添加 */
//                if (workDb.getLogCout(titleBean.getNum(), stepBean.getNum(), work_id) == 0) {
//                    workDb.insertLog(titleBean.getNum(), stepBean.getNum(), work_id, "", "");
//                }
//
//                String title_step = "";
//                /*  7-31 */
////                if (stepBean.getNum().equals("1")){
////                    title_step += "第" + titleBean.getNum() + "步，" + titleBean.getTitle();
////                    mSpeechRecognition.addPlayString(title_step);
////                    showtext1.setText(title_step);
////                    showtext1.setVisibility(View.VISIBLE);
////                }else {
////                    showtext1.setVisibility(View.GONE);
////                }
//
//                /*  6-22 5719 */
//                /*
//                if (stepBean.getNum().equals("1")){
//                    if (!hasTitle){
//                        hasBigTitle = true;
//                        showImgBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON/images/" + item_num + "_T" + titleBean.getNum() + ".jpg");
//                        Log.e("titleBean.getNum()", titleBean.getNum());
//                        showImg.setImageBitmap(showImgBitmap);
//                        showImgLay.setVisibility(View.VISIBLE);
//                        title_step += "第" + titleBean.getNum() + "步，" + titleBean.getTitle();
//                        mSpeechRecognition.addPlayString(title_step);
//                        showtext2.setVisibility(View.GONE);
//                        showtext3.setVisibility(View.GONE);
//                        showtext4.setVisibility(View.GONE);
//                        showtext1.setText(title_step);
//                        showtext1.setVisibility(View.VISIBLE);
//                        return;
//                    }
//                }else {
//                    showtext1.setVisibility(View.GONE);
//                }
//                */
//                /*  6-22 */
//
//                substepid = 0;
//                for (int i = stepBean.getId(); i > 0; i--) {
//                    StepBean stepBean = workDb.getStep(item_num, i);
//                    if (!stepBean.getStep().equals("") && !stepBean.getStep().equals("NULL") && !stepBean.getStep().equals("null")) {
//                        if (stepBean.getNum().equals("1")) {
//                            substepid++;
//                            break;
//                        } else {
//                            substepid++;
//                        }
//                    }
//                }
//                Log.e("substepid", String.valueOf(substepid));
//
//                image_order = workDb.getimageCout(item_num, stepBean.getId());
//                op_sorder = 1;
//                for (int i = 1; i < stepBean.getId(); i++) {
//                    if (workDb.getimageCout(item_num, stepBean.getId() - i) != image_order) {
//                        break;
//                    } else {
//                        op_sorder++;
//                    }
//                }
//
//                RequestParams params = new RequestParams();
//                params.put("work_id", work_id);
//                params.put("item_id", item_num);
//                params.put("title_id", titleBean.getNum());
//                params.put("step_id", String.valueOf(substepid));
//                params.put("op_sorder", String.valueOf(op_sorder));
//                params.put("normal", String.valueOf(isNormal));
//                params.put("timestamp", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
//                HttpUtils.ClientPost(insertlogUrl, params, new NetCallBack() {
//                    @Override
//                    public void onMySuccess(String result) {
////                        Toast.makeText(MainActivity.this,result , Toast.LENGTH_LONG).show();
//                        Log.e("HttpUtils", "success");
//                    }
//
//                    @Override
//                    public void onMyFailure(Throwable throwable) {
////                        Toast.makeText(MainActivity.this, "请求失败！", Toast.LENGTH_LONG).show();
//                        Log.e("HttpUtils", "fail");
//                    }
//                });
//
//                Log.e("stepBean.getId()", String.valueOf(stepBean.getId()));
//                Log.e("image_order", String.valueOf(image_order));
//                hasTitle = false;
//                String step = "";
//                if (!stepBean.getStep().equals("") && !stepBean.getStep().equals("NULL") && !stepBean.getStep().equals("null")) {
//                    step = stepBean.getStep();
//                    showtext2.setText(step);
//                    showtext2.setVisibility(View.VISIBLE);
//                } else {
//                    showtext2.setVisibility(View.GONE);
//                }
//                mSpeechRecognition.addPlayString(step);
//
//                String operate = "";
//                if (!stepBean.getOperate().equals("/") && !stepBean.getOperate().equals("") && !stepBean.getOperate().equals("NULL") && !stepBean.getOperate().equals("null")) {
//                    operate = image_order + "." + op_sorder + "." + stepBean.getOperate();
//                    mSpeechRecognition.addPlayString(operate);
//                    showtext3.setText(operate);
//                    showtext3.setVisibility(View.VISIBLE);
//                } else {
//                    showtext3.setVisibility(View.GONE);
//                }
//
//                if (!stepBean.getImports().equals("") && !stepBean.getImports().equals("NULL") && !stepBean.getImports().equals("null")) {
//                    showtext4.setText(stepBean.getImports());
//                    showtext4.setVisibility(View.VISIBLE);
//                } else {
//                    showtext4.setVisibility(View.GONE);
//                }
//
//                /* 6-22 5719 */
//                /*
//                isEntry = false;
//                if (titleBean.getNum().equals("2") && stepBean.getNum().equals("1")){
//                    showImgBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON/images/" + item_num + "_" + (image_order) + "_1.jpg");
//                    showImg.setImageBitmap(showImgBitmap);
//                    showImgLay.setVisibility(View.VISIBLE);
//                }else if (titleBean.getNum().equals("2") && stepBean.getNum().equals("2")){
//                    showImgBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON/images/" + item_num + "_" + (image_order) + "_2.jpg");
//                    showImg.setImageBitmap(showImgBitmap);
//                    showImgLay.setVisibility(View.VISIBLE);
//                }else if (titleBean.getNum().equals("2") && stepBean.getNum().equals("3")){
//                    showImgBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON/images/" + item_num + "_" + (image_order) + "_3.jpg");
//                    showImg.setImageBitmap(showImgBitmap);
//                    showImgLay.setVisibility(View.VISIBLE);
//                }else {
//                    showImgBitmap = BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON/images/" + item_num + "_" + (image_order) + ".jpg");
//                    showImg.setImageBitmap(showImgBitmap);
//                    showImgLay.setVisibility(View.VISIBLE);
//                }
//                */
//                /*  6-22 */
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void getpoint(boolean speechpoint) {
//        if (speechpoint) {
//            point_icon1.setImageDrawable(null);
//            point_icon2.setImageDrawable(null);
//            point_icon3.setImageDrawable(null);
//            pointHander.postDelayed(pointThread1, 250);
//        } else {
//            point_icon1.setImageDrawable(getResources().getDrawable(R.drawable.point_icon1));
//            point_icon2.setImageDrawable(getResources().getDrawable(R.drawable.point_icon1));
//            point_icon3.setImageDrawable(getResources().getDrawable(R.drawable.point_icon1));
//            pointHander.removeCallbacks(pointThread1);
//            pointHander.removeCallbacks(pointThread2);
//        }
//    }
//
//    private Thread pointThread1 = new Thread() {
//        @Override
//        public void run() {
//            //违反单线程原则
//            point_icon1.setImageDrawable(getResources().getDrawable(R.drawable.point_icon2));
//            point_icon2.setImageDrawable(getResources().getDrawable(R.drawable.point_icon2));
//            point_icon3.setImageDrawable(getResources().getDrawable(R.drawable.point_icon2));
//            pointHander.postDelayed(pointThread2, 250);
//        }
//    };
//    private Thread pointThread2 = new Thread() {
//        @Override
//        public void run() {
//            point_icon1.setImageDrawable(null);
//            point_icon2.setImageDrawable(null);
//            point_icon3.setImageDrawable(null);
//            pointHander.postDelayed(pointThread1, 250);
//        }
//    };
//
//    /* 获取聚焦状态 */
//    @Override
//    public void getFocusing(boolean start) {
//        if (start) {
//            focusImg2.setTop(315);
//            focusImg1.setTop(165);
//        } else {
//            focusImg1.setTop(315);
//            focusImg2.setTop(165);
//            Handler focushandler = new Handler();
//            focushandler.postDelayed(new Thread() {
//                public void run() {
//                    focusImg2.setTop(315);
//                }
//            }, 300);
//        }
//    }
//
//    /* 拍照回调 */
//    @Override
//    public void onPictureTaken(byte[] bytes, Camera camera) {
//        Log.e(TAG, "myJpegCallback:onPictureTaken...");
//        boolean saveResult = false;     //图片是否存储完成
//        imgFile = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_IMAGE, itemBean.getNum(), itemBean.getTitle(), titleBean.getNum() + "." + titleBean.getTitle(), titleBean.getNum() + "." + stepBean.getNum(), work_id);
//        if (imgFile != null) {
//            try {
//                cameraInterface.myCamera.stopPreview();
//                cameraInterface.isPreviewing = false;
//                saveResult = FileUtil.writeDataToFile(bytes, imgFile);
//                saveResult = true;
//                /*　扫描新添加的媒体文件信息到ＭediaStore数据库 */
//                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                intent.setData(Uri.fromFile(imgFile));
//                sendBroadcast(intent);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        cameraInterface.myCamera.startPreview();
//        cameraInterface.isPreviewing = true;
//        cameraInterface.isCapturing = false;
//        cameraInterface.myCamera.setAutoFocusMoveCallback(cameraInterface.myAutoFocusMoveCallback);
//        if (!saveResult) {
//            Toast.makeText(getApplicationContext(), "照片存储失败", Toast.LENGTH_LONG).show();
//        } else {
//            Toast.makeText(getApplicationContext(), "拍摄完成。", Toast.LENGTH_LONG).show();
//            mSpeechRecognition.freshPlayStrings("拍摄完成。");
//            if (isBiger) {
//                isBiger = false;
//                cameraInterface.setZoom(-1);
//            }
//            updateUrl(isNormal, imgFile.getName());
//
//            /* 新增：上传图片 */
//            if (isUploading) {
//                uploadFileStrings.add(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON/" + itemBean.getTitle() + "/" + work_id + "/" + titleBean.getNum() + "." + titleBean.getTitle() + "/" + titleBean.getNum() + "." + stepBean.getNum() + "/" + imgFile.getName());
//            } else {
//                uploadFileStrings.add(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON/" + itemBean.getTitle() + "/" + work_id + "/" + titleBean.getNum() + "." + titleBean.getTitle() + "/" + titleBean.getNum() + "." + stepBean.getNum() + "/" + imgFile.getName());
//                ftpUpload();
//            }
//        }
//        imgFile = null;
//    }
//
//    /* 录制视频 */
//    private void startVideoRecord() {
//        if (!isRecording) {
//            if (getSDAvailableSize() < minMB) {
//                onRecordError(cacheError);
//                return;
//            }
//            new MediaPrepareTask().execute(null, null, null);
//        }
//    }
//
//    /* 获得sd卡剩余容量，即可用大小 单位(MB) */
//    private long getSDAvailableSize() {
//        File path = Environment.getExternalStorageDirectory();
//        StatFs stat = new StatFs(path.getPath());
//        long blockSize = stat.getBlockSize();
//        long availableBlocks = stat.getAvailableBlocks();
//        return blockSize * availableBlocks / 1024 / 1024;
//    }
//
//    /*录制失败的回调 @param error 失败的原因 */
//    private void onRecordError(String error) {
//        Log.e("录制失败的回调", error);
//    }
//
//    /* 录像线程 */
//    private class MediaPrepareTask extends AsyncTask<Void, Void, Boolean> {
//        @Override
//        protected Boolean doInBackground(Void... voids) {
//            if (!startVideoRecorder()) {
//                releaseMediaRecorder();
//                mSpeechRecognition.startSpeech();
//                return false;
//            }
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result) {
//            if (!result) {
//                Log.e("录像线程", "录制失败");
//                mSpeechRecognition.startSpeech();
//            } else {
//                updateUrl(isNormal, mOutputFile.getName());
//                isRecording = true;
//                tvRecordTime.setText("00:00");
//                tvRecordTime.setVisibility(View.VISIBLE);
//                mHanler.postDelayed(RecordCheckThread, delayCheckCacheTime);
//            }
//        }
//    }
//
//    /* 开始录制 @return 返回是否开启录制成功 */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
//    private boolean startVideoRecorder() {
//        if (!cameraInterface.isPreviewing) {
//            return false;
//        }
//        try {
//            // BEGIN_INCLUDE (configure_media_recorder)
//            mMediaRecorder = new MediaRecorder();
//            // Step 1: Unlock and set camera to MediaRecorder
//            //cameraInterface.myCamera.stopPreview();
//            cameraInterface.myCamera.unlock();
//            cameraInterface.isUnlockCamera = true;
//            mMediaRecorder.setCamera(cameraInterface.myCamera);
//            mMediaRecorder.setOnErrorListener(this);
//            // Step 2: Set sources
//            //音频采集方式
//            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
//            //视频采集方式
//            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
//            //文件输出格式
//            //mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
//            //audio编码格式
//            //mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
//            //video编码格式
//            //mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
//            //mMediaRecorder.setVideoSize(800, 480);
//            //视频编码比特率
//            //mMediaRecorder.setVideoEncodingBitRate(3 * 1024 * 1024);
//            //视频帧率
//            //mMediaRecorder.setVideoFrameRate(30);
//            //设置会话的最大持续时间（毫秒）
//            //mMediaRecorder.setMaxDuration(60 * 1000);
//            // Step 3: Set w_first_activity CamcorderProfile (requires API Level 8 or higher)
//            profile.audioCodec = MediaRecorder.AudioEncoder.AAC;
//            profile.videoCodec = MediaRecorder.VideoEncoder.H264;
//            profile.audioChannels = 1;
//            profile.audioSampleRate = 16000;
//            profile.audioBitRate = 90 * 1024;
//            profile.videoBitRate = 30 * 1024 * 1024;
//            profile.videoFrameRate = 30;
//            profile.duration = 60 * 1000;
//            mMediaRecorder.setProfile(profile);
//            mMediaRecorder.setPreviewDisplay(mySurfaceHolder.getSurface());
//            // Step 4: Set output file
//            mOutputFile = CameraHelper.getOutputMediaFile(CameraHelper.MEDIA_TYPE_VIDEO, itemBean.getNum(), itemBean.getTitle(), titleBean.getNum() + "." + titleBean.getTitle(), titleBean.getNum() + "." + stepBean.getNum(), work_id);
//            if (mOutputFile == null) {
//                return false;
//            }
//            mMediaRecorder.setOutputFile(mOutputFile.getPath());
//            // END_INCLUDE (configure_media_recorder)
//            // Step 5: Prepare configured MediaRecorder
//            try {
//                mMediaRecorder.prepare();
//                mMediaRecorder.start();
//            } catch (IllegalStateException e) {
//                Log.d("IllegalStateException", "IllegalStateException preparing MediaRecorder: " + e.getMessage());
//                releaseMediaRecorder();
//                return false;
//            } catch (IOException e) {
//                Log.d("IOException", "IOException preparing MediaRecorder: " + e.getMessage());
//                releaseMediaRecorder();
//                return false;
//            }
//        } catch (RuntimeException e) {
//            Log.e("Video", "处理超时！！");
//            e.printStackTrace();
//        }
//        return true;
//    }
//
//    /* 停止录像 */
//    private void stopVideoRecord() {
//        mHanler.removeCallbacks(RecordCheckThread);
//        recordTime = 0;    //录像的时间
//        isRecording = false;
//        tvRecordTime.setVisibility(View.GONE);
//        tvRecordTime.setText("00:00");
//        if (mMediaRecorder != null) {
//            try {
//                mMediaRecorder.stop();  // stop the recording
//                /*　扫描新添加的媒体文件信息到ＭediaStore数据库 */
//                Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                intent.setData(Uri.fromFile(mOutputFile));
//                sendBroadcast(intent);
//
//                /* 新增：上传录像 */
//                Toast.makeText(getApplicationContext(), "录制成功。", Toast.LENGTH_LONG).show();
//                mSpeechRecognition.freshPlayStrings("录制完成。");
//                if (isUploading) {
//                    uploadFileStrings.add(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON/" + itemBean.getTitle() + "/" + work_id + "/" + titleBean.getNum() + "." + titleBean.getTitle() + "/" + titleBean.getNum() + "." + stepBean.getNum() + "/" + mOutputFile.getName());
//                } else {
//                    uploadFileStrings.add(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "FSON/" + itemBean.getTitle() + "/" + work_id + "/" + titleBean.getNum() + "." + titleBean.getTitle() + "/" + titleBean.getNum() + "." + stepBean.getNum() + "/" + mOutputFile.getName());
//                    ftpUpload();
//                }
//            } catch (RuntimeException e) {
//                Log.d("RuntimeException", "RuntimeException: stop() is called immediately after start()");
//                mOutputFile.delete();
//            }
//            releaseMediaRecorder();
//            //releaseCamera();
//        }
//        if (isBiger) {
//            isBiger = false;
//            cameraInterface.setZoom(-1);
//        }
//        mSpeechRecognition.startSpeech();
//        //startPreview();
//    }
//
//    /* 检测内存并更新录制时间 */
//    private Thread RecordCheckThread = new Thread() {
//        @Override
//        public void run() {
//            super.run();
//            setRecordCheckTime();
//        }
//    };
//
//    /* 设置录制时间UI */
//    private void setRecordCheckTime() {
//        if (isRecording) {
//            if (getSDAvailableSize() < minMB) {
//                stopVideoRecord();
//                onRecordError(cacheError);
//            } else {
//                recordTime += 1;
//                long seconds = recordTime % 60;
//                long minus = recordTime / 60;
//                String secondStr = seconds > 9 ? (seconds + "") : ("0" + seconds);
//                String minusStr = minus > 9 ? (minus + "") : ("0" + minus);
//                String time = minusStr + ":" + secondStr;
//                tvRecordTime.setText(time);
//                mHanler.postDelayed(RecordCheckThread, delayCheckCacheTime);
//            }
//        }
//    }
//
//    @Override
//    public void onError(MediaRecorder mediaRecorder, int i, int i1) {
//        stopVideoRecord();
//        onRecordError("error:" + i);
//    }
//
//    @Override
//    public void onCompletion(MediaPlayer mediaPlayer) {
//    }
//
//    /* 释放mediaRecorder */
//    private void releaseMediaRecorder() {
//        try {
//            if (mMediaRecorder != null) {
//                mMediaRecorder.reset();
//                mMediaRecorder.release();
//                mMediaRecorder = null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (cameraInterface.isUnlockCamera) {
//            cameraInterface.myCamera.lock();
//            cameraInterface.isUnlockCamera = false;
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        Log.e("That:", "onResume");
//        mSpeechRecognition.loadSpeech(this, this);
//        if (step_column == 1) {
//            step_column = 0;
//            int get_order = workDb.getStepOrder(item_num);
//            if (order - 1 != get_order) {
//                mSpeechRecognition.freshPlayStrings("跳转工作节点。");
//                order = get_order;
//                getProcedure(order);
//                order++;
//            }
//        } else {
//            mSpeechRecognition.freshPlayStrings(changetext(saying));
//        }
//        mSpeechRecognition.startSpeech();
//        cameraInterface.doStartPreview(mySurfaceHolder);
//        registerReceiver(exitReceiver, new IntentFilter(EXITACTION));
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (isRecording) {
//            stopVideoRecord();
//        }
//        mSpeechRecognition.stopSpeech();
//        mSpeechRecognition.stopSpeak();
//        mHanler.removeCallbacks(RecordCheckThread);
//        unregisterReceiver(exitReceiver);
//        releaseMediaRecorder();
//        cameraInterface.releaseCamera();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        cameraInterface.doStartPreview(mySurfaceHolder);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        workDb.onDestory();
//    }
//
//    /* 退出广播 */
//    private BroadcastReceiver exitReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            finish();
//        }
//    };
//}