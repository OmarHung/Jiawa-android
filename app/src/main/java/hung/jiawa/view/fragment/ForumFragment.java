package hung.jiawa.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.List;
import java.util.Map;

import hung.jiawa.R;
import hung.jiawa.presenter.ForumPresenterCompl;
import hung.jiawa.presenter.IForumPresenter;
import hung.jiawa.presenter.MapPresenterCompl;
import hung.jiawa.view.IForumView;

public class ForumFragment extends Fragment implements IForumView, View.OnClickListener{
    public final String TAG = "JiaWa";
    public final String NAME = "ForumFragment - ";
    private Button btn_post;
    private Spinner spinner_forum;
    IForumPresenter forumPresenter;
    public static ForumFragment newInstance() {
        ForumFragment fragmentFirst = new ForumFragment();
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_forum, container, false); //實體化佈局

        btn_post = (Button) view.findViewById(R.id.button_post);
        spinner_forum = (Spinner) view.findViewById(R.id.spinner_forum);


        btn_post.setOnClickListener(this);

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
    public void onClick(View v) {

    }
}
