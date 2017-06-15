package com.netsize.netsizeqa;

import android.content.Context;

import com.netsize.netsizeqa.utils.QaViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by loxu on 13/06/2017.
 */

public interface MainContract {

    interface View extends BaseView<Presenter>{

        void showCountries(String[] countries);

        void showProgressDialog();

        void updateDialog(int progress);

        void showTestcases(QaViewModel qaViewModel);

        Context getContext();
    }

    interface Presenter extends  BasePresenter{

        void loadCountries();

        void loadTestcases ();

        void downloadXML();


    }
}