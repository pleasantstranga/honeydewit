package com.ajbtechnologies.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ajbtechnologies.Application;
import com.ajbtechnologies.CheckBoxListActivity;
import com.ajbtechnologies.Constants;
import com.ajbtechnologies.ImportedList;
import com.ajbtechnologies.R;
import com.ajbtechnologies.listeners.OneOffClickListener;

import java.util.ArrayList;

public class CheckListAdapter extends ArrayAdapter<ImportedList> {


    private ArrayList<ImportedList> sheets;
    private Context context;
    private OnCheckedChangeListener checkboxListener;
    private Integer resourceId;

    public CheckListAdapter(Context context, int resource, ArrayList<ImportedList> sheets) {
        super(context, resource, sheets);
        this.context = context;
        this.sheets = sheets;
        this.resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(resourceId, null);
        }
        final ImportedList importedList = sheets.get(position);
        final CheckBox checkbox = (CheckBox) v.findViewById(R.id.checkbox);

        LinearLayout checkboxinfo = (LinearLayout) v.findViewById(R.id.checkboxinfo);
        checkboxinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkbox.isChecked()) {
                    checkbox.setChecked(false);
                } else {
                    checkbox.setChecked(true);
                }
            }
        });
        if (importedList.isChecked()) {
            checkbox.setChecked(true);
        }
        String sheetName = importedList.getCurrentName();
        String newSheetName = importedList.getNewName();

        TextView checkBoxTextView = (TextView) v.findViewById(R.id.topRow);
        checkBoxTextView.setText("Sheet Name: " + sheetName);

        final TextView newSheetNameTextView = (TextView) v.findViewById(R.id.bottomRow);
        if (newSheetName != null && !newSheetName.equals(importedList.getCurrentName())) {
            newSheetNameTextView.setVisibility(View.VISIBLE);
            newSheetNameTextView.setTextColor(Color.RED);
            newSheetNameTextView.setText("New List Name: " + newSheetName);
        } else {
            newSheetNameTextView.setVisibility(View.GONE);
        }

        ImageButton editButton = (ImageButton) v.findViewById(R.id.editSheetBtn);
        editButton.setOnClickListener(new OneOffClickListener() {


            @Override
            public void onClick(View v) {

                if(checkbox.isChecked()) {
                    boolean isExists = ((Application) context.getApplicationContext()).getShoppingListDbHelper().isListExists(importedList.getCurrentName(), Constants.SHOPPING_LIST_TYPE_CDE, true);
                    boolean isEmpty = (importedList.getCurrentName() == null || importedList.getCurrentName().length() < 3);

                    ((CheckBoxListActivity) context).showImportChoicesDialog(importedList, isEmpty, isExists, false, checkbox);

                }
                else {
                    Toast.makeText(context, "You cannot edit an item that is not checked",Toast.LENGTH_LONG).show();
                }
            }
        });

        checkbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    importedList.setChecked(true);
                    boolean isExists = ((Application) context.getApplicationContext()).getShoppingListDbHelper().isListExists(importedList.getCurrentName(), Constants.SHOPPING_LIST_TYPE_CDE, true);
                    boolean isEmpty = (importedList.getCurrentName() == null || importedList.getCurrentName().length() < 3);
                    if(isExists || isEmpty) {
                        ((CheckBoxListActivity) context).showImportChoicesDialog(importedList, isEmpty, isExists, true,buttonView);
                    }
                }
                else {
                    importedList.setChecked(false);
                }
            }

        });


        return v;
    }

    public boolean isRowChecked() {
        for (ImportedList list : sheets) {
            if (list.isChecked()) {
                return true;
            }
        }
        return false;

    }

}