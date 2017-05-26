package hung.jiawa.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.model.DetailModelCompl;
import hung.jiawa.model.IDetailModel;
import hung.jiawa.view.IDetailView;

/**
 * Created by omar8 on 2017/5/22.
 */

public class DetailPresenterCompl implements IDetailPresenter, AsyncTaskCallBack {
    public final String TAG = "JiaWa";
    public final String NAME = "DetailActivity - ";
    IDetailView iDetailView;
    IDetailModel iDetailModel;
    Context context;
    String id;
    public DetailPresenterCompl(Context context, IDetailView iDetailView, String id) {
        this.iDetailView = iDetailView;
        this.iDetailModel = new DetailModelCompl(context, this);
        this.context = context;
        this.id = id;
        iDetailView.showLoadingDialog();
        iDetailModel.getDetail(id);
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                String status = jsonData.getString("status");
                String msg = jsonData.getString("msg");
                if (status.equals("501")) {
                    iDetailView.toast(msg);
                } else if (status.equals("201")) {
                    String id = jsonData.getString("id");
                    String title = jsonData.getString("title");
                    String content = jsonData.getString("content");
                    String img = jsonData.getString("img");
                    String latlng = jsonData.getString("latlng");
                    String city_id = jsonData.getString("city_id");
                    String type_id = jsonData.getString("type_id");
                    String machine = jsonData.getString("machines");
                    String like = jsonData.getString("like");
                    iDetailView.dismissLoadingDialog();
                    iDetailView.showDetail(id, title, latlng, city_id, type_id, content, machine, like);
                    if(img.length()>1) iDetailView.showImage(img);
                }
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iDetailView.toast(error);
        iDetailView.dismissLoadingDialog();
    }

    //讀取網路圖片，型態為Bitmap
    private static Bitmap getBitmapFromURL(String imageUrl)
    {
        try
        {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);
            return bitmap;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
