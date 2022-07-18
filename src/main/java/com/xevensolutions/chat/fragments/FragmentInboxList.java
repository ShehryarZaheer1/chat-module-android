package com.xevensolutions.chat.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.xevensolutions.baseapp.fragments.BaseFragment;
import com.xevensolutions.baseapp.utils.MyRecyclerViewScrollListener;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.activities.ActivityArchivedChats;
import com.xevensolutions.chat.activities.ActivityChat;
import com.xevensolutions.chat.chatAdapter.AdapterChat;
import com.xevensolutions.chat.databinding.FragmentInboxListBinding;
import com.xevensolutions.chat.interfaces.InboxListlListener;
import com.xevensolutions.chat.models.ChatItem;
import com.xevensolutions.chat.models.ChatMessage;
import com.xevensolutions.chat.presenters.InboxView;
import com.xevensolutions.chat.utils.DataProvider;
import com.xevensolutions.chat.utils.SwipeHelper;
import com.xevensolutions.chat.viewmodels.VmInbox;


import java.util.ArrayList;
import java.util.List;

import static com.xevensolutions.baseapp.utils.CollectionUtils.isEmpty;


public class FragmentInboxList extends BaseFragment<FragmentInboxListBinding> implements InboxView {

    VmInbox vmInbox;


    private static final String KEY_SELECTED_SPACE = "SELECTED_SPACE";
    private static final String KEY_ENABLE_SENDING = "key_enable_sending";

    String selectedSpace;
    boolean enableSending;
    private SwipeHelper swipeHelper;
    private AdapterChat adapterChat;

    public static FragmentInboxList newInstance(String space) {
        Bundle args = new Bundle();
        args.putString(KEY_SELECTED_SPACE, space);
        FragmentInboxList fragment = new FragmentInboxList();
        fragment.setArguments(args);
        return fragment;
    }

    public static FragmentInboxList newInstance(boolean enableSending) {

        Bundle args = new Bundle();
        args.putBoolean(KEY_ENABLE_SENDING, enableSending);
        FragmentInboxList fragment = new FragmentInboxList();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void receiveExtras(final Bundle arguments) {
        selectedSpace = arguments.getString(KEY_SELECTED_SPACE);
        enableSending = arguments.getBoolean(KEY_SELECTED_SPACE);
    }

    @Override
    public void setListeners() {

        binding.btnArchiveChats.setOnClickListener(v -> {
            ActivityArchivedChats.startActivity(getActivity(), new ArrayList<>(), enableSending);
        });

        binding.btnShowFavorites.setOnClickListener(view -> {
            filterChats(binding.btnShowFavorites.getText().toString().equals(getString(R.string.show_favorites)));
        });
    }

    @Override
    public int getFragmentName() {
        return R.string.inbox;
    }

    @Override
    public void onCreateView(final View view) {
        super.onCreateView(view);
        adapterChat = new AdapterChat(getActivity(), new ArrayList<>(), this, false, new InboxListlListener() {
            @Override
            public void onFavToggled(int pos) {
                // vmInbox.chatAddToFav(adapterChat.getItem(pos), pos);
            }

            @Override
            public void onListItemClicked(int pos) {
                ActivityChat.startActivity(requireActivity(), adapterChat.getItem(pos), false, enableSending, false);
            }
        });

        binding.rvInboxItems.setAdapter(adapterChat);
        binding.rvInboxItems.addOnScrollListener(new MyRecyclerViewScrollListener(binding.rvInboxItems.getLayoutManager(),
                false, 10) {
            @Override
            public void loadMoreItems() {
                vmInbox.loadNextPage();
            }
        });

        swipeHelper = new SwipeHelper(getActivity(), binding.rvInboxItems) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                SwipeHelper.UnderlayButton deleteButton = new SwipeHelper.UnderlayButton(
                        getString(R.string.archive_chat),
                        R.drawable.ic_baseline_archive_24,
                        Color.GRAY, pos -> vmInbox.archieveChat(pos, adapterChat.getItem(pos).getInboxId(), true)
                );

                underlayButtons.add(deleteButton);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeHelper);
        itemTouchhelper.attachToRecyclerView(binding.rvInboxItems);
    }

    @Override
    public void onResume() {
        super.onResume();
        vmInbox.loadInbox();
    }

    @Override
    public void observeData() {
        super.observeData();
        vmInbox.getChatMutableLiveData().observe(getViewLifecycleOwner(), chatItems -> adapterChat.updateItems((ArrayList<ChatItem>) DataProvider.moveFavsToTop(chatItems)));

    }

    @Override
    public FragmentInboxListBinding getViewBinding() {
        return FragmentInboxListBinding.inflate(getLayoutInflater());
    }

    private void udpateItems(List<ChatItem> chatItems) {
        adapterChat.updateItems((ArrayList<ChatItem>) chatItems);
        binding.tvNoRecords.setVisibility(isEmpty(chatItems) ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onAddToFavSuccessful(final ChatItem chatItem, final int pos) {
        //adapterChat.updateItem(pos, chatItem);
    }

    @Override
    public void onChatsFiltered(List<ChatItem> filteredChats, boolean favorites) {
        try {
            udpateItems(filteredChats);
            int icon = favorites ? R.drawable.ic_baseline_favorite_24 : R.drawable.ic_baseline_favorite_border_24;
            String title = favorites ? getString(R.string.show_all) : getString(R.string.show_favorites);
            binding.btnShowFavorites.setText(title);
            binding.btnShowFavorites.setIcon(ContextCompat.getDrawable(getActivity(), icon));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void filterChats(boolean showFavs) {

    }

    @Override
    public void initViewModel() {
        super.initViewModel();
        vmInbox = new ViewModelProvider(this).get(VmInbox.class);
        vmInbox.setInboxView(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();


    }

    @Override
    public void onTokenExpired() {

    }

    @Override
    public void onNoInternet() {

    }

    @Override
    public void exitActivity() {

    }

    @Override
    public void showToast(String error) {

    }

    @Override
    public void showLoading(String message) {
        binding.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void dismissLoading() {
        binding.progressBar.setVisibility(View.GONE);
    }

    public void onMessageReceived(ChatMessage chatMessage) {
        vmInbox.loadInbox();
    }
}
