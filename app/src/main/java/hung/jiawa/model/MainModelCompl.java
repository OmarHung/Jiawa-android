package hung.jiawa.model;

import android.content.Context;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.DBConnector;
import hung.jiawa.PreferenceHelper;

import static hung.jiawa.PreferenceHelper.with;

/**
 * Created by omar8 on 2017/5/22.
 */

public class MainModelCompl implements IMainModel {
    Context context;
    PreferenceHelper settings;
    String mid;
    public MainModelCompl(Context context) {
        this.context = context;
        settings = with(context);
        mid = settings.getString("id","");
    }
}
