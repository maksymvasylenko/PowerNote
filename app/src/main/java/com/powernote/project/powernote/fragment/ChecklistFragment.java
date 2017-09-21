package com.powernote.project.powernote.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * The ChecklistFragment is a fragment to be used within other activities that enables the
 * editing/viewing of a checklist, and handles all communication between itself and the
 * parent activity. 
 */
public class ChecklistFragment extends Fragment {
	
	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return super.onCreateView( inflater, container, savedInstanceState );
	}
	
	@Override
	public void onAttach(Context context) {
		super.onAttach( context );
	}
}
