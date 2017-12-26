package hung.jiawa.presenter;

import android.net.Uri;
import android.widget.EditText;
import android.widget.Spinner;

import com.jph.takephoto.app.TakePhoto;

import java.io.File;
import java.util.List;

import hung.jiawa.LoactionItem;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IPostLocationPresenter {
    void attemptLogin(EditText ed_title, EditText ed_content, EditText ed_lat, EditText ed_lng, Spinner sp_city, Spinner sp_type, Spinner sp_number_of_machine, List<Uri> img);
    List<LoactionItem> getAll();
    void showLocationDialog();
    void setLatlng(int which);
}
