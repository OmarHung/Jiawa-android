package hung.jiawa.model;

import android.content.Context;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.DBConnector;
import hung.jiawa.PreferenceHelper;

import static hung.jiawa.PreferenceHelper.with;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PersonalFavoritModelCompl implements IPersonalFavoritModel {
    Context context;
    PreferenceHelper settings;
    String mid;
    AsyncTaskCallBack callBack;
    public PersonalFavoritModelCompl(Context context, AsyncTaskCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        settings = with(context);
        mid = settings.getString("mid","");
    }

    @Override
    public void getFavorit() {
        // 16
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeGetFavorit(mid);
    }
}
