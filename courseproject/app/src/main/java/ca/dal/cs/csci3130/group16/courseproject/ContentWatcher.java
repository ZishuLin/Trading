package ca.dal.cs.csci3130.group16.courseproject;

import android.text.Editable;
import android.text.TextWatcher;

import javax.annotation.Nullable;

public abstract class ContentWatcher<T> implements TextWatcher {

    public interface ContentListener<T> {
        void afterChange(@Nullable T content);
    }

    private final ContentListener<T> listener;

    protected ContentWatcher(ContentListener<T> listener) {
        this.listener = listener;
    }

    protected abstract Object convertContent(String content);
    @Override
    public void afterTextChanged(Editable editable) {
        Object converted = convertContent(editable.toString());
        listener.afterChange((T) converted);
    }
    @Override
    public final void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // DO NOT do anything until we have the entire change
    }
    @Override
    public final void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // DO NOT do anything until we have the entire change
    }
}
