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

public class PreLoadModelCompl implements IPreLoadModel {
    Context context;
    AsyncTaskCallBack callBack;
    public PreLoadModelCompl(Context context, AsyncTaskCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
    }

    @Override
    public void getServerStatus() {
        // mode = 18
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeGetServerStatus();
    }
}
