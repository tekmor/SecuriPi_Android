package net.securipi.securi314;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.net.URI;

public class MjpegSample extends Activity {
    private static final String TAG = "MjpegActivity";
    String URL = "";
    private MjpegView mv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mv = new MjpegView(this);
        setContentView(mv);

        Bundle extras = getIntent().getExtras();
        if (extras != null && !extras.getString("ip").equals("")) {
            URL = extras.getString("ip");
        } else {
            URL = "http://webcam.aui.ma/axis-cgi/mjpg/video.cgi?resolution=CIF&amp";
        }

        new DoRead().execute(URL);
    }

    /**
     * Méthode onPause lorsque l'application est en pause
     */
    public void onPause() {
        super.onPause();
        mv.stopPlayback();
        finish();
    }

    /**
     * Envoi de la requête HTTP pour la lecture du flux
     */
    public class DoRead extends AsyncTask<String, Void, MjpegInputStream> {
        protected MjpegInputStream doInBackground(String... url) {
            HttpResponse res = null;
            DefaultHttpClient httpclient = new DefaultHttpClient();
            Log.d(TAG, "1. Sending http request");
            try {
                res = httpclient.execute(new HttpGet(URI.create(url[0])));
                Log.d(TAG, "2. Request finished, status = "
                        + res.getStatusLine().getStatusCode());
                if (res.getStatusLine().getStatusCode() == 401) {
                    return null;
                }
                return new MjpegInputStream(res.getEntity().getContent());
            } catch (ClientProtocolException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-ClientProtocolException", e);
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Request failed-IOException", e);
            }

            return null;
        }

        /**
         * Récupère le flux et l'adapte à l'écran de l'appareil
         * @param result
         */
        protected void onPostExecute(MjpegInputStream result) {
            mv.setSource(result);
            mv.setDisplayMode(MjpegView.SIZE_BEST_FIT);
            mv.showFps(true);
        }
    }
}