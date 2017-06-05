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
}
