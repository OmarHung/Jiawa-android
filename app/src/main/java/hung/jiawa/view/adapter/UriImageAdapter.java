package hung.jiawa.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import hung.jiawa.R;

/**
 * Created by omar8 on 2017/5/24.
 */

public class UriImageAdapter extends RecyclerView.Adapter<UriImageAdapter.AddViewHolder> {
    public final String TAG = "JiaWa";
    public final String NAME = "ImageAdapter - ";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<Uri> myDataset = new ArrayList<>();

    //自定義監聽事件
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position);
    }

    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //初始化
    public UriImageAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public AddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddViewHolder(mLayoutInflater.inflate(R.layout.list_view_img, parent, false));
    }

    @Override
    public void onBindViewHolder(AddViewHolder holder, int position) {
        if (holder instanceof AddViewHolder) {
            Picasso.with(mContext)
                    .load(myDataset.get(position))
                    .into(holder.img);
        }
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }

    public class AddViewHolder extends RecyclerView.ViewHolder {
        private SimpleDraweeView img;

        AddViewHolder(View view) {
            super(view);
            img = (SimpleDraweeView) view.findViewById(R.id.img);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(getPosition());
                }
            });
        }
    }

    public void setAllImages(List<Uri> imgList) {
        myDataset.clear();
        myDataset = imgList;
        notifyDataSetChanged();
    }

    public List<Uri> getIamgeList() {
        return myDataset;
    }
}
