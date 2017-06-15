package com.netsize.netsizeqa.utils;

import android.util.Xml;

import com.netsize.netsizeqa.TestCase;

import org.xmlpull.v1.XmlPullParser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by loxu on 15/06/2017.
 */

public class PullXmlParser {
    public static QaViewModel getTestCases(FileInputStream file) throws Exception{
        List<TestCase> TestCases = null;
        QaViewModel qaViewModel = new QaViewModel();
        TestCase testCase = null;
        XmlPullParser parser= Xml.newPullParser();
        Set<String> mUniqueCountryList = new HashSet<String>();

        //File file = new File( "cache.xml");
        //FileInputStream fileInputStream = new FileInputStream(file);
        InputStream inStream = file;
        parser.setInput(inStream, "UTF-8");
        int eventType = parser.getEventType();    //触发第一个事件
        while(eventType!=XmlPullParser.END_DOCUMENT){
            switch(eventType){
                case XmlPullParser.START_DOCUMENT:
                    TestCases = new ArrayList<TestCase>();
                    break;

                case XmlPullParser.START_TAG:    //开始元素事件
                    if("testCase".equals(parser.getName())){
                        testCase = new TestCase();
                        testCase.setId(new Integer(parser.getAttributeValue(0)));
                        testCase.setTitle(parser.getAttributeValue(1));
                        testCase.setCountry(parser.getAttributeValue(2));
                        mUniqueCountryList.add(parser.getAttributeValue(2));
                    }else if(testCase!=null){
                        if("data".equals(parser.getName())){
                            if("Command".equals(parser.getAttributeValue(null,"name")))
                                testCase.setCommand(parser.getAttributeValue(null,"value"));
                            if("ProductName".equals(parser.getAttributeValue(null,"name")))
                                testCase.setProductName(parser.getAttributeValue(null,"value"));
                            if("ServiceId".equals(parser.getAttributeValue(null,"name")))
                                testCase.setServiceId(parser.getAttributeValue(null,"value"));

                        }
                    }
                    break;

                case XmlPullParser.END_TAG:    //结束元素事件
                    if("testCase".equals(parser.getName())){
                        TestCases.add(testCase);
                        testCase = null;
                    }
                    break;

                default:
                    break;
            }
            eventType = parser.next();
        }
        qaViewModel.TestCases = TestCases;

        qaViewModel.CountryList = mUniqueCountryList;

        return qaViewModel;
    }




}

