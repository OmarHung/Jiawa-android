package hung.jiawa.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.R;
import hung.jiawa.model.IMapModel;
import hung.jiawa.model.MapModelCompl;
import hung.jiawa.view.IMapView;

/**
 * Created by omar8 on 2017/5/22.
 */

public class MapPresenterCompl implements IMapPresenter, AsyncTaskCallBack {
    public final String TAG = "JiaWa";
    public final String NAME = "MapFragment - ";
    IMapView iMapView;
    IMapModel iMapModel;
    Context context;
    GoogleMap mMap;
    public MapPresenterCompl(Context context, IMapView iMapView, GoogleMap mMap) {
        this.iMapView = iMapView;
        this.iMapModel = new MapModelCompl(context, this);
        this.context = context;
        this.mMap = mMap;
        iMapView.showLoadingDialog();
        iMapModel.getMarker(0,0);
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        iMapView.dismissLoadingDialog();
        try {
            iMapView.clearMark();
            List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();
            JSONObject jsonData = new JSONObject(result);
            String status = jsonData.getString("status");
            String msg = jsonData.getString("msg");
            if(status.equals("ok")) {
                String data = jsonData.getString("data");
                JSONArray array = new JSONArray(data);
                for(int i=0; i<array.length(); i++) {
                    JSONObject spot_detail = new JSONObject(array.get(i).toString());
                    //存入myDataset
                    Map<String, Object> item = new HashMap<String, Object>();
                    item.put("id", spot_detail.getString("id"));
                    item.put("title", spot_detail.getString("title"));
                    item.put("latlng", spot_detail.getString("latlng"));
                    myDataset.add(item);
                }

                String Zoom = jsonData.getString("zoom");
                String LatLng = jsonData.getString("latlng");
                //字串轉LatLng
                String[] latlong = LatLng.split(",");
                double latitude = Double.parseDouble(latlong[0]);
                double longitude = Double.parseDouble(latlong[1]);
                LatLng cityLatLng = new LatLng(latitude, longitude);
                iMapView.clearMark();
                iMapView.doFilter(myDataset, Float.valueOf(Zoom), cityLatLng);
            }else if(status.equals("empty")) {
                iMapView.toast(msg);
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iMapView.dismissLoadingDialog();
        iMapView.toast(error);
    }

    @Override
    public void doFilter(Spinner city, Spinner type) {
        iMapView.showLoadingDialog();
        int cityPosition = city.getSelectedItemPosition();
        int typePosition = type.getSelectedItemPosition();
        Log.d(TAG, NAME+"city = "+city.getSelectedItemPosition() + ": type = " + type.getSelectedItemPosition());
        iMapModel.getMarker(cityPosition, typePosition);
        /*
        iMapView.clearMark();
        List<Map<String, Object>> myDataset = new ArrayList<Map<String,Object>>();;
        switch (city.getSelectedItemPosition()) {
            case 0:
                iMapModel.getData(cityPosition, typePosition);
                LatLng cityLatLng = new LatLng(23.5160065,120.9812453);
                Map<String, Object> item = new HashMap<String, Object>();
                item.put("v", 23.5160065);
                item.put("v1", 120.9812453);
                item.put("title", "臺灣");
                myDataset.add(item);
                iMapView.doFilter(myDataset, 7f, cityLatLng);
                break;
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
            case 7:
                break;
            case 8:
                break;
            case 9:
                break;
            case 10:
                break;
            case 11:
                break;
            case 12:
                break;
            case 13:
                break;
            case 14:
                break;
            case 15:
                break;
            case 16:
                break;
            case 17:
                break;
            case 18:
                break;
            case 19:
                break;
            case 20:
                break;
            case 21:
                break;
        }
                */
    }
}
