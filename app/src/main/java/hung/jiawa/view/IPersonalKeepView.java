package hung.jiawa.view;

/**
 * Created by omar8 on 2017/6/5.
 */

public interface IPersonalKeepView {
    void toast(String msg);
    void startDetailActivity(String aid);
    void setNoFavorit(String msg);
    void showLoadingDialog();
    void dismissLoadingDialog();
}
