package hung.jiawa.view.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import hung.jiawa.R;

public class PersonFragment extends Fragment {
    public final String TAG = "Prototype";
    public final String NAME = "ChallengeFragment - ";
    public static PersonFragment newInstance() {
        PersonFragment fragmentFirst = new PersonFragment();
        return fragmentFirst;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_empty, container, false); //實體化佈局
        return view;
    }

    public void load() {
        Log.i(TAG, NAME+"load()............");

    }
}
