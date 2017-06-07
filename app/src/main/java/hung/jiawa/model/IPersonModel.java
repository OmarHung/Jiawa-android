package hung.jiawa.model;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IPersonModel {
    String getMid();
    String getName();
    String getProfileImage();
    String getEmail();
    void logout();
    void setName(String name);
    void setProfileImage(String img);
    void uploadProfileImage(String img);
    void setPreferenceName(String name);
    void setPreferenceProfileImage(String img);
}
