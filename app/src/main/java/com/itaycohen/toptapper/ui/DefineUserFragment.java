package com.itaycohen.toptapper.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.itaycohen.toptapper.R;
import com.itaycohen.toptapper.models.UserModel;
import com.itaycohen.toptapper.repos.UserRepository;
import com.itaycohen.toptapper.ui.views.GridImagePicker;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

public class DefineUserFragment extends Fragment {

    private UserModel.Builder userBuilder = new UserModel.Builder();
    private TextInputEditText mUserEt;
    private TextInputLayout mUserLayout;

    public DefineUserFragment() {
        super(R.layout.fragment_define_user);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mUserEt = view.findViewById(R.id.username);
        mUserLayout = view.findViewById(R.id.usernameLayout);

        ((GridImagePicker)view.findViewById(R.id.gridImages)).setListener(drawableRes -> {
            userBuilder.setAvatarRes(drawableRes);
        });

        ((Button)view.findViewById(R.id.signupBtn)).setOnClickListener(btn -> {
            mUserLayout.setError(null);
            if (mUserEt.getText() != null)
                userBuilder.setUserName(mUserEt.getText().toString());
            if (isInputsValid()) {
                UserRepository.getInstance().setUserModel(userBuilder.build());
                Snackbar.make(btn, R.string.user_defined, Snackbar.LENGTH_INDEFINITE)
                        .setAction(R.string.continue_, v -> {
                            boolean continueToLevelsFrag = false;
                            if (getArguments() != null)
                                continueToLevelsFrag = getArguments().getBoolean(EntranceFragment.CONTINUE_TO_LEVELS_KEY, false);
                            if (continueToLevelsFrag) {
                                Navigation.findNavController(view).navigate(R.id.action_defineUserFragment_to_levelFragment);
                            } else {
                                Navigation.findNavController(view).popBackStack();
                            }
                        }).show();
            }
        });
    }

    private boolean isInputsValid() {
        boolean out = true;
        if (TextUtils.isEmpty(userBuilder.getUserName())) {
            mUserLayout.setError(getString(R.string.enter_name));
            out = false;
        }
        if (userBuilder.getAvatarRes() == View.NO_ID) {
            Toast.makeText(getContext(), R.string.select_an_avatar, Toast.LENGTH_SHORT).show();
            out = false;
        }
        return out;
    }
}
