package hung.jiawa.presenter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.TakePhotoOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.LoactionItem;
import hung.jiawa.R;
import hung.jiawa.model.PostLocationModelCompl;
import hung.jiawa.model.IPostLocationModel;
import hung.jiawa.view.IPostLocationView;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PostLocationPresenterCompl implements IPostLocationPresenter, AsyncTaskCallBack {
    public final String TAG = "JiaWa";
    public final String NAME = "PostLocationFragment - ";
    IPostLocationView iPostLocationView;
    IPostLocationModel iPostLocationModel;
    Context context;
    private int countImages=0;
    private int amountImages=0;
    private String nameImages="";
    private String title, content, latlng, city, type, number_of_machine;
    public PostLocationPresenterCompl(Context context, IPostLocationView iPostLocationView) {
        this.iPostLocationView = iPostLocationView;
        this.iPostLocationModel = new PostLocationModelCompl(context, this);
        this.context = context;
        //取得論壇板清單用以顯示spinner
    }
    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        if(mode == 9) iPostLocationView.dismissLoadingDialog();
        try {
            JSONObject jsonData = new JSONObject(result);
            String status = jsonData.getString("status");
            String msg = jsonData.getString("msg");
            if (status.equals("error")) {
                iPostLocationView.toast(msg);
            } else if (status.equals("ok")) {
                if (mode == 10) {
                    countImages++;
                    if (countImages == amountImages) {
                        nameImages += jsonData.getString("img");
                        iPostLocationModel.postLoaction(title, content, latlng, city, type, number_of_machine, nameImages);
                    } else nameImages += jsonData.getString("img") + ",";
                } else if (mode == 9) {
                    String aid = jsonData.getString("id");
                    iPostLocationView.toast(msg);
                    iPostLocationView.showLocationDetail(aid);
                }
                Log.d(TAG, NAME + "onResult : " + countImages + "   nameImages =  " + nameImages);
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iPostLocationView.dismissLoadingDialog();
        iPostLocationView.toast(error);
    }

    @Override
    public void attemptLogin(EditText ed_title, EditText ed_content, EditText ed_lat, EditText ed_lng, Spinner sp_city, Spinner sp_type, Spinner sp_number_of_machine, List<Uri> imgList) {
        String title = ed_title.getText().toString();
        String content = ed_content.getText().toString();
        String lat = ed_lat.getText().toString();
        String lng = ed_lng.getText().toString();
        int city = sp_city.getSelectedItemPosition();
        int type = sp_type.getSelectedItemPosition();
        int number_of_machine = sp_number_of_machine.getSelectedItemPosition();
        boolean cancel = false;
        String msg = "";

        if (TextUtils.isEmpty(content)) {
            cancel = true;
            msg = "內容未填";
        }
        if (TextUtils.isEmpty(lng)) {
            cancel = true;
            msg = "座標未填";
        }
        if (TextUtils.isEmpty(lat)) {
            cancel = true;
            msg = "座標未填";
        }
        if(number_of_machine==0) {
            cancel = true;
            msg = "機台數未選";
        }
        if(type==0) {
            cancel = true;
            msg = "店面類型未選";
        }
        if(city==0) {
            cancel = true;
            msg = "所在縣市未選";
        }
        if (TextUtils.isEmpty(title)) {
            cancel = true;
            msg = "標題未填";
        }
        if (cancel) {
            iPostLocationView.toast(msg);
        } else {
            // 表單無誤，顯示loading並連網確認用戶
            countImages=0;
            nameImages="";
            this.title = title;
            this.content = content;
            this.latlng = lat+","+lng;
            this.city = String.valueOf(city);
            this.type = String.valueOf(type);
            this.number_of_machine = String.valueOf(number_of_machine);
            iPostLocationView.showLoadingDialog();
            amountImages = imgList.size();
            if(amountImages>0) {
                for (int i = 0; i < amountImages; i++) {
                    String str_Images = "";
                    try {
                        InputStream iStream = context.getContentResolver().openInputStream(imgList.get(i));
                        byte[] inputData = getBytes(iStream);
                        str_Images = Base64.encodeToString(inputData, Base64.DEFAULT);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    iPostLocationModel.uploadImage(str_Images, String.valueOf(i + 1));
                }
            }else {
                iPostLocationModel.postLoaction(this.title, this.content, this.latlng, this.city, this.type, this.number_of_machine, "");
            }
        }
    }

    @Override
    public List<LoactionItem> getAll() {
        return iPostLocationModel.getAll();
    }

    @Override
    public void showLocationDialog() {
        List<LoactionItem> items = getAll();
        int amont = items.size();
        String[] Items = new String[amont];
        for(int i=0;i<amont;i++) {
            Items[i] = items.get(i).getName();
        }
        iPostLocationView.showLocationDialog(Items);
    }

    @Override
    public void setLatlng(int which) {
        List<LoactionItem> items = getAll();
        String LatLng = items.get(which).getLatlng();
        //字串轉LatLng
        String[] latlong = LatLng.split(",");
        String latitude = latlong[0];
        String longitude = latlong[1];
        iPostLocationView.setLatlng(latitude, longitude);
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
}
