package hung.jiawa.view.adapter;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hung.jiawa.ImageListItem;
import hung.jiawa.LoadingDialog;
import hung.jiawa.R;

/**
 * Created by omar8 on 2017/5/24.
 */

public class LocaionDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final String TAG = "JiaWa";
    public final String NAME = "ImageAdapter - ";
    private Context mContext;
    private List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();
    private boolean isMapReady=false, isDataReady=false;

    public LocaionDetailAdapter(Context mContext) {
        this.mContext = mContext;
    }

    //自定義監聽事件
    public interface OnRecyclerViewItemClickListener {
        void onResponseLocationClick(String aid);
        void onResponseClick(String rid);
        void onLikeClick(String rid);
        void onProfileClick(String mid);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return Integer.valueOf(myDataset.get(position).get("ViewType").toString());
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        //文章
        if(viewType==0){
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_view_location_detail, parent, false);
            final LocationViewHolder holder = new LocationViewHolder(view);
            return holder;
        }
        //回應
        else{
            View view = LayoutInflater.from(mContext).inflate(R.layout.list_view_response, parent, false);
            final ResopnseViewHolder holder = new ResopnseViewHolder(view);
            return holder;
        }
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        if(holder instanceof LocationViewHolder){
            ((LocationViewHolder) holder).mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    Log.d(TAG, NAME+"onMapReady");
                    try {
                        ((LocationViewHolder) holder).mMap = googleMap;
                        String[] latlong = myDataset.get(0).get("latlng").toString().split(",");
                        double latitude = Double.parseDouble(latlong[0]);
                        double longitude = Double.parseDouble(latlong[1]);
                        LatLng location = new LatLng(latitude,longitude);
                        UiSettings mUiSettings = ((LocationViewHolder) holder).mMap.getUiSettings();
                        mUiSettings.setRotateGesturesEnabled(false);
                        mUiSettings.setScrollGesturesEnabled(false);
                        mUiSettings.setTiltGesturesEnabled(false);
                        ((LocationViewHolder) holder).mMap.addMarker(new MarkerOptions().position(location));
                        ((LocationViewHolder) holder).mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f));
                        //((LocationViewHolder) holder).content_layout.setVisibility(View.VISIBLE);
                    }catch (SecurityException e){
                        e.printStackTrace();
                    }
                }
            });
            ((LocationViewHolder) holder).tv_name.setText(myDataset.get(position).get("name").toString());
            ((LocationViewHolder) holder).tv_time.setText(myDataset.get(position).get("time").toString());
            ((LocationViewHolder) holder).tv_forum.setText(myDataset.get(position).get("forum").toString());
            if(!myDataset.get(position).get("profile_img").toString().equals(""))
                ((LocationViewHolder) holder).profile_img.setImageURI(myDataset.get(position).get("profile_img").toString());
            ((LocationViewHolder) holder).tv_title.setText(myDataset.get(position).get("title").toString());
            ((LocationViewHolder) holder).tv_city.setText(myDataset.get(position).get("city").toString());
            ((LocationViewHolder) holder).tv_type.setText(myDataset.get(position).get("type").toString());
            ((LocationViewHolder) holder).tv_machine.setText(myDataset.get(position).get("machine").toString());

            if(!myDataset.get(position).get("images").toString().equals(""))
                ((LocationViewHolder) holder).uriImageAdapter.setAllImages(setImages(myDataset.get(position).get("images").toString()));
            ((LocationViewHolder) holder).tv_content.setText(myDataset.get(position).get("content").toString());
            ((LocationViewHolder) holder).tv_total_like.setText(myDataset.get(position).get("like").toString());
            ((LocationViewHolder) holder).tv_total_response.setText(myDataset.get(position).get("response").toString());
            if(Integer.valueOf(myDataset.get(position).get("response").toString())>0) {
                ((LocationViewHolder) holder).tv_all_response.setText("全部回文");
            }else {
                ((LocationViewHolder) holder).tv_all_response.setText("尚未有回文");
            }
        }else if(holder instanceof ResopnseViewHolder){
            ((ResopnseViewHolder) holder).tv_floor.setText(position+"樓");
            ((ResopnseViewHolder) holder).tv_name.setText(myDataset.get(position).get("name").toString());
            ((ResopnseViewHolder) holder).tv_time.setText(myDataset.get(position).get("time").toString());
            ((ResopnseViewHolder) holder).tv_content.setText(myDataset.get(position).get("content").toString());
            //((MyViewHolder) holder).tv_response.setText(myDataset.get(position).get("response").toString());
            ((ResopnseViewHolder) holder).tv_number_of_like.setText(myDataset.get(position).get("number_of_like").toString());
            if(!myDataset.get(position).get("img").toString().equals(""))
                ((ResopnseViewHolder) holder).profile_img.setImageURI(myDataset.get(position).get("img").toString());
        }
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        //private RelativeLayout content_layout;
        //private LinearLayout detailView;
        private GoogleMap mMap;
        private UriImageAdapter uriImageAdapter;
        private TextView tv_title, tv_content, tv_city, tv_type, tv_machine, tv_total_like, tv_all_response, tv_total_response, btn_response, tv_name, tv_time, tv_forum;
        private SimpleDraweeView profile_img;
        private RecyclerView img_list;
        private MapFragment mapFragment;
        //private EditText edt_content;
        LocationViewHolder(View view) {
            super(view);
            //content_layout = (RelativeLayout) view.findViewById(R.id.content_layout);
            //detailView = (LinearLayout) view.findViewById(R.id.detailView);
            FragmentManager manager = ((Activity) mContext).getFragmentManager();
            mapFragment = (MapFragment) manager.findFragmentById(R.id.mainMap);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_city = (TextView) view.findViewById(R.id.tv_city);
            tv_type = (TextView) view.findViewById(R.id.tv_type);
            tv_machine = (TextView) view.findViewById(R.id.tv_machine);
            tv_total_like = (TextView) view.findViewById(R.id.tv_total_like);
            tv_all_response = (TextView) view.findViewById(R.id.tv_all_response);
            tv_total_response = (TextView) view.findViewById(R.id.tv_total_response);
            btn_response = (TextView) view.findViewById(R.id.btn_response);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_forum = (TextView) view.findViewById(R.id.tv_forum);
            img_list = (RecyclerView) view.findViewById(R.id.img_list);
            img_list.setNestedScrollingEnabled(false);
            profile_img = (SimpleDraweeView) view.findViewById(R.id.profile_img);

            btn_response.setOnClickListener(this);
            btn_response.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onResponseLocationClick(myDataset.get(0).get("aid").toString());
                }
            });
            uriImageAdapter = new UriImageAdapter(mContext);
            uriImageAdapter.setOnItemClickListener(new UriImageAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    showImages(position);
                }
            });
            final LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
            layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            img_list.setLayoutManager(layoutManager);
            img_list.setAdapter(uriImageAdapter);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public class ResopnseViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_floor, tv_content, tv_time, btn_response, tv_number_of_like, tv_name;
        private ImageView check_like;
        private SimpleDraweeView profile_img;
        ResopnseViewHolder(View view) {
            super(view);
            tv_floor = (TextView) view.findViewById(R.id.tv_floor);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            btn_response = (TextView) view.findViewById(R.id.btn_response);
            btn_response.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onResponseClick(myDataset.get(getPosition()).get("rid").toString());
                }
            });
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_number_of_like = (TextView) view.findViewById(R.id.tv_number_of_like);
            check_like = (ImageView) view.findViewById(R.id.check_like);
            check_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onLikeClick(myDataset.get(getPosition()).get("rid").toString());
                }
            });
            profile_img = (SimpleDraweeView) view.findViewById(R.id.profile_img);
            profile_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onProfileClick(myDataset.get(getPosition()).get("mid").toString());
                }
            });
        }
    }

    private List<Uri> setImages(String imgUrl) {
        List<Uri> imgList = new ArrayList<>();
        String[] img = imgUrl.split(",");
        for(int i=0;i<img.length;i++) {
            Log.d(TAG, NAME+"images : " + img[i]+"  i : "+i);
            imgList.add(Uri.parse(img[i]));
        }
        return imgList;
    }

    private void showImages(int position) {
        new ImageViewer.Builder(mContext, setImages(myDataset.get(0).get("images").toString()))
                .setStartPosition(position)
                .show();
    }
    //添加一个item
    public void addItem(Map<String, Object> item) {
        myDataset.add(item);
        notifyDataSetChanged();
    }
    public void clearData() {
        myDataset.clear();
        notifyDataSetChanged();
    }
}
