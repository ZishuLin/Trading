package ca.dal.cs.csci3130.group16.courseproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ChooseStringFragment extends DialogFragment {

    public interface ChooseStringListener {
        void onDialogClick(DialogFragment dialog, CharSequence item);
    }

    private ChooseStringListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChooseStringListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement ChooseStringListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        String title;
        CharSequence[] options;
        if (bundle == null) throw new NullPointerException();
        else {
            title = bundle.getString("title");
            options = bundle.getCharSequenceArray("options");
        }
        if (title == null || options == null) {
            throw new NullPointerException();
        }

        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title)
                .setItems(options, (dialogInterface, i) -> listener.onDialogClick(ChooseStringFragment.this, options[i]));

        return builder.create();
    }
}
