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
      
      //Cargamos el menú desde el fichero de recursos indicado por su identificador en la variable R
      MenuInflater menuInflater = getMenuInflater();
      menuInflater.inflate(R.menu.main_menu, menu);
      
      //Capturamos el item "Ayuda" del menú, para asociarle una intención, 
      //en este caso la itención que está asociada a la actividad HelpActivity
      MenuItem helpMenuItem = menu.findItem(R.id.help);
      helpMenuItem.setIntent(new Intent("com.example.android.intent.action.HELP"));
      
      return result;
    }
    
    @Override
    public boolean onMenuItemSelected(int featureId, MenuItem item) {
      boolean result = super.onMenuItemSelected(featureId, item);
      
      //Buscamos que opción de menú ha sido seleccionada en base a su identificador
      switch (item.getItemId()) {
        case R.id.add:
          //Con getString() nos "traemos" un recurso de tipo String con el identificador que se le pasa por parámetros
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
          //Se lanzará la intención asociada a la opción del menú
          result = false; 
          break;
      }
      
      return result;
    }
    
    private void showToast() {
      //Muestra un Toast de larga duración, el Toast es independiente a la vida de la actividad
      //una vez se muestra.
      Toast toast = Toast.makeText(this, R.string.remove_text, Toast.LENGTH_LONG);
      toast.show();
    }
    
    private void showNotification() {
      //Mostramos una notificación, primero accedemos al servicio de notificaciones
      NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE); 
      
      //Obtenemos los recursos que usaremos en la notificación
      int icon = android.R.drawable.stat_notify_voicemail; 
      String tickerText = getText(R.string.ticker_text).toString();
      String contentTitle = getText(R.string.content_title).toString(); 
      String contentText = getText(R.string.content_description).toString();
      
      //Creamos la intención que asociaremos a la notificación, una vez creada la notificación
      //el usuario puede pulsarla, lanzandose la intención
      PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MenuActivity.class), 0);
      
      //Creamos la notificación
      Notification notification = new Notification(icon, tickerText, System.currentTimeMillis());
      //Establecemos el nuevo estado para la notificación (podemos reaprovechar este objeto para lanzar
      //varias veces la misma notificación o actualizar una ya lanzada
      notification.setLatestEventInfo(this, contentTitle, contentText, contentIntent);
      //Mostramos la notificación, le damos un identificador (en este caso 1), que nos servirá para identificarla
      //para por ejemplo, cerrarla.
      manager.notify(1, notification);
    }
    
    @Override
    protected void onStart() {
      super.onStart();
      
      //Cancelamos la notificación con id=1, esto servirá para cuando Android nos abre porque el usuario
      //pulsó sobre la notificación creada con la opción de menú "Añadir"
      NotificationManager manager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
      manager.cancel(1);
    }
}