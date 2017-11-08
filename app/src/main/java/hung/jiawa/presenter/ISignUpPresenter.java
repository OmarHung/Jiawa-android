package hung.jiawa.presenter;

import android.widget.EditText;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface ISignUpPresenter {
    void attemptSignUp(EditText Ed_Email, EditText Ed_Password, EditText Ed_ReEnterPassword, EditText Ed_Name);//, EditText Ed_NickName);
}
