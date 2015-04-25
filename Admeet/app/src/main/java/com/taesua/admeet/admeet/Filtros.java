package com.taesua.admeet.admeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);

        Button evento = (Button) findViewById(R.id.buttonAnuncios);
        Button perfil = (Button) findViewById(R.id.buttonPerfil);
        Button publicar = (Button) findViewById(R.id.buttonPublicar);
  //      perfil.setBackground(getDrawable(R.drawable.sinbordeazulseleccionado));
    //    evento.setBackground(getDrawable(R.drawable.bordeazulseleccionado));
      //  publicar.setBackground(getDrawable(R.drawable.sinbordeazulseleccionado));

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
                    Intent intent = new Intent(Filtros.this,Eventos.class);

                    for(int i=0;i<fields.size();i++) {
                        intent.putExtra("field" + i,fields.get(i));
                        intent.putExtra("operator" + i,operators.get(i));
                        intent.putExtra("value" + i,values.get(i));
                    }
                    startActivity(intent);
                }
                else
                    startActivity(new Intent(Filtros.this,Eventos.class));
            }
        });

        acabar.setVisibility(View.INVISIBLE);



        findViewById(R.id.volver_sin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Filtros.this,Eventos.class));
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


        //PARA IR A EVENTOS
        findViewById(R.id.buttonAnuncios).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ji = new Intent(Filtros.this, Eventos.class);
                startActivity(ji);
            }
        });

        //PARA IR A PERFIL
        findViewById(R.id.buttonPerfil).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ji = new Intent(Filtros.this, Perfil.class);
                startActivity(ji);
            }
        });

        //PARA IR A PUBLICAR
        findViewById(R.id.buttonPublicar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent ji = new Intent(Filtros.this, CrearEvento.class);
                startActivity(ji);
            }
        });


    }
}
