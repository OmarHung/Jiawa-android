package hung.jiawa.model;

import java.util.List;

import hung.jiawa.LoactionItem;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface ISavedLocationModel {
    List<LoactionItem> getAll();
    List<LoactionItem> getAllForDel();
    void delete(long id);
}
