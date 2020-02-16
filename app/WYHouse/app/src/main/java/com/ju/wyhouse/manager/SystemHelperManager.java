package com.ju.wyhouse.manager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.ju.wyhouse.MainActivity;
import com.ju.wyhouse.activity.SendWallActivity;
import com.ju.wyhouse.utils.GlideEngine;
import com.ju.wyhouse.utils.LogUtil;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureMimeType;

import java.io.File;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author:gaoju
 * Date:2020/2/11 20:04
 * Path:com.ju.wyhouse.manager
 * Desc:相机，系统操作相关
 */
public class SystemHelperManager {

    //PictureSelector标志
    public static final int REQUEST_PICTURE = 3;
    //相册
    public static final int REQUEST_IMAGE = 1;
    //相机
    public static final int REQUEST_CAMERA = 2;
    //时间
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
    //照相机的拍照文件
    private File cameraFile;

    private static volatile SystemHelperManager mInstance;

    public static SystemHelperManager getInstance() {
        if (mInstance == null) {
            synchronized (SystemHelperManager.class) {
                if (mInstance == null) {
                    mInstance = new SystemHelperManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 打开相册Fragment
     *
     * @param fragment
     */
    public void openAblum(Fragment fragment) {
        Intent albumIntent = new Intent(Intent.ACTION_PICK);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        fragment.startActivityForResult(albumIntent, REQUEST_IMAGE);
    }


    /**
     * 打开相机
     *
     * @param fragment
     */
    public void openCamera(Fragment fragment) {
        Uri uri;
        String fileName = simpleDateFormat.format(new Date());
        //创建目录
        File dir = new File(Environment.getExternalStorageDirectory(), "WYHouse");
        if (!dir.exists()) {
            dir.mkdir();
        }
        cameraFile = new File(Environment.getExternalStorageDirectory() + "/WYHouse", fileName + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //第二个参数为 包名.fileprovider
            LogUtil.i("++:" + fragment.getActivity().getPackageName());
            uri = FileProvider.getUriForFile(fragment.getActivity(), fragment.getActivity().getPackageName() + ".fileProvider", cameraFile);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraFile);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        fragment.startActivityForResult(intent, REQUEST_CAMERA);
    }


    public File getCameraFile() {
        return cameraFile;
    }


    /**
     * 通过PictureSelector来选择背景图片
     */
    public void openPictureSelectorBg(Activity activity) {
        PictureSelector.create(activity)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .enableCrop(true)
                .showCropFrame(true)
                .compress(true)
                .maxSelectNum(1)
                .withAspectRatio(3, 2)
                .forResult(REQUEST_PICTURE);
    }

    /**
     * 通过PictureSelector来选择头像
     */
    public void openPictureSelectorCir(Fragment fragment) {
        PictureSelector.create(fragment)
                .openGallery(PictureMimeType.ofImage())
                .loadImageEngine(GlideEngine.createGlideEngine())
                .enableCrop(true)
                .showCropFrame(true)
                .compress(true)
                .maxSelectNum(1)
                .withAspectRatio(2, 2)
                .forResult(REQUEST_PICTURE);
    }
}
