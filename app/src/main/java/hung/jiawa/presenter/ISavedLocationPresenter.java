package hung.jiawa.presenter;

import android.widget.ImageButton;

import java.util.List;

import hung.jiawa.LoactionItem;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface ISavedLocationPresenter {
    List<LoactionItem> getAll();
    List<LoactionItem> getAllForDel();
    void doCheckDel(ImageButton btn_delete);
}
