package hung.jiawa.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

import hung.jiawa.R;
import hung.jiawa.presenter.ForumPresenterCompl;
import hung.jiawa.presenter.IForumPresenter;
import hung.jiawa.view.IForumView;
import hung.jiawa.view.adapter.ArticleAdapter;

public class ForumFragment extends Fragment implements IForumView, View.OnClickListener, AdapterView.OnItemSelectedListener{
    private ImageButton btn_post, btn_search, btn_notification;
    private RelativeLayout btn_new, btn_hot;
    private TextView tv_new, tv_hot;
    private Spinner spinner_forum;
    private RecyclerView article_list;
    private ArticleAdapter articleAdapter;
    IForumPresenter forumPresenter;
    public static ForumFragment newInstance() {
        ForumFragment fragmentFirst = new ForumFragment();
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false); //實體化佈局

        //find view
        btn_post = (ImageButton) view.findViewById(R.id.btn_post);
        btn_search = (ImageButton) view.findViewById(R.id.btn_search);
        btn_notification = (ImageButton) view.findViewById(R.id.btn_notification);
        btn_new = (RelativeLayout) view.findViewById(R.id.btn_new);
        btn_hot = (RelativeLayout) view.findViewById(R.id.btn_hot);
        tv_new = (TextView) view.findViewById(R.id.tv_new);
        tv_hot = (TextView) view.findViewById(R.id.tv_hot);
        spinner_forum = (Spinner) view.findViewById(R.id.spinner_forum);
        article_list = (RecyclerView) view.findViewById(R.id.article_list);

        //set listener
        btn_post.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_notification.setOnClickListener(this);
        btn_new.setOnClickListener(this);
        btn_hot.setOnClickListener(this);
        spinner_forum.setOnItemSelectedListener(this);

        //init
        articleAdapter = new ArticleAdapter(getActivity());
        article_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        forumPresenter = new ForumPresenterCompl(getActivity(), this);

        return view;
    }

    public void load() {
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void setForumList(List<Map<String, Object>> list) {
        String[] forum = new String[list.size()+1];
        forum[0] = "全部";
        for(int i=0;i<list.size();i++) {
            forum[i+1] = list.get(i).get("fName").toString();
        }
        ArrayAdapter<String> forumList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                forum);
        spinner_forum.setAdapter(forumList);
    }

    @Override
    public void setArticleList(List<Map<String, Object>> article) {
        articleAdapter.setMyDataset(article);
        article_list.setAdapter(articleAdapter);
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void setNoArticle() {
        articleAdapter.setMyDataset(null);
        article_list.setAdapter(articleAdapter);
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post:
                break;
            case R.id.btn_search:
                break;
            case R.id.btn_notification:
                break;
            case R.id.btn_new:
                btn_new.setBackgroundResource(R.drawable.seleted_new);
                btn_hot.setBackgroundResource(0);
                tv_new.setTextColor(getResources().getColor(R.color.accent));
                tv_hot.setTextColor(getResources().getColor(R.color.primary));
                break;
            case R.id.btn_hot:
                btn_hot.setBackgroundResource(R.drawable.seleted_hot);
                btn_new.setBackgroundResource(0);
                tv_hot.setTextColor(getResources().getColor(R.color.accent));
                tv_new.setTextColor(getResources().getColor(R.color.primary));
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        forumPresenter.showArticle(position);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}