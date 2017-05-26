package hung.jiawa;

import android.graphics.drawable.Drawable;
import android.net.Uri;

import java.io.File;

/**
 * Created by omar8 on 2017/5/25.
 */

public class ImageListItem {
    private int id;
    private int type;
    private File image;
    public ImageListItem(File image, int id, int type) {
        this.image=image;
        this.id=id;
        this.type=type;
    }
    public File getImage() {
        return image;
    }
    public int getId() {
        return id;
    }
    public int getType() {
        return type;
    }
}
