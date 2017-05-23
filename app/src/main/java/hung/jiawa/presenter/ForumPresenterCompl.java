package hung.jiawa.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.model.ForumModelCompl;
import hung.jiawa.model.IForumModel;
import hung.jiawa.view.IForumView;

/**
 * Created by omar8 on 2017/5/22.
 */

public class ForumPresenterCompl implements IForumPresenter, AsyncTaskCallBack {
    public final String TAG = "JiaWa";
    public final String NAME = "ForumActivity - ";
    IForumView iForumView;
    IForumModel iForumModel;
    Context context;
    public ForumPresenterCompl(Context context, IForumView iForumView) {
        this.iForumView = iForumView;
        this.iForumModel = new ForumModelCompl(context, this);
        this.context = context;
        iForumModel.getForum();
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        try {
            String status="", msg="";
            List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                Log.d(TAG, NAME+"lengh = "+jsonArray.length()+" : i = "+i + ": jsonData = " + jsonData.toString());
                if(i==0) {
                    status = jsonData.getString("status");
                    msg = jsonData.getString("msg");
                    if (status.equals("501")) {
                        iForumView.toast(msg);
                    }
                }else {
                    if (status.equals("201")) {
                        String fid = jsonData.getString("fid");
                        String fName = jsonData.getString("fName");
                        String article = jsonData.getString("article");
                        //存入myDataset
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("fid", fid);
                        item.put("fName", fName);
                        item.put("article", article);
                        myDataset.add(item);
                        iForumView.setForumList(myDataset);
                    }
                }
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iForumView.toast(error);
    }
}
