package hung.jiawa.model;

import android.content.Context;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.DBConnector;
import hung.jiawa.PreferenceHelper;

import static hung.jiawa.PreferenceHelper.with;

/**
 * Created by omar8 on 2017/5/22.
 */

public class DetailModelCompl implements IDetailModel {
    Context context;
    PreferenceHelper settings;
    String mid;
    AsyncTaskCallBack callBack;
    public DetailModelCompl(Context context, AsyncTaskCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        settings = with(context);
        mid = settings.getString("id","");
    }

    @Override
    public void getDetail(String id) {
        //6
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeGetDetail(mid, id);
    }

    @Override
    public void getResponse(String id) {
        //11
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeGetResponse(mid, id);
    }

    @Override
    public void getProfile(String id) {
        //12
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeGetProfile(id);
    }

    @Override
    public void postResponse(String type, String id, String content) {
        if(type.equals("a")) {
            //13
            DBConnector mDBConnector = new DBConnector(context);
            mDBConnector.setCallBack(callBack);
            mDBConnector.executePostResponse(mid, content, id);
        }
    }

    @Override
    public void checkArticleLike(String aid) {
        //14
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeCheckArticleLike(mid,aid);
    }

    @Override
    public void checkArticleKeep(String aid) {
        //17
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeCheckArticleKeep(mid,aid);
    }

    @Override
    public String getProfileImage() {
        return settings.getString("img","");
    }
}
