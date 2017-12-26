package hung.jiawa.view;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface ISignUpView {
    void toast(String msg);
    void goToPreLoadActivity();
    void showLoadingDialog();
    void dismissLoadingDialog();
}
