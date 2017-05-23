package hung.jiawa.presenter;

import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.maps.model.Marker;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IMapPresenter {
    void doFilter(Spinner city, Spinner type);
}
