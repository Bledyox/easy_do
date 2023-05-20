package dev.sash.hsel.mad.easydo.widget;

import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DataBindingAdapters {

    @BindingAdapter("app:errorText")
    public static void setErrorText(TextInputLayout view, @Nullable CharSequence error_text) {
        view.setError(error_text);
    }

    @BindingAdapter("app:onFocusChange")
    public static void setOnFocusChangeListener(TextInputEditText view, @Nullable View.OnFocusChangeListener listener) {
        view.setOnFocusChangeListener(listener);
    }

    @BindingAdapter("app:addTextWatcher")
    public static void setTextChangedListener(TextInputEditText view, @Nullable TextWatcher watcher) {
        view.addTextChangedListener(watcher);
    }

    @BindingAdapter("app:viewAdapter")
    public static void setAdapter(RecyclerView view, @Nullable RecyclerView.Adapter adapter) {
        view.setAdapter(adapter);
    }

    @BindingAdapter("app:onClickCloseIcon")
    public static void onClickCloseIcon(Chip view, @Nullable View.OnClickListener listener) {
        view.setOnCloseIconClickListener(listener);
    }

}
