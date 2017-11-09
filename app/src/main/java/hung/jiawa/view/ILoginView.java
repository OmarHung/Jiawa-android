package hung.jiawa.view;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface ILoginView {
    void toast(String msg);
    void goToMainActivity();
    void showLoadingDialog();
    void dismissLoadingDialog();
}
