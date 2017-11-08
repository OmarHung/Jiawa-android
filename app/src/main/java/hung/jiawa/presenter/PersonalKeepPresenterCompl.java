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
import hung.jiawa.model.IPersonalKeepModel;
import hung.jiawa.model.PersonalKeepModelCompl;
import hung.jiawa.view.IPersonalKeepView;
import hung.jiawa.view.adapter.ArticleAdapter;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PersonalKeepPresenterCompl implements IPersonalKeepPresenter, AsyncTaskCallBack, ArticleAdapter.OnRecyclerViewItemClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "PersonalKeepPresenterCompl - ";
    private ArticleAdapter articleAdapter;
    IPersonalKeepView iPersonalKeepView;
    IPersonalKeepModel iPersonalKeepModel;
    Context context;
    public PersonalKeepPresenterCompl(Context context, IPersonalKeepView iPersonalKeepView) {
        this.iPersonalKeepView = iPersonalKeepView;
        this.iPersonalKeepModel = new PersonalKeepModelCompl(context, this);
        this.context = context;
    }
    @Override
    public void onResult(int mode, String result) {
        iPersonalKeepView.dismissLoadingDialog();
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        try {
            List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();
            JSONObject jsonData = new JSONObject(result);
            String status = jsonData.getString("status");
            String  msg = jsonData.getString("msg");
            if (status.equals("error") || status.equals("empty")) {
                iPersonalKeepView.toast(msg);
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
                    myDataset.add(item);
                }
            }
            if(myDataset.size()>0) iPersonalKeepView.setNoFavorit("");
            else iPersonalKeepView.setNoFavorit(msg);
            showFavorit(myDataset);
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iPersonalKeepView.toast(error);
    }

    @Override
    public void getFavorit() {
        iPersonalKeepView.showLoadingDialog();
        iPersonalKeepModel.getFavorit();
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
        if(fid.equals("2")) {
            iPersonalKeepView.startDetailActivity(aid);
        }else {
            //toast("fid = "+fid+" ,  aid = "+aid);
        }
    }
}
