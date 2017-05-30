package hung.jiawa.model;

import android.content.Context;

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

public class SavedLocationModelCompl implements ISavedLocationModel {
    private ItemDAO itemDAO;
    Context context;
    public SavedLocationModelCompl(Context context) {
        this.context = context;
        itemDAO = new ItemDAO(context);
    }

    @Override
    public List<LoactionItem> getAll() {
        return itemDAO.getAll();
    }

    @Override
    public List<LoactionItem> getAllForDel() {
        return itemDAO.getAllForDel();
    }

    @Override
    public void delete(long id) {
        itemDAO.delete(id);
    }
}
