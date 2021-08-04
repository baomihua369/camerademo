package com.example.camera_surfaceview;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private String TAG = MainActivity.class.getSimpleName();
    private int screenHeight;//屏幕高度
    private int screenWidth;//屏幕宽度
    private Boolean bIfPreview = true;
    private CameraManager mCameraManager;
    private CameraDevice mDevice;
    private CameraCaptureSession mCameraCaptureSession;
    private String mCameraId;
    private Handler childHandler, mainHandler;
    private SurfaceView mSurfaceView = null;
    private SurfaceHolder mSurfaceHolder = null;
    private ImageReader mImageReader;
    private Button mButton;
    private ImageView mImageView;

    private static final SparseIntArray ORIENTATIONS = new SparseIntArray();
    ///为了使照片竖直显示
    static {
        ORIENTATIONS.append(Surface.ROTATION_0, 90);
        ORIENTATIONS.append(Surface.ROTATION_90, 0);
        ORIENTATIONS.append(Surface.ROTATION_180, 270);
        ORIENTATIONS.append(Surface.ROTATION_270, 180);
    }

    //传感器监听
    private OrientationEventListener orientationEventListener;
    private int mOrientation;
    private int rotation;

    /*
     * 生命周期
     * */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();//初始化组件
        initSurfacelistener();//初始化Surface监听
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    /*
     * 根据调用顺序自定义的方法；
     * */
    private void init() {
        mButton = findViewById(R.id.myButton);
        mButton.setOnClickListener(this);
        mImageView = findViewById(R.id.myTextView);
        mSurfaceView = (SurfaceView) this.findViewById(R.id.mSurfaceView);
        mSurfaceHolder = mSurfaceView.getHolder();
        mSurfaceHolder.setKeepScreenOn(true);//保持屏幕打开
        //获得屏幕参数
        DisplayMetrics dm = this.getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
        Log.e(TAG, "screenWidth ：" + screenWidth + ",screenHeight : " + screenHeight);

        /*WindowManager windowManager = (WindowManager) this
                .getSystemService(Context.WINDOW_SERVICE);
        double windowManagerWidth = windowManager.getDefaultDisplay().getWidth();
        double windowManagerHeight = windowManager.getDefaultDisplay().getHeight();
        Log.e(TAG,"windowManagerWidth ：" +windowManagerWidth + ",windowManagerHeight : "+windowManagerHeight);
        */
        //设置Holder大小和类型
        mSurfaceHolder.setFixedSize(1920, 1080);
        //传感器监听
        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                mOrientation = orientation;
            }
        };
        orientationEventListener.enable();
    }


    private void initSurfacelistener() {
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(@NonNull SurfaceHolder holder) {
                // SurfaceView创建时，该方法被调用
                initCamera2();
            }

            @Override
            public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
                // SurfaceView改变时，该方法被调用

            }

            @Override
            public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
                // SurfaceView销毁时，该方法被调用
                // 关闭设备Device；
                if (null != mDevice) {
                    mDevice.close();
                    MainActivity.this.mDevice = null;
                }
            }
        });
    }

    /*
     *   open camera;
     * */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initCamera2() {
        //权限
        String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                , Manifest.permission.RECORD_AUDIO};
        int i = 0;
        for (String permission : permissions) {
            if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(permissions, i++);
                return;
            }
        }

        HandlerThread handlerThread = new HandlerThread("Camera2");
        handlerThread.start();
        childHandler = new Handler(handlerThread.getLooper());
        mainHandler = new Handler(getMainLooper());
        mCameraId = "" + CameraCharacteristics.LENS_FACING_FRONT;//后置摄像头
        mImageReader = ImageReader.newInstance(1080, 1920, ImageFormat.JPEG, 10);
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Log.e(TAG, "进入 onImageAvailable了" );
                Image image = reader.acquireNextImage();
                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                if (bitmap != null) {
                    //相框图片设置  setimageBitmap(bitmap);
                    mImageView.setImageBitmap(bitmap);
                    Log.e(TAG, "bitmap != null" );
                }
            }
        }, mainHandler);
        //获取摄像头管理
        mCameraManager = (CameraManager) this.getSystemService(CAMERA_SERVICE);
        //CameraCharacteristics characteristics = null;

        try {
            mCameraManager.openCamera(mCameraId, mStateCallback, childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            mDevice = camera;
            startPreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {

        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {

        }
    };

    void startPreview() {
        Surface mSurface = mSurfaceHolder.getSurface();

        try {
            CaptureRequest.Builder previewRequestBuilder = mDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            previewRequestBuilder.addTarget(mSurface);

            mDevice.createCaptureSession(Arrays.asList(mSurface,mImageReader.getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                    if (mDevice == null){
                        return;
                    }
                    mCameraCaptureSession = session;
                    try {

                        previewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                        previewRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);

                        CaptureRequest previewRequest = previewRequestBuilder.build();

                        mCameraCaptureSession.setRepeatingRequest(previewRequest,null,childHandler);
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                    Toast.makeText(MainActivity.this, "配置失败", Toast.LENGTH_SHORT).show();
                }
            },childHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void takePicture() {
        if (mDevice != null){
            Log.e(TAG, "进入了 takePicture（）");

            CaptureRequest.Builder captureRequestBuilder;//拍照用的Builder

            try {
                captureRequestBuilder = mDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
                captureRequestBuilder.addTarget(mImageReader.getSurface());
                //自动对焦
                captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                //自动曝光
                captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
                //获取手机方向
//                int rotation = MainActivity.this.getWindowManager().getDefaultDisplay().getRotation();
                //根据设备方向计算设置照片的方向
                if (mOrientation >= 45 && mOrientation < 135) {
                    rotation = 3;
                } else if (mOrientation >= 135 && mOrientation < 225) {
                    rotation = 2;
                } else if (mOrientation >= 225 && mOrientation < 315) {
                    rotation = 1;
                } else {
                    rotation = 0;
                }
                captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION,(Integer) ORIENTATIONS.get(rotation));

                //拍照
                CaptureRequest mCaptureRequest = captureRequestBuilder.build();
                mCameraCaptureSession.capture(mCaptureRequest,null,childHandler);

            } catch (CameraAccessException e) {
                e.printStackTrace();
            }


        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.myButton:
                Log.e(TAG, "进入 onCilck" );

                takePicture();
                break;
        }
    }
}