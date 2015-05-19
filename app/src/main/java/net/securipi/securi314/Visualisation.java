package net.securipi.securi314;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class Visualisation extends Activity implements OnClickListener {
    Button b1;
    Button b2;
    Button b3;
    String ip = "http://172.16.81.100:8081";
    String ip2 = "http://172.16.81.101:8081";
    String ip3 = "http://172.16.81.102:8081";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.visualisation);
        b1 = ((Button) this.findViewById(R.id.button1));
        b1.setOnClickListener(this);
        b2 = ((Button) this.findViewById(R.id.button2));
        b2.setOnClickListener(this);
        b3 = ((Button) this.findViewById(R.id.button3));
        b3.setOnClickListener(this);
    }

    /**
     * Démarre l'activity en fonction du bouton click
     * @param v
     */
    @Override
    public void onClick(View v) {
        if (v == b1) {
            String ip_adress = ip;
            Intent i = new Intent(Visualisation.this, MjpegSample.class);
            i.putExtra("ip", ip_adress);
            startActivity(i);
        }

        if (v == b2) {
            String ip_adress = ip2;
            Intent i = new Intent(Visualisation.this, MjpegSample.class);
            i.putExtra("ip", ip_adress);
            startActivity(i);
        }

        if (v == b3) {
            String ip_adress = ip3;
            Intent i = new Intent(Visualisation.this, MjpegSample.class);
            i.putExtra("ip", ip_adress);
            startActivity(i);
        }

    }
}
