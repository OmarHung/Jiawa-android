package hung.jiawa;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by omar8 on 2017/3/28.
 */

public class DBConnector {
    public final String TAG = "JiaWa";
    public final String NAME = "DBConnector - ";
    public int MODE_LOGIN=1;
    public int MODE_SIGNUP=2;
    public int MODE_SEND_EMAIL=3;
    public int MODE_GET_SERVER_STATUS=4;
    public int MODE_GET_MARKER=5;
    public int MODE_GET_DETAIL=6;
    public int MODE_GET_FORUM=7;
    public int MODE_GET_ARTICLE=8;
    public int MODE_POST_LOCATION=9;
    public int MODE_UPLOAD_IMAGE=10;

    private static DBConnector mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private String website = "http://zollow.why3s.tw/jiawa/";
    int timeoutConnection = 10000;
    int timeoutSocket = 10000;
    AsyncTaskCallBack mAsyncTaskCallBack;
    public DBConnector(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();
    }
    public void setCallBack(AsyncTaskCallBack mAsyncTaskCallBack) {
        this.mAsyncTaskCallBack = mAsyncTaskCallBack;
    }
    public static synchronized DBConnector getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DBConnector(context);
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }

    public void executeLogin(final String email, final String password) {
        String url =website+"Login.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest  = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("executeLogin", "response："+response);
                        mAsyncTaskCallBack.onResult(MODE_LOGIN, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ErrorList(error);
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("email", email);
                map.put("password", password);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest );
    }

    public void executeSignUp(final String name, final String email, final String password) {
        String url =website+"Check_and_new_member.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    mAsyncTaskCallBack.onResult(MODE_SIGNUP, response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    // TODO Auto-generated method stub
                    ErrorList(error);
                }
            }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", name);
                map.put("email", email);
                map.put("password", password);
                return map;
            }
        };
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeoutConnection,
                -1,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //取得論壇板清單
    public void executeGetForum() {
        String url =website+"Get_forum.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_GET_FORUM, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ErrorList(error);
            }
        });
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //取得論壇文章清單
    public void executeGetArticle(final String fid, final String cate) {
        String url =website+"Get_article.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_GET_ARTICLE, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ErrorList(error);
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("fid", fid);
                map.put("cate", cate);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    public void executeSendEmail(final String email) {
        String url =website+"send_email.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_SEND_EMAIL, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ErrorList(error);
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("email", email);
                return map;
            }
        };
        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
                timeoutConnection,
                -1,  // maxNumRetries = 0 means no retry
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //取得伺服器狀態
    public void executeGetServerStatus() {
        String url =website+"Get_server_status.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_GET_SERVER_STATUS, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ErrorList(error);
            }
        });
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //取得最新版本資訊
    public void executeGetMarker(final int cityPosition, final int typePosition) {
        String url =website+"Get_marker.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_GET_MARKER, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ErrorList(error);
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("city", String.valueOf(cityPosition));
                map.put("type", String.valueOf(typePosition));
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //取得店家資訊
    public void executeGetDetail(final String id) {
        String url =website+"Get_detail.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_GET_DETAIL, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ErrorList(error);
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("id", id);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //發表夾點
    public void executePostLocation(final String mid, final String title, final String content, final String latlng, final String city, final String type, final String number_of_machine, final String img) {
        String url =website+"Get_post_location.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_POST_LOCATION, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ErrorList(error);
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("mid", mid);
                map.put("title", title);
                map.put("content", content);
                map.put("img", img);
                map.put("latlng", latlng);
                map.put("city", city);
                map.put("type", type);
                map.put("number_of_machine", number_of_machine);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }
    public void executeUploadImage(final String mid, final String img, final String id) {
        String url =website+"Get_upload_image.php";
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_UPLOAD_IMAGE, response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO Auto-generated method stub
                ErrorList(error);
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name", getPhotoFileName(mid,img,id));
                map.put("img", img);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    private void ErrorList(VolleyError error) {
        String message = null;
        if (error instanceof NetworkError) {
            message = "無法連接到網路...請檢查您的網路狀態！";
        } else if (error instanceof ServerError) {
            message = "無法連接到伺服器...請稍後再試!";
        } else if (error instanceof AuthFailureError) {
            message = "無法連接到網路...請檢查您的網路狀態！";
        } else if (error instanceof ParseError) {
            message = "參數錯誤...請稍後再試!";
        } else if (error instanceof NoConnectionError) {
            message = "無法連接到網路...請檢查您的網路狀態！";
        } else if (error instanceof TimeoutError) {
            message = "連接超時...請檢查您的網路狀態！";
        }
        mAsyncTaskCallBack.onError(message);
    }
    private String getPhotoFileName(String mid, String img, String id) {
        String result="";
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss_");
        result += mid + dateFormat.format(date) + id + ".jpg";
        /*
        String[] token = img.split("|");
        for (int i=0;i<token.length;i++){
            if(i==token.length-1) result += mid + dateFormat.format(date) + (i+1) + ".jpg";
            else result += mid + dateFormat.format(date) + (i+1) + ".jpg|";
        }*/
        return result;
    }
}
