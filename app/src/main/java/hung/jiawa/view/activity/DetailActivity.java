package hung.jiawa.view.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import hung.jiawa.R;
import hung.jiawa.presenter.DetailPresenterCompl;
import hung.jiawa.presenter.IDetailPresenter;
import hung.jiawa.view.IDetailView;

public class DetailActivity extends AppCompatActivity implements IDetailView, View.OnClickListener {
    private Button btn_back;
    private TextView tv_title;
    private ImageView img_picture;
    IDetailPresenter detailPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Bundle bundle = getIntent().getExtras();
        String id = bundle.getString("id");

        btn_back = (Button) findViewById(R.id.btn_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        img_picture = (ImageView) findViewById(R.id.img_picture);

        btn_back.setOnClickListener(this);

        detailPresenter = new DetailPresenterCompl(this, this, id);
    }

    @Override
    public void onClick(View v) {
        finish();
    }

    @Override
    public void toast(String msg) {

    }

    @Override
    public void showDetail(String id, String title, String img, String v, String v1, String city_id, String type_id) {
        tv_title.setText(title);
    }

    @Override
    public void showImage(String imgUrl) {
        Picasso.with(this)
                .load(imgUrl)
                .into(img_picture);
    }
}
