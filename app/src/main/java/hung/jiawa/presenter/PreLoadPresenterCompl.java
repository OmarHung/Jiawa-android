package hung.jiawa.presenter;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.LoadingDialog;
import hung.jiawa.R;
import hung.jiawa.model.IPreLoadModel;
import hung.jiawa.model.PreLoadModelCompl;
import hung.jiawa.view.IPreLoadView;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PreLoadPresenterCompl implements IPreLoadPresenter, AsyncTaskCallBack {
    public final String TAG = "JiaWa";
    public final String NAME = "PreLoadActivity - ";
    private int serverStatus;
    IPreLoadView iPreLoadView;
    IPreLoadModel iPreLoadModel;
    Context context;
    public PreLoadPresenterCompl(Context context, IPreLoadView iPreLoadView) {
        this.iPreLoadView = iPreLoadView;
        this.iPreLoadModel = new PreLoadModelCompl(context, this);
        this.context = context;
        iPreLoadModel.getServerStatus();
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        try {
            JSONObject jsonData = new JSONObject(result);
            String status = jsonData.getString("status");
            String msg = jsonData.getString("msg");
            if (status.equals("error")) {
                iPreLoadView.toast(msg);
                iPreLoadView.finishActivity();
            } else if (status.equals("ok")) {
                serverStatus = Integer.parseInt(msg);
                if (serverStatus==0) iPreLoadView.showLoadingDialog();
                else iPreLoadView.goToMainActivity();
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iPreLoadView.dismissLoadingDialog();
        iPreLoadView.toast(error);
    }

    @Override
    public int getServerStatus() {
        return serverStatus;
    }
}
