package hung.jiawa.model;

import android.content.Context;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.DBConnector;
import hung.jiawa.PreferenceHelper;

import static hung.jiawa.PreferenceHelper.with;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PersonModelCompl implements IPersonModel {
    Context context;
    PreferenceHelper settings;
    String mid, name, profile_img;
    AsyncTaskCallBack callBack;
    public PersonModelCompl(Context context, AsyncTaskCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        settings = with(context);
        mid = settings.getString("mid","");
    }

    @Override
    public String getMid() {
        return mid;
    }

    @Override
    public String getName() {
        return settings.getString("name","");
    }

    @Override
    public String getProfileImage() {
        return settings.getString("img","");
    }

    @Override
    public String getEmail() {
        return settings.getString("email","");
    }

    @Override
    public void logout() {
        settings.save("email", "");
        settings.save("password", "");
    }

    @Override
    public void setName(String name) {
        // 19
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeUpdateProfileName(mid, name);
    }

    @Override
    public void setProfileImage(String img) {
        // 20
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeUpdateProfileImage(mid, img);
    }

    @Override
    public void uploadProfileImage(String img) {
        // 21
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeUploadProfileImage(mid, img);
    }

    @Override
    public void setPreferenceName(String name) {
        settings.save("name", name);
    }

    @Override
    public void setPreferenceProfileImage(String img) {
        settings.save("img", img);
    }
}
