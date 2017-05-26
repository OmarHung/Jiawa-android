package hung.jiawa.view;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IForumView {
    void toast(String msg);
    void setForumList(List<Map<String, Object>> list);
    void setArticleList(List<Map<String, Object>> article);
    void setNoArticle();
    void showLoadingDialog();
    void dismissLoadingDialog();
}
