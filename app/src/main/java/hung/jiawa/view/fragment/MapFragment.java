package hung.jiawa.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import hung.jiawa.CustomProgressDialog;
import hung.jiawa.FragmentCallBack;
import hung.jiawa.ItemDAO;
import hung.jiawa.LoactionItem;
import hung.jiawa.LoadingDialog;
import hung.jiawa.LocationHelper;
import hung.jiawa.MainActivity;
import hung.jiawa.R;
import hung.jiawa.SQLiteHelper;
import hung.jiawa.presenter.IMapPresenter;
import hung.jiawa.presenter.MapPresenterCompl;
import hung.jiawa.view.IMapView;
import hung.jiawa.view.activity.DetailActivity;
import hung.jiawa.view.activity.PostLocationActivity;
import hung.jiawa.view.activity.SavedLocationActivity;
import hung.jiawa.view.adapter.CustomInfoWindowAdapter;

public class MapFragment extends Fragment implements IMapView, LocationHelper.OnLocationCallbacks, OnMapReadyCallback, View.OnClickListener, PopupMenu.OnMenuItemClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnInfoWindowClickListener {
    private GoogleMap mMap;
    private ImageButton btn_save_location, btn_filter;
    private Spinner spinner_city, spinner_type;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private LoadingDialog mLoadingDialog = null;
    private PopupMenu popupmenu;
    private LocationHelper locationHelper=null;
    private Marker myLocationmarker = null;
    private LatLng myLocationLatLng;
    IMapPresenter mapPresenter;
    FragmentCallBack fragmentCallBack;

    public static MapFragment newInstance() {
        MapFragment fragmentFirst = new MapFragment();
        return fragmentFirst;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        fragmentCallBack = (MainActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false); //實體化佈局

        //find view
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mainMap);
        btn_save_location = (ImageButton) view.findViewById(R.id.btn_save_location);
        btn_filter = (ImageButton) view.findViewById(R.id.button_filter);
        spinner_city = (Spinner) view.findViewById(R.id.spinner_city);
        spinner_type = (Spinner) view.findViewById(R.id.spinner_type);
        popupmenu = new PopupMenu(getActivity(), btn_save_location);
        popupmenu.getMenuInflater().inflate(R.menu.menu_map, popupmenu.getMenu());

        //set listener
        btn_save_location.setOnClickListener(this);
        btn_filter.setOnClickListener(this);
        popupmenu.setOnMenuItemClickListener(this);

