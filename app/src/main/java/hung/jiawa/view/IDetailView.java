package hung.jiawa.view;

import android.graphics.Bitmap;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IDetailView {
    void toast(String msg);
    void showDetail(String id, String title, String latlng, String city_id, String type_id, String content, String machine_id, String like);
    void showImage(String imgUrl);
    void showLoadingDialog();
    void dismissLoadingDialog();
}
