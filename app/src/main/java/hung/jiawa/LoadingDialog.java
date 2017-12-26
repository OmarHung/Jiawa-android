package hung.jiawa;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by omar8 on 2017/5/15.
 */

public class LoadingDialog {
    private CustomProgressDialog progressDialog = null;
    private Context context;
    public LoadingDialog(Context context) {
        this.context = context;
    }
    public void show() {
        progressDialog = CustomProgressDialog.createDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }
    public void show(String msg) {
        progressDialog = CustomProgressDialog.createDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }
    public void dismiss() {
        if(progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
    }
}
