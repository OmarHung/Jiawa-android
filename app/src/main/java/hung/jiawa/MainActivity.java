package hung.jiawa;

import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import hung.jiawa.presenter.IMainPresenter;
import hung.jiawa.presenter.MainPresenterCompl;
import hung.jiawa.view.IMainView;
import hung.jiawa.view.adapter.MainPageAdapter;
import hung.jiawa.view.fragment.ForumFragment;
import hung.jiawa.view.fragment.MapFragment;
import hung.jiawa.view.fragment.PersonFragment;

public class MainActivity extends AppCompatActivity implements IMainView, OnTabSelectListener, ViewPager.OnPageChangeListener, FragmentCallBack {
    private BottomBar mBottomBar;
    private ViewPager mPager;
    ForumFragment forumFragment;
    MapFragment mapFragment;
    PersonFragment personFragment;
    List<Fragment> mFragment = new ArrayList<Fragment>(); //填充要的Fragment頁卡
    private MainPageAdapter mainPageAdapter;
    IMainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find view
        mPager = (ViewPager) findViewById(R.id.pager);
        mBottomBar = (BottomBar) findViewById(R.id.bottomBar);

        //set listener
        mBottomBar.setOnTabSelectListener(this);
        mPager.setOnPageChangeListener(this);

        //init
        mainPresenter = new MainPresenterCompl(this, this);
        addFragment();
    }

    private void addFragment() {
        forumFragment = new ForumFragment();
        mapFragment = new MapFragment();
        personFragment = new PersonFragment();

        mFragment.add(forumFragment);
        mFragment.add(mapFragment);
        mFragment.add(personFragment);

        mainPageAdapter = new MainPageAdapter(getSupportFragmentManager(), mFragment, this);
        if (mPager != null) {
            mPager.setOffscreenPageLimit(2);
            mPager.setAdapter(mainPageAdapter);  //設定Adapter給viewPager
        }
        mPager.setCurrentItem(1);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        int currentItem = mPager.getCurrentItem();
        switch (currentItem) {
            case 0:
                forumFragment.load();
                break;
            case 1:
                mapFragment.load();
                break;
            case 2:
                personFragment.load();
                break;
        }
        mBottomBar.selectTabAtPosition(currentItem);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onTabSelected(@IdRes int tabId) {
        switch (tabId) {
            case R.id.bottombar_forum:
                mPager.setCurrentItem(0);
                break;
            case R.id.bottombar_map:
                mPager.setCurrentItem(1);
                break;
            case R.id.bottombar_person:
                mPager.setCurrentItem(2);
                break;
        }
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void goToShareForum() {
        mPager.setCurrentItem(0);
    }
}
