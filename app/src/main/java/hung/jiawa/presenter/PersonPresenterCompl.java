package hung.jiawa.presenter;

import android.content.Context;
import android.net.Uri;
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
            JSONObject jsonData = new JSONObject(result);
            String status = jsonData.getString("status");
            String msg = jsonData.getString("msg");
            if (status.equals("error")) {
                iPersonView.toast(msg);
            } else if (status.equals("ok")) {
                if(mode == 19) {
                    //改名
                    String name = jsonData.getString("name");
                    iPersonView.setName(name);
                    iPersonModel.setPreferenceName(name);
                }else if(mode == 20) {
                    //改頭像
                    String img = jsonData.getString("img");
                    iPersonView.setProfileImage(img);
                    iPersonModel.setPreferenceProfileImage(img);
                    iPersonView.toast(msg);
                }else if(mode == 21) {
                    //上傳頭像
                    String l_img = jsonData.getString("img");
                    iPersonModel.setProfileImage(l_img);
                }
            }
        } catch (JSONException e) {}
    }

    @Override
    public void onError(String error) {
        iPersonView.toast(error);
    }

    @Override
    public void logout() {
        iPersonModel.logout();
        iPersonView.startLoginActivity();
    }

    @Override
    public String getName() {
        return iPersonModel.getName();
    }

    @Override
    public List<Uri> getProfileImage() {
        List<Uri> img = new ArrayList<>();
        img.add(Uri.parse(iPersonModel.getProfileImage()));
        return img;
    }

    @Override
    public void setName(String name) {
        iPersonModel.setName(name);
    }

    @Override
    public void uploadProfileImage(String img) {
        iPersonModel.uploadProfileImage(img);
    }
}
