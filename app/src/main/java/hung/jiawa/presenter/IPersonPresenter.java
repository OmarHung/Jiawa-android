package hung.jiawa.presenter;

import android.net.Uri;

import java.util.List;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IPersonPresenter {
    void logout();
    String getName();
    List<Uri> getProfileImage();
    void setName(String name);
    void uploadProfileImage(String img);
}
