package com.xevensolutions.chat.chatAdapter;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public interface DiffCalListener<T> {

    void onDiffCalculated(DiffUtil.DiffResult diffResult, List<T> newList);
}
