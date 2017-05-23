package hung.jiawa.view.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

/**
 * Created by omar8 on 2017/3/22.
 */

public class MainPageAdapter extends FragmentStatePagerAdapter {
    private Context context;
    List<Fragment> fragments; //切換頁面的Fragments

    public MainPageAdapter(FragmentManager fm , List<Fragment> f, Context context) {
        super(fm);
        this.context=context;
        fragments=f;
    }

    @Override
    public int getCount() { //頁卡數量
        return fragments.size();
    }

    @Override
    public Fragment getItem(int position) { //回傳Frament頁卡
        return fragments.get(position); //從上方List<Fragment> fragments取得
    }
}