        //init
        locationHelper = new LocationHelper(getActivity(), this);
        mLoadingDialog = new LoadingDialog(getActivity());
        mapPresenter = new MapPresenterCompl(getActivity(), this, mMap);
        checkPermissions();
        mapFragment.getMapAsync(this);
        ArrayAdapter<CharSequence> cityList = ArrayAdapter.createFromResource(getActivity(),
                R.array.city,
                android.R.layout.simple_spinner_dropdown_item);
        spinner_city.setAdapter(cityList);
        ArrayAdapter<CharSequence> typeList = ArrayAdapter.createFromResource(getActivity(),
                R.array.type,
                android.R.layout.simple_spinner_dropdown_item);
        spinner_type.setAdapter(typeList);
        return view;
    }

    public void load() {
    }
    void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    public void onStart() {
        locationHelper.connect();
        super.onStart();
    }

    public void onStop() {
        locationHelper.disconnect();
        super.onStop();
    }

    public void onDestroyView()
    {
        try {
            Fragment fragment = (getChildFragmentManager().findFragmentById(R.id.mainMap));
            if (fragment != null) {
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.remove(fragment);
                ft.commit();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        super.onDestroyView();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mMap.setMyLocationEnabled(true);
            }
            mMap.setOnMarkerClickListener(this);
            mMap.setOnMarkerDragListener(this);
            mMap.setOnInfoWindowClickListener(this);
            LatLng sydney = new LatLng(23.5160065,120.9812453);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 7f));
            CustomInfoWindowAdapter adapter = new CustomInfoWindowAdapter(getActivity());
            mMap.setInfoWindowAdapter(adapter);
        }catch (SecurityException e){}
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void clearMark() {
        mMap.clear();
    }

    @Override
    public void doFilter(List<Map<String, Object>> latLngTitle, float zoom, LatLng city) {
        for(int i=0; i<latLngTitle.size(); i++) {
            Map<String, Object> data = latLngTitle.get(i);
            String latlng = data.get("latlng").toString();
            String[] latlong = latlng.split(",");
            double latitude = Double.parseDouble(latlong[0]);
            double longitude = Double.parseDouble(latlong[1]);
            LatLng markerLatLng = new LatLng(latitude, longitude);
            String title = data.get("title").toString();
            String id = data.get("id").toString();
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(markerLatLng)
                    .title(title)
                    .snippet("點我觀看詳細"));
            marker.setTag(id);
        }
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(city, zoom),2000,null);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE:
                if (grantResults.length>0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try{
                        mMap.setMyLocationEnabled(true);
                    }catch (SecurityException e) {

                    }
                }else {
                    //ActivityCompat.requestPermissions(MainActivity.this,new String[]{android.Manifest.permission.CALL_PHONE, Manifest.permission.READ_CONTACTS},LOCATION_PERMISSION_REQUEST_CODE);
                }
                return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save_location:
                /*
                TextView latlng = (TextView) getView().findViewById(R.id.latlng);
                TextView zoom = (TextView) getView().findViewById(R.id.zoom);
                latlng.setText(String.valueOf(mMap.getCameraPosition().target));
                zoom.setText(String.valueOf(mMap.getCameraPosition().zoom)); */
                showPopupMenu();
                break;
            case R.id.button_filter:
                mapPresenter.doFilter(spinner_city, spinner_type);
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void showDetail(String tag) {
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("aid", tag);
        intent.putExtras(bundle);
        startActivity(intent);
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
    public void onInfoWindowClick(Marker marker) {
        String id = marker.getTag().toString();
        if(id.equals("")){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("儲存位置");
            final double lat = myLocationLatLng.latitude;
            final double lng = myLocationLatLng.longitude;
            final NumberFormat nf = NumberFormat.getInstance();
            nf.setMaximumFractionDigits(7);    //小數後兩位
            builder.setMessage("lat/lng:("+nf.format(lat)+","+nf.format(lng)+")");
            final LayoutInflater inflater = getActivity().getLayoutInflater();
            final View view = inflater.inflate(R.layout.dialog_view,null);
            builder.setView(view)
                    // Add action buttons
                    .setPositiveButton("儲存", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            // sign in the user ...
                            EditText name = (EditText) view.findViewById(R.id.location_name);
                            String strName = name.getText().toString();
                            if(TextUtils.isEmpty(strName)) toast("請填入座標名稱");
                            else {
                                // 建立資料庫物件
                                ItemDAO itemDAO = new ItemDAO(getActivity());
                                LoactionItem item = itemDAO.insert(new LoactionItem(0, strName, nf.format(lat)+","+nf.format(lng), false));
                                if(item.getId()>0) {
                                    toast("新增成功");
                                    startActivity(new Intent(getActivity(), SavedLocationActivity.class));
                                }else toast("新增失敗");
                            }
                        }
                    })
                    .setNegativeButton("取消", null);
            builder.create();
            builder.show();
        }else {
            showDetail(id);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_save:
                showLoadingDialog();
                locationHelper.getLocation();
                break;
            case R.id.menu_post:
                startActivity(new Intent(getActivity(), PostLocationActivity.class));
                break;
        }
        return false;
    }

    private void showPopupMenu() {
        popupmenu.show();
    }

    @Override
    public void onGetLatLng(double lat, double lng) {
        if(myLocationmarker!=null) {
            myLocationmarker.remove();
            myLocationmarker=null;
        }
        dismissLoadingDialog();
        myLocationLatLng = new LatLng(lat, lng);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocationLatLng, 20f));
        myLocationmarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.pin))
                .position(myLocationLatLng)
                .title("點我儲存此位置")
                .draggable(true)
                .snippet("長按圖釘可變換位置"));
        myLocationmarker.setTag("");
        myLocationmarker.showInfoWindow();
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        myLocationLatLng = marker.getPosition();
    }

}
