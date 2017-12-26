package hung.jiawa;

import java.util.Date;
import java.util.Locale;

/**
 * Created by omar8 on 2017/5/30.
 */

public class LoactionItem implements java.io.Serializable {

    // 編號、名稱、經緯度
    private long id;
    private String name;
    private String latlng;
    private boolean delete=false;
    private boolean checked=false;

    public LoactionItem() {
        checked = false;
    }

    public LoactionItem(long id, String name, String latlng, boolean delete) {
        this.id = id;
        this.name = name;
        this.latlng = latlng;
        this.delete = delete;
        checked = false;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLatlng() {
        return latlng;
    }

    public void setLatlng(String latlng) {
        this.latlng = latlng;
    }

    public void setShowCheckBox(boolean delete) {
        this.delete = delete;
    }

    public boolean showCheckBox() {
        return delete;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean getChecked() {
        return checked;
    }
}