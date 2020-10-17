package com.itaycohen.toptapper.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.db.AppDatabase;
import com.itaycohen.toptapper.db.dao.UserDao;
import com.itaycohen.toptapper.db.room_helpers.UserAndRecords;
import com.itaycohen.toptapper.models.Records;
import com.itaycohen.toptapper.models.UserModel;
import com.itaycohen.toptapper.ui.models.Level;

import java.util.List;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class LeaderBoardFragment extends Fragment {

    private RecyclerView mRecycler;
    private TextView mHeadline;
    private UserDao userDao = AppDatabase.getInstance().userDao();
    public LeaderBoardFragment() {
        super(R.layout.fragment_leaderboard);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecycler = view.findViewById(R.id.leaderboardRecycler);
        mHeadline = view.findViewById(R.id.levelHeadline);
        RadioGroup mRadioGroup = view.findViewById(R.id.radioGroup);

        mHeadline.setText(R.string.newb);
        mRadioGroup.setOnCheckedChangeListener((radioGroup, btnId) -> {
            mHeadline.setText(getHeadlineFor(btnId));
            LeaderBoardAdapter adapter = ((LeaderBoardAdapter)mRecycler.getAdapter());
            if (adapter != null)
                adapter.setLevel(getLevelFor(btnId));
        });
        mRecycler.setAdapter(new LeaderBoardAdapter(userDao.getUsersAndRecords(), this, Level.BASIC));
    }

    private Level getLevelFor(@IdRes int btnId) {
        Level level = Level.BASIC;
        if (btnId == R.id.intermediateRadioBtn)
            level = Level.INTERMEDIATE;
        else if (btnId == R.id.expertRadioBtn)
            level = Level.EXPERT;
        return level;
    }

    @StringRes
    private int getHeadlineFor(@IdRes int btnId) {
        @StringRes
        int stringRes = R.string.newb;
        if (btnId == R.id.intermediateRadioBtn)
            stringRes = R.string.intermediate;
        else if (btnId == R.id.expertRadioBtn)
            stringRes = R.string.expert;
        return stringRes;
    }
}
