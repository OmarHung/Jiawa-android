package hung.jiawa.view.activity;

import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import hung.jiawa.LoadingDialog;
import hung.jiawa.R;
import hung.jiawa.presenter.DetailPresenterCompl;
import hung.jiawa.presenter.IDetailPresenter;
import hung.jiawa.view.IDetailView;

public class DetailActivity extends AppCompatActivity implements IDetailView, View.OnClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "DetailActivity - ";
    private RecyclerView recyclerView;
    private EditText edt_content;
    private TextView tv_toolTitle, btn_back;
    private ImageButton btn_more;
    private String aid, strContent="";
    private LoadingDialog mLoadingDialog=null;
    private AlertDialog alertDialog=null;
    IDetailPresenter detailPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        aid = bundle.getString("aid");

        recyclerView= (RecyclerView) findViewById(R.id.recyclerView);
        btn_more    = (ImageButton) findViewById(R.id.btn_more);
        btn_back    = (TextView) findViewById(R.id.btn_back);
        tv_toolTitle= (TextView) findViewById(R.id.tv_toolTitle);

        btn_back.setOnClickListener(this);
        btn_more.setOnClickListener(this);

        mLoadingDialog  = new LoadingDialog(this);
        detailPresenter = new DetailPresenterCompl(this, this, aid, recyclerView);
    }
    @Override
    protected void onResume() {
        super.onResume();
        detailPresenter.onResume();
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setToolBarTitle(String title) {
        tv_toolTitle.setText(title);
    }

    @Override
    public void setAid(String aid) {
        this.aid = aid;
    }

    @Override
    public void showLoadingDialog() {
        mLoadingDialog.show();
    }

    @Override
    public void showResponseDialog(final String type, final String rid, int forFloor) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final LayoutInflater inflater = getLayoutInflater();
        final View view = inflater.inflate(R.layout.dialog_response_view,null);
        builder.setView(view);
        alertDialog = builder.create();
        SimpleDraweeView profile_img = (SimpleDraweeView) view.findViewById(R.id.profile_img);
        TextView tv_floor = (TextView) view.findViewById(R.id.tv_floor);
        TextView btn_send = (TextView) view.findViewById(R.id.btn_send);
        edt_content = (EditText) view.findViewById(R.id.edt_content);
        edt_content.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.d(TAG,NAME+"beforeTextChanged : "+s);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.d(TAG,NAME+"onTextChanged : "+s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                strContent = edt_content.getText().toString();
            }
        });
        edt_content.setText(strContent);
        edt_content.setSelection(edt_content.getText().toString().length());
        String uri = detailPresenter.getProfileImage();
        if(!uri.equals("")) profile_img.setImageURI(uri);
        if(forFloor>0) tv_floor.setText("回覆"+forFloor+"樓");
        else tv_floor.setText("發布留言");
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, NAME+"btn_send  : " + rid+"  type="+type);
                strContent = edt_content.getText().toString();
                if(type.equals("a")) detailPresenter.postResponse(type, aid, strContent);
                else if(type.equals("r")) detailPresenter.postResponse(type, rid, strContent);
            }
        });
        alertDialog.show();
    }

    @Override
    public void dismissLoadingDialog() {
        mLoadingDialog.dismiss();
    }

    @Override
    public void dismissResponseDialog() {
        if(alertDialog!=null && alertDialog.isShowing()) alertDialog.dismiss();
        strContent="";
        detailPresenter.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_more:
                break;
        }
    }
}
