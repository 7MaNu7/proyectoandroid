package com.taesua.admeet.admeet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtros);


        final Spinner spinnerField = (Spinner) findViewById(R.id.field);
        final Spinner spinnerOperador = (Spinner) findViewById(R.id.operator);
        final TextView smalltext = (TextView) findViewById(R.id.textView2);
        final EditText value = (EditText) findViewById(R.id.textoValue);
        final Button aplicar = (Button) findViewById(R.id.buttonFilter);


        spinnerOperador.setVisibility(View.INVISIBLE);
        smalltext.setVisibility(View.INVISIBLE);
        value.setVisibility(View.INVISIBLE);
        aplicar.setVisibility(View.INVISIBLE);


        //final Spinner spinnerValue = (Spinner) findViewById(R.id.value);
        final List<String> listfield = new ArrayList<String>();
        listfield.add("Select field");
        listfield.add("CITY");
        listfield.add("TOPIC");
        listfield.add("MONTH");
        listfield.add("MAX_ATTENDEES");
        ArrayAdapter<String> dataAdapterfield = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listfield);
        dataAdapterfield.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerField.setAdapter(dataAdapterfield);

        List<String> listoperator = new ArrayList<String>();
        listoperator.add("Select operator");
        listoperator.add("EQ");
        listoperator.add("LT");
        listoperator.add("GT");
        listoperator.add("LTEQ");
        listoperator.add("GTEQ");
        listoperator.add("NE");
        ArrayAdapter<String> dataAdapterOp = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, listoperator);
        dataAdapterOp.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOperador.setAdapter(dataAdapterOp);






        findViewById(R.id.volver_sin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Filtros.this,Eventos.class));
            }
        });


        spinnerField.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(!parent.getItemAtPosition(position).toString().equals("Select field")) {

                    spinnerOperador.setVisibility(View.VISIBLE);
                    System.out.println(parent.getItemAtPosition(position).toString());

                    spinnerOperador.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            if(!parent.getItemAtPosition(position).toString().equals("Select operator")) {
                                smalltext.setVisibility(View.VISIBLE);
                                value.setVisibility(View.VISIBLE);
                                aplicar.setVisibility(View.VISIBLE);

                                aplicar.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Filtros.this,Eventos.class);


                                        //intent.putExtra("field",listfield.);
                                        intent.putExtra("field",spinnerField.getSelectedItem().toString());
                                        intent.putExtra("operator",spinnerOperador.getSelectedItem().toString());
                                        intent.putExtra("value",value.getText().toString());

                                        startActivity(intent);
                                    }
                                });

                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("HAY QUE SELECCIONAR ALGO");
            }
        });

        //final Button botontodos = (Button) findViewById(R.id.buttontodos);
        //final Button botonmios = (Button) findViewById(R.id.buttonmios);
        //final Button botonasisto = (Button) findViewById(R.id.buttonasisto);
        //eventos = (ListView)findViewById(R.id.listviewev);
    }
}
