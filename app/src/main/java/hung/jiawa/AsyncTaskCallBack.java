package hung.jiawa;

/**
 * Created by omar8 on 2017/3/29.
 */

public interface AsyncTaskCallBack {
    void onResult(int mode, String result);
    void onError(String error);
}
