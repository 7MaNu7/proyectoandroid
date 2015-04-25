package com.taesua.admeet.admeet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import conference.Conference;
import conference.model.Profile;

/**
 * Created by Hector on 14/04/2015.
 */
public class Perfil extends ActionBarActivity {

    private TextView nombre;
    private TextView ciudad;
    private TextView tlf;
    private TextView id;

    private TextView textNombre;
    private TextView textCiudad;
    private TextView textTlf;
    private Button editar_perfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        Button evento = (Button) findViewById(R.id.buttonAnuncios);
        Button perfil2 = (Button) findViewById(R.id.buttonPerfil);
        Button publicar = (Button) findViewById(R.id.buttonPublicar);
   //     perfil2.setBackground(getDrawable(R.drawable.bordeazulseleccionado));
     //   evento.setBackground(getDrawable(R.drawable.sinbordeazulseleccionado));
       // publicar.setBackground(getDrawable(R.drawable.sinbordeazulseleccionado));

        //BOTON PERFIL, MARCARLO
        Button perfil = (Button)findViewById(R.id.buttonPerfil);
        perfil.setTypeface(null, Typeface.BOLD);

        //BOTON VOLVER A EVENTOS
        Button b = (Button) findViewById(R.id.buttonAnuncios);
        b.setTypeface(null,Typeface.NORMAL);

        //Accion del boton
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, Eventos.class);
                startActivity(intent);
            }
        });

        //BOTON EDITAR PERFIL
        editar_perfil = (Button) findViewById(R.id.buttonEditarPerfil);
        editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Perfil.this, EditarPerfil.class);
                intent.putExtra("nombre",nombre.getText().toString());
                intent.putExtra("ciudad",ciudad.getText().toString());
                intent.putExtra("tlf",tlf.getText().toString());
                startActivity(intent);
            }
        });
        editar_perfil.setVisibility(View.INVISIBLE);

        textNombre = (TextView) findViewById(R.id.textNombre);
        textCiudad = (TextView) findViewById(R.id.textCiudad);
        textTlf = (TextView) findViewById(R.id.textTlf);

        textNombre.setVisibility(View.INVISIBLE);
        textCiudad.setVisibility(View.INVISIBLE);
        textTlf.setVisibility(View.INVISIBLE);

        Button p = (Button) findViewById(R.id.buttonPerfil);
        p.setTypeface(null, Typeface.BOLD);

        nombre = (TextView) findViewById(R.id.textViewNombre);
        ciudad = (TextView) findViewById(R.id.textViewCiudad);
        tlf = (TextView) findViewById(R.id.textViewTlf);
        id = (TextView) findViewById(R.id.textViewNombreID);
        id.setText("");


        //PARA IR A PUBLICAR
        findViewById(R.id.buttonPublicar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ji = new Intent(Perfil.this,CrearEvento.class);
                startActivity(ji);
            }
        });

        GetPerfil getperfil = (GetPerfil) new GetPerfil().execute();
    }

    /**
     * Ver todos los eventos
     */
    private class GetPerfil extends AsyncTask<Void, Void,Profile>
    {
        private ProgressDialog pd;

        public GetPerfil() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd = new ProgressDialog(Perfil.this);
            pd.setMessage("Cargando información de perfil...");
            pd.show();
        }
        @Override
        protected Profile doInBackground(Void ... unused)
        {
            Profile perfil = null;
            try
            {
                Conference.GetProfile prof = ConferenceUtils.getProfile();
                perfil = prof.execute();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return perfil;
        }

        @Override
        protected void onPostExecute(Profile result)
        {
            //Clear the progress dialog and the fields
            pd.dismiss();

            if(result==null)
            {
                nombre.setText("No especificado");
                ciudad.setText("No especificado");
                tlf.setText("No especificado");
            }
            else {
                nombre.setText(result.getDisplayName());
                ciudad.setText(result.getCiudad());
                tlf.setText(result.getTelefono());
                id.setText(result.getMainEmail());
            }
            //Display success message to user
            Toast.makeText(getBaseContext(), "Información de perfil cargada correctamente",
                    Toast.LENGTH_SHORT).show();

            textNombre.setVisibility(View.VISIBLE);
            textCiudad.setVisibility(View.VISIBLE);
            textTlf.setVisibility(View.VISIBLE);
            editar_perfil.setVisibility(View.VISIBLE);

        }
    }
}
