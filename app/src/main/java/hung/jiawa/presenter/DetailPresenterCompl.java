package hung.jiawa.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.PreferenceHelper;
import hung.jiawa.R;
import hung.jiawa.model.DetailModelCompl;
import hung.jiawa.model.IDetailModel;
import hung.jiawa.view.IDetailView;
import hung.jiawa.view.adapter.LocaionDetailAdapter;

import static hung.jiawa.PreferenceHelper.with;

/**
 * Created by omar8 on 2017/5/22.
 */

public class DetailPresenterCompl implements IDetailPresenter, AsyncTaskCallBack, LocaionDetailAdapter.OnRecyclerViewItemClickListener{
    public final String TAG = "JiaWa";
    public final String NAME = "DetailActivity - ";
    private RecyclerView recyclerView;
    private LocaionDetailAdapter locaionDetailAdapter;
    private boolean isResponsed=false;
    PreferenceHelper settings;
    IDetailView iDetailView;
    IDetailModel iDetailModel;
    Context context;
    String id, mid;
    public DetailPresenterCompl(Context context, IDetailView iDetailView, String id, RecyclerView recyclerView) {
        this.iDetailView = iDetailView;
        this.iDetailModel = new DetailModelCompl(context, this);
        this.context = context;
        this.id = id;
        this.recyclerView = recyclerView;
        settings = with(context);
        mid = settings.getString("id","");
        initRecyclerView();

        //載入文章
        this.iDetailView.showLoadingDialog();
        locaionDetailAdapter.clearData();
        iDetailModel.getDetail(this.id);
        iDetailModel.getProfile(mid);
    }

