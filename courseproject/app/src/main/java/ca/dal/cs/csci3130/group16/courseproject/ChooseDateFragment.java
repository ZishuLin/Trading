package ca.dal.cs.csci3130.group16.courseproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class ChooseDateFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    public interface ChooseDateListener {
        void onDateChosen(DialogFragment dialog, Calendar calendar);
    }

    private ChooseDateListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ChooseDateListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context
                    + " must implement ChooseDateListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(requireContext(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        listener.onDateChosen(ChooseDateFragment.this, c);
    }
}
