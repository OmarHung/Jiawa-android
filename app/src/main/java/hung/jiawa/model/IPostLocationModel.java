package hung.jiawa.model;

import java.util.List;

import hung.jiawa.LoactionItem;

/**
 * Created by omar8 on 2017/5/22.
 */

public interface IPostLocationModel {
    void postLoaction(String title, String content, String latlng, String city, String type, String number_of_machine, String img);
    void uploadImage(String img, String id);
    List<LoactionItem> getAll();
}
