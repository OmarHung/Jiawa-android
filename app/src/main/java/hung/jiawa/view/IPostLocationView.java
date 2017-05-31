package hung.jiawa.view;

import java.util.List;
import java.util.Map;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IPostLocationView {
    void toast(String msg);
    void showLoadingDialog();
    void showLocationDialog(String Items[]);
    void dismissLoadingDialog();
    void showLocationDetail(String aid);
    void setLatlng(String lat, String lng);
}
