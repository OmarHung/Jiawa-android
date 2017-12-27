package hung.jiawa.view;

import android.graphics.Bitmap;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IDetailView {
    void toast(String msg);
    void setToolBarTitle(String title);
    void setAid(String aid);
    void showLoadingDialog();
    void showResponseDialog(final String type, final String rid, int forFloor);
    void dismissLoadingDialog();
    void dismissResponseDialog();
}
