package hung.jiawa.presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import hung.jiawa.AsyncTaskCallBack;
import hung.jiawa.ItemDAO;
import hung.jiawa.LoactionItem;
import hung.jiawa.R;
import hung.jiawa.model.DetailModelCompl;
import hung.jiawa.model.IDetailModel;
import hung.jiawa.model.ISavedLocationModel;
import hung.jiawa.model.SavedLocationModelCompl;
import hung.jiawa.view.IDetailView;
import hung.jiawa.view.ISavedLocationView;
import hung.jiawa.view.adapter.SavedLocationAdapter;

/**
 * Created by omar8 on 2017/5/22.
 */

public class SavedLocationPresenterCompl implements ISavedLocationPresenter {
    public final String TAG = "JiaWa";
    public final String NAME = "DetailActivity - ";
    private List<LoactionItem> items;
    private SavedLocationAdapter savedLocationAdapter;
    //private ItemDAO itemDAO;
    ISavedLocationView iSavedLocationView;
    ISavedLocationModel iSavedLocationModel;
    Context context;
    private boolean isChecking=false;
    public SavedLocationPresenterCompl(Context context, ISavedLocationView iSavedLocationView, RecyclerView saved_location_list) {
        this.iSavedLocationView = iSavedLocationView;
        this.iSavedLocationModel = new SavedLocationModelCompl(context);
        this.context = context;
        initRecyclerView(saved_location_list);
    }

    @Override
    public List<LoactionItem> getAll() {
        return iSavedLocationModel.getAll();
    }

    @Override
    public List<LoactionItem> getAllForDel() {
        return iSavedLocationModel.getAllForDel();
    }

    @Override
    public void doCheckDel(ImageButton btn_delete) {
        if (!isChecking) {
            btn_delete.setImageResource(R.mipmap.ic_check);
            isChecking = true;
            items = getAllForDel();
            savedLocationAdapter.setData(items);
        } else {
            btn_delete.setImageResource(R.mipmap.ic_delete);
            isChecking = false;
            List<LoactionItem> items = savedLocationAdapter.getMyDataset();
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i).getChecked()) {
                    iSavedLocationModel.delete(items.get(i).getId());
                }
            }
            items = iSavedLocationModel.getAll();
            savedLocationAdapter.setData(items);
        }
    }

    private void initRecyclerView(RecyclerView saved_location_list) {
        items = getAll();
        savedLocationAdapter = new SavedLocationAdapter(context, items);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        saved_location_list.setLayoutManager(layoutManager);
        saved_location_list.setAdapter(savedLocationAdapter);
    }
}
