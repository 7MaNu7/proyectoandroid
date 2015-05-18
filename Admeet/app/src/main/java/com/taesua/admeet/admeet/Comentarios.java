package com.taesua.admeet.admeet;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.appspot.ad_meet.conference.Conference;
import com.appspot.ad_meet.conference.model.Comment;
import com.appspot.ad_meet.conference.model.CommentCollection;
import com.appspot.ad_meet.conference.model.CommentForm;
import com.appspot.ad_meet.conference.model.CommentQueryForm;

import java.util.ArrayList;
import java.util.List;


public class Comentarios extends ActionBarActivity {

    private DrawerLayout drawerLayout = null;
    private ListView listView;
    private Intent IntentExtras;
    private String websafeKey;
    private List<Comment> listacomments = new ArrayList<Comment>();
    private ListView listViewComentarios;

    private Context contexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comentarios);

        contexto = getApplicationContext();

        //para quitar el focus automatico al abrir la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        listView = (ListView) findViewById(R.id.list_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final String[]  opciones = { "Eventos", "Filtros", "Publicar", "Perfil" };

        MenuAdapter adapter = new MenuAdapter(Comentarios.this, opciones);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = null;

                if(opciones[arg2].equals("Eventos"))
                    intent = new Intent(Comentarios.this,MainActivity.class);
                else if(opciones[arg2].equals("Filtros"))
                    intent = new Intent(Comentarios.this,Filtros.class);
                else if(opciones[arg2].equals("Publicar"))
                    intent = new Intent(Comentarios.this,CrearEvento.class);
                else
                    intent = new Intent(Comentarios.this,EditarPerfil.class);


                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                drawerLayout.closeDrawers();
            }
        });

        listViewComentarios = (ListView) findViewById(R.id.listViewComentarios);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu48);

        IntentExtras = this.getIntent();
        websafeKey = this.getIntent().getExtras().getString("websafeKey");

        GetComentarios getComents = new GetComentarios();
        getComents.execute();

        Button c = (Button) findViewById(R.id.buttonComentar);
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText edit = (EditText) findViewById(R.id.editText);

                //SI HAY ALGO ESCRITO, COMENTAR
                if(!edit.getText().toString().equals(""))
                {
                    Comentar comentar = new Comentar();
                    comentar.execute();

                    //recarga comentarios
                    GetComentarios coments = new GetComentarios();
                    coments.execute();
                }


            }
        });


    }

    public void onBackPressed() {

        Intent intent = new Intent(Comentarios.this, Evento.class);
        intent.putExtras(IntentExtras.getExtras());
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private class GetComentarios extends AsyncTask<Void, Void, CommentCollection>
    {
        public GetComentarios() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected CommentCollection doInBackground(Void ... unused)
        {
            CommentCollection messages = null;
            try
            {
                CommentQueryForm form = new CommentQueryForm();
                form.setSafeKey(websafeKey);
                Conference.GetComments create = ConferenceUtils.getComentarios(form);
                messages = create.execute();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return messages;
        }

        @Override
        protected void onPostExecute(CommentCollection result)
        {
            int tam=0;

            if(result!=null)
                listacomments = result.getItems();
            if(listacomments!=null)
                tam = listacomments.size();

            System.out.println("EL TAM ES " + tam);
            rellenaListViewComentarios(listacomments, tam);

        }
    }

    private class Comentar extends AsyncTask<Void, Void,Void>
    {
        public Comentar() {}
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void ... unused)
        {

            try
            {
                Comment c = null;

                CommentForm form = new CommentForm();
                form.setWebSafeKey(websafeKey);
                EditText edit = (EditText) findViewById(R.id.editText);
                form.setComment(edit.getText().toString());
                Conference.CreateComment create = ConferenceUtils.crearComentario(form);
                create.execute();
                edit.getText().clear();
            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void nada)
        {
            EditText edit = (EditText) findViewById(R.id.editText);
            edit.getText().clear();
        }

    }

    public void rellenaListViewComentarios(List<com.appspot.ad_meet.conference.model.Comment> listacomentarios,int tam)
    {

        String nombres[] = new String[tam];
        String comentarios[] = new String[tam];

        for(int i=0;i<tam;i++) {
            nombres[i]=(listacomentarios.get(i).getComment());
            comentarios[i]=(listacomentarios.get(i).getComment());
            System.out.println("----------Aqui tenemos el comentario::::::"+comentarios[i]);
        }

        //GUARRADAS INCOMING
        ComentariosAdapter adapterc = new ComentariosAdapter(contexto, nombres, comentarios);
        listViewComentarios.setAdapter(adapterc);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_eventos, menu);
        return true;
    }

    // Mostramos el botón en la barra de la aplicación
    //getActionBar().setDisplayHomeAsUpEnabled(true);
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (drawerLayout.isDrawerOpen(listView)) {
                    drawerLayout.closeDrawers();
                } else {
                    drawerLayout.openDrawer(listView);
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
