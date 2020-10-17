package com.itaycohen.toptapper.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.db.room_helpers.UserAndRecords;
import com.itaycohen.toptapper.ui.models.Level;

import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.ViewHolder> {

    private List<UserAndRecords> mData;
    @NonNull
    private Level level;
    private Comparator<UserAndRecords> sortComparator = (r1, r2) -> {
        if (r1.records == null || r2.records == null)
            return 0;

        int out = 0;
        switch (level) {
            case BASIC:
                out = Integer.compare(r1.records.beginnerModeRecord, r2.records.beginnerModeRecord);
                break;
            case INTERMEDIATE:
                out =  Integer.compare(r1.records.intermediateModeRecord, r2.records.intermediateModeRecord);
                break;
            case EXPERT:
                out = Integer.compare(r1.records.expertModeRecord, r2.records.expertModeRecord);
                break;
        }
        return out * -1; // Descending
    };

    public LeaderBoardAdapter(
            LiveData<List<UserAndRecords>> liveData,
            LifecycleOwner lifecycleOwner,
            @NonNull Level level
    ) {
        setLevel(level);
        Observer<? super List<UserAndRecords>> userAndRecordsObserver = (Observer<List<UserAndRecords>>) userAndRecords -> {
            if (userAndRecords != null && userAndRecords.size() > 0) {
                Iterator<UserAndRecords> iterator = userAndRecords.iterator();
                UserAndRecords o;
                while (iterator.hasNext()) {
                    o = iterator.next();
                    if (o.records == null)
                        iterator.remove();
                }
                if (sortComparator != null)
                    Collections.sort(userAndRecords, sortComparator);
            }
            this.mData = userAndRecords;
            notifyDataSetChanged();
        };
        liveData.observe(lifecycleOwner, userAndRecordsObserver);
    }

    public void setLevel(@NonNull Level level) {
        this.level = level;
        if (mData != null) {
            Collections.sort(mData, sortComparator);
            notifyDataSetChanged();
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.leadboard_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(level, mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView scoreTv;
        private TextView rank;
        private ImageView avatar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.user_name_text);
            scoreTv = itemView.findViewById(R.id.user_points_text);
            rank = itemView.findViewById(R.id.user_rank_text);
            avatar = itemView.findViewById(R.id.user_avatar_image);
        }

        public void bindTo(Level level, UserAndRecords userAndRecords) {
            name.setText(userAndRecords.userModel.userName);
            int score;
            switch (level) {
                case BASIC:
                    score = userAndRecords.records.beginnerModeRecord;
                    break;
                case INTERMEDIATE:
                    score = userAndRecords.records.intermediateModeRecord;
                    break;
                case EXPERT:
                    score = userAndRecords.records.expertModeRecord;
                    break;
                default:
                    throw new IllegalArgumentException("support the new Level");
            }
            scoreTv.setText(itemView.getResources().getString(R.string.points, score));
            rank.setText(String.format("#%s", getAdapterPosition()+1));
            avatar.setBackgroundResource(userAndRecords.userModel.avatarRes);
        }
    }
}
