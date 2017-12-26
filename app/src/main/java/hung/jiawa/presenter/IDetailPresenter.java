package hung.jiawa.presenter;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IDetailPresenter {
    void onResume();
    void postResponse(String type, String id, String content);
    String getProfileImage();
}
