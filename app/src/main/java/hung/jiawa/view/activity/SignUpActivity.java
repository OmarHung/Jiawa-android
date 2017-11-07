package hung.jiawa.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.DBConnector;
import hung.jiawa.LoadingDialog;
import hung.jiawa.PreferenceHelper;
import hung.jiawa.R;
import hung.jiawa.presenter.ISignUpPresenter;
import hung.jiawa.presenter.SignUpPresenterCompl;
import hung.jiawa.view.ISignUpView;

import static hung.jiawa.PreferenceHelper.with;

public class SignUpActivity extends AppCompatActivity implements ISignUpView, View.OnClickListener {
    public final String TAG = "Prototype";
    public final String NAME = "SignUpActivity - ";
    //16位的英數組合位元，可自行填寫 (下為小黑人暫訂)
    //32位的英數組合Key欄位，可自行填寫 (下為小黑人暫訂)
    //欲進行加密的文字字串
    private final static String IvAES = "pgkf250ff5se5gs2" ;
    private final static String KeyAES = "059sd0397svc59s64ge6q3wrdf183dwe";

    private EditText Ed_Name, Ed_NickName, Ed_Email, Ed_Password, Ed_ReEnterPassword;
    private Button Bt_Signup;
    private TextView Tv_Login;

    private String name = "";
    private String nick_name = "";
    private String email = "";
    private String password = "";
    private String re_enter_password = "";
    private String mobile = "";
    private String mid = "";

    private PreferenceHelper settings;
    private LoadingDialog mLoadingDialog = null;
    ISignUpPresenter signUpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //find view
        Ed_Name = (EditText) findViewById(R.id.input_name);
        Ed_NickName = (EditText) findViewById(R.id.input_nick_name);
        Ed_Email = (EditText) findViewById(R.id.input_email);
        Ed_Password = (EditText) findViewById(R.id.input_password);
        Ed_ReEnterPassword = (EditText) findViewById(R.id.input_reEnterPassword);
        Bt_Signup = (Button) findViewById(R.id.btn_next);
        Tv_Login = (TextView) findViewById(R.id.link_login);

        //set listener
        Bt_Signup.setOnClickListener(this);
        Tv_Login.setOnClickListener(this);

        //init
        mLoadingDialog = new LoadingDialog(this);
        signUpPresenter = new SignUpPresenterCompl(this, this);
        settings = with(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                signUpPresenter.attemptSignUp(Ed_Email, Ed_Password, Ed_ReEnterPassword, Ed_Name, Ed_NickName);
                break;
            case R.id.link_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToPreLoadActivity() {
        startActivity(new Intent(this, PreLoadActivity.class));
        finish();
    }

    @Override
    public void showLoadingDialog() {
        mLoadingDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }
}
