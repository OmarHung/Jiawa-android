package hung.jiawa.view;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IPreLoadView {
    void toast(String msg);
    void finishActivity();
    void goToMainActivity();
    void showLoadingDialog();
    void dismissLoadingDialog();
}
