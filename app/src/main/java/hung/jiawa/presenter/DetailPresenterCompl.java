package hung.jiawa.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import hung.jiawa.model.DetailModelCompl;
import hung.jiawa.model.IDetailModel;
import hung.jiawa.view.IDetailView;
import hung.jiawa.view.adapter.ResponseAdapter;
import hung.jiawa.view.adapter.UriImageAdapter;

/**
 * Created by omar8 on 2017/5/22.
 */

public class DetailPresenterCompl implements IDetailPresenter, AsyncTaskCallBack, UriImageAdapter.OnRecyclerViewItemClickListener, ResponseAdapter.OnRecyclerViewItemClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "DetailActivity - ";
    private RecyclerView img_list, response_list;
    private UriImageAdapter uriImageAdapter;
    private ResponseAdapter responseAdapter;
    List<Uri> imgList = new ArrayList<>();
    List<Map<String, Object>> responseList = new ArrayList<Map<String, Object>>();
    IDetailView iDetailView;
    IDetailModel iDetailModel;
    Context context;
    String id;
    public DetailPresenterCompl(Context context, IDetailView iDetailView, String id, RecyclerView img_list, RecyclerView response_list) {
        this.iDetailView = iDetailView;
        this.iDetailModel = new DetailModelCompl(context, this);
        this.context = context;
        this.id = id;
        this.img_list = img_list;
        this.response_list = response_list;
        initRecyclerView();
        iDetailView.showLoadingDialog();
        iDetailModel.getDetail(id);
        iDetailModel.getResponse(id);
    }

    @Override
    public void onResult(int mode, String result) {
        iDetailView.dismissLoadingDialog();
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
                            String machine = jsonData.getString("machines");
                            String like = jsonData.getString("like");
                            String resopne = jsonData.getString("resopne");
                            iDetailView.showDetail(id, title, latlng, city_id, type_id, content, machine, like, resopne);
                            if (img.length() > 1) {
                                setImages(img);
                            }
                        }
                    }else if(mode==11) {
                        if (status.equals("201")) {
                            //文章回覆
                            String mid = jsonData.getString("mid");
                            String rid = jsonData.getString("rid");
                            String time = jsonData.getString("time");
                            String content = jsonData.getString("content");
                            String img = jsonData.getString("img");
                            //String resopne = jsonData.getString("resopne");
                            String like = jsonData.getString("like");
                            Map<String, Object> item = new HashMap<String, Object>();
                            item.put("mid", mid);
                            item.put("rid", rid);
                            item.put("time", time);
                            item.put("content", content);
                            item.put("number_of_like", like);
                            //item.put("resopne", resopne);
                            item.put("img", img);
                            responseList.add(item);
                            //showResponse(mid, time, content, img, resopne, like, floor);
                        }else if(status.equals("401")) {

                        }
                    }
                }
            }
            if(mode==11 && status.equals("201")) {
                responseAdapter.setMyDataset(responseList);
                responseAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iDetailView.toast(error);
        iDetailView.dismissLoadingDialog();
    }

    private void initRecyclerView() {
        uriImageAdapter = new UriImageAdapter(context);
        uriImageAdapter.setOnItemClickListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        img_list.setLayoutManager(layoutManager);
        img_list.setAdapter(uriImageAdapter);

        responseAdapter = new ResponseAdapter(context);
        responseAdapter.setOnItemClickListener(this);
        final LinearLayoutManager responseLayoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        response_list.setLayoutManager(responseLayoutManager);
        response_list.setAdapter(responseAdapter);
    }

    @Override
    public void onItemClick(int position) {
        showImages(position);
    }
    private void setImages(String imgUrl) {
        String[] img = imgUrl.split(",");
        for(int i=0;i<img.length;i++) {
            Log.d(TAG, NAME+"images : " + img[i]+"  i : "+i);
            imgList.add(Uri.parse(img[i]));
        }
        uriImageAdapter.setAllImages(imgList);
    }

    private void showImages(int position) {
        new ImageViewer.Builder(context, imgList)
                .setStartPosition(position)
                .show();
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
}
