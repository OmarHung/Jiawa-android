package hung.jiawa.presenter;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.FragmentCallBack;
import hung.jiawa.model.IMainModel;
import hung.jiawa.model.MainModelCompl;
import hung.jiawa.view.IMainView;

/**
 * Created by omar8 on 2017/5/22.
 */

public class MainPresenterCompl implements IMainPresenter, AsyncTaskCallBack {
    public final String TAG = "JiaWa";
    public final String NAME = "MainActivity - ";
    IMainView iMainView;
    IMainModel iMainModel;
    Context context;
    public MainPresenterCompl(Context context, IMainView iMainView) {
        this.iMainView = iMainView;
        this.iMainModel = new MainModelCompl(context);
        this.context = context;
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                String status = jsonData.getString("status");
                String msg = jsonData.getString("msg");
                if (status.equals("501")) {
                    iMainView.toast(msg);
                } else if (status.equals("201")) {
                }
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iMainView.toast(error);
    }
}
