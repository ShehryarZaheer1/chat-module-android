package com.xevensolutions.chat.fragments;

import android.view.View;

import androidx.annotation.NonNull;

import com.xevensolutions.baseapp.fragments.BaseMediaPickerFragment;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.activities.ActivityChat;
import com.xevensolutions.chat.databinding.FragmentAttachmentPickerChatBinding;

public class FragmentAttachmentPicker extends BaseMediaPickerFragment<FragmentAttachmentPickerChatBinding> {
    @Override
    public FragmentAttachmentPickerChatBinding getViewBinding() {
        return FragmentAttachmentPickerChatBinding.inflate(getLayoutInflater());
    }

    @Override
    public int getFragmentName() {
        return R.string.attachment_picker;
    }

    @Override
    public void setListeners() {
        super.setListeners();
        binding.btnAttachment.setOnClickListener(view1 -> {
            pickOnlyImageFromGallery(false);
        });

    }

    @Override
    public void onCreateView(View view) {
        super.onCreateView(view);

    }

    @Override
    protected void onFilePathGenerated(@NonNull String path) {
        super.onFilePathGenerated(path);
        ((ActivityChat) getActivity()).sendFile(path);
    }
}
