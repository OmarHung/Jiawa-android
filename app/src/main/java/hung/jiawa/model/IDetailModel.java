package hung.jiawa.model;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IDetailModel {
    void getDetail(String id);
    void getResponse(String id);
    void getProfile(String id);
    void postResponse(String type, String aid, String content);
    void checkArticleLike(String aid);
    void checkArticleKeep(String aid);
    String getProfileImage();
}
