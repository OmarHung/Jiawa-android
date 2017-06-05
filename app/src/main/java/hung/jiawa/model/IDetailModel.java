package hung.jiawa.model;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IDetailModel {
    void getDetail(String id);
    void getResponse(String id);
    void getProfile(String id);
    void setProfileToPre(String name, String email, String img);
    void postResponse(String aid, String content);
    void checkArticleLike(String aid);
    void checkResponseLike(String rid);
    String getProfileImage();
}
