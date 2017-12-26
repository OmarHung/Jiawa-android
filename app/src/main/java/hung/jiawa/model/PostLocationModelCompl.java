package hung.jiawa.model;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.DBConnector;
import hung.jiawa.ItemDAO;
import hung.jiawa.LoactionItem;
import hung.jiawa.PreferenceHelper;

import static hung.jiawa.PreferenceHelper.with;

/**
 * Created by omar8 on 2017/5/22.
 */

public class PostLocationModelCompl implements IPostLocationModel {
    private ItemDAO itemDAO;
    Context context;
    PreferenceHelper settings;
    String mid;
    AsyncTaskCallBack callBack;
    public PostLocationModelCompl(Context context, AsyncTaskCallBack callBack) {
        this.context = context;
        this.callBack = callBack;
        itemDAO = new ItemDAO(context);
        settings = with(context);
        mid = settings.getString("id","");
    }

    @Override
    public void postLoaction(String title, String content, String latlng, String city, String type, String number_of_machine, String img) {
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executePostLocation(mid, title, content, latlng, city, type, number_of_machine, img);
    }

    @Override
    public void uploadImage(String img, String id) {
        DBConnector mDBConnector = new DBConnector(context);
        mDBConnector.setCallBack(callBack);
        mDBConnector.executeUploadImage(mid, img, id);
    }

    @Override
    public List<LoactionItem> getAll() {
        return itemDAO.getAll();
    }
}
