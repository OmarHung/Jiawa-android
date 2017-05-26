package hung.jiawa.view.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
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
import hung.jiawa.view.adapter.UriImageAdapter;

public class DetailActivity extends AppCompatActivity implements IDetailView, View.OnClickListener, UriImageAdapter.OnRecyclerViewItemClickListener, OnMapReadyCallback {
    public final String TAG = "JiaWa";
    public final String NAME = "DetailActivity - ";
    private String latlng;
    private GoogleMap mMap;
    private ImageButton btn_more;
    private TextView tv_toolTitle, tv_title, tv_content, tv_city, tv_type, tv_machine, btn_back;
    private RecyclerView img_list;
    IDetailPresenter detailPresenter;
    private UriImageAdapter uriImageAdapter;
    private LoadingDialog mLoadingDialog = null;
    private MapFragment mapFragment;
    List<Uri> imgList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        String lid = bundle.getString("lid");
        mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mainMap);
        tv_toolTitle = (TextView) findViewById(R.id.tv_toolTitle);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_content = (TextView) findViewById(R.id.tv_content);
        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_machine = (TextView) findViewById(R.id.tv_machine);
        btn_back = (TextView) findViewById(R.id.btn_back);
        btn_more = (ImageButton) findViewById(R.id.btn_more);
        img_list = (RecyclerView) findViewById(R.id.img_list);

        btn_back.setOnClickListener(this);
        btn_more.setOnClickListener(this);

        mLoadingDialog = new LoadingDialog(this);
        initRecyclerView();
        detailPresenter = new DetailPresenterCompl(this, this, lid);
    }

    private void initRecyclerView() {
        uriImageAdapter = new UriImageAdapter(this);
        uriImageAdapter.setOnItemClickListener(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        img_list.setLayoutManager(layoutManager);
        img_list.setAdapter(uriImageAdapter);
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
    public void showDetail(String id, String title, String latlng, String city_id, String type_id, String content, String machine_id, String like) {
        this.latlng = latlng;
        tv_title.setText(title);
        tv_toolTitle.setText(title);
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
    public void showImage(String imgUrl) {
        String[] img = imgUrl.split(",");
        for(int i=0;i<img.length;i++) {
            Log.d(TAG, NAME+"images : " + img[i]+"  i : "+i);
            imgList.add(Uri.parse(img[i]));
        }
        uriImageAdapter.setAllImages(imgList);
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
    public void onItemClick(int position) {
        //檢視相片
        new ImageViewer.Builder(this, imgList)
                .setStartPosition(position)
                .show();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            String[] latlong = latlng.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            LatLng location = new LatLng(latitude,longitude);
            mMap.addMarker(new MarkerOptions()
                    .position(location));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 18f));
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }
}
