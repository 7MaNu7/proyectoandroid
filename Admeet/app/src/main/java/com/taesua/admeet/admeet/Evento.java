package com.taesua.admeet.admeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Evento extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        //com.appspot.ad_meet.conference.model.Conference evento;
        //evento = (com.appspot.ad_meet.conference.model.Conference) this.getIntent().getExtras().get("eventoselec");

        TextView t;
        t = (TextView)findViewById(R.id.textviewnombre);
        t.setText(this.getIntent().getExtras().getString("name"));
        t = (TextView)findViewById(R.id.textviewlugar);
        t.setText(this.getIntent().getExtras().getString("city"));
        t = (TextView)findViewById(R.id.textviewdescripcion);
        t.setText(this.getIntent().getExtras().getString("description"));
        t = (TextView)findViewById(R.id.textviewfecha);
        t.setText(this.getIntent().getExtras().getString("fecha"));
        t = (TextView)findViewById(R.id.textViewNPersonas);
        t.setText(this.getIntent().getExtras().getString("asientos"));
        t = (TextView)findViewById(R.id.textViewcategoria);
        t.setText(this.getIntent().getExtras().getString("categorias"));

        //Instanciar elemento
        Button b = (Button) findViewById(R.id.buttonEvento);
        //Accion del boton
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Evento.this, Eventos.class);
                startActivity(intent);
            }
        });

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_evento, menu);
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
}