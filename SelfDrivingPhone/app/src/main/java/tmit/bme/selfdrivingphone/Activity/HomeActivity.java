package tmit.bme.selfdrivingphone.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import tmit.bme.selfdrivingphone.R;
import tmit.bme.selfdrivingphone.Utilities.Permission;

public class HomeActivity extends Activity {

    private Button startCamera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        startCamera = findViewById(R.id.start_camera_button);
        startCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CameraActivity.class));
            }
        });

        Permission.checkAllPermissions(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
