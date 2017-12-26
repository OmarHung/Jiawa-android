package hung.jiawa.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import hung.jiawa.ItemDAO;
import hung.jiawa.LoactionItem;
import hung.jiawa.R;
import hung.jiawa.presenter.ISavedLocationPresenter;
import hung.jiawa.presenter.SavedLocationPresenterCompl;
import hung.jiawa.view.ISavedLocationView;
import hung.jiawa.view.adapter.ImageAdapter;
import hung.jiawa.view.adapter.SavedLocationAdapter;

public class SavedLocationActivity extends AppCompatActivity implements ISavedLocationView, View.OnClickListener {
    private RecyclerView saved_location_list;
    private TextView btn_back;
    private ImageButton btn_delete;
    ISavedLocationPresenter savedLocationPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_location);
        saved_location_list = (RecyclerView) findViewById(R.id.saved_location_list);
        btn_delete = (ImageButton) findViewById(R.id.btn_delete);
        btn_back = (TextView) findViewById(R.id.btn_back);

        btn_delete.setOnClickListener(this);
        btn_back.setOnClickListener(this);

        savedLocationPresenter = new SavedLocationPresenterCompl(this, this, saved_location_list);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_delete:
                savedLocationPresenter.doCheckDel(btn_delete);
                break;
            case R.id.btn_back:
                finish();
        }
    }

    @Override
    public void toast(String msg) {
        Toast.makeText(this,msg,Toast.LENGTH_SHORT);
    }
}
