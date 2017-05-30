package hung.jiawa.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hung.jiawa.R;

/**
 * Created by omar8 on 2017/5/24.
 */

public class ResponseAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final String TAG = "JiaWa";
    public final String NAME = "ResponseAdapter - ";
    private Context mContext;
    private List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();

    //自定義監聽事件
    public interface OnRecyclerViewItemClickListener {
        void onResponseClick(String rid);
        void onLikeClick(String rid);
        void onProfileClick(String mid);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //初始化
    public ResponseAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setMyDataset(List<Map<String, Object>> myDataset) {
        this.myDataset = myDataset;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_view_response, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            ((MyViewHolder) holder).tv_floor.setText(position+1+"樓");
            ((MyViewHolder) holder).tv_time.setText(myDataset.get(position).get("time").toString());
            ((MyViewHolder) holder).tv_content.setText(myDataset.get(position).get("content").toString());
            //((MyViewHolder) holder).tv_response.setText(myDataset.get(position).get("response").toString());
            ((MyViewHolder) holder).tv_number_of_like.setText(myDataset.get(position).get("number_of_like").toString());
            if(!myDataset.get(position).get("img").toString().equals(""))
                ((MyViewHolder) holder).profile_img.setImageURI(Uri.parse(myDataset.get(position).get("img").toString()));
        }
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_floor, tv_content, tv_time, tv_response, tv_number_of_like;
        private ImageView check_like;
        private CircleImageView profile_img;
        public MyViewHolder(View view) {
            super(view);
            tv_floor = (TextView) view.findViewById(R.id.tv_floor);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_response = (TextView) view.findViewById(R.id.tv_response);
            tv_response.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onResponseClick(myDataset.get(getPosition()).get("rid").toString());
                }
            });
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_number_of_like = (TextView) view.findViewById(R.id.tv_number_of_like);
            check_like = (ImageView) view.findViewById(R.id.check_like);
            check_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onLikeClick(myDataset.get(getPosition()).get("rid").toString());
                }
            });
            profile_img = (CircleImageView) view.findViewById(R.id.profile_img);
            profile_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onProfileClick(myDataset.get(getPosition()).get("mid").toString());
                }
            });
        }
    }
    public void clearData() {
        myDataset.clear();
        notifyDataSetChanged();
    }
}
