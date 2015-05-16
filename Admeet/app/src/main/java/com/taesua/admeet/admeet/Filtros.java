package com.taesua.admeet.admeet;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hector on 12/04/2015.
 */
public class Filtros extends ActionBarActivity {

    private ArrayList<String> fields = new ArrayList<String>();
    private ArrayList<String> operators = new ArrayList<String>();
    private ArrayList<String> values = new ArrayList<String>();
    private Button acabar;
    private DrawerLayout drawerLayout = null;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);


        listView = (ListView) findViewById(R.id.list_view);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final String[]  opciones = { "Eventos", "Filtros", "Publicar", "Perfil" };

        MenuAdapter adapter = adapter = new MenuAdapter(Filtros.this, opciones);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView arg0, View arg1, int arg2,
                                    long arg3) {
                Intent intent = null;


                if(!opciones[arg2].equals("Filtros")) {
                    if (opciones[arg2].equals("Perfil"))
                        intent = new Intent(Filtros.this, EditarPerfil.class);
                    else if (opciones[arg2].equals("Eventos"))
                        intent = new Intent(Filtros.this, MainActivity.class);
                    else
                        intent = new Intent(Filtros.this, CrearEvento.class);
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




        final Spinner spinnerField = (Spinner) findViewById(R.id.field);
        final Spinner spinnerOperador = (Spinner) findViewById(R.id.operator);
        final TextView smalltext = (TextView) findViewById(R.id.textView2);
        final TextView filtrosStack = (TextView) findViewById(R.id.textFiltros);
        final EditText value = (EditText) findViewById(R.id.textoValue);
        final Button aplicar = (Button) findViewById(R.id.buttonFilter);

        spinnerField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                if(spinnerField.getSelectedItem().toString().equals("MONTH") ||
                        spinnerField.getSelectedItem().toString().equals("MAX_ATTENDEES"))
                {
                    value.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                else
                {
                    value.setInputType(InputType.TYPE_CLASS_TEXT);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filtrosStack.setText("Has creado los siguientes filtros:\n");

        //final Spinner spinnerValue = (Spinner) findViewById(R.id.value);
        final List<String> listfield = new ArrayList<String>();
        //listfield.add("Select field");
        listfield.add("CITY");
        listfield.add("TOPIC");
        listfield.add("MONTH");
        listfield.add("MAX_ATTENDEES");
        ArrayAdapter<String> dataAdapterfield = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listfield);
        dataAdapterfield.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerField.setAdapter(dataAdapterfield);

        List<String> listoperator = new ArrayList<String>();
        //listoperator.add("Select operator");
        listoperator.add("="); //EQ
        listoperator.add("<"); //LT
        listoperator.add(">"); //GT
        listoperator.add("<="); //LTEQ
        listoperator.add(">="); //GTEQ
        listoperator.add("!="); //NE
        ArrayAdapter<String> dataAdapterOp = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listoperator);
        dataAdapterOp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperador.setAdapter(dataAdapterOp);


        acabar = (Button) findViewById(R.id.buttonAcabarFiltros);
        acabar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(fields.size()>0)
                {
                    Intent intent = new Intent(Filtros.this,MainActivity.class);

                    for(int i=0;i<fields.size();i++) {
                        intent.putExtra("field" + i,fields.get(i));
                        intent.putExtra("operator" + i,operators.get(i));
                        intent.putExtra("value" + i,values.get(i));
                    }
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                else {
                    Intent intent = new Intent(Filtros.this,MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        });

        acabar.setVisibility(View.INVISIBLE);

        findViewById(R.id.volver_sin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Filtros.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });

        aplicar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (value.getText().toString().equals("")) //si esta vacio, validar
                {
                    value.setError("¡No se puede dejar vacío el campo de valor!");
                }
                else
                {
                    fields.add(spinnerField.getSelectedItem().toString());
                    String s = spinnerOperador.getSelectedItem().toString();
                    String res = "";

                    switch (s) {
                        case "=":
                            res = "EQ";
                            break;
                        case "<":
                            res = "LT";
                            break;
                        case ">":
                            res = "GT";
                            break;
                        case "<=":
                            res = "LTEQ";
                            break;
                        case ">=":
                            res = "GTEQ";
                            break;
                        case "!=":
                            res = "NE";
                            break;
                    }

                    operators.add(res);

                    values.add(value.getText().toString());

                    filtrosStack.append("{" + spinnerField.getSelectedItem().toString() +
                            " " + spinnerOperador.getSelectedItem().toString() +
                            " " + value.getText().toString() + "}\n");

                    spinnerField.setSelection(0);
                    spinnerOperador.setSelection(0);
                    value.getText().clear();
                    acabar.setVisibility(View.VISIBLE);
                }
            }
        });


        //para quitar el focus automatico al abrir la actividad
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    public void onBackPressed() {
        Intent intent = new Intent(Filtros.this,MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
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
