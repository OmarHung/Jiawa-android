package hung.jiawa.view;

/**
 * Created by omar8 on 2017/6/5.
 */

public interface IPersonalArticleView {
    void toast(String msg);
    void startDetailActivity(String aid);
    void setNoArticle(String msg);
    void showLoadingDialog();
    void dismissLoadingDialog();
}
