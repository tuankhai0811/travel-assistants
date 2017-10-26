package com.tuankhai.travelassistants.bottomsheet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.bottomsheet.interfaces.OnItemMenuSheetBottomClickListener;

/**
 * Created by Khai on 26/10/2017.
 */

public class MenuBottomSheet extends BottomSheetDialogFragment {

    OnItemMenuSheetBottomClickListener mListener;

    View delete, edit;

    public MenuBottomSheet(OnItemMenuSheetBottomClickListener listener) {
        this.mListener = listener;
    }

    public MenuBottomSheet() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_bottom_sheet_menu_schedule, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        delete = view.findViewById(R.id.layout_action_delete_schedule);
        edit = view.findViewById(R.id.layout_action_edit_schedule);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDeleteClick();
                MenuBottomSheet.this.dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onEditClick();
                MenuBottomSheet.this.dismiss();
            }
        });
    }
}
