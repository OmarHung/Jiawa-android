package hung.jiawa.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

import hung.jiawa.LoadingDialog;
import hung.jiawa.R;
import hung.jiawa.presenter.ForumPresenterCompl;
import hung.jiawa.presenter.IForumPresenter;
import hung.jiawa.view.IForumView;
import hung.jiawa.view.activity.DetailActivity;
import hung.jiawa.view.activity.PostArticleActivity;
import hung.jiawa.view.activity.PostLocationActivity;
import hung.jiawa.view.adapter.ArticleAdapter;
import hung.jiawa.widget.XCDropDownListView;

public class ForumFragment extends Fragment implements IForumView, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, AdapterView.OnItemSelectedListener, PopupMenu.OnMenuItemClickListener, ArticleAdapter.OnRecyclerViewItemClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "ForumFragment - ";
    private ImageButton btn_post, btn_search, btn_notification;
    private RelativeLayout btn_new, btn_hot;
    private TextView tv_new, tv_hot;
    private Spinner spinner_forum;
    private RecyclerView article_list;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ArticleAdapter articleAdapter;
    private PopupMenu popupmenu;
    private int nowForum;
    private int nowCate=0;
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
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        popupmenu = new PopupMenu(getActivity(), btn_post);
        articleAdapter = new ArticleAdapter(getActivity());

        //set listener
        btn_post.setOnClickListener(this);
        btn_search.setOnClickListener(this);
        btn_notification.setOnClickListener(this);
        btn_new.setOnClickListener(this);
        btn_hot.setOnClickListener(this);
        spinner_forum.setOnItemSelectedListener(this);
        popupmenu.setOnMenuItemClickListener(this);
        swipeRefreshLayout.setOnRefreshListener(this);
        articleAdapter.setOnItemClickListener(this);

        //init
        article_list.setLayoutManager(new LinearLayoutManager(getActivity()));
        forumPresenter = new ForumPresenterCompl(getActivity(), this);

        return view;
    }

    public void load() {
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setForumList(List<Map<String, Object>> list) {
        String[] forum = new String[list.size()+1];
        forum[0] = "全部";
        for(int i=0;i<list.size();i++) {
            popupmenu.getMenu().add(0, i+1, 0, list.get(i).get("fName").toString());//.getMenu().add(list.get(i).get("fName").toString());
            forum[i+1] = list.get(i).get("fName").toString();
        }
        ArrayAdapter<String> forumList = new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_spinner_dropdown_item,
                forum);
        spinner_forum.setAdapter(forumList);
    }

    @Override
    public void setArticleList(List<Map<String, Object>> article) {
        swipeRefreshLayout.setRefreshing(false);
        articleAdapter.setMyDataset(article);
        article_list.setAdapter(articleAdapter);
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void setNoArticle() {
        swipeRefreshLayout.setRefreshing(false);
        articleAdapter.setMyDataset(null);
        article_list.setAdapter(articleAdapter);
        articleAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_post:
                showPopupMenu();
                break;
            case R.id.btn_search:
                break;
            case R.id.btn_notification:
                break;
            case R.id.btn_new:
                if(nowCate!=1) {
                    swipeRefreshLayout.setRefreshing(true);
                    nowCate = 1;
                    articleAdapter.clearData();
                    btn_new.setBackgroundResource(R.drawable.seleted_new);
                    btn_hot.setBackgroundResource(0);
                    tv_new.setTextColor(getResources().getColor(R.color.accent));
                    tv_hot.setTextColor(getResources().getColor(R.color.primary));
                    forumPresenter.showArticle(nowForum, nowCate);
                }
                break;
            case R.id.btn_hot:
                if(nowCate!=0) {
                    swipeRefreshLayout.setRefreshing(true);
                    nowCate = 0;
                    articleAdapter.clearData();
                    btn_hot.setBackgroundResource(R.drawable.seleted_hot);
                    btn_new.setBackgroundResource(0);
                    tv_hot.setTextColor(getResources().getColor(R.color.accent));
                    tv_new.setTextColor(getResources().getColor(R.color.primary));
                    forumPresenter.showArticle(nowForum, nowCate);
                }
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, NAME+"onItemSelected : "+position);
        articleAdapter.clearData();
        swipeRefreshLayout.setRefreshing(true);
        nowForum=position;
        forumPresenter.showArticle(position, nowCate);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void showPopupMenu() {
        popupmenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Log.d(TAG, NAME+"onMenuItemClick :" + item.getItemId());
        switch (item.getItemId()) {
            case 1:
                Intent intent = new Intent(getActivity(), PostLocationActivity.class);
                startActivity(intent);
                break;
            case 2:
                break;
            case 3:
                break;
        }
        //Intent intent = new Intent(getActivity(), PostArticleActivity.class);
        //startActivity(intent);
        return false;
    }

    @Override
    public void onRefresh() {
        Log.d(TAG, NAME+"onRefresh");
        forumPresenter.showArticle(nowForum, nowCate);
    }

    @Override
    public void onItemClick(String fid, String aid) {
        if(fid.equals("1")) {
            //toast("fid = "+fid+" ,  aid = "+aid);
            Intent intent = new Intent(getActivity(), DetailActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("lid", aid);
            intent.putExtras(bundle);
            startActivity(intent);
        }else {
            //toast("fid = "+fid+" ,  aid = "+aid);
        }
    }
}
