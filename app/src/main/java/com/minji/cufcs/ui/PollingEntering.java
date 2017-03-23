package com.minji.cufcs.ui;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lidroid.xutils.http.client.multipart.content.FileBody;
import com.lidroid.xutils.http.client.multipart.content.StringBody;
import com.minji.cufcs.ApiField;
import com.minji.cufcs.IntentFields;
import com.minji.cufcs.R;
import com.minji.cufcs.adapter.HorizontalListViewAdapter;
import com.minji.cufcs.base.BaseActivity;
import com.minji.cufcs.bean.PollingSingleSave;
import com.minji.cufcs.bean.PollingSingleSave.SingleContent;
import com.minji.cufcs.http.HttpBasic;
import com.minji.cufcs.manger.ThreadManager;
import com.minji.cufcs.uitls.ACache;
import com.minji.cufcs.uitls.GsonTools;
import com.minji.cufcs.uitls.SharedPreferencesUtil;
import com.minji.cufcs.uitls.StringUtils;
import com.minji.cufcs.uitls.ToastUtil;
import com.minji.cufcs.uitls.ViewsUitls;
import com.minji.cufcs.widget.HorizontalListView;

import org.apache.http.message.BasicNameValuePair;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PollingEntering extends BaseActivity implements OnClickListener {

    private List<Bitmap> list = new ArrayList<Bitmap>();
    private String pollingContent;
    private String mSaveFourId;
    private int itemPosition;
    private AlertDialog alertDialog;

    @Override
    public void onCreateContent() {

        // 展示返回键
        showBack();
        // 获取提交的项目数和巡检内容和保存四项后的id
        itemPosition = mIntentDate.getIntExtra(IntentFields.ALREADY_SUBMIT, 0);
        pollingContent = mIntentDate
                .getStringExtra(IntentFields.POLLING_CONTENT);
        mSaveFourId = mIntentDate.getStringExtra(IntentFields.MSAVEFOURID);
        number = mIntentDate.getStringExtra("number");

        // 加载布局
        view = setContent(R.layout.layout_polling_entering);

        initAllView();

        // 缓存
        File file = Environment.getExternalStorageDirectory();
        aCache = ACache.get(new File(file, "Cufcs/image"));

    }

    private void initAllView() {
        // 设置保存按钮
        Button mEnteringSave = (Button) view.findViewById(R.id.bt_polling_save);
        mEnteringSave.setOnClickListener(this);

        // 设置添加图片功能
        ImageView mAddPicture = (ImageView) view
                .findViewById(R.id.iv_polling_enter_add_picture);
        mAddPicture.setOnClickListener(this);
        HorizontalListView horizontalListView = (HorizontalListView) view
                .findViewById(R.id.hlv);
        horizontalListViewAdapter = new HorizontalListViewAdapter(list);
        horizontalListView.setAdapter(horizontalListViewAdapter);

        // 设置巡视内容要求
        TextView textView = (TextView) view
                .findViewById(R.id.tv_polling_content);
        textView.setText(pollingContent);

        // 巡检记录
        pollingNote = (EditText) view.findViewById(R.id.et_pull_down);
        // 巡检情况
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroups);

    }

    private HttpBasic httpBasic;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_polling_enter_add_picture:
                showDialogs();
                break;
            case R.id.bt_polling_save:

                // 是否正常
                int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
                if (checkedRadioButtonId == R.id.rb_no_nromal) {
                    if (StringUtils.isEmpty(pollingNote.getText().toString())) {
                        makeToast("由于巡视情况为不正常,请填写巡视记录");
                    } else {
                        showReminderDialog("是否保存本项巡检信息，保存后将无法在进行编辑");
                    }
                }
                if (checkedRadioButtonId == R.id.rb_nromal) {
                    showReminderDialog("是否保存本项巡检信息，保存后将无法在进行编辑");
                }
                break;
            case R.id.bt_cancel:
                alertDialog.cancel();
                break;
            case R.id.bt_make_sure:
                // 此处进行项目提交和图片上传
                saveGson = getSaveGson();
                // System.out.println(saveGson);
                setLoadIsVisible(View.VISIBLE);
                ThreadManager.getInstance().execute(new Runnable() {
                    @Override
                    public void run() {
                        // TODO 在提交信息前先上传图片
                        httpBasic = new HttpBasic();

                        boolean isSuccesful = submitPicture();
                        if (isSuccesful) {
                            webSaveSingle();
                        } else {
                            ViewsUitls.runInMainThread(new Runnable() {
                                @Override
                                public void run() {
                                    setLoadIsVisible(View.GONE);
                                    ToastUtil.showToast(ViewsUitls.getContext(),
                                            "网络异常,图片上传失败");
                                }
                            });
                        }
                    }
                });
                alertDialog.cancel();
                break;
        }
    }

    private boolean submitPicture() {
        for (int i = 0; i < list.size(); i++) {
            System.out.println("保存了第" + (i + 1) + "张图片");
            try {
                // 图片名
                String name = mSaveFourId + "_" + number + "_" + (i + 1)
                        + ".jpg";
                // 图片地址
                String file = "Cufcs/image/" + name;
                // 图片路径
                FileBody fileBody = new FileBody(new File(
                        Environment.getExternalStorageDirectory(), file));
                // 图片描述
                StringBody stringBody = new StringBody(name);
                // url
                String address = SharedPreferencesUtil.getString(
                        ViewsUitls.getContext(), "address", "");
                String url = address + ApiField.PATROLRECEIPT
                        + "savePicture.html";
                System.out.println("url: " + url);
                String postFile = httpBasic.postFile(url, fileBody, stringBody);
                System.out.println("postFile: " + postFile);
                if (StringUtils.interentIsNormal(postFile)) {// 网络连接正常
                    if (postFile.contains("false")) {
                        return false;// 如果上传失败就直接跳出
                    }
                } else {
                    return false;// 网络连接一不不正常，就跳出
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    private void showReminderDialog(String content) {
        alertDialog = new AlertDialog.Builder(PollingEntering.this).create();
        LayoutParams attributes = alertDialog.getWindow().getAttributes();// 获取对话框的属性集
        WindowManager m = PollingEntering.this.getWindowManager();
        Display d = m.getDefaultDisplay(); // 为获取屏幕宽、高
        attributes.width = (int) (d.getWidth() * 0.9);
        alertDialog.show();
        // 设置对话框中自定义内容
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_reminder);
        TextView reminderContents = (TextView) window
                .findViewById(R.id.tv_reminder_contents);
        reminderContents.setText(content);
        Button mCancel = (Button) window.findViewById(R.id.bt_cancel);
        Button mSure = (Button) window.findViewById(R.id.bt_make_sure);
        mCancel.setOnClickListener(this);
        mSure.setOnClickListener(this);
    }

    private void webSaveSingle() {
        List<BasicNameValuePair> list = new ArrayList<BasicNameValuePair>();
        list.add(new BasicNameValuePair("jsonStr", saveGson));
        try {
            String address = SharedPreferencesUtil.getString(
                    ViewsUitls.getContext(), "address", "");
            final String postBack = httpBasic.postBack(address
                    + ApiField.POLLING_SAVE_ONE_OF, list);
            System.out.println(saveGson);
            System.out.println(address + ApiField.POLLING_SAVE_ONE_OF);
            System.out.println(postBack);
            ViewsUitls.runInMainThread(new Runnable() {
                @Override
                public void run() {
                    setLoadIsVisible(View.GONE);
                    if (postBack.equals("{'result':true}")) {
                        // 成功后跳转回前一界面
                        Intent data = new Intent();
                        PollingEntering.this.setResult(itemPosition, data);
                        finish();
                    } else {
                        ToastUtil.showToast(ViewsUitls.getContext(),
                                "网络异常，保存失败");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSaveGson() {
        PollingSingleSave pollingSingleSave = new PollingSingleSave();
        int istype = -1;
        String enumid = "";
        // 是否正常
        int checkedRadioButtonId = radioGroup.getCheckedRadioButtonId();
        if (checkedRadioButtonId == R.id.rb_no_nromal) {
            istype = 1;
        }
        if (checkedRadioButtonId == R.id.rb_nromal) {
            istype = 0;
        }
        // 问题说明
        String contents = pollingNote.getText().toString();

        // 18项中的某一个
        if (itemPosition < 10) {
            enumid = "0" + itemPosition;
        } else {
            enumid = "" + itemPosition;
        }
        SingleContent singleContent = new SingleContent(
                mSaveFourId, enumid, istype, contents);
        ArrayList<SingleContent> arrayList = new ArrayList<SingleContent>();
        arrayList.add(singleContent);
        pollingSingleSave.setId(mSaveFourId);
        pollingSingleSave.setPds(arrayList);

        return GsonTools.createGsonString(pollingSingleSave);
    }

    public final static int PHOTO_ZOOM = 0;
    public final static int TAKE_PHOTO = 1;
    public final static int PHOTO_RESULT = 2;
    public static final String IMAGE_UNSPECIFIED = "image/*";
    private String imageDir;
    private ACache aCache;
    private HorizontalListViewAdapter horizontalListViewAdapter;
    private View view;
    private EditText pollingNote;
    private RadioGroup radioGroup;
    private String saveGson;
    private String number;

    // 弹出添加图片的对话框
    private void showDialogs() {
        AlertDialog alertDialog = new AlertDialog.Builder(PollingEntering.this)
                .create();
        alertDialog.show();
        Window window = alertDialog.getWindow();
        window.setContentView(R.layout.dialog_polling_entering);
        TextView mCamera = (TextView) window.findViewById(R.id.tv_camera);
        TextView mPhotos = (TextView) window.findViewById(R.id.tv_photos);
        mCamera.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (Build.VERSION.SDK_INT >= 23) {// 当前应用所在安卓系统大于等于23也就是6.0版
//                    popupPermission();
//                } else {
                skipCamera();
//                }
            }
        });
        mPhotos.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转图库
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(IMAGE_UNSPECIFIED);
                Intent wrapperIntent = Intent.createChooser(intent, null);
                startActivityForResult(wrapperIntent, PHOTO_ZOOM);
            }
        });

    }

    private void popupPermission() {
        // ContextCompat.checkSelfPermission用于检测某个权限是否已经被授予
        if (ContextCompat.checkSelfPermission(PollingEntering.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(PollingEntering.this, Manifest.permission.CAPTURE_AUDIO_OUTPUT) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("拍照的权限没有申请成功");
            // 该方法是异步的，第一个参数是Context；第二个参数是需要申请的权限的字符串数组；第三个参数为requestCode，主要用于回调的时候检测。可以从方法名requestPermissions以及第二个参数看出，是支持一次性申请多个权限的，系统会通过对话框逐一询问用户是否授权。
            ActivityCompat.requestPermissions(PollingEntering.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.CAPTURE_AUDIO_OUTPUT}, 1);
        } else {
            System.out.println("拍照的权限已经申请成功过了");
            skipCamera();
        }
    }

    private void skipCamera() {
        // 跳转相机
        imageDir = "temp.jpg";
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(
                Environment.getExternalStorageDirectory(), imageDir)));
        startActivityForResult(intent, TAKE_PHOTO);
    }

    // 图片缩放
    public void photoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_UNSPECIFIED);
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, PHOTO_RESULT);
    }

    /* 申请权限的回调 */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        System.out.println();
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    System.out.println("拍照的权限申请成功");
                    skipCamera();
                } else {
                    System.out.println("拍照的权限没有申请成功");
                }
                return;
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    // 在onActivityResult对返回的图片进行处理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == PHOTO_ZOOM) {
                // 从图库获取了图片后在次跳转到缩放页面进行图片处理
                photoZoom(data.getData());
            }
            if (requestCode == TAKE_PHOTO) {
                // 从相机获取了图片后在次跳转到缩放页面进行图片处理
                File picture = new File(
                        Environment.getExternalStorageDirectory() + "/"
                                + imageDir);
                photoZoom(Uri.fromFile(picture));
            }

            if (requestCode == PHOTO_RESULT) {
                // 缩放图片处理页面跳回时调用
                Bundle extras = data.getExtras();
                if (extras != null) {
                    // 这是处理后的图片Bitmap
                    Bitmap photo = extras.getParcelable("data");
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    // 对图片进行压缩处理
                    photo.compress(Bitmap.CompressFormat.JPEG, 75, stream);

                    // TODO 修改存储名称
                    aCache.put(mSaveFourId + "_" + number + "_"
                            + (list.size() + 1) + ".jpg", photo);

                    list.add(photo);
                    horizontalListViewAdapter.notifyDataSetChanged();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
