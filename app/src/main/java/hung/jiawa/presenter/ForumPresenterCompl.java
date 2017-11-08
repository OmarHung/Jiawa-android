package hung.jiawa.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
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
    public final String NAME = "ForumFragment - ";
    IForumView iForumView;
    IForumModel iForumModel;
    Context context;
    public ForumPresenterCompl(Context context, IForumView iForumView) {
        this.iForumView = iForumView;
        this.iForumModel = new ForumModelCompl(context, this);
        this.context = context;
        //取得論壇板清單用以顯示spinner
        iForumModel.getForum();
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        try {
            List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();
            JSONObject jsonData = new JSONObject(result);
            String status = jsonData.getString("status");
            String msg = jsonData.getString("msg");
            if(mode == 7) {
                if(status.equals("error")) {
                    iForumView.toast(msg);
                }else if(status.equals("ok")) {
                    String data = jsonData.getString("data");
                    JSONArray array = new JSONArray(data);
                    for(int i=0; i<array.length(); i++) {
                        JSONObject forum_detail = new JSONObject(array.get(i).toString());
                        //存入myDataset
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("id", forum_detail.getString("id"));
                        item.put("title", forum_detail.getString("title"));
                        item.put("article_count", forum_detail.getString("article_count"));
                        myDataset.add(item);
                    }
                }
                iForumView.setForumList(myDataset);
            }else if(mode == 8) {
                if(status.equals("error")) {
                    iForumView.toast(msg);
                }else if(status.equals("empty")) {
                    iForumView.setNoArticle();
                }else if(status.equals("ok")) {
                    String data = jsonData.getString("data");
                    JSONArray array = new JSONArray(data);
                    for(int i=0; i<array.length(); i++) {
                        JSONObject article_detail = new JSONObject(array.get(i).toString());
                        //存入myDataset
                        Map<String, Object> item = new HashMap<String, Object>();
                        item.put("aid", article_detail.getString("id"));
                        item.put("fid", article_detail.getString("forum_id"));
                        item.put("type", article_detail.getString("type"));
                        item.put("forum", article_detail.getString("forum_title"));
                        item.put("title", article_detail.getString("title"));
                        item.put("content", article_detail.getString("content"));
                        item.put("author", article_detail.getString("member_id"));
                        item.put("response", article_detail.getString("response_count"));
                        myDataset.add(item);
                    }
                }
                iForumView.setArticleList(myDataset);
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iForumView.toast(error);
    }

    @Override
    public void showArticle(int position, int cate) {
        Log.d(TAG, NAME+"showArticle" + ": position = " + position+ "  cate = "+cate);
        iForumModel.getArticle(String.valueOf(position), String.valueOf(cate));
    }
}
