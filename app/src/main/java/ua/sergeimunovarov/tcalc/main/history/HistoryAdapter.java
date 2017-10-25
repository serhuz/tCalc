/*
 * Copyright © Sergei Munovarov. All rights reserved.
 * See LICENCE.txt for license details.
 */

package ua.sergeimunovarov.tcalc.main.history;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ua.sergeimunovarov.tcalc.databinding.ItemHistoryBinding;
import ua.sergeimunovarov.tcalc.main.history.db.Entry;
import ua.sergeimunovarov.tcalc.main.history.listeners.HistoryEntryClickListener;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.EntryViewHolder> {

    private final HistoryEntryViewModel mViewModel;
    private final List<Entry> mEntries;


    public HistoryAdapter(HistoryEntryClickListener clickListener) {
        mViewModel = new HistoryEntryViewModel(clickListener);
        mEntries = new ArrayList<>();
    }


    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemHistoryBinding binding = ItemHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new EntryViewHolder(binding);
    }


    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        holder.setViewModel(mViewModel);

        Entry entry = mEntries.get(position);
        holder.bind(entry);
    }


    @Override
    public int getItemCount() {
        return mEntries.size();
    }


    public void setEntries(@NonNull List<Entry> entries) {
        mEntries.clear();
        mEntries.addAll(entries);
        notifyDataSetChanged();
    }


    static class EntryViewHolder extends RecyclerView.ViewHolder {

        private final ItemHistoryBinding mBinding;


        public EntryViewHolder(ItemHistoryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }


        public void bind(Entry entry) {
            mBinding.setEntry(entry);
            mBinding.executePendingBindings();
        }


        public void setViewModel(HistoryEntryViewModel viewModel) {
            mBinding.setModel(viewModel);
        }
    }
}
