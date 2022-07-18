package com.xevensolutions.chat.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.material.appbar.MaterialToolbar;
import com.xevensolutions.baseapp.utils.AlertUtils;
import com.xevensolutions.baseapp.utils.CollectionUtils;
import com.xevensolutions.baseapp.utils.MyRecyclerViewScrollListener;
import com.xevensolutions.chat.R;
import com.xevensolutions.chat.R2;
import com.xevensolutions.chat.chatAdapter.AdapterChat;
import com.xevensolutions.chat.databinding.ActivityArchivedChatsBinding;
import com.xevensolutions.chat.interfaces.InboxListlListener;
import com.xevensolutions.chat.models.ChatItem;
import com.xevensolutions.chat.presenters.ArchivedChatView;
import com.xevensolutions.chat.utils.CacheManager;
import com.xevensolutions.chat.utils.SwipeHelper;
import com.xevensolutions.chat.viewmodels.VmArchivedChats;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class ActivityArchivedChats extends BaseSignalActivity<ActivityArchivedChatsBinding> implements ArchivedChatView, InboxListlListener {

    boolean isGroup;
    boolean enableSending;

    @BindView(R2.id.recyclerview)
    RecyclerView recyclerView;
    @BindView(R2.id.tv_no_data)
    TextView tvNoData;
    VmArchivedChats vmArchivedChats;
    List<ChatItem> chatItems;
    @BindView(R2.id.toolbar)
    MaterialToolbar materialToolbar;
    AdapterChat adapter;
    private SwipeHelper swipeHelper;
    private SearchView searchView;
    private MyRecyclerViewScrollListener recyclerViewScrollListener;

    public static void startActivity(Activity activity, List<ChatItem> chatItems, boolean enableSending) {
        Intent intent = new Intent(activity, ActivityArchivedChats.class);
        intent.putParcelableArrayListExtra("chatitems", (ArrayList<? extends Parcelable>) chatItems);
        intent.putExtra("enable_sending", enableSending);
        activity.startActivity(intent);
    }

    @Override
    public void initViewModel() {
        super.initViewModel();
        vmArchivedChats = new ViewModelProvider(this).get(VmArchivedChats.class);
        vmArchivedChats.setArchivedChatView(this);
    }

    @Override
    public ActivityArchivedChatsBinding getViewBinding() {
        return ActivityArchivedChatsBinding.inflate(getLayoutInflater());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new AdapterChat(this, new ArrayList<>(chatItems), this, true, this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        recyclerViewScrollListener = new MyRecyclerViewScrollListener(recyclerView.getLayoutManager(),
                false, 10) {
            @Override
            public void loadMoreItems() {
                recyclerViewScrollListener.setLoading(true);
                vmArchivedChats.loadNextPage();
            }
        };

        recyclerView.addOnScrollListener(recyclerViewScrollListener);

        swipeHelper = new SwipeHelper(this, recyclerView) {
            @Override
            public void instantiateUnderlayButton(RecyclerView.ViewHolder viewHolder, List<UnderlayButton> underlayButtons) {
                SwipeHelper.UnderlayButton deleteButton = new SwipeHelper.UnderlayButton(
                        getString(R.string.delete),
                        R.drawable.ic_baseline_unarchive_24,
                        Color.parseColor("#FF3C30"), pos -> AlertUtils.showConfirmationAlert(ActivityArchivedChats.this,
                        getString(R.string.confirmation), getString(R.string.delete_chat_msg),
                        getString(R.string.delete), getString(R.string.cancel), null,
                        null, (dialog, which) -> vmArchivedChats.deleteChat(pos), null));

                SwipeHelper.UnderlayButton unArchiveButton = new SwipeHelper.UnderlayButton(
                        getString(R.string.unarchive),
                        R.drawable.ic_baseline_unarchive_24,
                        Color.GRAY, pos -> removeFromArchives(pos, true));

                underlayButtons.add(deleteButton);
                underlayButtons.add(unArchiveButton);
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeHelper);
        itemTouchhelper.attachToRecyclerView(recyclerView);

        materialToolbar.setNavigationOnClickListener(v -> onBackPressed());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.archive_chat_options, menu);
        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!searchView.isIconified()) {
                    searchView.setIconified(true);
                }
                myActionMenuItem.collapseActionView();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.filter(s);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (CacheManager.getCurrentUser() != null)
            vmArchivedChats.getArchivedChats(CacheManager.getCurrentUser().getUserId());
    }

    private void removeFromArchives(int pos, boolean showLoading) {
        vmArchivedChats.setArchive(pos, showLoading);
    }

    @Override
    public void observeData() {
        super.observeData();
        vmArchivedChats.getChatsLiveData().observe(this, chatItems -> {
            if (!CollectionUtils.isEmpty(chatItems)) {
                tvNoData.setVisibility(View.GONE);
                adapter.updateItems(new ArrayList<>(chatItems));
            } else {
                adapter.updateItems(new ArrayList<>());
                tvNoData.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    public int getActivityName() {
        return R.string.conversations;
    }

    @Override
    public void receiveExtras(Bundle arguments) {
        isGroup = arguments.getBoolean("isGroup", false);
        enableSending = arguments.getBoolean("enable_sending");
        chatItems = arguments.getParcelableArrayList("chatitems");
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onListItemClicked(int pos) {
        if (pos < 0)
            return;
        try {
            ChatItem chatItem = adapter.getItem(pos);
            ActivityChat.startActivity(this,
                    chatItem, false, enableSending, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFavToggled(int pos) {

    }
}