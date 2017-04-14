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
 * ParserClass extends Asynctask
 * Make the connection the the url
 * and parse the xml get from the url
 * launch the ReleaseActivity when parsage is done
 */

public class ParserClass extends AsyncTask<String, Void, Void> implements Serializable {

    private HashMap<String, String> tabUrl;
    private List<TomeClass> tomesReleases = new ArrayList<>();
    public volatile boolean parsingComplete = true;
    private transient ProgressDialog progressDialog;
    private transient Context context;

    public ParserClass(Context context) {
        // prepare list of url to retreive the new releases from them.
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
        // Launch a progressDialog during the processus
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Retrieve new releases from Kurokawa");
        progressDialog.setMessage("Loading...");
        progressDialog.show();
    }


    @Override
    protected void onPostExecute(Void result) {
        // Processus done close progressDialog and launch the ReleaseActivity
        parsingComplete = false;
        progressDialog.dismiss();
        Intent in = new Intent(context, ReleaseActivity.class);
        in.putExtra("sampleObject", (Serializable) this.tomesReleases);
        context.startActivity(in);
    }


    @Override
    protected Void doInBackground(String... params) {
        // Call the method to connect to the urls
        parsingComplete = true;
        try {
            fetchHTML();
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("Error : ", e.getMessage());
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Connect to the urls give by the ParserClass
     * Retrieve the xml from urls
     * Launch parsage for every urs given.
     * @throws IOException
     * @throws XmlPullParserException
     */
    public void fetchHTML() throws IOException, XmlPullParserException {

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

    /**
     * Start the processus of parsage and recup information we need from the xml given
     *
     * @param myParser
     * @param category name of the category of tomes retrieved (Shonen,Seinen,...)
     */
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
                            // correct syntax
                            if (title.contains("tome "))
                                title = title.replaceAll("tome ", "T");
                            if (title.contains("!"))
                                title = title.replaceAll("!", "");

                        } else if (tagname.equalsIgnoreCase("description")) {
                            desc = text;
                        } else if (tagname.equalsIgnoreCase("item")) {
                            // add to the important part of the ReleaseActivity. The ArrayList which contains all of the
                            // new Releases from urls. Use by most every file in Link with ReleaseActivity
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

