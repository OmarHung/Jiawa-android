package hung.jiawa.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dinuscxj.refresh.RecyclerRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.model.ForumModelCompl;
import hung.jiawa.model.IForumModel;
import hung.jiawa.model.IPersonalFavoritModel;
import hung.jiawa.model.PersonalFavoritModelCompl;
import hung.jiawa.view.IForumView;
import hung.jiawa.view.IPersonalFavoritView;
import hung.jiawa.view.activity.DetailActivity;
import hung.jiawa.view.adapter.ArticleAdapter;
import hung.jiawa.view.adapter.LocaionDetailAdapter;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PersonalFavoritPresenterCompl implements IPersonalFavoritPresenter, AsyncTaskCallBack, ArticleAdapter.OnRecyclerViewItemClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "PersonalFavoritPresenterCompl - ";
    private ArticleAdapter articleAdapter;
    IPersonalFavoritView iPersonalFavoritView;
    IPersonalFavoritModel iPersonalFavoritModel;
    Context context;
    public PersonalFavoritPresenterCompl(Context context, IPersonalFavoritView iPersonalFavoritView) {
        this.iPersonalFavoritView = iPersonalFavoritView;
        this.iPersonalFavoritModel = new PersonalFavoritModelCompl(context, this);
        this.context = context;
    }
    @Override
    public void onResult(int mode, String result) {
        iPersonalFavoritView.dismissLoadingDialog();
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
                        iPersonalFavoritView.toast(msg);
                    }
                }else {
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
            if(myDataset.size()>0) iPersonalFavoritView.setNoFavorit("");
            else iPersonalFavoritView.setNoFavorit(msg);
            showFavorit(myDataset);
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iPersonalFavoritView.toast(error);
    }

    @Override
    public void getFavorit() {
        iPersonalFavoritView.showLoadingDialog();
        iPersonalFavoritModel.getFavorit();
    }
    @Override
    public void showFavorit(List<Map<String, Object>> myDataset) {
        //articleAdapter.clearData();
        articleAdapter.setMyDataset(myDataset);
        articleAdapter.notifyDataSetChanged();
    }

    public void initRecyclerView(RecyclerView recyclerView) {
        articleAdapter = new ArticleAdapter(context);
        articleAdapter.setOnItemClickListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(articleAdapter);
    }

    @Override
    public void onItemClick(String fid, String aid) {
        if(fid.equals("1")) {
            iPersonalFavoritView.startDetailActivity(aid);
        }else {
            //toast("fid = "+fid+" ,  aid = "+aid);
        }
    }
}
