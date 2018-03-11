/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ua.sergeimunovarov.tcalc.databinding.ItemEntryBinding;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.viewmodel.SingleLiveEvent;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.EntryViewHolder> {

    public final EntryClickEvent mEntryClickEvent = new EntryClickEvent();

    private final EntryViewModel mViewModel = new EntryViewModel(mEntryClickEvent);
    private final List<Entry> mItems = new ArrayList<>();


    @NonNull
    @Override
    public EntryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemEntryBinding binding = ItemEntryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new EntryViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(@NonNull EntryViewHolder holder, int position) {
        holder.setViewModel(mViewModel);

        Entry entry = mItems.get(position);
        holder.bind(entry);
    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }


    public void setItems(@NonNull List<Entry> items) {
        if (items.isEmpty()) {
            mItems.addAll(items);
            notifyDataSetChanged();
        } else {
            DiffUtil.DiffResult result = DiffUtil.calculateDiff(new DiffUtil.Callback() {
                @Override
                public int getOldListSize() {
                    return mItems.size();
                }


                @Override
                public int getNewListSize() {
                    return items.size();
                }


                @Override
                public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                    return mItems.get(oldItemPosition).getId() == items.get(newItemPosition).getId();
                }


                @Override
                public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                    return mItems.get(oldItemPosition).equals(items.get(newItemPosition));
                }
            });
            mItems.clear();
            mItems.addAll(items);
            result.dispatchUpdatesTo(this);
        }
    }


    @SuppressWarnings("WeakerAccess")
    static class EntryViewHolder extends RecyclerView.ViewHolder {

        private final ItemEntryBinding mBinding;


        public EntryViewHolder(ItemEntryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }


        public void bind(Entry item) {
            mBinding.setItem(item);
            mBinding.executePendingBindings();
        }


        public void setViewModel(EntryViewModel viewModel) {
            mBinding.setModel(viewModel);
        }
    }


    public static class EntryClickEvent extends SingleLiveEvent<Entry> {

    }
}
