package hung.jiawa.presenter;

import android.support.v7.widget.RecyclerView;

import java.util.List;
import java.util.Map;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IPersonalKeepPresenter {
    void getFavorit();
    void showFavorit(List<Map<String, Object>> myDataset);
    void initRecyclerView(RecyclerView recyclerView);
}
