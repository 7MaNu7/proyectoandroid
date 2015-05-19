package com.taesua.admeet.admeet;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.ad_meet.conference.Conference;
import com.appspot.ad_meet.conference.model.CommentQueryForm;
import com.appspot.ad_meet.conference.model.ConferenceCollection;
import com.appspot.ad_meet.conference.model.KickerForm;
import com.appspot.ad_meet.conference.model.Profile;
import com.appspot.ad_meet.conference.model.ProfileCollection;
import com.appspot.ad_meet.conference.model.WrappedBoolean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Evento extends ActionBarActivity {

    private List<com.appspot.ad_meet.conference.model.Conference> conferencias_atendidas = new ArrayList<com.appspot.ad_meet.conference.model.Conference>();
    private String websafeKey;
    private String id_seleccionado="";
    private boolean asiste=false;

    private TextView t1;
    private TextView t2;
    private TextView t3;
    private TextView t4;
    private TextView t5;
    private TextView t6;
    private TextView t7;
    private TextView t8;
    private TextView t11;
    private TextView t12;
    private Button buttonEditEvento;
    private DrawerLayout drawerLayout = null;
    private ListView listView;
    Intent IntentExtras;
    private ExpandableHeightListView listViewAsistentes;
    private ParticipantesAdapter adapter;


    private List<com.appspot.ad_meet.conference.model.Comment> listacomments = new ArrayList();
    private List<Profile> listaparticipantes = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evento);

        //ImageButton ja = (ImageButton) findViewById(R.id.ButtonEchar);
        //ja.setFocusable(false);

        listView = (ListView) findViewById(R.id.list_view);
        listViewAsistentes = (ExpandableHeightListView) findViewById(R.id.listViewAsistentes);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final String[]  opciones = { "Eventos", "Filtros", "Publicar", "Perfil" };

        MenuAdapter adapter = new MenuAdapter(Evento.this, opciones);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = null;

                if(opciones[arg2].equals("Eventos"))
                    intent = new Intent(Evento.this,MainActivity.class);
                else if(opciones[arg2].equals("Filtros"))
                    intent = new Intent(Evento.this,Filtros.class);
                else if(opciones[arg2].equals("Publicar"))
                    intent = new Intent(Evento.this,CrearEvento.class);
                else
                    intent = new Intent(Evento.this,EditarPerfil.class);


                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                drawerLayout.closeDrawers();
            }
        });

        listViewAsistentes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,long id)
            {
                Profile persona = listaparticipantes.get(position);

                Intent intent = new Intent(Evento.this,PerfilPublico.class);
                intent.putExtras(IntentExtras.getExtras());
                intent.putExtra("userid",persona.getUserId());
                intent.putExtra("nombre",persona.getDisplayName());
                intent.putExtra("ciudad",persona.getCiudad());
                intent.putExtra("telefono",persona.getTelefono());

                List<String> l = persona.getConferenceKeysToAttend();
                for(int i=0;i<l.size();i++) {
                    intent.putExtra("evento" + i,l.get(i));
                }
                intent.putExtra("tam_keys",persona.getConferenceKeysToAttend().size());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //LIST VIEW COMENTARIOS
        //listViewComentarios = (ExpandableHeightListView) findViewById(R.id.listViewComentarios);
        //listViewComentarios = (ExpandableListView) findViewById(R.id.listViewComentarios);


        // Mostramos el botón en la barra de la aplicación
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu48);



        IntentExtras = this.getIntent();

        t1 = (TextView)findViewById(R.id.textviewnombre);
        t1.setText(this.getIntent().getExtras().getString("name"));
        t1.setVisibility(View.INVISIBLE);

        t2 = (TextView)findViewById(R.id.textviewlugar);
        t2.setText(this.getIntent().getExtras().getString("city"));
        t2.setVisibility(View.INVISIBLE);

        t3 = (TextView)findViewById(R.id.textviewdescripcion);
        t3.setText(this.getIntent().getExtras().getString("description"));
        t3.setVisibility(View.INVISIBLE);

        t4 = (TextView)findViewById(R.id.textviewfecha);
        t4.setText(this.getIntent().getExtras().getString("fecha"));
        t4.setVisibility(View.INVISIBLE);

        t5 = (TextView)findViewById(R.id.textViewNPersonas);
        t5.setText(this.getIntent().getExtras().getString("asientos"));
        t5.setVisibility(View.INVISIBLE);

        t6 = (TextView)findViewById(R.id.textViewcategoria);
        t6.setText(this.getIntent().getExtras().getString("categorias"));
        t6.setVisibility(View.INVISIBLE);

        t7 = (TextView)findViewById(R.id.textview2);
        t7.setVisibility(View.INVISIBLE);

        t8 = (TextView)findViewById(R.id.textViewTituloCategoria);
        t8.setVisibility(View.INVISIBLE);

        t11 = (TextView)findViewById(R.id.textViewTituloNPersonas);
        t11.setVisibility(View.INVISIBLE);

        t12 = (TextView)findViewById(R.id.textViewCreador);
        t12.setText(this.getIntent().getExtras().getString("creador"));
        t12.setVisibility(View.INVISIBLE);

        buttonEditEvento = (Button) findViewById(R.id.buttonEditEvento);
        buttonEditEvento.setVisibility(View.INVISIBLE);

        GetPerfil getPerfil = new GetPerfil();
        getPerfil.execute();

        /*
        if(t12.getText().toString().equals(midisplayname)) {
            buttonEditEvento.setVisibility(View.VISIBLE);
            buttonEditEvento.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Evento.this,EditEvento.class);

                    //SE SUPONE QUE LE PASO TOOOODOS LOS EXTRAS
                    intent.putExtras(IntentExtras.getExtras());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }
        */

        websafeKey = this.getIntent().getExtras().getString("websafeKey");
        //eventoid = this.getIntent().getExtras().getLong("eventoid");
        System.out.println("SU KEY ES " + websafeKey);

        GetEventosAsisto get = new GetEventosAsisto();
        get.execute();

        Button b = (Button) findViewById(R.id.buttonApuntarse);
        b.setVisibility(View.INVISIBLE);

        Button c = (Button) findViewById(R.id.buttonComentar);
        //COMENTAR
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Evento.this,Comentarios.class);

                //SE SUPONE QUE LE PASO TOOOODOS LOS EXTRAS
                intent.putExtras(IntentExtras.getExtras());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //para quitar el focus automatico al abrir la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);


        //GET PARTICIPANTES
        GetParticipantes partici = new GetParticipantes();
        partici.execute();

    }

    public void onBackPressed() {
        Intent intent = new Intent(Evento.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }


    /**
     * Get datos de perfil
     */
    private class GetPerfil extends AsyncTask<Void, Void,Profile>
    {

        public GetPerfil() { }

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

            if(result==null)
            {
            }
            else {
                if(t12.getText().toString().equals(result.getDisplayName())) {
                    buttonEditEvento.setVisibility(View.VISIBLE);
                    buttonEditEvento.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(Evento.this,EditEvento.class);

                            //SE SUPONE QUE LE PASO TOOOODOS LOS EXTRAS
                            intent.putExtras(IntentExtras.getExtras());
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();

                        }
                    });
                }
            }
        }
    }






    private class Apuntarse extends AsyncTask<Void, Void,WrappedBoolean>
    {

        public Apuntarse() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected WrappedBoolean doInBackground(Void ... unused)
        {
             WrappedBoolean registrado = null;
            try
            {
                Conference.RegisterForConference apunt = ConferenceUtils.registrarseEvento(websafeKey);
                registrado = apunt.execute();

            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return registrado;
        }

        @Override
        protected void onPostExecute(WrappedBoolean result)
        {
            System.out.println("EL BOOLEANO DE APUNTARSE ES " + result.getResult());
        }
    }

    private class Desapuntarse extends AsyncTask<Void, Void,WrappedBoolean>
    {

        public Desapuntarse() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected WrappedBoolean doInBackground(Void ... unused)
        {
            WrappedBoolean desapuntado = null;
            try
            {
                Conference.UnregisterFromConference desap = ConferenceUtils.desregistrarseEvento(websafeKey);
                desapuntado = desap.execute();

            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return desapuntado;
        }

        @Override
        protected void onPostExecute(WrappedBoolean result)
        {
            System.out.println("EL BOOLEANO DE DESAPUNTARSE ES " + result.getResult());
        }
    }

    private class GetEventosAsisto extends AsyncTask<Void, Void, ConferenceCollection>
    {
        private final ProgressDialog dialog = new ProgressDialog(Evento.this);
        public GetEventosAsisto() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            this.dialog.setMessage("Cargando...");
            this.dialog.show();
         }
        @Override
        protected ConferenceCollection doInBackground(Void ... unused)
        {
            ConferenceCollection messages = null;
            try
            {
                Conference.GetConferencesToAttend create = ConferenceUtils.getEventosAsisto();
                messages = create.execute();

            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return messages;
        }

        @Override
        protected void onPostExecute(ConferenceCollection result)
        {
            Button x = (Button) findViewById(R.id.buttonApuntarse);
            if(result==null || result.getItems()==null) {

                //APUNTARSE
                x.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Apuntarse apuntarse = new Apuntarse();
                        apuntarse.execute();

                        Intent intent = new Intent(Evento.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            else {
                conferencias_atendidas = result.getItems();
                for(com.appspot.ad_meet.conference.model.Conference c:conferencias_atendidas)
                {
                    System.out.println("LA C ES " + c.getWebsafeKey());
                    if(c.getWebsafeKey().equals(websafeKey)) //ya atiende a esta conferencia, puede desapuntarse
                    {
                        System.out.println("YA ATIENDE A ESTA CONFERENCIA, PODRA DESAPUNTARSE");
                        asiste=true;
                        break;
                    }

                }

                if(asiste) {
                    System.out.println("HA ENTRADO EN DESAPUNTARSE");
                    x.setText("Desapuntarse -");
                    x.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Desapuntarse desapuntarse = new Desapuntarse();
                            desapuntarse.execute();
                            Intent intent = new Intent(Evento.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                else {
                    //APUNTARSE
                    x.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            Apuntarse apuntarse = new Apuntarse();
                            apuntarse.execute();

                            Intent intent = new Intent(Evento.this, MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            }

            t1.setVisibility(View.VISIBLE);
            t2.setVisibility(View.VISIBLE);
            t3.setVisibility(View.VISIBLE);
            t4.setVisibility(View.VISIBLE);
            t5.setVisibility(View.VISIBLE);
            t6.setVisibility(View.VISIBLE);
            t7.setVisibility(View.VISIBLE);
            t8.setVisibility(View.VISIBLE);
            t11.setVisibility(View.VISIBLE);
            t12.setVisibility(View.VISIBLE);
            x.setVisibility(View.VISIBLE);
            dialog.dismiss();

        }
    }

    private class GetParticipantes extends AsyncTask<Void, Void, ProfileCollection>
    {
        public GetParticipantes() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
        }
        @Override
        protected ProfileCollection doInBackground(Void ... unused)
        {
            ProfileCollection messages = null;
            try
            {
                //Conference.GetConferencesToAttend create = ConferenceUtils.getEventosAsisto();
                //messages = create.execute();
                CommentQueryForm form = new CommentQueryForm();
                form.setSafeKey(websafeKey);
                Conference.GetParticipants create = ConferenceUtils.getParticipantes(form);
                messages = create.execute();

            }
            catch (Exception e)
            {
                System.out.println(e.getMessage());
            }

            return messages;
        }

        @Override
        protected void onPostExecute(ProfileCollection result)
        {
            int tam=0;

            if(result!=null)
                listaparticipantes = result.getItems();
            if(listaparticipantes!=null)
                tam = listaparticipantes.size();

            System.out.println("EL TAM DE PARTICIPANTES ES " + tam);
            //rellenaListViewComentarios(listacomments, tam);

            if(tam>0) {
                adapter = new ParticipantesAdapter(Evento.this, R.layout.listviewparticipante_item, listaparticipantes);
                listViewAsistentes.setAdapter(adapter);
                listViewAsistentes.setExpanded(true);
            }

        }
    }

    private class Kick extends AsyncTask<Void, Void,Void>
    {
        public Kick() { }

        @Override
        protected Void doInBackground(Void ... unused)
        {
            try
            {
                KickerForm form = new KickerForm();
                form.setUserKey(id_seleccionado);
                form.setEventKey(websafeKey);
                System.out.println("USERID: " + form.getUserKey());
                System.out.println("WEBSAFEKEY: " + form.getEventKey());
                Conference.KickFromConference kick = ConferenceUtils.echarDeConferencia(form);
                kick.execute();
            }
            catch (Exception e)
            {
                //System.out.println(e.getMessage());
                //e.printStackTrace();
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }
    }

    //HANDLER PARA BOTON DE BORRAR PARTICIPANTE
    public void echarHandler(View v) {
        Profile itemToRemove = (Profile)v.getTag();
        System.out.println("CLICK AL BOTON DE " + itemToRemove.getUserId());

        id_seleccionado=itemToRemove.getUserId();
        //la websafe ya esta
        Kick echar = new Kick();
        echar.execute();
        //adapter.remove(itemToRemove);
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