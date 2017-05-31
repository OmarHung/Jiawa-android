package hung.jiawa.presenter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;

import com.stfalcon.frescoimageviewer.ImageViewer;

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
import hung.jiawa.PreferenceHelper;
import hung.jiawa.R;
import hung.jiawa.model.DetailModelCompl;
import hung.jiawa.model.IDetailModel;
import hung.jiawa.view.IDetailView;
import hung.jiawa.view.adapter.LocaionDetailAdapter;
import hung.jiawa.view.adapter.ResponseAdapter;
import hung.jiawa.view.adapter.UriImageAdapter;

import static hung.jiawa.PreferenceHelper.with;

/**
 * Created by omar8 on 2017/5/22.
 */

public class DetailPresenterCompl implements IDetailPresenter, AsyncTaskCallBack, LocaionDetailAdapter.OnRecyclerViewItemClickListener {
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
        mid = settings.getString("mid","");
        initRecyclerView();
    }

    @Override
    public void onResult(int mode, String result) {
        iDetailView.dismissLoadingDialog();
        List<Map<String, Object>> locationDetialList = new ArrayList<Map<String, Object>>();
        Resources res = context.getResources();
        String[] city=res.getStringArray(R.array.post_city);
        String[] type=res.getStringArray(R.array.post_type);
        String[] machine=res.getStringArray(R.array.post_machine);
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        try {
            String status="", msg="";
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                if(i==0) {
                    status = jsonData.getString("status");
                    msg = jsonData.getString("msg");
                    if (status.equals("501")) {
                        iDetailView.toast(msg);
                    }
                    if (status.equals("201")) {
                        if(mode==13) {
                            //發表回覆
                            isResponsed = true;
                            iDetailView.toast(msg);
                            iDetailView.dismissResponseDialog();
                        }
                    }
                }else {
                    if(mode==6) {
                        if (status.equals("201")) {
                            String id = jsonData.getString("id");
                            String title = jsonData.getString("title");
                            String content = jsonData.getString("content");
                            String img = jsonData.getString("img");
                            String latlng = jsonData.getString("latlng");
                            String city_id = jsonData.getString("city_id");
                            String type_id = jsonData.getString("type_id");
                            String machine_id = jsonData.getString("machines");
                            String like = jsonData.getString("like");
                            String response = jsonData.getString("response");
                            String name = jsonData.getString("name");
                            String profile_img = jsonData.getString("profile_img");
                            String time = jsonData.getString("time");
                            //iDetailView.showDetail(id, title, latlng, city_id, type_id, content, machine, like, resopne, name, time, profile_img);
                            Map<String, Object> item = new HashMap<String, Object>();
                            item.put("aid", id);
                            item.put("title", title);
                            item.put("name", name);
                            item.put("forum", "夾點分享");
                            item.put("time", time);
                            item.put("content", content);
                            item.put("profile_img", profile_img);
                            item.put("city", city[Integer.valueOf(city_id)]);
                            item.put("type", type[Integer.valueOf(type_id)]);
                            item.put("machine", machine[Integer.valueOf(machine_id)]);
                            //item.put("resopne", resopne);
                            item.put("latlng", latlng);
                            item.put("images", img);
                            item.put("like", like);
                            item.put("response", response);
                            item.put("ViewType",0);
                            iDetailView.setToolBarTitle(title);
                            iDetailView.setAid(id);
                            locaionDetailAdapter.addItem(item);
                            iDetailModel.getResponse(id);
                        }
                    }else if(mode==11) {
                        if (status.equals("201")) {
                            //文章回覆
                            String mid = jsonData.getString("mid");
                            String name = jsonData.getString("name");
                            String rid = jsonData.getString("rid");
                            String time = jsonData.getString("time");
                            String content = jsonData.getString("content");
                            String img = jsonData.getString("img");
                            String like = jsonData.getString("like");
                            Map<String, Object> item = new HashMap<String, Object>();
                            item.put("ViewType",1);
                            item.put("mid", mid);
                            item.put("name", name);
                            item.put("rid", rid);
                            item.put("time", time);
                            item.put("content", content);
                            item.put("number_of_like", like);
                            item.put("img", img);
                            locaionDetailAdapter.addItem(item);
                        }else if(status.equals("401")) {

                        }
                    }else if(mode==12) {
                        if (status.equals("201")) {
                            //個人資訊
                            String name = jsonData.getString("name");
                            String email = jsonData.getString("email");
                            String img = jsonData.getString("img");
                            iDetailModel.setProfileToPre(name, email, img);
                        }
                    }
                }
            }
            if(mode==11 && isResponsed) {
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
    public void onResponseLocationClick(String aid) {
        iDetailView.showResponseDialog(locaionDetailAdapter.getItemCount());
    }

    @Override
    public void onResponseClick(String rid) {
        Log.d(TAG, NAME+"onResponseClick  : " + rid);
    }

    @Override
    public void onLikeClick(String rid) {
        Log.d(TAG, NAME+"onLikeClick  : " + rid);
    }

    @Override
    public void onProfileClick(String mid) {
        Log.d(TAG, NAME+"onProfileClick  : " + mid);
    }

    public void onResume() {
        iDetailView.showLoadingDialog();
        locaionDetailAdapter.clearData();
        iDetailModel.getDetail(id);
        iDetailModel.getProfile(mid);
    }

    @Override
    public void postResponse(String aid, String content) {
        if(TextUtils.isEmpty(content)) {
            iDetailView.toast("未填寫回文");
        }else {
            iDetailView.showLoadingDialog();
            iDetailModel.postResponse(aid, content);
        }
    }

    @Override
    public String getProfileImage() {
        return iDetailModel.getProfileImage();
    }
}
