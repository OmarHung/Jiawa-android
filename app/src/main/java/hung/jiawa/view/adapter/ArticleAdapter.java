package hung.jiawa.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import hung.jiawa.R;

/**
 * Created by omar8 on 2017/5/24.
 */

public class ArticleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public final String TAG = "JiaWa";
    public final String NAME = "ArticleAdapter - ";
    private Context mContext;
    private List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();

    //自定義監聽事件
    public static interface OnRecyclerViewItemClickListener {
        void onItemClick(String type, String aid);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //初始化
    public ArticleAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setMyDataset(List<Map<String, Object>> myDataset) {
        this.myDataset = myDataset;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.list_view_article, parent,false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof MyViewHolder){
            ((MyViewHolder) holder).tv_title.setText(myDataset.get(position).get("title").toString());
            ((MyViewHolder) holder).tv_content.setText(myDataset.get(position).get("content").toString());
            ((MyViewHolder) holder).tv_response.setText(myDataset.get(position).get("response").toString());
            ((MyViewHolder) holder).tv_forum.setText(myDataset.get(position).get("forum").toString());
        }
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_title, tv_content, tv_response, tv_forum;

        public MyViewHolder(View view) {
            super(view);
            tv_title = (TextView) view.findViewById(R.id.tv_title);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            tv_response = (TextView) view.findViewById(R.id.tv_response);
            tv_forum = (TextView) view.findViewById(R.id.tv_forum);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onItemClick(myDataset.get(getPosition()).get("type").toString(), myDataset.get(getPosition()).get("aid").toString());
                }
            });
        }
    }
    public void clearData() {
        myDataset.clear();
        notifyDataSetChanged();
    }
}
