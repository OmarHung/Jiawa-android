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
    public int MODE_GET_RESPONSE=11;
    public int MODE_GET_PROFILE=12;
    public int MODE_POST_RESPONSE=13;
    public int MODE_CHECK_ARTICLE_LIKE=14;
    public int MODE_CHECK_RESPONSE_LIKE=15;
    public int MODE_GET_FAVORIT=16;
    public int MODE_CHECK_ARTICLE_KEEP=17;
    public int MODE_GET_PERSONAL_ARTICLE=18;
    public int MODE_UPDATE_PROFILE_NAME=19;
    public int MODE_UPDATE_PROFILE_IMAGE=20;
    public int MODE_UPLOAD_PROFILE_IMAGE=21;

    private static DBConnector mInstance;
    private RequestQueue mRequestQueue;
    private static Context mCtx;
    private String website = "http://zollow.why3s.tw/japi/";
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
        String url =website+"member_api/sign_in";
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

    public void executeSignUp(final String email, final String password, final String name, final String nick_name) {
        String url =website+"member_api/sign_up";
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
                map.put("nick_name", nick_name);
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
        String url =website+"forum_api/forums_list";
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
    public void executeGetArticle(final String forum_id, final String cate) {
        String url =website+"article_api/articles_list";
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
                map.put("forum_id", forum_id);
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
        String url =website+"server_api";
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
        String url =website+"article_api/spots_list";
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
    public void executeGetDetail(final String mid, final String id) {
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
                map.put("mid", mid);
                map.put("id", id);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //取得文章回覆
    public void executeGetResponse(final String mid, final String id) {
        String url =website+"Get_response.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_GET_RESPONSE, response);
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
                map.put("id", id);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //發表夾點
    public void executePostLocation(final String mid, final String title, final String content, final String latlng, final String city, final String type, final String number_of_machine, final String img) {
        String url =website+"Post_location.php";
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

    //發表回覆
    public void executePostResponse(final String mid, final String content, final String aid) {
        String url =website+"Post_response.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_POST_RESPONSE, response);
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
                map.put("content", content);
                map.put("aid", aid);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    public void executeUploadImage(final String mid, final String img, final String id) {
        String url =website+"Upload_image.php";
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

    //取得個人資訊
    public void executeGetProfile(final String id) {
        String url =website+"Get_profile.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_GET_PROFILE, response);
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

    //檢查文章是否LIKE
    public void executeCheckArticleLike(final String mid, final String aid) {
        String url =website+"Check_article_like.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_CHECK_ARTICLE_LIKE, response);
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
                map.put("aid", aid);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //檢查回文是否LIKE
    public void executeCheckResponseLike(final String mid, final String rid) {
        String url =website+"Check_response_like.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_CHECK_RESPONSE_LIKE, response);
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
                map.put("rid", rid);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //取得收藏的文章列表
    public void executeGetFavorit(final String mid) {
        String url =website+"Get_keep.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_GET_FAVORIT, response);
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
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //取得自己發佈的文章列表
    public void executeGetPersonalArticle(final String mid) {
        String url =website+"Get_personal_article.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_GET_PERSONAL_ARTICLE, response);
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
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    public void executeCheckArticleKeep(final String mid, final String aid) {
        String url =website+"Check_article_keep.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_CHECK_ARTICLE_KEEP, response);
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
                map.put("aid", aid);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //更新使用者名稱
    public void executeUpdateProfileName(final String mid, final String name) {
        String url =website+"Update_profile_name.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_UPDATE_PROFILE_NAME, response);
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
                map.put("name", name);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    //更新使用者頭像連結
    public void executeUpdateProfileImage(final String mid, final String img) {
        String url =website+"Update_profile_image.php";
        // Formulate the request and handle the response.
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_UPDATE_PROFILE_IMAGE, response);
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
                map.put("img", img);
                return map;
            }
        };
        DBConnector.getInstance(mCtx).addToRequestQueue(mStringRequest);
    }

    public void executeUploadProfileImage(final String mid, final String img) {
        String url =website+"Upload_profile_image.php";
        StringRequest mStringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        mAsyncTaskCallBack.onResult(MODE_UPLOAD_PROFILE_IMAGE, response);
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
                map.put("name", getPhotoFileName(mid,img,""));
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
