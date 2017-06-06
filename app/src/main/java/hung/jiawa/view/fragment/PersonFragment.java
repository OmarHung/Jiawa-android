package hung.jiawa.view.fragment;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import hung.jiawa.LoadingDialog;
import hung.jiawa.R;
import hung.jiawa.presenter.IPersonPresenter;
import hung.jiawa.presenter.PersonPresenterCompl;
import hung.jiawa.view.IPersonView;
import hung.jiawa.view.activity.LoginActivity;
import hung.jiawa.view.activity.PersonalArticleActivity;
import hung.jiawa.view.activity.PersonalKeepActivity;
import hung.jiawa.view.activity.SavedLocationActivity;
import hung.jiawa.view.activity.SettingActivity;

public class PersonFragment extends Fragment implements IPersonView, View.OnClickListener {
    public final String TAG = "JiaWa";
    public final String NAME = "PersonFragment - ";
    private LoadingDialog mLoadingDialog = null;
    private SimpleDraweeView profile_img;
    private RelativeLayout profile, favorit, article, friend, setting, about, location, logout;
    private TextView tv_name;

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
        location = (RelativeLayout) view.findViewById(R.id.list_location);
        favorit = (RelativeLayout) view.findViewById(R.id.list_favorit);
        article = (RelativeLayout) view.findViewById(R.id.list_artcle);
        friend = (RelativeLayout) view.findViewById(R.id.list_friend);
        setting = (RelativeLayout) view.findViewById(R.id.list_setting);
        about = (RelativeLayout) view.findViewById(R.id.list_about);
        logout = (RelativeLayout) view.findViewById(R.id.list_logout);

        profile.setOnClickListener(this);
        location.setOnClickListener(this);
        favorit.setOnClickListener(this);
        article.setOnClickListener(this);
        friend.setOnClickListener(this);
        setting.setOnClickListener(this);
        about.setOnClickListener(this);
        logout.setOnClickListener(this);

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
    public void startLoginActivity() {
        startActivity(new Intent(getActivity(), LoginActivity.class));
        getActivity().finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.list_profile:
                break;
            case R.id.list_location:
                startActivity(new Intent(getActivity(), SavedLocationActivity.class));
                break;
            case R.id.list_favorit:
                startActivity(new Intent(getActivity(), PersonalKeepActivity.class));
                break;
            case R.id.list_artcle:
                startActivity(new Intent(getActivity(), PersonalArticleActivity.class));
                break;
            case R.id.list_friend:
                break;
            case R.id.list_setting:
                //startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.list_about:
                AlertDialog alertDialog=null;
                AlertDialog.Builder builder_about = new AlertDialog.Builder(getActivity());
                final LayoutInflater inflater = getActivity().getLayoutInflater();
                final View view = inflater.inflate(R.layout.dialog_about_view,null);
                builder_about.setView(view);
                alertDialog = builder_about.create();
                TextView tv_version = (TextView) view.findViewById(R.id.tv_version);
                String appVersion="";
                PackageManager manager = getActivity().getPackageManager();
                try { PackageInfo info = manager.getPackageInfo(getActivity().getPackageName(), 0);
                    appVersion = info.versionName; //版本名
                } catch (PackageManager.NameNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                tv_version.setText("版本:"+appVersion);
                alertDialog.show();
                break;
            case R.id.list_logout:
                AlertDialog.Builder builder_logout = new AlertDialog.Builder(getActivity());
                builder_logout.setTitle("您即將要登出");
                //builder.setMessage("您即將要登出");
                builder_logout.setPositiveButton("登出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        personPresenter.logout();
                    }
                });
                builder_logout.setNegativeButton("取消", null);
                builder_logout.create();
                builder_logout.show();
                break;
        }
    }
}
