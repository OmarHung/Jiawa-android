package hung.jiawa;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by omar8 on 2017/5/29.
 */

public class LocationHelper implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    public final String TAG = "JiaWa";
    public final String NAME = "LocationHelper - ";
    private Context context;
    private boolean getConnected=false;
    private Double latitude,longitude;
    private OnLocationCallbacks onLocationCallbacks = null;
    GoogleApiClient mGoogleApiClient=null;

    //自定義監聽事件
    public interface OnLocationCallbacks {
        void onGetLatLng(double lat, double lng);
    }

    public LocationHelper(Context context, OnLocationCallbacks onLocationCallbacks) {
        this.context = context;
        this.onLocationCallbacks = onLocationCallbacks;
        initGoogleApiClient();
    }
    public void connect(){
        mGoogleApiClient.connect();
    }
    public void disconnect(){
        mGoogleApiClient.disconnect();;
    }
    public void initGoogleApiClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }
    private LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }
    public void getLocation() {
        if(getConnected) {
            LocationManager status = (LocationManager) (context.getSystemService(Context.LOCATION_SERVICE));
            if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                try {
                    LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, createLocationRequest(), this);
                } catch (SecurityException e) {
                }
            } else {
                Toast.makeText(context, "請開啟定位服務", Toast.LENGTH_LONG).show();
                context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));    //開啟設定頁面
            }
        }else {
            Toast.makeText(context, "目前無法定位，請稍後再試", Toast.LENGTH_LONG).show();
        }
    }
    public double getLat() {
        return latitude;
    }
    public double getLng() {
        return longitude;
    }
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Toast.makeText(context, "目前無法定位，請稍後再試", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        latitude = location.getLatitude();	//取得緯度
        longitude = location.getLongitude();	//取得經度
        onLocationCallbacks.onGetLatLng(latitude, longitude);
        stopGetLocation();
    }

    private void stopGetLocation() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
}
