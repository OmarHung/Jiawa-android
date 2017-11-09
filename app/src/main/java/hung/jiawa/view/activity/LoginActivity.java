package hung.jiawa.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import hung.jiawa.LoadingDialog;
import hung.jiawa.MainActivity;
import hung.jiawa.R;
import hung.jiawa.presenter.ILoginPresenter;
import hung.jiawa.presenter.LoginPresenterCompl;
import hung.jiawa.view.ILoginView;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements ILoginView, View.OnClickListener {

    private EditText Ed_Email, Ed_Password;
    private Button Bt_Login;
    private TextView Tv_linkFindpassword, Tv_linkSignup;
    private LoadingDialog mLoadingDialog = null;
    ILoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //find view
        Ed_Email = (EditText) findViewById(R.id.email);
        Ed_Password = (EditText) findViewById(R.id.password);
        Bt_Login = (Button) findViewById(R.id.btn_login);
        Tv_linkFindpassword = (TextView) findViewById(R.id.link_findpassword);
        Tv_linkSignup = (TextView) findViewById(R.id.link_signup);

        //set listener
        Bt_Login.setOnClickListener(this);
        Tv_linkFindpassword.setOnClickListener(this);
        Tv_linkSignup.setOnClickListener(this);

        //init
        mLoadingDialog = new LoadingDialog(this);
        loginPresenter = new LoginPresenterCompl(this, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                loginPresenter.attemptLogin(Ed_Email, Ed_Password);
                break;
            case R.id.link_findpassword:
                startActivity(new Intent(this, FindPasswordActivity.class));
                break;
            case R.id.link_signup:
                startActivity(new Intent(this, SignUpActivity.class));
                finish();
                break;
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
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

