package com.taesua.admeet.admeet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import conference.Conference;
import conference.model.Profile;
import conference.model.ProfileForm;

/**
 * Created by Hector on 14/04/2015.
 */
public class EditarPerfil extends ActionBarActivity {
    private EditText nombre;
    private EditText ciudad;
    private EditText tlf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_perfil);


        System.out.println("HA ENTRADO EN EDITARPERFIL!!!!!!!!!!!!!!!!!!!");
        //BOTON VOLVER A EVENTOS
        Button b = (Button) findViewById(R.id.buttonAnuncios);
        //Accion del boton
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EditarPerfil.this, Eventos.class);
                startActivity(intent);
            }
        });


        //BOTON EDITAR PERFIL

        Button s = (Button) findViewById(R.id.buttonSaveProf);

        s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(nombre.getText().toString().equals(""))
                {
                    /*
                    Toast.makeText(getBaseContext(), "¡No se puede dejar vacío el campo de nombre!",
                            Toast.LENGTH_SHORT).show();
                    */

                    nombre.setError("¡No se puede dejar vacío el campo de nombre!");
                }
                else {
                    GuardarPerfil guardarPerfil = (GuardarPerfil) new GuardarPerfil().execute();
                    Intent intent = new Intent(EditarPerfil.this, Perfil.class);
                    intent.putExtra("editado", true);
                    startActivity(intent);
                }
            }
        });

        Button p = (Button) findViewById(R.id.buttonPerfil);
        p.setTextColor(Color.parseColor("#000000"));

        nombre = (EditText) findViewById(R.id.editTextName);
        ciudad = (EditText) findViewById(R.id.editTextCity);
        tlf = (EditText) findViewById(R.id.editTextPhone);


        nombre.setText(this.getIntent().getExtras().getString("nombre"));
        ciudad.setText(this.getIntent().getExtras().getString("ciudad"));
        tlf.setText(this.getIntent().getExtras().getString("tlf"));

        tlf.setInputType(InputType.TYPE_CLASS_NUMBER);



    }

    /**
     * Ver todos los eventos
     */
    private class GuardarPerfil extends AsyncTask<Void, Void,Profile>
    {

        public GuardarPerfil() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected Profile doInBackground(Void ... unused)
        {
            Profile perfil = null;
            try
            {

                ProfileForm form = new ProfileForm();
                form.setDisplayName(nombre.getText().toString());
                form.setCiudad(ciudad.getText().toString());
                form.setTelefono(tlf.getText().toString());
                Conference.SaveProfile prof = ConferenceUtils.saveProfile(form);
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
        }
    }
}
