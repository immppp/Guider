package cn.imppp.guider.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;
import cn.imppp.guider.R;
import cn.imppp.guider.base.BaseViewHolder;
import cn.imppp.guider.databinding.CardItemLayoutBinding;

public class CardAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private List<String> mList;

    public CardAdapter(List<String> list) {
        mList = list;
    }

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardItemLayoutBinding cardItemLayoutBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.card_item_layout, parent, false);
        return new BaseViewHolder(cardItemLayoutBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder holder, int position) {
        CardItemLayoutBinding binding = (CardItemLayoutBinding) holder.binding;
        binding.tvCard.setText(mList.get(position));
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }
}
