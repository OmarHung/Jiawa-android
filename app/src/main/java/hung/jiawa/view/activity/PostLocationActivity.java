package hung.jiawa.view.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
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
import hung.jiawa.LocationHelper;
import hung.jiawa.R;
import hung.jiawa.presenter.IPostLocationPresenter;
import hung.jiawa.presenter.PostLocationPresenterCompl;
import hung.jiawa.view.IPostLocationView;
import hung.jiawa.view.adapter.ImageAdapter;

public class PostLocationActivity extends TakePhotoActivity implements IPostLocationView, LocationHelper.OnLocationCallbacks, AdapterView.OnItemSelectedListener, View.OnClickListener, View.OnTouchListener, PopupMenu.OnMenuItemClickListener, ImageAdapter.OnRecyclerViewItemClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "PostLocationActivity - ";
    private Spinner sp_city, sp_type, sp_number_of_machine;
    private TextView btn_cancel, btn_quick_insert;
    private ImageButton btn_send;
    private EditText edt_title, edt_lat, edt_lng, edt_content;
    private RecyclerView img_list;
    private ImageAdapter imageAdapter;
    private LoadingDialog mLoadingDialog = null;
    IPostLocationPresenter postLocationPresenter;
    private PopupMenu popupmenu;
    private int hasImageSelectCount = 0;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LocationHelper locationHelper=null;

    //List<Uri> images = new ArrayList<>();
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
        showImg(result.getImages());
    }

    private void showImg(ArrayList<TImage> images) {
        for (int i = 0; i < images.size(); i++) {
            imageAdapter.addItem(new ImageListItem(new File(images.get(i).getCompressPath()), imageAdapter.getItemCount(), 0), 1);
            hasImageSelectCount++;
        }
        img_list.scrollToPosition(0);
    }

    /** 從給定路徑載入圖片*/
    public static Bitmap loadBitmap(String imgpath) {
        return BitmapFactory.decodeFile(imgpath);
    }


    /** 從給定的路徑載入圖片，並指定是否自動旋轉方向*/
    public static Bitmap loadBitmap(String imgpath, boolean adjustOritation) {
        if (!adjustOritation) {
            return loadBitmap(imgpath);
        } else {
            Bitmap bm = loadBitmap(imgpath);
            int digree = 0;
            ExifInterface exif = null;
            try {
                exif = new ExifInterface(imgpath);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }
            if (exif != null) {
                //讀取圖片中相機方向資訊
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                //計算旋轉角度
                switch (ori) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        digree = 90;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        digree = 180;
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        digree = 270;
                        break;
                    default:
                        digree = 0;
                        break;
                }
            }
            if (digree != 0) {
                //旋轉圖片
                Matrix m = new Matrix();
                m.postRotate(digree);
                bm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), m, true);
            }
            return bm;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_location);

        //find view
        sp_city = (Spinner) findViewById(R.id.sp_city);
        sp_type = (Spinner) findViewById(R.id.sp_type);
        sp_number_of_machine = (Spinner) findViewById(R.id.sp_number_of_machine);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);
        btn_send = (ImageButton) findViewById(R.id.btn_send);
        btn_quick_insert = (TextView) findViewById(R.id.btn_quick_insert);
        edt_title = (EditText) findViewById(R.id.edt_title);
        edt_lat = (EditText) findViewById(R.id.edt_lat);
        edt_lng = (EditText) findViewById(R.id.edt_lng);
        edt_content = (EditText) findViewById(R.id.edt_content);
        img_list = (RecyclerView) findViewById(R.id.img_list);
        popupmenu = new PopupMenu(this, btn_quick_insert);
        popupmenu.getMenuInflater().inflate(R.menu.menu_post_location, popupmenu.getMenu());

        //set listener
        sp_city.setOnItemSelectedListener(this);
        sp_type.setOnItemSelectedListener(this);
        sp_number_of_machine.setOnItemSelectedListener(this);
        btn_cancel.setOnClickListener(this);
        btn_send.setOnClickListener(this);
        btn_quick_insert.setOnClickListener(this);
        edt_content.setOnTouchListener(this);
        popupmenu.setOnMenuItemClickListener(this);

        //init
        locationHelper = new LocationHelper(this, this);
        postLocationPresenter = new PostLocationPresenterCompl(this, this);
        mLoadingDialog = new LoadingDialog(this);
        initSpinner();
        initRecyclerView();
    }

    protected void onStart() {
        locationHelper.connect();
        super.onStart();
    }

    protected void onStop() {
        locationHelper.disconnect();
        super.onStop();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send:
                List<Uri> uri_Images = new ArrayList<>();
                uri_Images = imageAdapter.getIamgeList();
                postLocationPresenter.attemptLogin(edt_title, edt_content, edt_lat, edt_lng, sp_city, sp_type, sp_number_of_machine, uri_Images);
                break;
            case R.id.btn_quick_insert:
                showPopupMenu();
                break;
            case R.id.btn_cancel:
                checkCancel();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> cityList = ArrayAdapter.createFromResource(this,
                R.array.post_city,
                android.R.layout.simple_spinner_dropdown_item);
        sp_city.setAdapter(cityList);
        ArrayAdapter<CharSequence> typeList = ArrayAdapter.createFromResource(this,
                R.array.post_type,
                android.R.layout.simple_spinner_dropdown_item);
        sp_type.setAdapter(typeList);
        ArrayAdapter<CharSequence> machineList = ArrayAdapter.createFromResource(this,
                R.array.post_machine,
                android.R.layout.simple_spinner_dropdown_item);
        sp_number_of_machine.setAdapter(machineList);
    }

    private void initRecyclerView() {
        imageAdapter = new ImageAdapter(this);
        imageAdapter.setOnItemClickListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        img_list.setLayoutManager(layoutManager);
        img_list.setAdapter(imageAdapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.edt_content) {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                case MotionEvent.ACTION_UP:
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                    break;
            }
        }
        return false;
    }

    @Override
    public void onItemClick(int position, int id, int type, File img) {
        if (type == 1) {
            //新增相片
            final String[] Items = {"拍照", "從相簿選擇"};
            new AlertDialog.Builder(this)
                    .setTitle("新增照片")
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
                                case 0:
                                    // 指定調用相機拍照後照片的儲存路徑
                                    takePhoto.onPickFromCaptureWithCrop(imageUri, builderCropOptions.create());
                                    break;
                                case 1:
                                    takePhoto.onPickFromGalleryWithCrop(imageUri, builderCropOptions.create());
                                    break;
                            }
                        }
                    }).show();
        } else {
            //檢視相片
            List<Uri> images = imageAdapter.getIamgeList();
            Log.d(TAG, NAME + "images : " + images + "  position : " + position);
            int i = -1;
            if (images.size() == 5) i = 0;
            new ImageViewer.Builder(this, images)
                    .setStartPosition(position + i)
                    .show();
        }
    }

    @Override
    public void onItemLongClick(final int position, int id, int type) {
        //Toast.makeText(this, "position: "+position+"  id: "+id+" type: "+type, Toast.LENGTH_SHORT).show();
        if (type == 0 && imageAdapter.getItemCount() > 1) {
            final String[] Items = {"刪除"};
            new AlertDialog.Builder(this)
                    .setTitle("刪除照片")
                    .setItems(Items, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            imageAdapter.removeItem(position);
                            hasImageSelectCount--;
                        }
                    }).show();
        }
        //移動相片
        if (id != 0) {

        }
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingDialog() {
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void showLocationDetail(String aid) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("lid", aid);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    private void checkCancel() {

    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_saved:
                break;
            case R.id.menu_now:
                showLoadingDialog();
                //取得系統定位服務
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    dismissLoadingDialog();
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
                }else {
                    locationHelper.getLocation();
                }
                break;
        }
        return false;
    }

    private void showPopupMenu() {
        popupmenu.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length>0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    showLoadingDialog();
                    locationHelper.getLocation();
                }else {
                    //ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS},LOCATION_PERMISSION_REQUEST_CODE);
                }
                return;
        }
    }

    @Override
    public void onGetLatLng(double lat, double lng) {
        dismissLoadingDialog();
        edt_lat.setText(String.valueOf(lat));
        edt_lng.setText(String.valueOf(lng));
    }
}
