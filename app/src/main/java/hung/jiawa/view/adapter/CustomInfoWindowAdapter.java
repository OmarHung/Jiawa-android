package hung.jiawa.view.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import hung.jiawa.R;

/**
 * Created by omar8 on 2017/5/23.
 */

public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
    private Activity context;
    public CustomInfoWindowAdapter(Activity context){
        this.context = context;
    }
    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }
    @Override
    public View getInfoContents(Marker marker) {
        View view = context.getLayoutInflater().inflate(R.layout.custom_info_window, null);
        ImageView img = (ImageView) view.findViewById(R.id.infowindow_img);
        TextView tvTitle = (TextView) view.findViewById(R.id.infowindow_tv_title);
        TextView tvSubTitle = (TextView) view.findViewById(R.id.infowindow_tv_subtitle);
        //img.setImageBitmap(marker.getTag());
        tvTitle.setText(marker.getTitle());
        tvSubTitle.setText(marker.getSnippet());
        return view;
    }
}