package hung.jiawa.view.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.DBConnector;
import hung.jiawa.LoadingDialog;
import hung.jiawa.MainActivity;
import hung.jiawa.R;
import hung.jiawa.presenter.IPreLoadPresenter;
import hung.jiawa.presenter.LoginPresenterCompl;
import hung.jiawa.presenter.PreLoadPresenterCompl;
import hung.jiawa.view.IPreLoadView;

public class PreLoadActivity extends AppCompatActivity implements IPreLoadView {
    /**
     * 確認版本、連線測試、預先載入廣告、server狀態
     */
    private LoadingDialog mLoadingDialog = null;
    IPreLoadPresenter preLoadPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pre_load);

        //init
        preLoadPresenter = new PreLoadPresenterCompl(this, this);
        mLoadingDialog = new LoadingDialog(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(preLoadPresenter.getServerStatus()==0) {
            mLoadingDialog.dismiss();
            finish();
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void goToLoginActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void showLoadingDialog() {
        mLoadingDialog.show("例行維護中...");
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void finishActivity() {
        finish();
    }
}
