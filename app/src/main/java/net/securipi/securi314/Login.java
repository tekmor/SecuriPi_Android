package net.securipi.securi314;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class Login extends Activity {
    Button b;
    EditText et, pass;
    TextView tv;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        b = (Button) findViewById(R.id.btnLogin);
        et = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        //tv = (TextView)findViewById(R.id.tv);
        ConnectionDetector cd = new ConnectionDetector(getApplicationContext());

        final Boolean isInternetPresent = cd.isConnectingToInternet();

        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isInternetPresent) {
                    dialog = ProgressDialog.show(Login.this, "", "Vérification...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            login();
                        }
                    }).start();
                } else {
                    Toast.makeText(Login.this, "Pas de connexion internet !", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    /**
     * Permet la connexion à la base de donnée et la connexion d'un utilisateur
     */
    void login() {
        try {
            httpclient = new DefaultHttpClient();
            httppost = new HttpPost("http://172.16.81.100/Scripts/Android/connexion.php"); // make sure the url is correct.
            //add your data
            nameValuePairs = new ArrayList<NameValuePair>(2);
            // Always use the same variable name for posting i.e the android side variable name and php side variable name should be similar,
            nameValuePairs.add(new BasicNameValuePair("username", et.getText().toString().trim()));  // $Edittext_value = $_POST['Edittext_value'];
            nameValuePairs.add(new BasicNameValuePair("password", pass.getText().toString().trim()));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            //Execute HTTP Post Request
            response = httpclient.execute(httppost);
            // edited by James from coderzheaven.. from here....
            ResponseHandler<String> responseHandler = new BasicResponseHandler();
            final String response = httpclient.execute(httppost, responseHandler);
            System.out.println("Réponse : " + response);
            runOnUiThread(new Runnable() {
                public void run() {
                    //tv.setText("Réponse de PHP : " + response);
                    dialog.dismiss();
                }
            });

            if (response.equalsIgnoreCase("true")) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(Login.this, "Connexion réussi", Toast.LENGTH_SHORT).show();
                    }
                });

                startActivity(new Intent(Login.this, Visualisation.class));
            } else {
                showAlert();
            }

        } catch (Exception e) {
            dialog.dismiss();
            System.out.println("Exception : " + e.getMessage());
        }
    }

    /**
     * Renvoi une alerte en cas d'erreur d'authentification
     */
    public void showAlert() {
        Login.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
                builder.setTitle("Erreur !");
                builder.setMessage("Erreur d'authentification")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });
    }
}