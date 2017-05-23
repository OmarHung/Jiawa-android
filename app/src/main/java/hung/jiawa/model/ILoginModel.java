package hung.jiawa.model;

import hung.jiawa.AsyncTaskCallBack;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface ILoginModel {
    void checkMember(String account, String password);
    void savePreference(String email, String mid, String name, String logintime);
    String getPreferenceLogined();
}
