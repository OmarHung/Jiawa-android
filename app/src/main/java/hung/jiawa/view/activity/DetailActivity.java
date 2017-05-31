package hung.jiawa.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.renderscript.Long3;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import hung.jiawa.ImageListItem;
import hung.jiawa.LoadingDialog;
import hung.jiawa.R;
import hung.jiawa.presenter.DetailPresenterCompl;
import hung.jiawa.presenter.IDetailPresenter;
import hung.jiawa.view.IDetailView;
import hung.jiawa.view.adapter.CustomInfoWindowAdapter;
import hung.jiawa.view.adapter.ImageAdapter;
import hung.jiawa.view.adapter.ResponseAdapter;
import hung.jiawa.view.adapter.UriImageAdapter;

public class DetailActivity extends AppCompatActivity implements IDetailView, View.OnClickListener, OnMapReadyCallback {
    public final String TAG = "JiaWa";
    public final String NAME = "DetailActivity - ";
    private String latlng;
    private RelativeLayout content_layout;
    private GoogleMap mMap;
    private ImageButton btn_more;
    private TextView tv_toolTitle, tv_title, tv_content, tv_city, tv_type, tv_machine, btn_back, tv_total_like, tv_all_response, tv_total_response;
    private RecyclerView img_list, response_list;
    private LoadingDialog mLoadingDialog = null;
    private MapFragment mapFragment;
    IDetailPresenter detailPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        String lid = bundle.getString("lid");

        content_layout = (RelativeLayout) findViewById(R.id.content_layout);
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
        btn_more = (ImageButton) findViewById(R.id.btn_more);
        img_list = (RecyclerView) findViewById(R.id.img_list);
        response_list = (RecyclerView) findViewById(R.id.response_list);

        btn_back.setOnClickListener(this);
        btn_more.setOnClickListener(this);

        mLoadingDialog = new LoadingDialog(this);
        detailPresenter = new DetailPresenterCompl(this, this, lid, img_list, response_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_more:
                break;
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDetail(String id, String title, String latlng, String city_id, String type_id, String content, String machine_id, String like, String response) {
        Log.d(TAG,NAME+"showDetail");
        this.latlng = latlng;
        tv_title.setText(title);
        tv_toolTitle.setText(title);
        tv_total_like.setText(like);
        tv_total_response.setText(response);
        if(Integer.valueOf(response)<1) tv_all_response.setText("尚未有回文");
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
}
