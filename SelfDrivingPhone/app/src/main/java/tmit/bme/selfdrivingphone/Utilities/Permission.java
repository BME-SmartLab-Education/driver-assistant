package tmit.bme.selfdrivingphone.Utilities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by atig on 10/3/17.
 */

public class Permission {

    public static void checkAllPermissions(Activity context) {
        boolean isCameraGranted = true;
        boolean isStorageGranted = true;

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
            isCameraGranted = false;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            isStorageGranted = false;

        if (!isCameraGranted && !isStorageGranted)
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.CAMERA}, 1);
        else {
            if (!isCameraGranted)
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.CAMERA}, 1);
            else
                ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }
}
