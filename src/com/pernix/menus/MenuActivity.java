package com.pernix.menus;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

public class MenuActivity extends Activity {
    private static final String MI_APP = "MI_APP";

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      boolean result = super.onCreateOptionsMenu(menu);
      
      //Cargamos el men� desde el fichero de recursos indicado por su identificador en la variable R
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.main_menu, menu);
      
      //Capturamos el item "Ayuda" del men�, para asociarle una intenci�n, 
      //en este caso la itenci�n que est� asociada a la actividad HelpActivity
      MenuItem helpMenuItem = menu.findItem(R.id.help);
      helpMenuItem.setIntent(new Intent("com.example.android.intent.action.HELP"));
      
      return result;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
      boolean result = super.onMenuItemSelected(featureId, item);
      
      //Buscamos que opci�n de men� ha sido seleccionada en base a su identificador
      switch (item.getItemId()) {
        case R.id.add:
          //Con getString() nos "traemos" un recurso de tipo String con el identificador que se le pasa por par�metros
          Log.d(MI_APP, getString(R.string.menu_add_log_text));
          showNotification();
          result = true;
          break;
        case R.id.remove:
          Log.d(MI_APP, getString(R.string.menu_remove_log_text));
          showToast();          
          result = true;
          break;
        case R.id.help:
          Log.d(MI_APP, getString(R.string.menu_help_log_text));
          //Devolveremos falso para indicarle a Android que no hemos tratado este evento
          //Se lanzar� la intenci�n asociada a la opci�n del men�
          result = false; 
          break;
      }
      
      return result;
    }
    
    private void showToast() {
      //Muestra un Toast de larga duraci�n, el Toast es independiente a la vida de la actividad
      //una vez se muestra.
      Toast toast = Toast.makeText(this, R.string.remove_text, Toast.LENGTH_LONG);
      toast.show();
    }
    
    private void showNotification() {
      //Mostramos una notificaci�n, primero accedemos al servicio de notificaciones
      NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE); 
      
      //Obtenemos los recursos que usaremos en la notificaci�n
      int icon = android.R.drawable.stat_notify_voicemail; 
      String tickerText = getText(R.string.ticker_text).toString();
      String contentTitle = getText(R.string.content_title).toString(); 
      String contentText = getText(R.string.content_description).toString();
      
      //Creamos la intenci�n que asociaremos a la notificaci�n, una vez creada la notificaci�n
      //el usuario puede pulsarla, lanzandose la intenci�n
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MenuActivity.class), 0);
      
      //Creamos la notificaci�n
      Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
      //Establecemos el nuevo estado para la notificaci�n (podemos reaprovechar este objeto para lanzar
      //varias veces la misma notificaci�n o actualizar una ya lanzada
      notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
      //Mostramos la notificaci�n, le damos un identificador (en este caso 1), que nos servir� para identificarla
      //para por ejemplo, cerrarla.
      manager.notify(1, notification);
    }
    
    @Override
    protected void onStart() {
      super.onStart();
      
      //Cancelamos la notificaci�n con id=1, esto servir� para cuando Android nos abre porque el usuario
      //puls� sobre la notificaci�n creada con la opci�n de men� "A�adir"
      NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
      manager.cancel(1);
    }
}