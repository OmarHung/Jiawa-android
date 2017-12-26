package hung.jiawa.model;

import java.util.Map;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface ISignUpModel {
    void SignUp(String account, String password, String name);//, String nick_name);
    void savePreference(Map<String, String> data);
}
