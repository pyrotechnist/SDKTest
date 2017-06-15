package com.netsize.netsizeqa;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.netsize.netsizeqa.utils.QaViewModel;
import com.netsize.netsizeqa.utils.TestcasesListAdapter;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by loxu on 13/06/2017.
 */

public class MainFragment extends Fragment implements MainContract.View {

    private MainContract.Presenter mPresenter;

    private Spinner spinnerCountry;

    private Spinner spinnerEnv;

    private TextView textView;

    private String selectedCountry;

    private ListView testcasesListView;

    private ProgressDialog mProgressDialog;

    private static List<TestCase> mTestList = new LinkedList<TestCase>();


    public MainFragment() {
        // Requires empty public constructor
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mListAdapter = new TasksAdapter(new ArrayList<Task>(0), mItemListener);
    }

    @Override
    public void showCountries(String[] countries) {

        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item, countries);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCountry.setAdapter(arrayAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.mainfragment, container, false);

        spinnerCountry = (Spinner) root.findViewById(R.id.spinner_country);
        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int pos, long id) {
                updateTestCases(spinnerCountry.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        spinnerEnv = (Spinner) root.findViewById(R.id.spinner_env);

        //textView = (TextView) root.findViewById(R.id.test);

        testcasesListView = (ListView)root.findViewById(R.id.testcases_list) ;

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setMessage("Downloading cache file..");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);


        setHasOptionsMenu(true);

        return root;

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.start();
    }



    @Override
    public void showProgressDialog() {
        mProgressDialog.show();
    }

    @Override
    public void updateDialog(int progress) {
        if (progress == 100) {
            mProgressDialog.dismiss();
        } else
            mProgressDialog.setProgress(progress);

    }

    @Override
    public void showTestcases(QaViewModel qaViewModel) {


        if(!qaViewModel.TestCases.isEmpty())

        {

            String[] countries = qaViewModel.CountryList.isEmpty() ? new String[0] : qaViewModel.CountryList.toArray(new String[qaViewModel.CountryList.size()]);

            this.showCountries(countries);

            selectedCountry = countries[0];
        }


        if(qaViewModel.TestCases != null )
        {

            mTestList = qaViewModel.TestCases;

            updateTestCases(selectedCountry);

        }


    }



    @Override
    public void setPresenter(@NonNull MainContract.Presenter presenter) {

        mPresenter = presenter;

    }

    private void updateTestCases (String country){



        Iterator<TestCase> it = mTestList.iterator();
        List<TestCase> filteredList = new LinkedList<TestCase>();
        while (it.hasNext()) {
            TestCase local = it.next();

            if (local.getCountry().equalsIgnoreCase(country))
                filteredList.add(local);
        }

        TestCase[] arr = filteredList.toArray(new TestCase[mTestList.size()]);
        TestcasesListAdapter adapter = new TestcasesListAdapter(getActivity(),arr);
        testcasesListView.setAdapter(adapter);

    }

    public Context getContext()
    {
        return  getActivity();
    }
}
