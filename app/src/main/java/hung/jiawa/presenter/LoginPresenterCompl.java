package hung.jiawa.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.R;
import hung.jiawa.model.ILoginModel;
import hung.jiawa.model.LoginModelCompl;
import hung.jiawa.view.ILoginView;

/**
 * Created by omar8 on 2017/5/22.
 */

public class LoginPresenterCompl implements ILoginPresenter, AsyncTaskCallBack {
    public final String TAG = "JiaWa";
    public final String NAME = "LoginActivity - ";
    ILoginView iLoginView;
    ILoginModel iLoginModel;
    Context context;
    public LoginPresenterCompl(Context context, ILoginView iLoginView) {
        this.iLoginView = iLoginView;
        this.iLoginModel = new LoginModelCompl(context, this);
        this.context = context;

        //取出skip屬性的字串
        String login = iLoginModel.getPreferenceLogined();
        if (login.equals("logined")) {
            Log.d(TAG, NAME+"已登入");
            iLoginView.goToPreLoadActivity();
        }
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        iLoginView.dismissLoadingDialog();
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                String status = jsonData.getString("status");
                String msg = jsonData.getString("msg");
                if (status.equals("501")) {
                    iLoginView.toast(msg);
                } else if (status.equals("201")) {
                    iLoginView.toast(msg);
                    String email = jsonData.getString("email");
                    String mid = jsonData.getString("mid");
                    String name = jsonData.getString("name");
                    String logintime = jsonData.getString("logintime");
                    iLoginModel.savePreference(email, mid, name, logintime);
                    iLoginView.goToPreLoadActivity();
                }
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iLoginView.dismissLoadingDialog();
        iLoginView.toast(error);
    }

    @Override
    public void attemptLogin(EditText Ed_Email, EditText Ed_Password) {
        // 重置錯誤
        Ed_Email.setError(null);
        Ed_Password.setError(null);

        String email = Ed_Email.getText().toString();
        String password = Ed_Password.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(password)) {
            Ed_Password.setError(context.getString(R.string.error_field_required));
            focusView = Ed_Password;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            Ed_Password.setError(context.getString(R.string.error_invalid_password));
            focusView = Ed_Password;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            Ed_Email.setError(context.getString(R.string.error_field_required));
            focusView = Ed_Email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            Ed_Email.setError(context.getString(R.string.error_invalid_email));
            focusView = Ed_Email;
            cancel = true;
        }

        if (cancel) {
            // 表單有錯誤，focus到錯誤的地方
            focusView.requestFocus();
        } else {
            // 表單無誤，顯示loading並連網確認用戶
            iLoginView.showLoadingDialog();
            iLoginModel.checkMember(email, password);
        }
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 8;
    }
}
