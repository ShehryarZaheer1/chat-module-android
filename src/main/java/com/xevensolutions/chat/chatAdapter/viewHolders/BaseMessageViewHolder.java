package com.xevensolutions.chat.chatAdapter.viewHolders;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Trace;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.xevensolutions.baseapp.utils.DateUtils;
import com.xevensolutions.baseapp.utils.GenericUtils;
import com.xevensolutions.baseapp.utils.ImageUtils;
import com.xevensolutions.baseapp.utils.ViewUtils;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.R2;
import com.xevensolutions.chat.chatAdapter.ChatAdapterListener;
import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.utils.CacheManager;


import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;


import butterknife.BindView;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public abstract class BaseMessageViewHolder extends BaseViewHolder {


    public Context context;

    @Nullable

    FrameLayout contProfile;
    @Nullable
    @BindView(R2.id.tv_time)
    public TextView tvTime;
    @Nullable
    @BindView(R2.id.iv_profile)
    ImageView ivProfile;

    @Nullable
    @BindView(R2.id.main_layout)
    ViewGroup mainLayout;

    @Nullable
    @BindView(R2.id.cont_extra)
    LinearLayout contExtra;

    @Nullable
    @BindView(R2.id.cont_date)
    LinearLayout contDate;


    @Nullable
    @BindView(R2.id.tv_message_date)
    TextView tvMessageDate;

    public Handler handler;
    public ChatAdapterListener chatAdapterListener;


    public BaseMessageViewHolder(Context context, ChatAdapterListener chatAdapterListener, @NotNull View itemView) {
        super(itemView);
        this.chatAdapterListener = chatAdapterListener;
        this.context = context;
        handler = new Handler();
    }

    public void onBindData(ChatMessage chatMessage, ChatMessage nextMessage) {

        Trace.beginSection("onBind Base");


        Trace.beginSection("message sent status");
/*        if (chatMessage.getSentStatus() == Constants.SentStatus.ERROR) {
            onMessageSentError(chatMessage);
        } else if (chatMessage.getSentStatus() == Constants.SentStatus.SENDING) {
            onMessageSending(chatMessage);
        } else if (chatMessage.getSentStatus() == Constants.SentStatus.SENT) {*/
        Trace.beginSection("set message time");
        setMessageTime(chatMessage);
        Trace.endSection();
        Trace.beginSection("toggle message sending");
        onMessageSent(chatMessage);
        Trace.endSection();
        Trace.beginSection("hide profile and time");
        //test opt
        //hideProfileAndTime(chatMessage, nextMessage);
        Trace.endSection();
        //   }

        Trace.endSection();

        Trace.beginSection("set profile image");
        setProfileImage(chatMessage);
        Trace.endSection();

        Trace.beginSection("set background color");
        setBackgroundColor(chatMessage);
        Trace.endSection();

        Trace.beginSection("set Click listeners");
        setClickListeners(chatMessage);
        Trace.endSection();

        Trace.beginSection("update on receive colors");
        updateOnReceiveColors(chatMessage, getOnReceiveViews());
        Trace.endSection();


        if (chatMessage.hasQuotedMessage()) {
            Trace.beginSection("set quoted message");

            Trace.endSection();
        }

        Trace.beginSection("set forward indicator");
        setForwardIndicator(chatMessage);
        Trace.endSection();

        Trace.beginSection("set message date");
        setMessageDate(chatMessage);
        Trace.endSection();

        Trace.beginSection("set layout gravity");
        // setLayoutGravity(contExtra, chatMessage);
        Trace.endSection();
        Trace.endSection();

    }

    private void setMessageDate(ChatMessage chatMessage) {

        AsyncTask.execute(() -> {
            if (tvMessageDate != null) {
                String date = DateUtils.getFormattedDate(chatMessage.getCreatedDate());
                String today = DateUtils.getFormattedDate(DateUtils.getFormattedDateTime(Calendar.getInstance()));
                handler.post(() -> tvMessageDate.setText(today.equals(date) ? context.getString(R.string.today) : date));
            }
        });
/*
        new Thread(() -> {
            if (tvMessageDate != null) {
                String date = DateUtils.getFormattedDate(chatMessage.getCreatedDate());
                String today = DateUtils.getFormattedDate(DateUtils.getFormattedDateTime(Calendar.getInstance()));
                handler.post(() -> tvMessageDate.setText(today.equals(date) ? "Today" : date));
            }
        }).start();*/

    }

    private void setForwardIndicator(ChatMessage chatMessage) {

    }


    private void setLayoutGravity(ViewGroup forwardedMessageLayout, ChatMessage chatMessage) {
        try {
            ViewGroup.LayoutParams layoutParams = forwardedMessageLayout.getLayoutParams();

            if (layoutParams instanceof LinearLayout.LayoutParams) {
                LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) layoutParams;
                if (chatMessage.isSentByMe()) {
                    layoutParams1.gravity = Gravity.END;
                } else {
                    layoutParams1.gravity = Gravity.START;
                }
                forwardedMessageLayout.setLayoutParams(layoutParams1);

            } else if (layoutParams instanceof ConstraintLayout.LayoutParams) {
                ConstraintLayout.LayoutParams layoutParams1 = (ConstraintLayout.LayoutParams) layoutParams;
                if (chatMessage.isSentByMe())
                    layoutParams1.horizontalBias = 1.0f;
                else
                    layoutParams1.horizontalBias = 0.0f;

                forwardedMessageLayout.setLayoutParams(layoutParams1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void hideProfileAndTime(ChatMessage chatMessage, ChatMessage nextMessage) {


        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Trace.beginSection("hide profile calc");

                ChatMessage currentMessage = chatMessage;
                String currentTimeAgo = DateUtils.getFormattedTimeToShow(chatMessage.getCreatedDate());


                String nextTimeAgo = nextMessage == null ? null :
                        DateUtils.getFormattedTimeToShow(nextMessage.getCreatedDate());


                boolean isCurrentByMe = currentMessage.isSentByMe();
                boolean isNextByMe = nextMessage == null ? false : nextMessage.isSentByMe();

                Trace.endSection();

                String finalCurrentTimeAgo = currentTimeAgo;
                String finalNextTimeAgo = nextTimeAgo;
                handler.post(() -> {
                    Trace.beginSection("hide profile update");
                    if (finalNextTimeAgo == null || (finalCurrentTimeAgo != null && !finalCurrentTimeAgo.equals(finalNextTimeAgo))
                            || isCurrentByMe != isNextByMe) {
                        tvTime.setVisibility(VISIBLE);
                        toggleProfileVisibility(VISIBLE, chatMessage);
                    } else {
                        if (tvTime != null)
                            tvTime.setVisibility(GONE);
                        toggleProfileVisibility(INVISIBLE, chatMessage);
                    }

                    Trace.endSection();
                });
            }
        });


    }

    private void toggleProfileVisibility(int visibility, ChatMessage chatMessage) {
        if (contProfile != null) {
            contProfile.setVisibility(visibility);
        }
    }

    private void setProfileImage(ChatMessage chatMessage) {
        if (ivProfile != null) {
            // test opt
            ImageUtils.setImage(context, null, chatMessage.getToUserProfilePath(), ivProfile,
                    true, true, false,
                    R.drawable.ic_baseline_image_24, null);
            ivProfile.setOnClickListener(v -> {
                chatAdapterListener.onProfileTapped(getAdapterPosition(), ivProfile);
            });
        }
    }

    private void setClickListeners(ChatMessage chatMessage) {


        if (chatMessage.isSent()) {
            if (getLongClickView() != null) {
              /*  getLongClickView().setOnLongClickListener(
                        new ChatItemLongClickListener(this,
                                getAdapterPosition(), context, chatMessage, chatAdapterListener));*/
            }
        }
    }

    private void setBackgroundColor(ChatMessage chatMessage) {
        if (getParentLayout() == null)
            return;

        int backgroundDrawable;
        if (chatMessage.isSentByMe())
            backgroundDrawable = R.drawable.chat_send_background;
        else
            backgroundDrawable = R.drawable.chat_receive_background;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Trace.beginSection("get drawable");
                Drawable drawable = ContextCompat.getDrawable(context, backgroundDrawable);
                Trace.endSection();
                handler.post(() -> {
                    Trace.beginSection("set drawable");
                    getParentLayout().setBackground(drawable);
                    Trace.endSection();

                });


            }
        });

    }


    protected void setMessageTime(ChatMessage chatMessage) {
        if (tvTime == null)
            return;

        AsyncTask.execute(() -> {
            String formattedTime;

            formattedTime = DateUtils.getFormattedTimeToShow(chatMessage.getCreatedDate());

//            if (CacheManager.getCurrentUser().getUserRoleId() == 2)
//                formattedTime = DateUtils.getFormattedTimeToShow(DateUtils.getDateStringInUTC(chatMessage.getCreatedDate()));
//            else
//                formattedTime = DateUtils.getFormattedTimeToShow(chatMessage.getCreatedDate());

            String senderName = "";
            if (chatMessage.getUserId().equals(CacheManager.getInstance().getCurrentUser().getUserId())) {
                senderName = context.getString(R.string.you);
            } else
                senderName = chatMessage.getFromUser();
            formattedTime = senderName + " " + formattedTime;
            String finalFormattedTime = formattedTime;
            handler.post(() -> {
                tvTime.setText(finalFormattedTime);
            });
        });

/*

        new Thread(() -> {
            String formattedTime = DateUtils.getFormattedTimeToShow(chatMessage.getCreatedDate());
            handler.post(() -> {
                tvTime.setText((chatMessage.getChatTypeId() == Constants.ChatTypeId.GROUP_CHAT ?
                        chatMessage.getFromUsername() : "") + " " +
                        formattedTime);
            });
        }).run();
*/


    }


    protected void onMessageSentError(ChatMessage chatMessage) {
        tvTime.setText(context.getString(R.string.not_sent));
        toggleMessageSending(false);
        if (getRetryView() != null) {
            getRetryView().setVisibility(VISIBLE);
            getRetryView().setOnClickListener(v -> {
                chatAdapterListener.onResendTapped(getAdapterPosition());
            });
        }
    }

    protected void toggleMessageSending(boolean b) {
        if (getMessageSendLoader() != null)
            getMessageSendLoader().setVisibility(b ? VISIBLE : GONE);
    }

    ;


    public void onMessageSent(ChatMessage chatMessage) {
        toggleMessageSending(false);
    }


    public void onMessageSending(ChatMessage chatMessage) {
        tvTime.setText(context.getString(R.string.sending));
        toggleMessageSending(true);

    }

    public abstract View getRetryView();

    public ProgressBar getMessageSendLoader() {
        return null;
    }

    public void setTextViewIcon(TextView textView, int icon, ChatMessage chatMessage, boolean oneSideOnly, boolean atEnd) {
        Trace.beginSection("set Textview icon");
        textView.setCompoundDrawablePadding(20);
        if (oneSideOnly) {
            textView.setCompoundDrawablesWithIntrinsicBounds(atEnd ? 0 : icon, 0, atEnd ? icon : 0, 0);
        } else {
            if (chatMessage.isSentByMe())
                textView.setCompoundDrawablesWithIntrinsicBounds(icon, 0, 0, 0);
            else
                textView.setCompoundDrawablesWithIntrinsicBounds(0, 0, icon, 0);
        }
        Trace.endSection();

    }

    public View getParentLayout() {
        return mainLayout;
    }

    public int getMarginInDps() {
        return 24;
    }

    public void onCreateViewHolder(ViewGroup parent) {
        //    tvTime.setGravity(chatMessage.isSentByMe() ? Gravity.END : Gravity.START);
    }


    public View getLongClickView() {
        return mainLayout;
    }

    public ArrayList<View> getOnReceiveViews() {
        ArrayList<View> views = new ArrayList<>();

        return views;
    }


    public void updateOnReceiveColors(ChatMessage val, ArrayList<View> views) {
        AsyncTask.execute(() -> {
            Trace.beginSection("get attribute color start");
            int colorOnSecondary = GenericUtils.getAttributedColor(context, val.isSentByMe() ?
                    com.google.android.material.R.attr.colorOnSecondary : com.google.android.material.R.attr.colorOnSurface);
            Trace.endSection();
            handler.post(() -> {
                Trace.beginSection("set attribute color start");
                for (View view : views) {
                    if (view != null) {
                        if (view instanceof MaterialButton) {
                            MaterialButton materialButton = (MaterialButton) view;
                            materialButton.setIconTint(ColorStateList.valueOf(colorOnSecondary));
                        } else if (view instanceof TextView) {
                            TextView textView = (TextView) view;
                            textView.setTextColor(colorOnSecondary);
                            if (textView.getId() != R.id.tv_document_name)
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                    textView.setCompoundDrawableTintList(ColorStateList.valueOf(colorOnSecondary));
                                }
                        } else if (view instanceof ImageView) {
                            ImageView imageView = (ImageView) view;
                            imageView.setImageTintList(ColorStateList.valueOf(colorOnSecondary));
                        } else if (view instanceof SeekBar) {
                            SeekBar seekBar = (SeekBar) view;
                            ViewUtils.setTintToSeekbar(seekBar, colorOnSecondary);
                        } else if (view instanceof ProgressBar) {
                            ProgressBar progressBar = (ProgressBar) view;
                            ViewUtils.setTintToProgressBar(progressBar, colorOnSecondary);
                        }
                    }
                }
                Trace.endSection();

            });


        });


    }

    ;;
}
