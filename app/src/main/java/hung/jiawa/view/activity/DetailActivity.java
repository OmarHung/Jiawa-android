package hung.jiawa.view.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.NumberFormat;

import de.hdodenhof.circleimageview.CircleImageView;
import hung.jiawa.ItemDAO;
import hung.jiawa.LoactionItem;
import hung.jiawa.LoadingDialog;
import hung.jiawa.R;
import hung.jiawa.presenter.DetailPresenterCompl;
import hung.jiawa.presenter.IDetailPresenter;
import hung.jiawa.view.IDetailView;

public class DetailActivity extends AppCompatActivity implements IDetailView, View.OnClickListener, OnMapReadyCallback {
    public final String TAG = "JiaWa";
    public final String NAME = "DetailActivity - ";
    private String latlng;
    private RelativeLayout content_layout;
    private LinearLayout detailView;
    private GoogleMap mMap;
    private ImageButton btn_more;
    private TextView tv_toolTitle, tv_title, tv_content, tv_city, tv_type, tv_machine, btn_back, tv_total_like, tv_all_response, tv_total_response, btn_response, tv_name, tv_time;
    private SimpleDraweeView profile_img;
    private RecyclerView img_list, response_list;
    private LoadingDialog mLoadingDialog = null;
    private MapFragment mapFragment;
    private EditText edt_content;
    private String lid, strContent="", nowFloor, aid;
    private AlertDialog alertDialog = null;
    private NestedScrollView scrollView;
    IDetailPresenter detailPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        lid = bundle.getString("lid");

        scrollView = (NestedScrollView) findViewById(R.id.scrollView);
        content_layout = (RelativeLayout) findViewById(R.id.content_layout);
        detailView = (LinearLayout) findViewById(R.id.detailView);
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mainMap);
        tv_toolTitle = (TextView) findViewById(R.id.tv_toolTitle);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_machine = (TextView) findViewById(R.id.tv_machine);
        btn_back = (TextView) findViewById(R.id.btn_back);
        tv_total_like = (TextView) findViewById(R.id.tv_total_like);
        tv_all_response = (TextView) findViewById(R.id.tv_all_response);
        tv_total_response = (TextView) findViewById(R.id.tv_total_response);
        btn_response = (TextView) findViewById(R.id.btn_response);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_time = (TextView) findViewById(R.id.tv_time);
        btn_more = (ImageButton) findViewById(R.id.btn_more);
        img_list = (RecyclerView) findViewById(R.id.img_list);
        img_list.setNestedScrollingEnabled(false);
        response_list = (RecyclerView) findViewById(R.id.response_list);
        response_list.setNestedScrollingEnabled(false);
        response_list.setFocusable(false);
        profile_img = (SimpleDraweeView) findViewById(R.id.profile_img);

        btn_back.setOnClickListener(this);
        btn_more.setOnClickListener(this);
        btn_response.setOnClickListener(this);

        mLoadingDialog = new LoadingDialog(this);
        detailPresenter = new DetailPresenterCompl(this, this, lid, img_list, response_list);
    }
    @Override
    protected void onResume() {
        super.onResume();
        detailPresenter.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_more:
                break;
            case R.id.btn_response:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                final LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.response_dialog_view,null);
                builder.setView(view);
                alertDialog = builder.create();
                SimpleDraweeView profile_img = (SimpleDraweeView) view.findViewById(R.id.profile_img);
                TextView tv_floor = (TextView) view.findViewById(R.id.tv_floor);
                TextView btn_send = (TextView) view.findViewById(R.id.btn_send);
                edt_content = (EditText) view.findViewById(R.id.edt_content);
                edt_content.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                        //Log.d(TAG,NAME+"beforeTextChanged : "+s);
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        //Log.d(TAG,NAME+"onTextChanged : "+s);
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        strContent = edt_content.getText().toString();
                    }
                });
                edt_content.setText(strContent);
                String uri = detailPresenter.getProfileImage();
                if(!uri.equals("")) profile_img.setImageURI(uri);
                tv_floor.setText(Integer.valueOf(nowFloor)+1+"樓");
                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        strContent = edt_content.getText().toString();
                        detailPresenter.postResponse(aid, strContent);
                    }
                });
                alertDialog.show();
                break;
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDetail(String id, String title, String latlng, String city_id, String type_id, String content, String machine_id, String like, String response, String name, String time, String img) {
        Log.d(TAG,NAME+"showDetail");
        aid = id;
        nowFloor = response;
        this.latlng = latlng;
        tv_title.setText(title);
        tv_toolTitle.setText(title);
        tv_total_like.setText(like);
        tv_total_response.setText(response);
        tv_name.setText(name);
        tv_time.setText(time);
        if(Integer.valueOf(response)<1) tv_all_response.setText("尚未有回文");
        if(!img.equals("")) profile_img.setImageURI(img);
        Resources res =getResources();
        String[] city=res.getStringArray(R.array.post_city);
        String[] type=res.getStringArray(R.array.post_type);
        String[] machine=res.getStringArray(R.array.post_machine);
        tv_city.setText(city[Integer.valueOf(city_id)]);
        tv_type.setText(type[Integer.valueOf(type_id)]);
        tv_machine.setText(machine[Integer.valueOf(machine_id)]);
        tv_content.setText(content);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void showLoadingDialog() {
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void dismissResponseDialog() {
        if(alertDialog!=null && alertDialog.isShowing()) alertDialog.dismiss();
        strContent="";
        detailPresenter.onResume();
    }

    @Override
    public void scrollToBottom() {
        scrollView.smoothScrollTo(0, detailView.getBottom());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG,NAME+"onMapReady");
        try {
            mMap = googleMap;
            String[] latlong = latlng.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            LatLng location = new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f));
            content_layout.setVisibility(View.VISIBLE);
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }
    //private AlertDialog getResponseDialog() {

        //builder.show();
    //}
}
