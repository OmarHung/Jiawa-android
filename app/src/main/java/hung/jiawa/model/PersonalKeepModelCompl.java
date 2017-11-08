package hung.jiawa.model;

import android.content.Context;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.DBConnector;
import hung.jiawa.PreferenceHelper;

import static hung.jiawa.PreferenceHelper.with;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PersonalKeepModelCompl implements IPersonalKeepModel {
    Context context;
    PreferenceHelper settings;
    String mid;
    AsyncTaskCallBack callBack;
    public PersonalKeepModelCompl(Context context, AsyncTaskCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        settings = with(context);
        mid = settings.getString("id","");
    }

    @Override
    public void getFavorit() {
        // 16
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeGetFavorit(mid);
    }
}
