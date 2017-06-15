package com.netsize.netsizeqa;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import com.netsize.netsizeqa.utils.PullXmlParser;
import com.netsize.netsizeqa.utils.QaViewModel;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;
import android.content.Context;


import static android.R.id.input;

/**
 * Created by loxu on 13/06/2017.
 */

public class MainPresenter implements MainContract.Presenter {

    private final MainContract.View mMainFragment;

    OkHttpClient client = new OkHttpClient();

    FileOutputStream mOutput ;

    public MainPresenter (MainContract.View mainFragment){

        mMainFragment = mainFragment;

        mMainFragment.setPresenter(this);
    }


    @Override
    public void start() {
        loadTestcases();
    }


    @Override
    public void loadCountries() {


    }

    @Override
    public void loadTestcases() {

        downloadXML();

    }

    @Override
    public void downloadXML() {

        String url= "http://dev.pay.netsize.com/merchantdemo/sdk";

        OkHttpHandler okHttpHandler= new OkHttpHandler();

        okHttpHandler.execute(url);

    }

    private void getTestCases(){


        FileInputStream file= null;
        try {

            file= mMainFragment.getContext().openFileInput("cache.xml");
            QaViewModel qaViewModel  = PullXmlParser.getTestCases(file);

            if(qaViewModel != null)
            {
                mMainFragment.showTestcases(qaViewModel);
            }




        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try { // because close can throw an exception
                if (file != null) file.close();
            } catch (IOException ignored) {}
        }


    }

    public class OkHttpHandler extends AsyncTask<String,String,String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mMainFragment.showProgressDialog();
        }

        @Override
        protected String doInBackground(String... params) {

            Request.Builder builder = new Request.Builder();
            builder.url(params[0]);
            Request request = builder.build();

            try {
                mOutput = mMainFragment.getContext().openFileOutput(
                        "cache.xml", Context.MODE_PRIVATE);

                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();

                String dd = response.header("");

                //  String dd = response.body().string();
                long contentLength = body.contentLength();
                InputStream input = response.body().byteStream();


                byte data[] = new byte[1024];
                long total = 0;
                int count;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // publishProgress(String.valueOf((int) (total * 100 / contentLength)));
                    mOutput.write(data, 0, count);
                }

                mOutput.flush();
                mOutput.close();
                response.close();
                input.close();

                //return response.body().string();

                /*ResponseBody body = response.body();
                long contentLength = body.contentLength();
                BufferedSource source = body.source();

                File file = new File("cache.xml");
                BufferedSink sink = Okio.buffer(Okio.sink(file));

                long totalRead = 0;
                long read = 0;
                int DOWNLOAD_CHUNK_SIZE = 2048;

                while ((read = source.read(sink.buffer(), DOWNLOAD_CHUNK_SIZE)) != -1){
                    totalRead += read;
                    int progress = (int) ((totalRead * 100) / contentLength);
                    publishProgress(String.valueOf(progress));
                }
                sink.writeAll(source);
                sink.flush();
                sink.close();*/



               // mMainFragment.updateDialog(100);
                return "done";

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

       /* protected void onProgressUpdate(String... progress) {
            Log.d("ANDRO_ASYNC", progress[0]);
            mMainFragment.updateDialog(Integer.parseInt(progress[0]));
        }
*/

        @Override
        protected void onPostExecute(String s) {
            //mMainFragment.dismissProgressDialog();
            //mMainFragment.showTestcases(s);
            getTestCases();

        }
    }


}