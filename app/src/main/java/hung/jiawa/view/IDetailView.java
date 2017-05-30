package hung.jiawa.view;

import android.graphics.Bitmap;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IDetailView {
    void toast(String msg);
    void showDetail(String id, String title, String latlng, String city_id, String type_id, String content, String machine_id, String like, String response);
    //void showResponse(String mid, String time, String content, String img, String resopne, String like, String floor);
    //void showImage(String imgUrl);
    void showLoadingDialog();
    void dismissLoadingDialog();
}
