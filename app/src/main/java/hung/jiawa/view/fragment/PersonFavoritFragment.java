package hung.jiawa.view.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import hung.jiawa.LoadingDialog;
import hung.jiawa.R;
import hung.jiawa.presenter.IPersonPresenter;
import hung.jiawa.presenter.PersonPresenterCompl;
import hung.jiawa.view.IPersonView;
import hung.jiawa.view.adapter.MainPageAdapter;

public class PersonFavoritFragment extends Fragment {
    public final String TAG = "JiaWa";
    public final String NAME = "PersonFavoritFragment - ";
    public static PersonFavoritFragment newInstance() {
        PersonFavoritFragment fragmentFirst = new PersonFavoritFragment();
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_person_favorit, container, false); //實體化佈局
        return view;
    }
}
