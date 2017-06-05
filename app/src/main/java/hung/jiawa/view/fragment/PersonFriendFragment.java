package hung.jiawa.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hung.jiawa.R;

public class PersonFriendFragment extends Fragment {
    public final String TAG = "JiaWa";
    public final String NAME = "PersonFavoritFragment - ";
    public static PersonFriendFragment newInstance() {
        PersonFriendFragment fragmentFirst = new PersonFriendFragment();
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_empty, container, false); //實體化佈局
        return view;
    }
}
