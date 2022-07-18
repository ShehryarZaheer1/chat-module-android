package com.xevensolutions.chat.chatAdapter.viewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Trace;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.xevensolutions.baseapp.utils.GenericUtils;
import com.xevensolutions.baseapp.utils.ImageUtils;
import com.xevensolutions.baseapp.utils.ViewUtils;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.R2;
import com.xevensolutions.chat.chatAdapter.ChatAdapterListener;
import com.xevensolutions.chat.models.ChatMessage;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import javax.sql.DataSource;

import butterknife.BindView;

public class VHImageMessage extends BaseMessageViewHolder {
    @Nullable
    @BindView(R2.id.iv_attachment)
    public ImageView ivAttachment;
    @Nullable
    @BindView(R2.id.pb_image_loader)
    public ProgressBar pbImageLoader;
    @BindView(R2.id.btn_play)
    public ImageView ivPlay;
    @BindView(R2.id.tv_duration)
    public TextView tvDuration;

    //@BindView(R2.id.group_image)
    ConstraintLayout imageGroup;


    public VHImageMessage(@NonNull @NotNull View itemView, Context context, ChatAdapterListener chatAdapterListener) {
        super(context, chatAdapterListener, itemView);

    }

    @Override
    protected void onMessageSentError(ChatMessage chatMessage) {
        super.onMessageSentError(chatMessage);
        ivPlay.setImageResource(R.drawable.ic_baseline_refresh_24);
        ivPlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMessageSending(ChatMessage chatMessage) {
        super.onMessageSending(chatMessage);
        ivPlay.setVisibility(View.GONE);
    }

    @Override
    public void onMessageSent(ChatMessage chatMessage) {
        super.onMessageSent(chatMessage);
    }

    @Override
    public void onBindData(ChatMessage val, ChatMessage nextMessage) {
        super.onBindData(val, nextMessage);

        Trace.beginSection("onBind Image message");
        ImageUtils.setImage(context,
                "", val.getEitherFilePath(), ivAttachment, false, false, val.isLocalFile(),
                R.drawable.ic_baseline_image_24, new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        if (val.isSent())
                            toggleMessageSending(false);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, com.bumptech.glide.load.DataSource dataSource, boolean isFirstResource) {
                        if (val.isSent())
                            toggleMessageSending(false);
                        return false;
                    }

                });


        if (val.isSent()) {


            ivPlay.setVisibility(val.isFileImage() ? View.GONE : View.VISIBLE);
            ivAttachment.setOnClickListener(v -> {
                if (val.isFileImage()) {
                    chatAdapterListener.onImageTapped(getAdapterPosition(), ivAttachment);
                } else if (val.isFileVideo()) {
                    chatAdapterListener.onVideoTapped(getAdapterPosition(), ivAttachment);
                }
            });

        }

        Trace.endSection();

    }

    @Override
    public View getLongClickView() {
        return ivAttachment;
    }

    @Override
    public View getRetryView() {
        return ivPlay;
    }

    @Override
    public ProgressBar getMessageSendLoader() {
        return pbImageLoader;
    }

    @Override
    public int getMarginInDps() {
        return (int) ViewUtils.convertPixelsToDp(ViewUtils.getScreenWidth() - ivAttachment.getMeasuredHeight());
    }

    @Override
    public ArrayList<View> getOnReceiveViews() {
        ArrayList<View> views = super.getOnReceiveViews();
        views.add(pbImageLoader);
        return views;
    }
}