    @Override
    public void onResult(int mode, String result) {
        iDetailView.dismissLoadingDialog();
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        try {
            JSONObject jsonData = new JSONObject(result);
            String status = jsonData.getString("status");
            String msg = jsonData.getString("msg");
            if(mode==6) {
                //文章內容
                if(status.equals("error")) {
                    iDetailView.toast(msg);
                }else if(status.equals("ok")) {
                    Resources res = context.getResources();
                    String[] city=res.getStringArray(R.array.post_city);
                    String[] type=res.getStringArray(R.array.post_type);
                    String[] machine=res.getStringArray(R.array.post_machine);
                    String detail = jsonData.getString("detail");
                    JSONObject spot_detail = new JSONObject(detail);
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("group", "normal");
                    item.put("aid", spot_detail.getString("id"));
                    item.put("title", spot_detail.getString("title"));
                    item.put("name", spot_detail.getString("name"));
                    item.put("forum", "夾點分享");
                    item.put("time", spot_detail.getString("date_add"));
                    item.put("content", spot_detail.getString("content"));
                    item.put("profile_img", spot_detail.getString("profile_img"));
                    item.put("city", city[Integer.valueOf(spot_detail.getString("city_id"))]);
                    item.put("type", type[Integer.valueOf(spot_detail.getString("type_id"))]);
                    item.put("machine", machine[Integer.valueOf(spot_detail.getString("unit"))]);
                    item.put("latlng", spot_detail.getString("latlng"));
                    item.put("images", spot_detail.getString("img"));
                    item.put("like", spot_detail.getString("member_favorite"));
                    item.put("keep", spot_detail.getString("member_keep"));
                    item.put("like_total", spot_detail.getString("favorite_count"));
                    item.put("response", spot_detail.getString("response_count"));
                    item.put("ViewType",0);
                    item.put("count", "0");
                    iDetailView.setToolBarTitle(spot_detail.getString("title"));
                    iDetailView.setAid(id);
                    locaionDetailAdapter.addItem(item);
                    iDetailModel.getResponse(id);
                }
            }else if(mode==11) {
                //留言列表
                if(status.equals("error")) {
                    iDetailView.toast(msg);
                }else if(status.equals("ok")) {
                    String data = jsonData.getString("data");
                    JSONArray array = new JSONArray(data);
                    for(int i=0; i<array.length(); i++) {
                        JSONObject response_detail = new JSONObject(array.get(i).toString());
                        Map<String, Object> item = new HashMap<String, Object>();
                        if(i==0) {
                            item.put("first", 1);
                        }else {
                            item.put("first", 0);
                        }
                        item.put("ViewType", 1);
                        item.put("group", "normal");
                        item.put("mid", response_detail.getString("member_id"));
                        item.put("name", response_detail.getString("name"));
                        item.put("rid", response_detail.getString("id"));
                        item.put("time", response_detail.getString("date_add"));
                        item.put("content", response_detail.getString("content"));
                        item.put("img", response_detail.getString("img"));
                        locaionDetailAdapter.addItem(item);
                    }
                }
            }else if(mode==13) {
                //發表留言
                if(status.equals("error")) {
                    iDetailView.toast(msg);
                }else if(status.equals("ok")) {
                    isResponsed = true;
                    String detail = jsonData.getString("detail");
                    JSONObject response_detail = new JSONObject(detail);
                    Map<String, Object> item = new HashMap<String, Object>();
                    if(locaionDetailAdapter.getItemCount()==0) {
                        item.put("first", 1);
                    }else {
                        item.put("first", 0);
                    }
                    item.put("ViewType", 1);
                    item.put("group", "normal");
                    item.put("mid", response_detail.getString("member_id"));
                    item.put("name", response_detail.getString("name"));
                    item.put("rid", response_detail.getString("id"));
                    item.put("time", response_detail.getString("date_add"));
                    item.put("content", response_detail.getString("content"));
                    item.put("img", response_detail.getString("img"));
                    locaionDetailAdapter.addItem(item);
                    //文章留言數+1
                    locaionDetailAdapter.addResponseCount();
                    iDetailView.toast(msg);
                    iDetailView.dismissResponseDialog();
                }
            }else if(mode==24) {
                //發表回覆
                if(status.equals("error")) {
                    iDetailView.toast(msg);
                }else if(status.equals("ok")) {
                    isResponsed = true;
                    iDetailView.toast(msg);
                    iDetailView.dismissResponseDialog();
                }
            }
            if(mode==13 && isResponsed) {
                Log.d(TAG, NAME+"isResponsed");
                isResponsed=false;
                recyclerView.smoothScrollToPosition(locaionDetailAdapter.getItemCount()-1);
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iDetailView.toast(error);
        iDetailView.dismissLoadingDialog();
    }

    private void initRecyclerView() {
        locaionDetailAdapter = new LocaionDetailAdapter(context);
        locaionDetailAdapter.setOnItemClickListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(locaionDetailAdapter);
    }

    @Override
    public void onCommentClick(String aid) {
        Log.d(TAG, NAME+"onResponseArticleClick  : " + aid);
        iDetailView.showResponseDialog("a","0",0);
    }
    @Override
    public void onResponseClick(String rid, int position) {
        Log.d(TAG, NAME+"onResponseResponseClick  : " + rid+"  position="+position);
        iDetailView.showResponseDialog("r", rid, position);
    }

    @Override
    public void onLikeArticleClick(String aid, String now) {
        Log.d(TAG, NAME+"onLikeArticleClick  : " + aid);
        iDetailModel.checkArticleLike(aid);
    }

    @Override
    public void onKeepArticleClick(String aid, String now) {
        iDetailModel.checkArticleKeep(aid);
    }


    @Override
    public void onProfileClick(String mid) {
        Log.d(TAG, NAME+"onProfileClick  : " + mid);
    }


    public void onResume() {
        Log.d(TAG, NAME+"onResume");
    }

    @Override
    public void postResponse(String type, String id, String content) {
        if(TextUtils.isEmpty(content)) {
            iDetailView.toast("未填寫內容");
        }else {
            iDetailView.showLoadingDialog();
            iDetailModel.postResponse(type, id, content);
        }
    }

    @Override
    public String getProfileImage() {
        return iDetailModel.getProfileImage();
    }
}
