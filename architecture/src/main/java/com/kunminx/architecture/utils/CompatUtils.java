/*
 * Copyright 2018-present KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.kunminx.architecture.utils;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;

/**
 * Create by KunMinX at 19/7/20
 */
public class CompatUtils {

    private static final int TAKE_PHOTO = 100;
    private static final int PHOTO_ALBUM = 200;
    private static final int PHOTO_CLIP = 300;

    private Uri uriClipUri;
    private Uri takePhotoSaveAdr;

    private void taskPhoto(AppCompatActivity activity) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri mImageCaptureUri;
        // 判断7.0android系统
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //临时添加一个拍照权限
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            //通过FileProvider获取uri
            takePhotoSaveAdr = FileProvider.getUriForFile(activity, activity.getPackageName(),
                    new File(activity.getExternalCacheDir(), "savephoto.jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, takePhotoSaveAdr);
        } else {
            mImageCaptureUri = Uri.fromFile(new File(activity.getExternalCacheDir(), "savephoto.jpg"));
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
        }
        activity.startActivityForResult(intent, TAKE_PHOTO);
    }

    private void selecetPhoto(AppCompatActivity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK, null);
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(intent, PHOTO_ALBUM);
    }

    private void startPhotoZoom(AppCompatActivity activity, Uri uri, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 60);
        intent.putExtra("outputY", 60);
        //uriClipUri为Uri类变量，实例化uriClipUri
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (requestCode == TAKE_PHOTO) {
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                intent.setClipData(ClipData.newRawUri(MediaStore.EXTRA_OUTPUT, uri));
                uriClipUri = uri;
            } else if (requestCode == PHOTO_ALBUM) {
                uriClipUri = Uri.parse("file://" + "/" + activity.getExternalCacheDir().getAbsolutePath() + "/" + "clip.jpg");
            }
        } else {
            uriClipUri = Uri.parse("file://" + "/" + activity.getExternalCacheDir().getAbsolutePath() + "/" + "clip.jpg");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriClipUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        activity.startActivityForResult(intent, PHOTO_CLIP);
    }

    public void onActivityResult(AppCompatActivity activity, int requestCode, int resultCode, Intent data) {
        if (resultCode == AppCompatActivity.RESULT_OK) {
            switch (requestCode) {
                case TAKE_PHOTO:
                    Uri clipUri = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        if (takePhotoSaveAdr != null) {
                            clipUri = takePhotoSaveAdr;
                        }
                    } else {
                        clipUri = Uri.fromFile(new File(activity.getExternalCacheDir().toString() + "/savephoto.jpg"));
                    }
                    startPhotoZoom(activity, clipUri, TAKE_PHOTO);
                    break;
                case PHOTO_ALBUM:
                    startPhotoZoom(activity, data.getData(), PHOTO_ALBUM);
                    break;
                case PHOTO_CLIP:
                    /*val optionsa = RequestOptions()
                    optionsa.placeholder(R.mipmap.ic_launcher)
                    optionsa.error(R.mipmap.ic_launcher_round)
                    optionsa.diskCacheStrategy(DiskCacheStrategy.NONE)
                    optionsa.skipMemoryCache(true)//禁用掉Glide的内存缓存
                    Glide.with(this).load(uriClipUri).apply(optionsa).into(iv_userPhoto);*/
                    break;
                default:
                    break;
            }
        }
    }

}
