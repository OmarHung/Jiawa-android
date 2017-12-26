package hung.jiawa.view.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import hung.jiawa.LoactionItem;
import hung.jiawa.R;

/**
 * Created by omar8 on 2017/5/30.
 */

public class SavedLocationAdapter extends RecyclerView.Adapter<SavedLocationAdapter.LocationViewHolder> {
    public final String TAG = "JiaWa";
    public final String NAME = "SavedLocationAdapter - ";
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private List<LoactionItem> myDataset;

    public SavedLocationAdapter(Context mContext, List<LoactionItem> items) {
        this.mContext = mContext;
        mLayoutInflater = LayoutInflater.from(mContext);
        myDataset = items;
    }

    @Override
    public LocationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LocationViewHolder(mLayoutInflater.inflate(R.layout.list_view_saved_location, parent, false));
    }

    @Override
    public void onBindViewHolder(LocationViewHolder holder, int position) {
        if (holder instanceof LocationViewHolder) {
            holder.name.setText(myDataset.get(position).getName());
            holder.latlng.setText(myDataset.get(position).getLatlng());
            holder.delete.setChecked(false);
            if(myDataset.get(position).showCheckBox()) {
                holder.delete.setVisibility(View.VISIBLE);
            } else {
                holder.delete.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return myDataset.size();
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {
        private TextView name, latlng;
        private CheckBox delete;
        LocationViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name_text);
            latlng = (TextView) view.findViewById(R.id.latlng_text);
            delete = (CheckBox) view.findViewById(R.id.check_delete);
            delete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    myDataset.get(getPosition()).setChecked(isChecked);
                }
            });
        }
    }
    public void setData(List<LoactionItem> item) {
        myDataset = item;
        notifyDataSetChanged();
    }

    public List<LoactionItem> getMyDataset() {
        return myDataset;
    }
}