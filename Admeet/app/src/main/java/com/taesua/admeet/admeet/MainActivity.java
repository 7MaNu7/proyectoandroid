package com.taesua.admeet.admeet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.PlusClient;

public class MainActivity extends /*ActionBar*/Activity implements View.OnClickListener,
        GooglePlayServicesClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int REQUEST_CODE_RESOLVE_ERR = 9000;

    private ProgressDialog mConnectionProgressDialog;
    private PlusClient mPlusClient;
    private ConnectionResult mConnectionResult;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //mPlusClient = new PlusClient.Builder(this, this, this).setVisibleActivities("http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity").build();
        mPlusClient = new PlusClient.Builder(this, this, this).setActions(
                "http://schemas.google.com/AddActivity", "http://schemas.google.com/BuyActivity")
                .setScopes("PLUS_LOGIN") // Space separated list of scopes
                .build();

        // Se tiene que mostrar esta barra de progreso si no se resuelve el fallo de conexión.
        mConnectionProgressDialog = new ProgressDialog(this);
        mConnectionProgressDialog.setMessage("Signing in...");

        //Instanciar elemento
        Button b = (Button) findViewById(R.id.buttonLogin);
        //Accion del boton
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Eventos.class);

                try {
                    String accountName = mPlusClient.getAccountName();
                    System.out.println("Este es; " + accountName + " el email");
                }
                catch(Exception e)
                {
                    System.out.println("ERROR mail: " + e.getMessage());
                }//mi try

                startActivity(intent);
            }
        });
    }



    @Override
    protected void onStart() {
        System.out.println("---ON START!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        super.onStart();
        try {
            mPlusClient.connect();
        }
        catch(Exception e)
        {
            System.out.println("ERROR conect: " + e.getMessage());
        }
    }

    @Override
    protected void onStop() {
        System.out.println("---ON STOP!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        super.onStop();
        mPlusClient.disconnect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        System.out.println("---ON CONNECTIONFAILED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(this, REQUEST_CODE_RESOLVE_ERR);
            } catch (IntentSender.SendIntentException e) {
                try {
                    mPlusClient.connect();
                }
                catch(Exception e1)
                {
                    System.out.println("ERROR conect: " + e1.getMessage());
                }//mi try
            }
        }
        // Guarda el resultado y resuelve el fallo de conexión con el clic de un usuario.
        mConnectionResult = result;
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        System.out.println("---ON ACTIVITY RESULT!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        if (requestCode == REQUEST_CODE_RESOLVE_ERR && responseCode == RESULT_OK) {
            mConnectionResult = null;
            try {
                mPlusClient.connect();
            }
            catch(Exception e)
            {
                System.out.println("ERROR conect: " + e.getMessage());
            }//mi try
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("---ON CONNECTED!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String accountName = mPlusClient.getAccountName();
        Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
    }

    //@Override
    public void onConnected() {
        System.out.println("---ON CONNECTED 2 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        String accountName = mPlusClient.getAccountName();
        Toast.makeText(this, accountName + " is connected.", Toast.LENGTH_LONG).show();
    }

    /*   @Override
       public void onConnected(Bundle bundle) {

       }
   */
    @Override
    public void onDisconnected() {
        Log.d(TAG, "disconnected");
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

    }
}