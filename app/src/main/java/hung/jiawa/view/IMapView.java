package hung.jiawa.view;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by omar8 on 2017/5/23.
 */

public interface IMapView {
    void toast(String msg);
    void clearMark();
    void doFilter(List<Map<String, Object>> latLngTitle, float zoom, LatLng city);
    void showDetail(String tag);
    void showLoadingDialog();
    void dismissLoadingDialog();
}
