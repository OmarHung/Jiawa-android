package hung.jiawa.view.fragment;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.gigamole.navigationtabstrip.NavigationTabStrip;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import hung.jiawa.LoadingDialog;
import hung.jiawa.R;
import hung.jiawa.presenter.IPersonPresenter;
import hung.jiawa.presenter.PersonPresenterCompl;
import hung.jiawa.view.IPersonView;
import hung.jiawa.view.activity.PersonalFavoritActivity;
import hung.jiawa.view.adapter.MainPageAdapter;

public class PersonFragment extends Fragment implements IPersonView, View.OnClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "PersonFragment - ";
    private LoadingDialog mLoadingDialog = null;
    private SimpleDraweeView profile_img;
    private RelativeLayout profile, favorit, article, response, friend, setting, about;
    private TextView tv_name;

    private String[] title = {"收藏", "文章", "朋友"};
    IPersonPresenter personPresenter;
    public static PersonFragment newInstance() {
        PersonFragment fragmentFirst = new PersonFragment();
        return fragmentFirst;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person, container, false); //實體化佈局

        profile_img = (SimpleDraweeView) view.findViewById(R.id.profile_img);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        profile = (RelativeLayout) view.findViewById(R.id.list_profile);
        favorit = (RelativeLayout) view.findViewById(R.id.list_favorit);
        article = (RelativeLayout) view.findViewById(R.id.list_artcle);
        response = (RelativeLayout) view.findViewById(R.id.list_response);
        friend = (RelativeLayout) view.findViewById(R.id.list_friend);
        setting = (RelativeLayout) view.findViewById(R.id.list_setting);
        about = (RelativeLayout) view.findViewById(R.id.list_about);

        profile.setOnClickListener(this);
        favorit.setOnClickListener(this);
        article.setOnClickListener(this);
        response.setOnClickListener(this);
        friend.setOnClickListener(this);
        setting.setOnClickListener(this);
        about.setOnClickListener(this);

        mLoadingDialog = new LoadingDialog(getActivity());
        personPresenter = new PersonPresenterCompl(getActivity(), this);
        return view;
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(getActivity(),msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showLoadingDialog() {
    }

    @Override
    public void setProfileImage(String imgUri) {
        profile_img.setImageURI(imgUri);
    }

    @Override
    public void setName(String name) {
        tv_name.setText(name);
    }

    @Override
    public void dismissLoadingDialog() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_profile:
                break;
            case R.id.list_favorit:
                startActivity(new Intent(getActivity(), PersonalFavoritActivity.class));
                break;
            case R.id.list_artcle:
                break;
            case R.id.list_response:
                break;
            case R.id.list_friend:
                break;
            case R.id.list_setting:
                break;
            case R.id.list_about:
                break;
        }
    }
}
