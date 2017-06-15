package com.netsize.netsizeqa.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.netsize.netsizeqa.R;
import com.netsize.netsizeqa.TestCase;

import java.util.List;

/**
 * Created by loxu on 15/06/2017.
 */

public class TestcasesListAdapter extends ArrayAdapter<TestCase> {

    private final Context context;
    private final TestCase[] values;


    public TestcasesListAdapter(Context context, TestCase[] values) {
        super(context, -1, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.testcase, parent, false);
        TextView textViewLabel = (TextView) rowView.findViewById(R.id.label);
        TextView textViewServiceId = (TextView) rowView.findViewById(R.id.serviceid);
        textViewLabel.setText(values[position].getTitle());

        textViewServiceId.setText(values[position].getServiceId());

        return rowView;
    }
}
