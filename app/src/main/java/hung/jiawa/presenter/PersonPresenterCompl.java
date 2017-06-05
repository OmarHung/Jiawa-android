package hung.jiawa.presenter;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.model.ForumModelCompl;
import hung.jiawa.model.IForumModel;
import hung.jiawa.model.IPersonModel;
import hung.jiawa.model.PersonModelCompl;
import hung.jiawa.view.IForumView;
import hung.jiawa.view.IPersonView;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PersonPresenterCompl implements IPersonPresenter, AsyncTaskCallBack {
    public final String TAG = "JiaWa";
    public final String NAME = "PersonFragment - ";
    IPersonView iPersonView;
    IPersonModel iPersonModel;
    Context context;
    public PersonPresenterCompl(Context context, IPersonView iPersonView) {
        this.iPersonView = iPersonView;
        this.iPersonModel = new PersonModelCompl(context, this);
        this.context = context;
        iPersonView.setName(iPersonModel.getName());
        iPersonView.setProfileImage(iPersonModel.getProfileImage());
    }

    @Override
    public void onResult(int mode, String result) {
        Log.d(TAG, NAME+"onResult"+result + ":" + mode);
        try {
            String status="", msg="";
            List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();
            JSONArray jsonArray = new JSONArray(result);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonData = jsonArray.getJSONObject(i);
                Log.d(TAG, NAME + "lengh = " + jsonArray.length() + " : i = " + i + ": jsonData = " + jsonData.toString());
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iPersonView.toast(error);
    }
}
