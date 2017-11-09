package hung.jiawa.model;

import hung.jiawa.view.IPersonView;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IPersonModel {
    String getMid();
    String getName();
    String getProfileImage();
    String getEmail();
    void logout(IPersonView iPersonView);
    void setName(String name);
    void setProfileImage(String img);
    void uploadProfileImage(String img);
    void setPreferenceName(String name);
    void setPreferenceProfileImage(String img);
}
