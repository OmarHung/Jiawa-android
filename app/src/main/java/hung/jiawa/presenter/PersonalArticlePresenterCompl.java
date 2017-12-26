package hung.jiawa.presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.model.IPersonalArticleModel;
import hung.jiawa.model.IPersonalKeepModel;
import hung.jiawa.model.PersonalArticleModelCompl;
import hung.jiawa.model.PersonalKeepModelCompl;
import hung.jiawa.view.IPersonalArticleView;
import hung.jiawa.view.IPersonalKeepView;
import hung.jiawa.view.adapter.ArticleAdapter;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PersonalArticlePresenterCompl implements IPersonalArticlePresenter, AsyncTaskCallBack, ArticleAdapter.OnRecyclerViewItemClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "PersonalKeepPresenterCompl - ";
    private ArticleAdapter articleAdapter;
    IPersonalArticleView iPersonalArticleView;
    IPersonalArticleModel iPersonalArticleModel;
    Context context;
    public PersonalArticlePresenterCompl(Context context, IPersonalArticleView iPersonalArticleView) {
        this.iPersonalArticleView = iPersonalArticleView;
        this.iPersonalArticleModel = new PersonalArticleModelCompl(context, this);
        this.context = context;
    }
    @Override
    public void onResult(int mode, String result) {
        iPersonalArticleView.dismissLoadingDialog();
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        try {
            List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();
            JSONObject jsonData = new JSONObject(result);
            String status = jsonData.getString("status");
            String  msg = jsonData.getString("msg");
            if (status.equals("error") || status.equals("empty")) {
                iPersonalArticleView.toast(msg);
            }else if (status.equals("ok")) {
                String data = jsonData.getString("data");
                JSONArray array = new JSONArray(data);
                for(int i=0; i<array.length(); i++) {
                    JSONObject article_detail = new JSONObject(array.get(i).toString());
                    //存入myDataset
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("aid", article_detail.getString("id"));
                    item.put("fid", article_detail.getString("forum_id"));
                    item.put("forum", article_detail.getString("forum_title"));
                    item.put("title", article_detail.getString("title"));
                    item.put("content", article_detail.getString("content"));
                    item.put("author", article_detail.getString("member_id"));
                    item.put("response", article_detail.getString("response_count"));
                    item.put("article_status", article_detail.getString("status"));
                    myDataset.add(item);
                }
            }
            if(myDataset.size()>0) iPersonalArticleView.setNoArticle("");
            else iPersonalArticleView.setNoArticle(msg);
            showArticle(myDataset);
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iPersonalArticleView.toast(error);
    }

    @Override
    public void getArticle() {
        iPersonalArticleView.showLoadingDialog();
        iPersonalArticleModel.getArticle();
    }
    @Override
    public void showArticle(List<Map<String, Object>> myDataset) {
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
        if(fid.equals("2")) {
            iPersonalArticleView.startDetailActivity(aid);
        }else {
            //toast("fid = "+fid+" ,  aid = "+aid);
        }
    }
}
