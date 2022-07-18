package com.xevensolutions.chat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.esafirm.imagepicker.features.ImagePicker;
import com.esafirm.imagepicker.model.Image;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.xevensolutions.baseapp.activities.ActivityWebview;
import com.xevensolutions.baseapp.interfaces.ImagePickerListener;
import com.xevensolutions.baseapp.utils.Constants;
import com.xevensolutions.baseapp.utils.MyRecyclerViewScrollListener;
import com.xevensolutions.baseapp.utils.ViewUtils;
import com.xevensolutions.chat.MyChatApp;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.chatAdapter.AdapterListItemMessagesV2;
import com.xevensolutions.chat.chatAdapter.ChatAdapterListener;
import com.xevensolutions.chat.databinding.ActivityChatBinding;
import com.xevensolutions.chat.fragments.FragmentAttachmentPicker;
import com.xevensolutions.chat.interfaces.PatientOptionsListener;
import com.xevensolutions.chat.interfaces.UriToPathListener;
import com.xevensolutions.chat.models.ChatItem;
import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.presenters.ChatView;
import com.xevensolutions.chat.utils.DataProvider;
import com.xevensolutions.chat.utils.FileUtils;
import com.xevensolutions.chat.utils.SharedData;
import com.xevensolutions.chat.viewmodels.VmChat;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class ActivityChat extends BaseSignalActivity<ActivityChatBinding> implements ChatView {

    RecyclerView rvMessages;
    EditText etMsg;
    FloatingActionButton btnSend;
    TextView tvSenderName;

    TextView tvSenderLocation;

    MaterialButton btnAddToFav;
    ProgressBar progressBar;


    AdapterListItemMessagesV2 adapterChatMessages;

    ChatItem chatItem;
    private static final String KEY_CHAT_ITEM = "CHAT_ITEM";

    VmChat vmChat;
    public static Integer CURRENT_ACTIVE_CHAT_ID;
    private String fileToSend;
    private MaterialToolbar materialToolbar;

    private CircleImageView ivProfile;
    private TextView tvTitle;
    private static final String KEY_SHOW_PATIENT_OPTIONS = "SHOW_PATIENT_OPTIONS";
    private static final String KEY_ENABLE_SENDING = "key_enable_sending";
    private static final String KEY_ENABLE_EMR = "key_enable_emr";

    boolean showPatientOptions;
    private boolean showOptions;
    private boolean enableSending;
    private boolean enableEMR;

    public static void startActivity(Activity activity, ChatItem chatItem) {
        startActivity(activity, chatItem, false, true, false);
    }


    public static void startActivity(Activity activity, ChatItem chatItem, boolean showOptions, boolean enableSending, boolean enableEMR) {
        activity.startActivity(createIntent(activity, chatItem, showOptions, enableSending, enableEMR));
    }

    public static Intent createIntent(Context context, ChatItem chatItem, boolean showOptions, boolean enableSending, boolean enableEMR) {
        Intent intent = new Intent(context, ActivityChat.class);
        intent.putExtra(KEY_CHAT_ITEM, chatItem);
        intent.putExtra(KEY_SHOW_PATIENT_OPTIONS, showOptions);
        intent.putExtra(KEY_ENABLE_SENDING, enableSending);
        intent.putExtra(KEY_ENABLE_EMR, enableEMR);
        return intent;
    }


    @Override
    public int getActivityName() {
        return R.string.chat;
    }

    @Override
    public void receiveExtras(Bundle arguments) {
        chatItem = arguments.getParcelable(KEY_CHAT_ITEM);
        showOptions = arguments.getBoolean(KEY_SHOW_PATIENT_OPTIONS);
        enableSending = arguments.getBoolean(KEY_ENABLE_SENDING);
        enableEMR = arguments.getBoolean(KEY_ENABLE_EMR);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedData.getInstance().setActiveChatId(chatItem.getInboxId());
        CURRENT_ACTIVE_CHAT_ID = chatItem.getInboxId();
        if (fileToSend != null) {
            vmChat.sendFile(fileToSend);
            fileToSend = null;
        }
    }


   /* @Override
    public void onMessageReceived(final SendMessageRequest sendMessageRequest) {
        super.onMessageReceived(sendMessageRequest);
        vmChat.loadMessages();

    }*/

    @Override
    protected void onPause() {
        super.onPause();
        SharedData.getInstance().setActiveChatId(-1);
        CURRENT_ACTIVE_CHAT_ID = -1;
    }

    @Override
    public ActivityChatBinding getViewBinding() {
        return ActivityChatBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
        changeFragment(new FragmentAttachmentPicker(), R.id.btnAttachment, true, false);
        adapterChatMessages = new AdapterListItemMessagesV2(this, new ArrayList<>(), this,
                new ChatAdapterListener() {
                    @Override
                    public void onAudioPlayToggled(int index) {

                    }

                    @Override
                    public void onImageTapped(int pos, ImageView imageView) {
/*
                        ActivityImages.startActivity(ActivityChat.this, pos,
                                adapterChatMessages.getItem(pos).getFilePath());
*/
                    }

                    @Override
                    public void onVideoTapped(int adapterPosition, ImageView imageView) {

                    }

                    @Override
                    public void onProfileTapped(int pos, ImageView imageView) {

                    }

                    @Override
                    public void onResendTapped(int pos) {

                    }

                    @Override
                    public void onEditMessage(int position) {

                    }

                    @Override
                    public void onQuoteTapped(int position) {

                    }

                    @Override
                    public void onQuotedMessageTapped(int adapterPosition) {

                    }

                    @Override
                    public void onForwardTapped(int position) {

                    }

                    @Override
                    public void onDeleteForMeTapped(int position) {

                    }

                    @Override
                    public void onDeleteForEveryOneTapped(int position) {

                    }

                    @Override
                    public void onDocumentTapped(int position) {
                        ChatMessage chatMessage = adapterChatMessages.getItem(position);
                        String path = chatMessage.getFilePath();
                        if (path != null && !path.startsWith("http"))
                            path = Constants.BLOB_PATH + path;
                        ActivityWebview.startActivity(ActivityChat.this, FileUtils.getFileNameFromURL(path),
                                Constants.GOOGLE_DOCS_URL + path);

                    }

                    @Override
                    public void scrollToBottom() {

                    }

                    @Override
                    public void onDownloadTapped(int position) {

                    }

                    @Override
                    public void onListItemClicked(int pos) {

                    }
                });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        rvMessages.setLayoutManager(linearLayoutManager);
        rvMessages.setAdapter(adapterChatMessages);
        rvMessages.addOnScrollListener(new MyRecyclerViewScrollListener(rvMessages.getLayoutManager(),
                false, 10) {
            @Override
            public void loadMoreItems() {
                vmChat.loadNextPage();
            }
        });

        tvSenderName.setText(chatItem.getToUser());
        tvSenderName.setTextColor(getResources().getColor(R.color.black));
        tvSenderLocation.setText(chatItem.getPropertyName());
        updateFavIcon();
    }

    private void initViews() {
        rvMessages = findViewById(R.id.rv_messages);
        etMsg = findViewById(R.id.et_msg);
        btnSend = findViewById(R.id.btn_send);
        tvSenderName = findViewById(R.id.tvSenderName);
        tvSenderLocation = findViewById(R.id.tvSenderLocation);
        btnAddToFav = findViewById(R.id.btnAddToFav);
        progressBar = findViewById(R.id.progressBar);
        materialToolbar = findViewById(R.id.toolbar);
        tvTitle = findViewById(R.id.tvTitle);
        ivProfile = findViewById(R.id.ivProfile);
        tvTitle.setText(chatItem.getUserName());

        if (chatItem.getProfilePath() != null) {

            if (chatItem.getProfilePath().startsWith("http"))
                Glide.with(this).load(chatItem.getProfilePath()).placeholder(R.drawable.ic_profile_placeholder).into(ivProfile);
            else
                Glide.with(this).load(Constants.BLOB_PATH + chatItem.getProfilePath()).placeholder(R.drawable.ic_profile_placeholder).into(ivProfile);

        } else if (chatItem.getToUserPhoto() != null) {

            if (chatItem.getToUserPhoto().startsWith("http"))
                Glide.with(this).load(chatItem.getToUserPhoto()).placeholder(R.drawable.ic_profile_placeholder).into(ivProfile);
            else
                Glide.with(this).load(Constants.BLOB_PATH + chatItem.getToUserPhoto()).placeholder(R.drawable.ic_profile_placeholder).into(ivProfile);

        }

        if (showOptions) {
            materialToolbar.inflateMenu(R.menu.patient_chat_menu);
            materialToolbar.setOnMenuItemClickListener(item -> {

                PatientOptionsListener patientOptionsListener = MyChatApp.getPatientOptionsListener();
                if (patientOptionsListener == null)
                    return false;
                int itemId = item.getItemId();
                if (itemId == R.id.action_emr) {

                    if (enableEMR)
                        patientOptionsListener.onViewEmrTapped(this, chatItem);
                    else
                        Toast.makeText(this, getString(R.string.emr_permission_denied), Toast.LENGTH_SHORT).show();

                } else if (itemId == R.id.action_treatment_plan) {
                    patientOptionsListener.onCreateTreatmentPlanTapped(this, chatItem);
                } else if (itemId == R.id.action_view_profile) {
                    patientOptionsListener.onViewProfileTapped(this, chatItem);

                }
                return true;
            });
        }
        setListenersHelper();
        initViewModelHelper();

   /*     if (!enableSending)
            disableSending();*/

    }

    private void disableSending() {

        etMsg.setEnabled(false);
        btnSend.setEnabled(false);

    }

    private void updateFavIcon() {
   /*     if (chatItem.isFavorite())
            btnAddToFav.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_24));
        else
            btnAddToFav.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_baseline_favorite_border_24));*/
    }

    @Override
    public void initViewModel() {
        super.initViewModel();
    }

    private void initViewModelHelper() {
        vmChat = new ViewModelProvider(this).get(VmChat.class);
        vmChat.setChatItem(chatItem);
        vmChat.setBaseFragmentView(this);
        vmChat.setChatView(this);
        vmChat.loadMessages();
        vmChat.getChatMessagesResponseMutableLiveData().observe(this, chatMessagesResponse -> {

                   /* if (fragmentAdInfo == null) {
                        fragmentAdInfo = FragmentAdInfo.newInstance(new AdInfo(chatMessagesResponse.getPostImagePath(),
                                chatMessagesResponse.getPostTitle(), chatMessagesResponse.getPrice(), 0, chatMessagesResponse.getPostedDate()));
                        changeFragment(fragmentAdInfo, R2.id.cont_ad_info, true);
                    }*/
                    progressBar.setVisibility(View.GONE);
                    adapterChatMessages.updateItems((ArrayList<ChatMessage>) chatMessagesResponse);
                    if (vmChat.getPageNo() == 1) {
                        rvMessages.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rvMessages.scrollToPosition(0);
                            }
                        }, 300);
                    }


                    //Hitting the MarkAsRead API....
                    vmChat.markAsRead(chatItem.getToUserId(), chatItem.getInboxId());

                }
        );

    }

    @Override
    public void observeData() {
        super.observeData();

    }

    @Override
    public void showLoading(String message) {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setListeners() {
        super.setListeners();
    }

    private void setListenersHelper() {


        materialToolbar.setNavigationOnClickListener(v -> {
            onBackPressed();
        });
         btnSend.setOnClickListener(v -> {
            vmChat.sendMessage(etMsg.getText().toString(), null);
            etMsg.setText("");
        });

        btnAddToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  vmChat.chatAddToFav(chatItem);
            }
        });
    }

    @Override
    public void onMsgMissing() {
        // GenericUtils.setFieldError(etMsg, getString(R.string.required));
    }


    @Override
    public void onAddToFavSuccess(ChatItem chatItem) {
        this.chatItem = chatItem;
        updateFavIcon();
    }

    @Override
    public void onMessageRecevied(final ChatMessage chatMessage) {
        super.onMessageRecevied(chatMessage);
        runOnUiThread(() -> {
            vmChat.onMessageReceived(chatMessage);
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, @Nullable @org.jetbrains.annotations.Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // Get a list of picked images
            List<Image> images = ImagePicker.getImages(data);
            // or get a single image only
            vmChat.sendFile(images.get(0).getPath());

        } else if (requestCode == 100 && resultCode == RESULT_OK) {

            vmChat.sendFile(vmChat.getCameraImagePath());

        } else if (requestCode == Constants.PICK_FILES && resultCode == RESULT_OK) {
            if (data != null) {
                DataProvider.getPickIt(ActivityChat.this,
                        new UriToPathListener() {
                            @Override
                            public void PickiTonCompleteListener(String path, boolean wasDriveFile, boolean wasUnknownProvider, boolean wasSuccessful, String Reason) {
                                fileToSend = path;
                            }
                        }).getPath(data.getData(), Build.VERSION.SDK_INT);
            }
        }
    }

    @Override
    public void exitActivity() {

    }

    @Override
    public void showToast(String error) {

    }

    public void sendFile(String path) {
        vmChat.sendFile(path);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2000 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            vmChat.setCameraImagePath(ViewUtils.pickImageFromCamera(this,
                    com.xevensolutions.chat.utils.Constants.FILE_PROVIDER,
                    100));
        }
    }

}
