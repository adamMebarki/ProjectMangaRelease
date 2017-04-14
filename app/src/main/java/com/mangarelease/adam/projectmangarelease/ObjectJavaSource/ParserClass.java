package com.mangarelease.adam.projectmangarelease.ObjectJavaSource;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.mangarelease.adam.projectmangarelease.ReleaseActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Adam on 23/03/2017.
 */

public class ParserClass extends AsyncTask<String, Void, Void> implements Serializable {

    private HashMap<String, String> tabUrl;
    private List<TomeClass> tomesReleases = new ArrayList<>();
    public volatile boolean parsingComplete = true;
    private transient ProgressDialog progressDialog;
    private transient Context context;

    public ParserClass(Context context) {
        tabUrl = new HashMap<>();
        tabUrl.put("Shojo", "https://www.kurokawa.fr/rss-subscribe/books-shojo/rss.xml");
        tabUrl.put("Seinen", "https://www.kurokawa.fr/rss-subscribe/books-seinen/rss.xml");
        tabUrl.put("Shonen", "https://www.kurokawa.fr/rss-subscribe/books-shonen/rss.xml");
        tabUrl.put("Humour", "https://www.kurokawa.fr/rss-subscribe/books-humour/rss.xml");
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Retrieve new releases from Kurokawa");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        Log.d("PRE EXECUTE", "PRE EXECUTE");
    }


    @Override
    protected void onPostExecute(Void result) {
        parsingComplete = false;
        progressDialog.dismiss();
        Intent in = new Intent(context, ReleaseActivity.class);
        in.putExtra("sampleObject", (Serializable) this.tomesReleases);
        context.startActivity(in);
    }



    @Override
    protected Void doInBackground(String... params) {
        parsingComplete = true;
        try {
            fetchHTML(params[0]);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error : ", e.getMessage());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void fetchHTML(String urlString) throws IOException, XmlPullParserException {

        for (Map.Entry<String, String> entry : tabUrl.entrySet()) {
            String cle = entry.getKey();
            String valeur = entry.getValue();
            // traitements

            URL url = new URL(valeur);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            Log.d("Connection : ", conn.getResponseCode() + "");
            // Starts the query
            conn.connect();
            InputStream stream = conn.getInputStream();

            XmlPullParserFactory xmlFactoryObject = XmlPullParserFactory.newInstance();
            XmlPullParser myparser = xmlFactoryObject.newPullParser();
            myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            myparser.setInput(stream, null);

            parseXML(myparser, cle);
            stream.close();
            conn.disconnect();
        }
        parsingComplete = false;
    }


    public void parseXML(XmlPullParser myParser, String category) {

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
                            if (title.contains("tome "))
                                title = title.replaceAll("tome ", "T");
                            if (title.contains("!"))
                                title = title.replaceAll("!", "");

                        } else if (tagname.equalsIgnoreCase("description")) {
                            desc = text;
                        } else if (tagname.equalsIgnoreCase("item")) {
                            tome = new TomeClass(title, desc, category);
                            tomesReleases.add(tome);
                        }
                        break;
                }
                event = myParser.next();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<TomeClass> getTomes() {
        return tomesReleases;
    }


}

