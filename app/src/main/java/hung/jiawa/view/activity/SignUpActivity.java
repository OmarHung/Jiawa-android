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
import java.util.regex.Pattern;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.DBConnector;
import hung.jiawa.PreferenceHelper;
import hung.jiawa.R;

import static hung.jiawa.PreferenceHelper.with;

public class SignUpActivity extends AppCompatActivity implements AsyncTaskCallBack, View.OnClickListener {
    public final String TAG = "Prototype";
    public final String NAME = "SignUpActivity - ";
    //16位的英數組合位元，可自行填寫 (下為小黑人暫訂)
    //32位的英數組合Key欄位，可自行填寫 (下為小黑人暫訂)
    //欲進行加密的文字字串
    private final static String IvAES = "pgkf250ff5se5gs2" ;
    private final static String KeyAES = "059sd0397svc59s64ge6q3wrdf183dwe";

    private EditText Ed_Name, Ed_Email, Ed_Password, Ed_ReEnterPassword;
    private Button Bt_Signup;
    private TextView Tv_Login;

    private String name = "";
    private String email = "";
    private String password = "";
    private String re_enter_password = "";
    private String mobile = "";
    private String mid = "";

    private PreferenceHelper settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //find view
        Ed_Name = (EditText) findViewById(R.id.input_name);
        Ed_Email = (EditText) findViewById(R.id.input_email);
        Ed_Password = (EditText) findViewById(R.id.input_password);
        Ed_ReEnterPassword = (EditText) findViewById(R.id.input_reEnterPassword);
        Bt_Signup = (Button) findViewById(R.id.btn_next);
        Tv_Login = (TextView) findViewById(R.id.link_login);

        //set listener
        Bt_Signup.setOnClickListener(this);
        Tv_Login.setOnClickListener(this);

        settings = with(this);
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult："+result+":"+mode);
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                String status = jsonData.getString("status");
                String msg = jsonData.getString("msg");
                if(status.equals("501")) {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                }else if(status.equals("401")) {
                    //重複帳號
                    Ed_Email.setError(getString(R.string.error_field_email_repeat));
                    View focusView = Ed_Email;
                    focusView.requestFocus();
                    //Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                }else if(status.equals("201")) {
                    Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
                    mid = jsonData.getString("mid");
                    String logintime = jsonData.getString("logintime");
                    settings.save("account", email);
                    settings.save("password", password);
                    settings.save("mid", mid);
                    settings.save("name", name);
                    settings.save("logintime", logintime);
                    settings.save("login", "logined");
                    startActivity(new Intent(SignUpActivity.this, PreLoadActivity.class));
                    finish();
                }
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this,error,Toast.LENGTH_SHORT).show();
    }
    /*
    void checkAccount(String account) {
        Log.d("checkAccount", " Account = "+account);
        DBConnector mDBConnector = new DBConnector(this);
        mDBConnector.setCallBack(this);
        mDBConnector.executeCheckAccount(account);
    }
    */
    void signUp(String name, String account, String password) {
        //將IvAES、KeyAES、TextAES轉成byte[]型態帶入EncryptAES進行加密，再將回傳值轉成字串
        byte[] TextByte = new byte[0];
        try {
            TextByte = EncryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        password = Base64.encodeToString(TextByte, Base64.DEFAULT);
        //加密字串結果為 : xq/WqrKuXIqLxw1BM4GJoAqPQp6Zh+vqLykVAj2GHFY=
        Log.d(TAG, NAME+"Name = "+name+", Account = "+account+", Password = "+password);
        DBConnector mDBConnector = new DBConnector(this);
        mDBConnector.setCallBack(this);
        mDBConnector.executeSignUp(name, account, password);
    }

    private void attemptSignUp() {
        // Reset errors.
        Ed_Name.setError(null);
        Ed_Email.setError(null);
        Ed_Password.setError(null);
        Ed_ReEnterPassword.setError(null);
        //Ed_Mobile.setError(null);

        // Store values at the time of the login attempt.
        name = Ed_Name.getText().toString();
        email = Ed_Email.getText().toString();
        password = Ed_Password.getText().toString();
        re_enter_password = Ed_ReEnterPassword.getText().toString();
        //mobile = Ed_Mobile.getText().toString();

        boolean cancel = false;
        View focusView = null;

        /*
        // Check for a valid mobile
        if (TextUtils.isEmpty(mobile)) {
            Ed_Mobile.setError(getString(R.string.error_field_required));
            focusView = Ed_Mobile;
            cancel = true;
        } else if (!TextUtils.isEmpty(mobile) && !isMobileValid(mobile)) {
            Ed_Mobile.setError(getString(R.string.error_invalid_mobile));
            focusView = Ed_Mobile;
            cancel = true;
        }*/

        // Check for a valid re enter password
        if (TextUtils.isEmpty(re_enter_password)) {
            Ed_ReEnterPassword.setError(getString(R.string.error_field_required));
            focusView = Ed_ReEnterPassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(re_enter_password) && !isPasswordValid(re_enter_password)) {
            Ed_ReEnterPassword.setError(getString(R.string.error_invalid_password));
            focusView = Ed_ReEnterPassword;
            cancel = true;
        } else if (!TextUtils.isEmpty(re_enter_password) && isPasswordValid(re_enter_password) && !re_enter_password.equals(password)) {
            Ed_ReEnterPassword.setError(getString(R.string.error_incorrect_password));
            focusView = Ed_ReEnterPassword;
            cancel = true;
        }

        // Check for a valid password
        if (TextUtils.isEmpty(password)) {
            Ed_Password.setError(getString(R.string.error_field_required));
            focusView = Ed_Password;
            cancel = true;
        } else if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            Ed_Password.setError(getString(R.string.error_invalid_password));
            focusView = Ed_Password;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            Ed_Email.setError(getString(R.string.error_field_required));
            focusView = Ed_Email;
            cancel = true;
        } else if (!isEmailValid(email)) {
            Ed_Email.setError(getString(R.string.error_invalid_email));
            focusView = Ed_Email;
            cancel = true;
        }

        // Check for a valid name
        if (TextUtils.isEmpty(name)) {
            Ed_Name.setError(getString(R.string.error_field_required));
            focusView = Ed_Name;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //checkAccount(email);
            signUp(name, email, password);
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

    /**
     * 手機門號檢查程式
     *
     * @since 2006/07/19
     **/
    public static final Pattern MSISDN_PATTERN = Pattern
            .compile("[+-]?\\d{10,12}");

    public static boolean isMobileValid(String msisdn) {
        boolean result = false;
        if (MSISDN_PATTERN.matcher(msisdn).matches()) {
            result = true;
        }
        return result;
    }

    //AES加密，帶入byte[]型態的16位英數組合文字、32位英數組合Key、需加密文字
    private static byte[] EncryptAES(byte[] iv, byte[] key,byte[] text) {
        try {
            AlgorithmParameterSpec mAlgorithmParameterSpec = new IvParameterSpec(iv);
            SecretKeySpec mSecretKeySpec = new SecretKeySpec(key, "AES");
            Cipher mCipher = null;
            mCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            mCipher.init(Cipher.ENCRYPT_MODE,mSecretKeySpec,mAlgorithmParameterSpec);

            return mCipher.doFinal(text);
        }catch(Exception ex) {
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                attemptSignUp();
                break;
            case R.id.link_login:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
