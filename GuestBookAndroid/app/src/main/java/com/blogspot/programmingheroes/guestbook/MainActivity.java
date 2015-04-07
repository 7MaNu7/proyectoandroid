

package com.blogspot.programmingheroes.guestbook;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.appspot.pruebas_gae.gbe.Gbe;
import com.appspot.pruebas_gae.gbe.Gbe.Create;
import com.appspot.pruebas_gae.gbe.Gbe.Get;
import com.appspot.pruebas_gae.gbe.model.GuestMessage;
import com.appspot.pruebas_gae.gbe.model.GuestMessageCollection;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.gson.GsonFactory;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;


/**
 * Si aparece en el LogCat algo como:
 * 
 * 07-28 03:25:39.011: W/System.err(575):
 * com.google.api.client.googleapis.json.GoogleJsonResponseException:
 * 404 Not Found
 * 
 * debes de asegurarte que la versión en la cual has hecho el deploy de tu
 * endpoint sea la versión default. Otra opción sería cambiar la constante
 * DEFAULT_ROOT_URL de la clase Gbe por la URL con protocolo https e indicando
 * la versión. Ej: https://guestbook-dot-pruebas-gae.appspot.com/_ah/api/
 * 
 * 
 * 
 * La aplicación se encuentra tan comprimida como he podido conseguir.
 * He borrado muchos jars de la carpeta libs.
 * google-api-client-1.18.0-rc.jar
 * google-api-client-android-1.18.0-rc.jar
 * google-http-client-1.18.0-rc.jar
 * google-http-client-android-1.18.0-rc.jar
 * google-http-client-gson-1.18.0-rc.jar
 * gson-2.1.jar
 * 
 * google-oauth-client-1.18.0-rc.jar se queda fuera porque no hace falta hacer
 * llamadas 'oautorizadas'
 * 
 * @author ProgrammingHeroes
 *
 */
public class MainActivity extends Activity implements OnClickListener
{

	private Context context;
	
	private Gbe guestBookEndpoint;
	
	private ListView listView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		this.context = this;
		
		listView = (ListView) findViewById(R.id.list_view);
		
		/* Array adapter de strings, hecho para probar.
		String[] items = {"hola", "adios"};
		 
		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					this, android.R.layout.simple_list_item_1, items);
		
		listView.setAdapter(arrayAdapter);*/
		
		Gbe.Builder builder = new Gbe.Builder(
				AndroidHttp.newCompatibleTransport(), new GsonFactory(), null);
		// No sé que es de application name...
		//builder.setApplicationName("gbe");
		guestBookEndpoint = builder.build();
		
		Button button = (Button) findViewById(R.id.button);
		button.setOnClickListener(this);
		
		GetMessage getMessage = new GetMessage();
		getMessage.execute();
	}
	
	private class CreateMessage extends AsyncTask<Void, Void, GuestMessage>
    {

        private String message;
        
        public CreateMessage(String message)
        {
            this.message = message;
        }
        
        @Override
        protected GuestMessage doInBackground(Void ... unused)
        {
        	GuestMessage guestMessage = null;
            
            try
            {
                Create create = guestBookEndpoint.create(message);
                guestMessage = create.execute();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            return guestMessage;
        }

        @Override
        protected void onPostExecute(GuestMessage result)
        {
            if (result == null)
            {
                Log.v("Resultados de create", "No creado, result == null");
                return;
            }
            
            Log.v("Propiedades del mensaje", "Date: " + result.getDate());
            Log.v("Propiedades del mensaje", "Message: " + result.getMessage());
            Log.v("Propiedades del mensaje", "Id: " + result.getId());
            
            if (listView.getAdapter() != null)
            {
            	MessageAdapter baseAdapter = (MessageAdapter) listView.getAdapter();
            	baseAdapter.add(result);
            }
            else
            {
            	ArrayList<GuestMessage> messages = new ArrayList<GuestMessage>();
            	messages.add(result);
            	MessageAdapter messageAdapter = new MessageAdapter(context,
            			messages, guestBookEndpoint);
            	listView.setAdapter(messageAdapter);
			}
        }
    }
	
	private class GetMessage extends AsyncTask<Void, Void, GuestMessageCollection>
    {

        public GetMessage()
        {
        }
        
        @Override
        protected GuestMessageCollection doInBackground(Void ... unused)
        {
        	GuestMessageCollection messages = null;
            
            try
            {
                Get create = guestBookEndpoint.get();
                messages = create.execute();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            
            return messages;
        }

		@Override
        protected void onPostExecute(GuestMessageCollection result)
        {
            if (result == null)
            {
                Log.v("Resultados de create", "No creado, result == null");
                return;
            }
            
            List<GuestMessage> list = result.getItems();
            
            if (list == null)
            {
            	Log.v("Resultados", "No hay mensajes");
            	return;
            }
            
            for (int i = 0; i < list.size(); i++)
            {
            	Log.v("Propiedades", "Date: " + list.get(i).getDate());
            	Log.v("Propiedades", "Message: " + list.get(i).getMessage());
            	Log.v("Propiedades", "Id: " + list.get(i).getId());
            	Log.v("Propiedades", "-------------");
            }
            
            MessageAdapter messageAdapter = new MessageAdapter(
            		context, list, guestBookEndpoint);
            listView.setAdapter(messageAdapter);
            listView.setSelection(list.size() - 1);
        }
    }
	
	@Override
	public void onClick(View v)
	{
		EditText textView = (EditText) findViewById(R.id.edit_text);		
		Log.v("ja", textView.getText().toString());
		CreateMessage createMessage = new CreateMessage(
				textView.getText().toString());
		createMessage.execute();		
	}

}
