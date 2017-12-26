package hung.jiawa.view.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoFragment;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TImage;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.model.TakePhotoOptions;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import hung.jiawa.ImageListItem;
import hung.jiawa.LoadingDialog;
import hung.jiawa.R;
import hung.jiawa.presenter.IPersonPresenter;
import hung.jiawa.presenter.PersonPresenterCompl;
import hung.jiawa.view.IPersonView;
import hung.jiawa.view.activity.LoginActivity;
import hung.jiawa.view.activity.PersonalArticleActivity;
import hung.jiawa.view.activity.PersonalKeepActivity;
import hung.jiawa.view.activity.SavedLocationActivity;
import hung.jiawa.view.activity.SettingActivity;

public class PersonFragment extends TakePhotoFragment implements IPersonView, View.OnClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "PersonFragment - ";
    private LoadingDialog mLoadingDialog = null;
    private SimpleDraweeView profile_img;
    private RelativeLayout profile, favorit, article, friend, setting, about, location, logout;
    private TextView tv_name;
    private ImageView edit_name;

    IPersonPresenter personPresenter;
    public static PersonFragment newInstance() {
        PersonFragment fragmentFirst = new PersonFragment();
        return fragmentFirst;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false); //實體化佈局

        profile_img = (SimpleDraweeView) view.findViewById(R.id.profile_img);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        profile = (RelativeLayout) view.findViewById(R.id.list_profile);
        location = (RelativeLayout) view.findViewById(R.id.list_location);
        favorit = (RelativeLayout) view.findViewById(R.id.list_favorit);
        article = (RelativeLayout) view.findViewById(R.id.list_artcle);
        friend = (RelativeLayout) view.findViewById(R.id.list_friend);
        setting = (RelativeLayout) view.findViewById(R.id.list_setting);
        about = (RelativeLayout) view.findViewById(R.id.list_about);
        logout = (RelativeLayout) view.findViewById(R.id.list_logout);
        edit_name = (ImageView) view.findViewById(R.id.edit_name);

        profile_img.setOnClickListener(this);
        profile.setOnClickListener(this);
        location.setOnClickListener(this);
        favorit.setOnClickListener(this);
        article.setOnClickListener(this);
        friend.setOnClickListener(this);
        setting.setOnClickListener(this);
        about.setOnClickListener(this);
        logout.setOnClickListener(this);
        edit_name.setOnClickListener(this);

        mLoadingDialog = new LoadingDialog(getActivity());
        personPresenter = new PersonPresenterCompl(getActivity(), this);
        return view;
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingDialog() {
    }

    @Override
    public void setProfileImage(String imgUri) {
        profile_img.setImageURI(imgUri);
    }

    @Override
    public void setName(String name) {
        tv_name.setText(name);
    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public void startLoginActivity() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }
    @Override
    public void takeCancel() {
        super.takeCancel();
        //images.remove(images.size()-1);
        //Log.d(TAG, NAME+"takeCancel : " + images);
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        //images.remove(images.size()-1);
        //Log.d(TAG, NAME+"takeFail : " + images);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        //Log.d(TAG, NAME+"takeSuccess : " + images);
        uploadProfileImage(result.getImages());
    }

    private void uploadProfileImage(ArrayList<TImage> images) {
        for (int i = 0; i < images.size(); i++) {
            Uri imgUri = Uri.fromFile(new File(images.get(i).getCompressPath()));
            String str_Images = "";
            try {
                InputStream iStream = getActivity().getContentResolver().openInputStream(imgUri);
                byte[] inputData = getBytes(iStream);
                str_Images = Base64.encodeToString(inputData, Base64.DEFAULT);
            } catch (IOException e) {
                e.printStackTrace();
            }
            personPresenter.uploadProfileImage(str_Images);
        }
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    @Override
    public void onClick(View v) {
        AlertDialog alertDialog=null;
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view;
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        switch (v.getId()) {
            case R.id.profile_img:
                //新增相片
                final String[] Items = {"檢視", "拍照", "從相簿選擇"};
                new AlertDialog.Builder(getActivity())
                        .setItems(Items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File file = new File(Environment.getExternalStorageDirectory(), "/jiawa/" + getPhotoFileName());
                                if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                                Uri imageUri = Uri.fromFile(file);
                                //images.add(0,imageUri);
                                TakePhoto takePhoto = getTakePhoto();

                                CompressConfig config = new CompressConfig.Builder()
                                        .setMaxSize(1024 * 50)
                                        .setMaxPixel(800)
                                        .enableReserveRaw(true)
                                        .create();
                                takePhoto.onEnableCompress(config, true);

                                TakePhotoOptions.Builder builderTakePhotoOptions = new TakePhotoOptions.Builder();
                                builderTakePhotoOptions.setCorrectImage(true);
                                takePhoto.setTakePhotoOptions(builderTakePhotoOptions.create());

                                CropOptions.Builder builderCropOptions = new CropOptions.Builder();
                                builderCropOptions.setAspectX(1).setAspectY(1);
                                builderCropOptions.setWithOwnCrop(false);

                                switch (which) {
                                    case 0://檢視相片
                                        List<Uri> images = personPresenter.getProfileImage();
                                        new ImageViewer.Builder(getActivity(), images).show();
                                        break;
                                    case 1:
                                        // 指定調用相機拍照後照片的儲存路徑
                                        takePhoto.onPickFromCaptureWithCrop(imageUri, builderCropOptions.create());
                                        break;
                                    case 2:
                                        takePhoto.onPickFromGalleryWithCrop(imageUri, builderCropOptions.create());
                                        break;
                                }
                            }
                        }).show();
                break;
            case R.id.list_profile:
                break;
            case R.id.list_location:
                startActivity(new Intent(getActivity(), SavedLocationActivity.class));
                break;
            case R.id.list_favorit:
                startActivity(new Intent(getActivity(), PersonalKeepActivity.class));
                break;
            case R.id.list_artcle:
                startActivity(new Intent(getActivity(), PersonalArticleActivity.class));
                break;
            case R.id.list_friend:
                break;
            case R.id.list_setting:
                //startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.list_about:
                view = inflater.inflate(R.layout.dialog_about_view,null);
                builder.setView(view);
                TextView tv_version = (TextView) view.findViewById(R.id.tv_version);
                String appVersion="";
                PackageManager manager = getActivity().getPackageManager();
                try { PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
                    appVersion = info.versionName; //版本名
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                tv_version.setText("版本:"+appVersion);
                alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.list_logout:
                builder.setTitle("您即將要登出");
                //builder.setMessage("您即將要登出");
                builder.setPositiveButton("登出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        personPresenter.logout();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.create();
                builder.show();
                break;
            case R.id.edit_name:
                view = inflater.inflate(R.layout.dialog_edit_name_view,null);
                final EditText edt_name = (EditText) view.findViewById(R.id.edt_name);
                edt_name.setText(personPresenter.getName());
                final String name = edt_name.getText().toString();
                edt_name.setSelection(name.length());
                builder.setView(view);
                builder.setPositiveButton("修改", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!name.equals(edt_name.getText().toString()))
                            personPresenter.setName(edt_name.getText().toString());
                    }
                });
                alertDialog = builder.create();
                alertDialog.show();
                break;
        }
    }
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }
}
