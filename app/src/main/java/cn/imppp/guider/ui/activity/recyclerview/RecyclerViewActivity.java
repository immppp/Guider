package cn.imppp.guider.ui.activity.recyclerview;

import android.app.Service;
import android.graphics.Color;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.imppp.guider.R;
import cn.imppp.guider.adapter.CardAdapter;
import cn.imppp.guider.app.App;
import cn.imppp.guider.base.BaseActivity;
import cn.imppp.guider.databinding.ActivityRecyclerViewBinding;

public class RecyclerViewActivity extends BaseActivity<RecyclerViewModel, ActivityRecyclerViewBinding> {

    private List<String> list;
    private CardAdapter cardAdapter;

    @Override
    public int layoutRes() {
        return R.layout.activity_recycler_view;
    }

    @Override
    public Class<RecyclerViewModel> viewModelClass() {
        return RecyclerViewModel.class;
    }

    @Override
    public void initClick() {

    }

    @Override
    public void loadData() {
        list = new ArrayList<>();
        for (int i = 0; i< 60; i++) {
            list.add(String.valueOf(i));
        }

        cardAdapter = new CardAdapter(list);
        mBinding.rvRecyclerView.setLayoutManager(new GridLayoutManager(App.getContext(), 4));
        mBinding.rvRecyclerView.setAdapter(cardAdapter);
        helper.attachToRecyclerView(mBinding.rvRecyclerView);
    }

    @Override
    public void observer() {

    }

    public ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFrlg = 0;
            if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
                dragFrlg = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
            }else if(recyclerView.getLayoutManager() instanceof LinearLayoutManager){
                dragFrlg = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
            }
            return makeMovementFlags(dragFrlg,0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

            //得到当拖拽的viewHolder的Position
            int fromPosition = viewHolder.getAdapterPosition();
            //拿到当前拖拽到的item的viewHolder
            int toPosition = target.getAdapterPosition();
            if (fromPosition < toPosition) {
                for (int i = fromPosition; i < toPosition; i++) {
                    Collections.swap(list, i, i + 1);
                }
            } else {
                for (int i = fromPosition; i > toPosition; i--) {
                    Collections.swap(list, i, i - 1);
                }
            }
            cardAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
            //侧滑删除可以使用；
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }
        /**
         * 长按选中Item的时候开始调用
         * 长按高亮
         * @param viewHolder
         * @param actionState
         */
        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
//                viewHolder.itemView.setBackgroundColor(Color.RED);
                //获取系统震动服务//震动70毫秒
                Vibrator vib = (Vibrator) App.getContext().getSystemService(Service.VIBRATOR_SERVICE);
                vib.vibrate(70);
            }
            super.onSelectedChanged(viewHolder, actionState);
        }

        /**
         * 手指松开的时候还原高亮
         * @param recyclerView
         * @param viewHolder
         */
        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            viewHolder.itemView.setBackgroundColor(0);
            cardAdapter.notifyDataSetChanged();  //完成拖动后刷新适配器，这样拖动后删除就不会错乱
        }
    });
}
