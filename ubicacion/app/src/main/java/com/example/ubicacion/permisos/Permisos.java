package com.example.ubicacion.permisos;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

// para no tenerlo que crear varias veces, a fin de cuentas como que todos usan el mismo
public class Permisos {
        /**
         * Metodo para solicitar un permiso
         * @param context actividad actual
         * @param permission permiso que se desea solicitar
         * @param just justificacion para el permiso
         * @param id identificador con el se marca la solicitud y se captura el callback de respuesta
         */
        public static void requestPermission(Activity context, String permission, String just, int id) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(context, permission)) {
                    // Show an expanation to the user *asynchronously*   
                    Toast.makeText(context, just, Toast.LENGTH_LONG).show();
                }
                // request the permission.   
                ActivityCompat.requestPermissions(context, new String[]{permission}, id);

            }
        }
}
