package hung.jiawa.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import hung.jiawa.R;
import hung.jiawa.presenter.IPostArticlePresenter;
import hung.jiawa.presenter.PostArticlePresenterCompl;
import hung.jiawa.view.IPostArticleView;

public class PostArticleActivity extends AppCompatActivity implements IPostArticleView, View.OnClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "PostArticleActivity - ";
    private ImageButton btn_send;
    private TextView btn_cancel;
    IPostArticlePresenter postArticlePresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_article);

        btn_send = (ImageButton) findViewById(R.id.btn_send);
        btn_cancel = (TextView) findViewById(R.id.btn_cancel);

        btn_send.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);

        postArticlePresenter = new PostArticlePresenterCompl(this, this);
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
    }
}
