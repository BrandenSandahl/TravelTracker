package com.sixtel.traveltracker;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Created by branden on 7/27/16.
 */
public class MemoryDialogFragment extends DialogFragment {

    private static final String MEMORY_KEY = "memory";

    private Memory mMemory;
    private Listener mListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments(); //get anything that was saved into the Bundle
        if (args != null) {
            mMemory = (Memory) args.getSerializable(MEMORY_KEY); //set the memory from the args if it is there
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Memory")
                .setMessage(mMemory.city + ", " + mMemory.country)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.OnSaveClicked(mMemory);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.OnCancelClicked(mMemory);
                    }
                });

        return builder.create();
    }

    //Factory method, creates an object (much like a constructor, works when you can't override a constructor), Do this for all Fragments
    public static MemoryDialogFragment newInstance(Memory memory) {
        MemoryDialogFragment fragment = new MemoryDialogFragment(); //create an instance

        Bundle args = new Bundle();
        args.putSerializable(MEMORY_KEY, memory);

        fragment.setArguments(args);

        return fragment;
    }



    //this method just makes sure that the activity does have a Listener interface
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (Listener)getActivity();
        } catch (ClassCastException e) {
            throw new IllegalStateException("Activity does not implement the contract");
        }

    }

    public interface Listener {
        public void OnSaveClicked(Memory memory);
        public void OnCancelClicked(Memory memory);
    }

}
