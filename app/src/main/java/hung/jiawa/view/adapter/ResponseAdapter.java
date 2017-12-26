package hung.jiawa.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

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
    private static final int HINT_TYPE = 1;
    private static final int RESPONSE_RESPONSE_TYPE = 2;
    private Context mContext;
    private int itemCount=0;
    private List<Map<String, Object>> myDataset = new ArrayList<Map<String, Object>>();
    private List<Map<String, Object>> responseDataset = new ArrayList<Map<String, Object>>();

    //自定義監聽事件
    public interface OnRecyclerViewItemClickListener {
        void onResponseClick(String rid);
        void onLikeClick(String rid);
        void onProfileClick(String mid);
        void onLoadResponseCkick(String rid, int position);
    }
    private OnRecyclerViewItemClickListener mOnItemClickListener = null;

    public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    //自定義監聽事件
    public interface OnLoadedCallBack {
        void onLoaded();
    }
    private OnLoadedCallBack mOnLoadedCallBack = null;

    public void setOnLoadedCallBack(OnLoadedCallBack callBack) {
        mOnLoadedCallBack = callBack;
    }

    //初始化
    public ResponseAdapter(Context mContext) {
        this.mContext = mContext;
    }
    public void setMyDataset(List<Map<String, Object>> myDataset) {
        Log.d(TAG,NAME+"setMyDataset ");
        itemCount=0;
        this.myDataset.clear();
        this.myDataset = myDataset;
        notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG,NAME+"onCreateViewHolder : "+viewType);
        View view;
        switch (viewType) {
            case RESPONSE_RESPONSE_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.list_view_response_response, parent,false);
                return new ResponseViewHolder(view);
            case HINT_TYPE:
                view = LayoutInflater.from(mContext).inflate(R.layout.list_view_response_hint, parent,false);
                return new HintViewHolder(view);
            default:
                view = LayoutInflater.from(mContext).inflate(R.layout.list_view_response, parent,false);
                return new MyViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG,NAME+"onBindViewHolder : "+position);
        if (position == RESPONSE_RESPONSE_TYPE) {
            itemCount++;
            if(holder instanceof ResponseViewHolder){
                ((ResponseViewHolder) holder).tv_name.setText(responseDataset.get(position).get("name").toString());
                ((ResponseViewHolder) holder).tv_time.setText(responseDataset.get(position).get("time").toString());
                ((ResponseViewHolder) holder).tv_content.setText(responseDataset.get(position).get("content").toString());
                //((MyViewHolder) holder).tv_response.setText(myDataset.get(position).get("response").toString());
                ((ResponseViewHolder) holder).tv_number_of_like.setText(responseDataset.get(position).get("number_of_like").toString());
                if(!responseDataset.get(position).get("img").toString().equals(""))
                    ((ResponseViewHolder) holder).profile_img.setImageURI(responseDataset.get(position).get("img").toString());
            }
        }else if(position == HINT_TYPE) {
            itemCount++;
            if(holder instanceof HintViewHolder){
                ((HintViewHolder) holder).btn_response.setText("查看"+myDataset.get(position).get("count").toString()+"則回覆");
            }
        }else {
            itemCount++;
            if(holder instanceof MyViewHolder){
                ((MyViewHolder) holder).tv_floor.setText(position+1+"樓");
                ((MyViewHolder) holder).tv_name.setText(myDataset.get(position).get("name").toString());
                ((MyViewHolder) holder).tv_time.setText(myDataset.get(position).get("time").toString());
                ((MyViewHolder) holder).tv_content.setText(myDataset.get(position).get("content").toString());
                //((MyViewHolder) holder).tv_response.setText(myDataset.get(position).get("response").toString());
                ((MyViewHolder) holder).tv_number_of_like.setText(myDataset.get(position).get("number_of_like").toString());
                if(!myDataset.get(position).get("img").toString().equals(""))
                    ((MyViewHolder) holder).profile_img.setImageURI(myDataset.get(position).get("img").toString());
            }
            if(itemCount==myDataset.size()) {
                mOnLoadedCallBack.onLoaded();
            }
        }
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_floor, tv_content, tv_time, btn_response, tv_number_of_like, tv_name;
        private ImageView check_like;
        private SimpleDraweeView profile_img;
        public MyViewHolder(View view) {
            super(view);
            Log.d(TAG,NAME+"MyViewHolder ");
            tv_floor = (TextView) view.findViewById(R.id.tv_floor);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            btn_response = (TextView) view.findViewById(R.id.btn_response);
            btn_response.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onResponseClick(myDataset.get(getPosition()).get("rid").toString());
                }
            });
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_number_of_like = (TextView) view.findViewById(R.id.tv_number_of_like);
            //check_like = (ImageView) view.findViewById(R.id.check_like);
            check_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onLikeClick(myDataset.get(getPosition()).get("rid").toString());
                }
            });
            profile_img = (SimpleDraweeView) view.findViewById(R.id.profile_img);
            profile_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onProfileClick(myDataset.get(getPosition()).get("mid").toString());
                }
            });
        }
    }

    class ResponseViewHolder extends RecyclerView.ViewHolder {
        private TextView tv_content, tv_time, btn_response, tv_number_of_like, tv_name;
        private ImageView check_like;
        private SimpleDraweeView profile_img;
        public ResponseViewHolder(View view) {
            super(view);
            Log.d(TAG,NAME+"ResponseViewHolder ");
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_content = (TextView) view.findViewById(R.id.tv_content);
            btn_response = (TextView) view.findViewById(R.id.btn_response);
            btn_response.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onResponseClick(myDataset.get(getPosition()).get("rrid").toString());
                }
            });
            tv_time = (TextView) view.findViewById(R.id.tv_time);
            tv_number_of_like = (TextView) view.findViewById(R.id.tv_number_of_like);
            //check_like = (ImageView) view.findViewById(R.id.check_like);
            check_like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onLikeClick(myDataset.get(getPosition()).get("rrid").toString());
                }
            });
            profile_img = (SimpleDraweeView) view.findViewById(R.id.profile_img);
            profile_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onProfileClick(myDataset.get(getPosition()).get("mid").toString());
                }
            });
        }
    }
    class HintViewHolder extends RecyclerView.ViewHolder {
        private TextView btn_response;
        public HintViewHolder(View view) {
            super(view);
            Log.d(TAG,NAME+"HintViewHolder ");
            btn_response = (TextView) view.findViewById(R.id.btn_response);
            btn_response.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onLoadResponseCkick(myDataset.get(getPosition()).get("rid").toString(), getPosition());
                    //notifyItemRangeRemoved(getPosition(), 1); //移除hint
                    //notifyItemRangeInserted(getPosition(), Integer.valueOf(myDataset.get(getPosition()).get("count").toString()));
                }
            });
        }
    }
    public void clearData() {
        myDataset.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(myDataset.get(position).get("type").toString().equals("normal")) {
            if (Integer.valueOf(myDataset.get(position).get("count").toString()) > 0) {
                return HINT_TYPE;
            }  else {
                return 0;
            }
        }else {
            return HINT_TYPE;
        }
    }
}
