package hung.jiawa.view;

/**
 * Created by omar8 on 2017/5/31.
 */

public interface IPersonView {
    void toast(String msg);
    void showLoadingDialog();
    void setProfileImage(String imgUri);
    void setName(String name);
    //void showResponseDialog(int floor);
    void dismissLoadingDialog();
    void startLoginActivity();
    //void dismissResponseDialog();
}
