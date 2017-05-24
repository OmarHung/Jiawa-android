package hung.jiawa.presenter;

import android.content.Context;
import android.util.Log;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.R;
import hung.jiawa.model.ForumModelCompl;
import hung.jiawa.model.IForumModel;
import hung.jiawa.model.IPostArticleModel;
import hung.jiawa.model.PostArticleModelCompl;
import hung.jiawa.view.IForumView;
import hung.jiawa.view.IPostArticleView;
import hung.jiawa.widget.XCDropDownListView;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PostArticlePresenterCompl implements IPostArticlePresenter, AsyncTaskCallBack {
    public final String TAG = "JiaWa";
    public final String NAME = "PostArticleActivity - ";
    IPostArticleView iPostArticleView;
    IPostArticleModel iPostArticleModel;
    Context context;
    public PostArticlePresenterCompl(Context context, IPostArticleView iPostArticleView) {
        this.iPostArticleView = iPostArticleView;
        this.iPostArticleModel = new PostArticleModelCompl(context, this);
        this.context = context;
        //取得論壇板清單用以顯示spinner
        iPostArticleModel.getForum();
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
                if (i == 0) {
                    status = jsonData.getString("status");
                    msg = jsonData.getString("msg");
                    if (status.equals("501")) {
                        iPostArticleView.toast(msg);
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
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iPostArticleView.toast(error);
    }
}
