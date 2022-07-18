package com.xevensolutions.chat.chatAdapter.viewHolders;

import android.content.Context;
import android.os.Trace;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;


import com.xevensolutions.baseapp.utils.FileUtils;
import com.xevensolutions.baseapp.utils.GenericUtils;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.R2;
import com.xevensolutions.chat.chatAdapter.ChatAdapterListener;
import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.utils.DataProvider;

import java.util.ArrayList;

import butterknife.BindView;

import static com.xevensolutions.baseapp.utils.TextUtils.isStringEmpty;

public class VhDocumentMessage extends BaseMessageViewHolder {


    @BindView(R2.id.pb_attachment)
    ProgressBar pbAttachment;
    @BindView(R2.id.tv_document_name)
    TextView tvDocument;
    @BindView(R2.id.cont_document)
    ConstraintLayout contDocument;

    public VhDocumentMessage(View itemView, Context context, ChatAdapterListener chatAdapterListener) {
        super(context, chatAdapterListener, itemView);
    }

    @Override
    public void onBindData(ChatMessage val, ChatMessage nextMessage) {
        super.onBindData(val, nextMessage);
        Trace.beginSection("onBind Document message");
        String fileName = null;
        if (!isStringEmpty(val.getFileName()))
            fileName = val.getFileName();
        else if (!isStringEmpty(val.getEitherFilePath())) {
            fileName = FileUtils.getFileNameFromPath(val.getEitherFilePath());
        }
        tvDocument.setText(fileName);


        setTextViewIcon(tvDocument, /*DataProvider.getDocumentIcon(fileName)*/R.drawable.icon_file_unknown,
                val, true, false);
        contDocument.setOnClickListener(v -> {
            chatAdapterListener.onDocumentTapped(getAdapterPosition());
        });

        Trace.endSection();

    }

    @Override
    public View getRetryView() {
        return contDocument;
    }

    @Override
    public View getLongClickView() {
        return contDocument;
    }

    @Override
    public View getParentLayout() {
        return contDocument;
    }

    @Override
    public ProgressBar getMessageSendLoader() {
        return pbAttachment;
    }

    @Override
    public ArrayList<View> getOnReceiveViews() {
        ArrayList<View> views = super.getOnReceiveViews();
        views.add(tvDocument);
        views.add(pbAttachment);
        return views;
    }
}
