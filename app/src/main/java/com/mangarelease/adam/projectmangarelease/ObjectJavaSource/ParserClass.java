package com.mangarelease.adam.projectmangarelease.ObjectJavaSource;

import android.os.AsyncTask;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Adam on 23/03/2017.
 */

public class ParserClass extends AsyncTask<String, Void, Void> {


    private List<TomeClass> tomesReleases = new ArrayList<>();
    private String text;
    public volatile boolean parsingComplete = true;
    private boolean finished;

    @Override
    protected void onPostExecute(Void result) {
        finished = true;
        parsingComplete = false;
    }


    @Override
    protected Void doInBackground(String... params) {
        parsingComplete = true;
        finished = false;
        try {
            fetchHTML(params[0]);
        } catch (IOException e) {
            e.printStackTrace();

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void fetchHTML(String urlString) throws IOException, XmlPullParserException {


        URL url = new URL("https://www.kurokawa.fr/rss-subscribe/books-seinen/rss.xml");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setReadTimeout(10000 /* milliseconds */);
        conn.setConnectTimeout(15000 /* milliseconds */);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);

        // Starts the query
        conn.connect();
        InputStream stream = conn.getInputStream();

        XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
        XmlPullParser myparser = xmlFactoryObject.newPullParser();
        myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        myparser.setInput(stream, null);

        parseXML(myparser);
        stream.close();
        conn.disconnect();
    }


    public void parseXML(XmlPullParser myParser) {

        int event;
        String title = "";
        String desc = "";
        String text = "";
        TomeClass tome = null;
        try {
            event = myParser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                String tagname = myParser.getName();
                switch (event) {
                    case XmlPullParser.START_TAG:
                        break;
                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;
                    case XmlPullParser.END_TAG:
                        if (tagname.equalsIgnoreCase("title")) {
                            title = text;
                        } else if (tagname.equalsIgnoreCase("description")) {
                            desc = text;
                        } else if (tagname.equalsIgnoreCase("item")) {
                            tome = new TomeClass(title, desc,"seinen");
                            tomesReleases.add(tome);
                        }
                        break;
                }
                event = myParser.next();
            }
            parsingComplete = false;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<TomeClass> getTomes() {
        return tomesReleases;
    }


}

