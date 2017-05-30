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
            String status="", msg="";
            List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                Log.d(TAG, NAME+"lengh = "+jsonArray.length()+" : i = "+i + ": jsonData = " + jsonData.toString());
                if(mode == 7) {
                    if (i == 0) {
                        status = jsonData.getString("status");
                        msg = jsonData.getString("msg");
                        if (status.equals("501")) {
                            iForumView.toast(msg);
                        }
                    } else {
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
                        }
                    }
                }else if(mode == 8) {
                    if (i == 0) {
                        status = jsonData.getString("status");
                        msg = jsonData.getString("msg");
                        if (status.equals("501")) {
                            iForumView.toast(msg);
                        }else if(status.equals("401")) {
                            iForumView.setNoArticle();
                        }
                    } else {
                        if (status.equals("201")) {
                            String aid = jsonData.getString("aid");
                            String fid = jsonData.getString("fid");
                            String forum = jsonData.getString("forum");
                            String aTitle = jsonData.getString("aTitle");
                            String aContent = jsonData.getString("aContent");
                            String aAuthor = jsonData.getString("aAuthor");
                            String response = jsonData.getString("response");
                            //存入myDataset
                            Map<String, Object> item = new HashMap<String, Object>();
                            item.put("aid", aid);
                            item.put("fid", fid);
                            item.put("forum", forum);
                            item.put("title", aTitle);
                            item.put("content", aContent);
                            item.put("author", aAuthor);
                            item.put("response", response);
                            myDataset.add(item);
                        }
                    }
                }
            }
            if(mode == 7) {
                iForumView.setForumList(myDataset);
                //iForumModel.getArticle("0");
            }else if(mode == 8) {
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
