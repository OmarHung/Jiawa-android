package hung.jiawa.view;

import android.graphics.Bitmap;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IDetailView {
    void toast(String msg);
    void showDetail(String id, String title, String img, String v, String v1, String city_id, String type_id);
    void showImage(String imgUrl);
}
