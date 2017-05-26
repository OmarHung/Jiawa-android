package hung.jiawa.view.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hung.jiawa.ImageListItem;
import hung.jiawa.R;

/**
 * Created by omar8 on 2017/5/24.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.AddViewHolder> {
    public final String TAG = "JiaWa";
    public final String NAME = "ImageAdapter - ";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private ArrayList<ImageListItem> myDataset = new ArrayList<ImageListItem>();

    //自定義監聽事件
    public interface OnRecyclerViewItemClickListener {
        void onItemClick(int position, int id, int type, File img);
        void onItemLongClick(int position, int id, int type);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //初始化
    public ImageAdapter(Context mContext) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        addAddImageView();
    }

    @Override
    public AddViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new AddViewHolder(mLayoutInflater.inflate(R.layout.list_view_img, parent, false));
    }


    @Override
    public void onBindViewHolder(AddViewHolder holder, int position) {
        if(holder instanceof AddViewHolder){
            if(myDataset.get(position).getType()!=1) {
                holder.img.setImageURI(Uri.fromFile(myDataset.get(position).getImage()));
            }else {
                holder.img.setImageResource(R.mipmap.ic_add);
            }
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
                    mOnItemClickListener.onItemClick(getPosition(), myDataset.get(getPosition()).getId(), myDataset.get(getPosition()).getType(), myDataset.get(getPosition()).getImage());
                }
            });
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onItemLongClick(getPosition(), myDataset.get(getPosition()).getId(), myDataset.get(getPosition()).getType());
                    return true;
                }
            });
        }
    }
    public void setAllImages(ArrayList<ImageListItem> imgList) {
        myDataset.clear();
        myDataset = imgList;
        notifyDataSetChanged();
    }
    public void clearData() {
        myDataset.clear();
        notifyDataSetChanged();
    }
    //添加一个item
    public void addItem(ImageListItem item, int position) {
        if(myDataset.size()==5) {
            myDataset.set(0, item);
        }else {
            myDataset.add(position, item);
            notifyItemInserted(position);
        }
        notifyDataSetChanged();
        //Log.d(TAG, NAME+"addItem : "+myDataset.size());
    }

    private void addAddImageView() {
        myDataset.add(0, new ImageListItem(null, 0, 1));
        notifyItemInserted(0);
        notifyDataSetChanged();
    }
    //删除一个item
    public void removeItem(final int position) {
        myDataset.remove(position);
        notifyItemRemoved(position);
        //若第一個item是照片
        if (myDataset.get(0).getType() == 0) {
            addAddImageView();
        }
        notifyDataSetChanged();
    }

    //移動add item
    public void moveItemToEnd() {
        notifyItemMoved(myDataset.size()-2, myDataset.size()-1);
    }

    public List<Uri> getIamgeList() {
        List<Uri> images = new ArrayList<>();
        int i=0;
        if(myDataset.get(0).getType()==1) i=1;
        for(; i<myDataset.size(); i++) {
            images.add(Uri.fromFile(myDataset.get(i).getImage()));
        }
        return images;
    }

    public List<File> getIamgeFileList() {
        List<File> images = new ArrayList<>();
        int i=0;
        if(myDataset.get(0).getType()==1) i=1;
        for(; i<myDataset.size(); i++) {
            images.add(myDataset.get(i).getImage());
        }
        return images;
    }
}
