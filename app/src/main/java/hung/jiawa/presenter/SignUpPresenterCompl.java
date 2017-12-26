package hung.jiawa.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.R;
import hung.jiawa.model.ILoginModel;
import hung.jiawa.model.ISignUpModel;
import hung.jiawa.model.LoginModelCompl;
import hung.jiawa.model.SignUpModelCompl;
import hung.jiawa.view.ILoginView;
import hung.jiawa.view.ISignUpView;

/**
 * Created by omar8 on 2017/5/22.
 */

public class SignUpPresenterCompl implements ISignUpPresenter, AsyncTaskCallBack {
    public final String TAG = "JiaWa";
    public final String NAME = " SignUpActivity - ";
    private String password;
    ISignUpView iSignUpView;
    ISignUpModel iSignUpModel;
    Context context;
    public SignUpPresenterCompl(Context context, ISignUpView iSignUpView) {
        this.iSignUpView = iSignUpView;
        this.iSignUpModel = new SignUpModelCompl(context, this);
        this.context = context;
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        iSignUpView.dismissLoadingDialog();
        try {
            JSONObject jsonData = new JSONObject(result);
            String status = jsonData.getString("status");
            String msg = jsonData.getString("msg");
            if (status.equals("error")) {
                iSignUpView.toast(msg);
            } else if (status.equals("ok")) {
                iSignUpView.toast(msg);
                String data = jsonData.getString("data");
                JSONObject json = new JSONObject(data);
                Map<String, String> hashMap = new HashMap<String, String>();
                Iterator<String> temp = json.keys();
                while (temp.hasNext()) {
                    String key = temp.next();
                    Object value = json.get(key);
                    hashMap.put(key,value.toString());
                    Log.d(TAG, NAME+"key:"+key + " value:" + value.toString());
                }
                hashMap.put("password",password);
                iSignUpModel.savePreference(hashMap);
                iSignUpView.goToPreLoadActivity();
            }

        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iSignUpView.dismissLoadingDialog();
        iSignUpView.toast(error);
    }

    @Override
    public void attemptSignUp(EditText Ed_Email, EditText Ed_Password, EditText Ed_ReEnterPassword, EditText Ed_Name) {//, EditText Ed_NickName) {
        // Reset errors.
        Ed_Name.setError(null);
        //Ed_NickName.setError(null);
        Ed_Email.setError(null);
        Ed_Password.setError(null);
        Ed_ReEnterPassword.setError(null);
        //Ed_Mobile.setError(null);

        // Store values at the time of the login attempt.
        String name = Ed_Name.getText().toString();
        //String nick_name = Ed_NickName.getText().toString();
        String email = Ed_Email.getText().toString();
        String password = Ed_Password.getText().toString();
        String re_enter_password = Ed_ReEnterPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid re enter password
        if (TextUtils.isEmpty(re_enter_password)) {
            Ed_ReEnterPassword.setError(context.getString(R.string.error_field_required));
            focusView = Ed_ReEnterPassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(re_enter_password) && !isPasswordValid(re_enter_password)) {
            Ed_ReEnterPassword.setError(context.getString(R.string.error_invalid_password));
            focusView = Ed_ReEnterPassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(re_enter_password) && isPasswordValid(re_enter_password) && !re_enter_password.equals(password)) {
            Ed_ReEnterPassword.setError(context.getString(R.string.error_incorrect_password));
            focusView = Ed_ReEnterPassword;
            cancel = true;
        }

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            Ed_Password.setError(context.getString(R.string.error_field_required));
            focusView = Ed_Password;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            Ed_Password.setError(context.getString(R.string.error_invalid_password));
            focusView = Ed_Password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            Ed_Email.setError(context.getString(R.string.error_field_required));
            focusView = Ed_Email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            Ed_Email.setError(context.getString(R.string.error_invalid_email));
            focusView = Ed_Email;
            cancel = true;
        }

        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
            Ed_Name.setError(context.getString(R.string.error_field_required));
            focusView = Ed_Name;
            cancel = true;
        }

        /* Check for a valid nickname
        if (TextUtils.isEmpty(nick_name)) {
            Ed_NickName.setError(context.getString(R.string.error_field_required));
            focusView = Ed_Name;
            cancel = true;
        }
        */
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // 表單無誤，顯示loading並連網確認用戶
            this.password = password;
            iSignUpView.showLoadingDialog();
            iSignUpModel.SignUp(email, password, name);//, nick_name);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 6;
    }
}
