package hung.jiawa;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by omar8 on 2017/5/15.
 */

public class LoadingDialog {
    private ProgressDialog progressDialog = null;
    private Context context;
    public LoadingDialog(Context context) {
        this.context = context;
    }
    public void show(String msg) {
        progressDialog = new ProgressDialog(context, R.style.AppTheme_Dark_Dialog);
        progressDialog.setCancelable(false);
        progressDialog.setMessage(msg);
        progressDialog.show();
    }
    public void dismiss() {
        if(progressDialog!=null && progressDialog.isShowing()) progressDialog.dismiss();
    }
}
