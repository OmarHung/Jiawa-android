package hung.jiawa.view.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.dinuscxj.refresh.RecyclerRefreshLayout;

import hung.jiawa.R;
import hung.jiawa.presenter.IPersonalArticlePresenter;
import hung.jiawa.presenter.PersonalArticlePresenterCompl;
import hung.jiawa.view.IPersonalArticleView;

public class PersonalArticleActivity extends AppCompatActivity implements IPersonalArticleView, View.OnClickListener, RecyclerRefreshLayout.OnRefreshListener {
    private RecyclerView article_list;
    private TextView tv_toolTitle, btn_back, tv_label;
    private ImageButton btn_more;
    private RecyclerRefreshLayout swipeRefreshLayout;
    IPersonalArticlePresenter personalArticlePresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_article);

        article_list = (RecyclerView) findViewById(R.id.article_list);
        btn_more = (ImageButton) findViewById(R.id.btn_more);
        btn_back = (TextView) findViewById(R.id.btn_back);
        tv_label = (TextView) findViewById(R.id.tv_label);
        tv_toolTitle = (TextView) findViewById(R.id.tv_toolTitle);
        swipeRefreshLayout = (RecyclerRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        btn_back.setOnClickListener(this);
        btn_more.setOnClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);

        tv_toolTitle.setText("文章");
        personalArticlePresenter = new PersonalArticlePresenterCompl(this,this);
        personalArticlePresenter.initRecyclerView(article_list);
    }

    @Override
    protected void onResume() {
        super.onResume();
        personalArticlePresenter.getArticle();
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void startDetailActivity(String aid) {
        Intent intent = new Intent(this, DetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("lid", aid);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void setNoArticle(String msg) {
        tv_label.setText(msg);
    }

    @Override
    public void showLoadingDialog() {
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void dismissLoadingDialog() {
        swipeRefreshLayout.setRefreshing(false);
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

    @Override
    public void onRefresh() {
        personalArticlePresenter.getArticle();
    }
}
