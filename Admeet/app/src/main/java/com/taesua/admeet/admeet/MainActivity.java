package com.taesua.admeet.admeet;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appspot.ad_meet.conference.Conference;
import com.appspot.ad_meet.conference.model.ConferenceCollection;
import com.appspot.ad_meet.conference.model.ConferenceQueryForm;
import com.appspot.ad_meet.conference.model.Filter;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = "MainActivity";
    private static final int ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION = 2222;
    private ListView eventos;
    private List<com.appspot.ad_meet.conference.model.Conference> listaeventos = new ArrayList();
    private Drawable estiloselec = null;
    private Drawable estilonoselec = null;
    private DrawerLayout drawerLayout = null;
    private ListView listView;
    ConferenceQueryForm query = new ConferenceQueryForm();
    private AuthorizationCheckTask mAuthTask;
    private String mEmailAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEmailAccount = Utils.getEmailAccount(this);
        query.clear();
        listView = (ListView) findViewById(R.id.list_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final String[]  opciones = { "Eventos", "Filtros", "Publicar", "Perfil" };

        MenuAdapter adapter = adapter = new MenuAdapter(MainActivity.this, opciones);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = null;

                if(!opciones[arg2].equals("Eventos")) {
                    if (opciones[arg2].equals("Filtros"))
                        intent = new Intent(MainActivity.this, Filtros.class);
                    else if (opciones[arg2].equals("Publicar"))
                        intent = new Intent(MainActivity.this, CrearEvento.class);
                    else
                        intent = new Intent(MainActivity.this, EditarPerfil.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                drawerLayout.closeDrawers();
            }
        });

        // Mostramos el botón en la barra de la aplicación
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu48);



        final Button botontodos = (Button) findViewById(R.id.buttontodos);
        final Button botonmios = (Button) findViewById(R.id.buttonmios);
        final Button botonasisto = (Button) findViewById(R.id.buttonasisto);
        eventos = (ListView)findViewById(R.id.listviewev);

        //Definimos los estilos guardandolos primero
        estiloselec = botontodos.getBackground();
        estilonoselec = botonasisto.getBackground();

        /**
         * Mostrar todos los eventos al iniciar esta activity
         */
        botontodos.setBackground(estiloselec);
        botonmios.setBackground(estilonoselec);
        botonasisto.setBackground(estilonoselec);



        //GetEventos getEventos = new GetEventos();
        //getEventos.execute();

        if(this.getIntent().getExtras()!=null)
        {

            ArrayList filtros = new ArrayList();
            int tam = this.getIntent().getExtras().size()/3;
            System.out.println("HAY " + tam + " FILTROS PA APLICAR");
            for(int i=0;i<tam;i++) {
                Filter filter = new Filter();
                filter.setField(this.getIntent().getExtras().getString("field" + i));

                //CUIDAO OJO !!!!!!!!!!!!!!!!!!!!!!!!!!
                filter.setOperator(this.getIntent().getExtras().getString("operator" + i));
                filter.setValue(this.getIntent().getExtras().getString("value" + i));
                filtros.add(filter);
            }
            query.setFilters(filtros);
        }

        eventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView <?> parent, View view, int position,
                                    long id) {
                com.appspot.ad_meet.conference.model.Conference evento;
                evento = listaeventos.get(position);
                Long idevento = evento.getId();
                Intent intent = new Intent(MainActivity.this, Evento.class);
                intent.putExtra("name", evento.getName());
                intent.putExtra("city", evento.getCity());
                intent.putExtra("description", evento.getDescription());
                //intent.putExtra("fecha", evento.getStartDate());
                String t = evento.getStartDate().toString();
                String[] trozos = t.split("T");
                intent.putExtra("fecha",trozos[0]);
                intent.putExtra("asientos", evento.getSeatsAvailable()+"/"+evento.getMaxAttendees());
                String categorias="";
                for(int i=0;i<evento.getTopics().size();i++)
                    categorias+=" " + evento.getTopics().get(i);
                intent.putExtra("categorias", categorias);
                intent.putExtra("websafeKey",evento.getWebsafeKey());
                intent.putExtra("eventoid",evento.getId());
                intent.putExtra("creador", evento.getOrganizerDisplayName());
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        //Accion del boton mios
        botonmios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botontodos.setBackground(estilonoselec);
                botonmios.setBackground(estiloselec);
                botonasisto.setBackground(estilonoselec);

                GetEventosMios getEventosMios = new GetEventosMios();
                getEventosMios.execute();
            }
        });

        //Accion del boton asisto
        botonasisto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botontodos.setBackground(estilonoselec);
                botonmios.setBackground(estilonoselec);
                botonasisto.setBackground(estiloselec);

                GetEventosAtendidos getEventos = new GetEventosAtendidos();
                getEventos.execute();
            }
        });

        //Accion del boton todos
        botontodos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                botontodos.setBackground(estiloselec);
                botonmios.setBackground(estilonoselec);
                botonasisto.setBackground(estilonoselec);

                GetEventos getEventos = new GetEventos();
                getEventos.execute();
            }
        });


    }

    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Cerrando AdMeet")
                .setMessage("¿Estás seguro de que quieres cerrar la aplicación?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }

                })
                .setNegativeButton("No", null)
                .create().show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAuthTask != null) {
            mAuthTask.cancel(true);
            mAuthTask = null;
        }
    }

    protected void onResume() {
        super.onResume();

        if (null != mEmailAccount) {
            performAuthCheck(mEmailAccount);
        } else {
            selectAccount();
        }
    }
    private void selectAccount() {
        Account[] accounts = Utils.getGoogleAccounts(this);
        int numOfAccount = accounts.length;
        switch (numOfAccount) {
            case 0:
                // No accounts registered, nothing to do.
                Toast.makeText(this, R.string.toast_no_google_accounts_registered,
                        Toast.LENGTH_LONG).show();
                break;
            case 1:
                mEmailAccount = accounts[0].name;
                performAuthCheck(mEmailAccount);
                break;
            default:
                // More than one Google Account is present, a chooser is necessary.
                // Invoke an {@code Intent} to allow the user to select a Google account.
                Intent accountSelector = AccountPicker.newChooseAccountIntent(null, null,
                        new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE}, false,
                        getString(R.string.select_account_for_access), null, null, null);
                startActivityForResult(accountSelector, ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION);
        }
    }

    private void performAuthCheck(String email) {
        // Cancel previously running tasks.
        if (mAuthTask != null) {
            mAuthTask.cancel(true);
        }

        // Start task to check authorization.
        mAuthTask = new AuthorizationCheckTask();
        mAuthTask.execute(email);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ACTIVITY_RESULT_FROM_ACCOUNT_SELECTION && resultCode == RESULT_OK) {
            // This path indicates the account selection activity resulted in the user selecting a
            // Google account and clicking OK.
            mEmailAccount = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
        } else {
            finish();
        }
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

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

    private class AuthorizationCheckTask extends AsyncTask<String, Integer, Boolean> {

        private final static boolean SUCCESS = true;
        private final static boolean FAILURE = false;
        private Exception mException;

        @Override
        protected Boolean doInBackground(String... emailAccounts) {
            Log.i(LOG_TAG, "LOGIN TASK STARTED.");

            if (!Utils.checkGooglePlayServicesAvailable(MainActivity.this)) {
                publishProgress(R.string.gms_not_available);
                return FAILURE;
            }

            String emailAccount = emailAccounts[0];
            // Ensure only one task is running at a time.
            mAuthTask = this;

            // Ensure an email was selected.
            if (TextUtils.isEmpty(emailAccount)) {
                publishProgress(R.string.toast_no_google_account_selected);
                return FAILURE;
            }

            mEmailAccount = emailAccount;
            Utils.saveEmailAccount(MainActivity.this, emailAccount);

            return SUCCESS;
        }

        @Override
        protected void onProgressUpdate(Integer... stringIds) {
            // Toast only the most recent.
            Integer stringId = stringIds[0];
            Toast.makeText(MainActivity.this, getString(stringId), Toast.LENGTH_SHORT).show();
        }

        @Override
        protected void onPreExecute() {
            mAuthTask = this;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                // Authorization check successful, get conferences.
                ConferenceUtils.build(MainActivity.this, mEmailAccount);
                //getConferencesForList();
            } else {
                // Authorization check unsuccessful.
                mEmailAccount = null;
                if (mException != null) {
                    Utils.displayNetworkErrorMessage(MainActivity.this);
                }
            }
            mAuthTask = null;

            GetEventos getEventos = new GetEventos();
            getEventos.execute();
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    /**
     * Ver todos los eventos
     */
    private class GetEventos extends AsyncTask<Void, Void, ConferenceCollection>
    {
        public GetEventos() { }

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            //Display success message to user
            if(query.getFilters()!=null) {
                Toast.makeText(getBaseContext(), "Se han aplicado correctamente los filtros.",
                        Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected ConferenceCollection doInBackground(Void ... unused)
        {
            ConferenceCollection messages = null;
            try
            {
                Conference.QueryConferences create = ConferenceUtils.getEventos(query);
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
            int tam=0;

            if(result!=null)
                listaeventos = result.getItems();
            if(listaeventos!=null)
                tam = listaeventos.size();

            if(listaeventos!=null)
                rellenaListView(listaeventos, tam);
            query.clear();
        }
    }

    /**
     * Para obtener los eventos del usuario
     */
    private class GetEventosMios extends AsyncTask<Void, Void, ConferenceCollection>
    {
        public GetEventosMios() { }

        @Override
        protected ConferenceCollection doInBackground(Void ... unused)
        {
            ConferenceCollection messages = null;
            try
            {
                Conference.GetConferencesCreated create = ConferenceUtils.getEventosMios();
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
            int tam=0;
            if(result!=null)
                listaeventos = result.getItems();
            if(listaeventos!=null)
                tam = listaeventos.size();

            rellenaListView(listaeventos, tam);

        }
    }

    /**
     * Para obtener los eventos que el usuario a atendido
     */
    private class GetEventosAtendidos extends AsyncTask<Void, Void, ConferenceCollection>
    {
        public GetEventosAtendidos() { }

        @Override
        protected ConferenceCollection doInBackground(Void ... unused)
        {
            ConferenceCollection messages = null;
            try
            {
                ConferenceQueryForm conferenceQueryForm = new ConferenceQueryForm();
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
            int tam=0;

            if(result!=null)
                listaeventos = result.getItems();
            if(listaeventos!=null)
                tam = listaeventos.size();

                rellenaListView(listaeventos, tam);
        }
    }

    public void rellenaListView(List<com.appspot.ad_meet.conference.model.Conference> listaeventos, int tam)  {

        if(listaeventos==null)
        {
            eventos.setAdapter(null);
            return;
        }
        //SE SUPONE QUE CON ESTO YA ESTA ORDENADO
        Collections.sort(listaeventos, new Comparator<com.appspot.ad_meet.conference.model.Conference>() {
            public int compare(com.appspot.ad_meet.conference.model.Conference c1, com.appspot.ad_meet.conference.model.Conference c2) {
                return new Long(c2.getStartDate().getValue()).compareTo(new Long(c1.getStartDate().getValue()));
            }
        });

        String nombres[] = new String[tam];
        String categorias[] = new String[tam];
        String fecha[] = new String[tam];
        Integer asis[] = new Integer[tam];
        Integer maxasis[] = new Integer[tam];

        for(int i=0;i<tam;i++) {
            nombres[i] = listaeventos.get(i).getName();
            categorias[i] = listaeventos.get(i).getTopics().get(0);
            asis[i] = (listaeventos.get(i).getMaxAttendees()-listaeventos.get(i).getSeatsAvailable());
            maxasis[i] = listaeventos.get(i).getMaxAttendees();
            fecha[i] = listaeventos.get(i).getStartDate().toString().split("T")[0];
        }

        ImageView imagen = (ImageView) findViewById(R.id.imgAnimal);
        TextView nombre = (TextView) findViewById(R.id.tvContent);
        TextView numCelda = (TextView) findViewById(R.id.textviewNombre);
        EventosAdapter adapter;
        // Inicializamos el adapter.
        adapter = new EventosAdapter(MainActivity.this, nombres, categorias, asis, maxasis, fecha);
        // Asignamos el Adapter al ListView, en este punto hacemos que el
        // ListView muestre los datos que queremos.
        eventos.setAdapter(adapter);
    }




}