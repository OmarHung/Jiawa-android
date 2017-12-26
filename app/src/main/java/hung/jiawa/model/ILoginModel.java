package hung.jiawa.model;

import java.util.HashMap;
import java.util.Map;

import hung.jiawa.AsyncTaskCallBack;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface ILoginModel {
    void checkMember(String account, String password);
    void savePreference(Map<String, String> data);
    Map<String, String> getPreferenceLoginData();
}
