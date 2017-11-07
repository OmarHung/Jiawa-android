package hung.jiawa.model;

import android.content.Context;
import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.security.spec.AlgorithmParameterSpec;
import java.util.HashMap;
import java.util.Map;

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

public class SignUpModelCompl implements ISignUpModel {
    //16位的英數組合位元，可自行填寫
    private final static String IvAES = "pgkf250ff5se5gs2";
    //32位的英數組合Key欄位，可自行填寫
    private final static String KeyAES = "059sd0397svc59s64ge6q3wrdf183dwe";
    Context context;
    PreferenceHelper settings;
    AsyncTaskCallBack callBack;
    public SignUpModelCompl(Context context, AsyncTaskCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        settings = with(context);
    }

    @Override
    public void SignUp(String account, String password, String name, String nick_name) {
        //將IvAES、KeyAES、TextAES轉成byte[]型態帶入EncryptAES進行加密，再將回傳值轉成字串
        byte[] TextByte = new byte[0];
        try {
            TextByte = EncryptAES(IvAES.getBytes("UTF-8"), KeyAES.getBytes("UTF-8"), password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        password = Base64.encodeToString(TextByte, Base64.DEFAULT);
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeSignUp(account, password, name, nick_name);
    }

    @Override
    public void savePreference(Map<String, String> data) {
        for (String key : data.keySet()) {
            settings.save(key, data.get(key));
        }
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
}
