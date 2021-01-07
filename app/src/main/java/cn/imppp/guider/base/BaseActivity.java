package cn.imppp.guider.base;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProvider;
import cn.imppp.guider.BR;

public abstract class BaseActivity<VM extends BaseViewModel, B extends ViewDataBinding> extends AppCompatActivity {

    public VM mViewModel;
    public B mBinding;

    // 获取列表信息
    public abstract int layoutRes();

    // 获取viewModel
    public abstract Class<VM> viewModelClass();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(viewModelClass());
        mBinding = DataBindingUtil.setContentView(this, layoutRes());
        mBinding.setVariable(BR.vm, mViewModel);
        initClick();
        loadData();
        observer();
    }

    public abstract void initClick();

    public abstract void loadData();

    public abstract void observer();
}
