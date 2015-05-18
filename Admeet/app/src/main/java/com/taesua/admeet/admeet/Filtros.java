package com.taesua.admeet.admeet;

import android.app.Activity;
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

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        activity = this.activity;

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
        final Spinner spinnerCategoria = (Spinner) findViewById(R.id.categoria);
        final TextView smalltext = (TextView) findViewById(R.id.textViewValue);
        final TextView filtrosStack = (TextView) findViewById(R.id.textFiltros);
        final EditText value = (EditText) findViewById(R.id.textoValue);
        final TextView titulovalue = (TextView) findViewById(R.id.textViewValue);
        final Button aplicar = (Button) findViewById(R.id.buttonFilter);

        //Rellenando categorias disponibles en sipnner
        final List<String> listcat = new ArrayList<String>();
        listcat.add("Cultura");
        listcat.add("Deportes");
        listcat.add("Fiesta");
        listcat.add("Otros");

        ArrayAdapter<String> dataAdaptercat = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listcat);
        dataAdaptercat.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(dataAdaptercat);

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

                if(spinnerField.getItemAtPosition(spinnerField.getSelectedItemPosition()).toString().equals("Categoría"))
                {
                    spinnerCategoria.setVisibility(View.VISIBLE);
                    titulovalue.setVisibility(View.INVISIBLE);
                    value.setVisibility(View.INVISIBLE);
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
        listfield.add("Ciudad");
        listfield.add("Categoría");
        listfield.add("Mes");
        listfield.add("Máximo nº de asistentes");
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
                        String filtro = "";
                        if(spinnerField.getItemAtPosition(i).toString().equals("Categoría"))
                            filtro = "TOPIC";
                        else if(spinnerField.getItemAtPosition(i).toString().equals("Ciudad"))
                            filtro = "CITY";
                        else if(spinnerField.getItemAtPosition(i).toString().equals("Máximo nº de asistentes"))
                            filtro = "MAX_ATTENDES";
                        else if(spinnerField.getItemAtPosition(i).toString().equals("Mes"))
                            filtro = "MONTH";
                        intent.putExtra("field" + i,filtro);
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
                if (value.getText().toString().equals("") &&
                        !(spinnerField.getItemAtPosition(spinnerField.getSelectedItemPosition()).toString().equals("Categoría"))) //si esta vacio, validar
                {
                    value.setError("¡No se puede dejar vacío el campo de valor!");
                }
                else
                {
                    String filtro = "";
                    if(spinnerField.getSelectedItem().toString().equals("Categoría"))
                        filtro = "TOPIC";
                    else if(spinnerField.getSelectedItem().toString().equals("Ciudad"))
                        filtro = "CITY";
                    else if(spinnerField.getSelectedItem().toString().equals("Máximo nº de asistentes"))
                        filtro = "MAX_ATTENDES";
                    else if(spinnerField.getSelectedItem().toString().equals("Mes"))
                        filtro = "MONTH";
                    fields.add(filtro);
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

                    if(spinnerField.getSelectedItem().toString().equals("Categoría"))
                        filtro = "TOPIC";
                    else if(spinnerField.getSelectedItem().toString().equals("Ciudad"))
                        filtro = "CITY";
                    else if(spinnerField.getSelectedItem().toString().equals("Máximo nº de asistentes"))
                        filtro = "MAX_ATTENDES";
                    else if(spinnerField.getSelectedItem().toString().equals("Mes"))
                        filtro = "MONTH";



                    if(spinnerField.getItemAtPosition(spinnerField.getSelectedItemPosition()).toString().equals("Categoría"))
                    {
                        String category = spinnerCategoria.getItemAtPosition(spinnerCategoria.getSelectedItemPosition()).toString();
                        values.add(category);
                        filtrosStack.append("{" + filtro +
                                " " + spinnerOperador.getSelectedItem().toString() +
                                " " + category + "}\n");
                    }
                    else
                    {
                        values.add(value.getText().toString());
                        filtrosStack.append("{" + filtro +
                                " " + spinnerOperador.getSelectedItem().toString() +
                                " " + value.getText().toString() + "}\n");
                    }



                    spinnerField.setSelection(0);
                    spinnerOperador.setSelection(0);
                    value.getText().clear();
                    acabar.setVisibility(View.VISIBLE);


                   // InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(activity.INPUT_METHOD_SERVICE);
                   // inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
                    //Apariencia a como estaba
                    spinnerCategoria.setVisibility(View.INVISIBLE);
                    titulovalue.setVisibility(View.VISIBLE);
                    value.setVisibility(View.VISIBLE);
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
