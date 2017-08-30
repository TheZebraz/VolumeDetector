package by.mastihin.volumedetector.activities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import by.mastihin.volumedetector.R;
import by.mastihin.volumedetector.utils.MessageBox;


public class LaunchActivity extends AppCompatActivity {

    private final int PERMISSION_REQUEST_CODE = 1;
    private final int SETTINGS_REQUEST_CODE = 2;

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && needPermissions()) {
            requestPermission();
        } else {
            continueExecution();
        }
    }

    private boolean needPermissions() {
        for (String permission : PERMISSIONS) {
            if (checkSelfPermission(permission) == PackageManager.PERMISSION_DENIED) {
                return true;
            }
        }
        return false;
    }

    private void requestPermission() {
        if (needPermissions()) {
            if (shouldShowRationaleAllPermissions()) {
                final String message = getString(R.string.exception_no_permissions);
                MessageBox.build(this, message, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPermissions();
                    }
                }).show();
            } else {
                getPermissions();
            }
        }
        else {
            continueExecution();
        }
    }

    private boolean shouldShowRationaleAllPermissions() {
        boolean shouldShowRationale = false;
        for (String permission : PERMISSIONS) {
            shouldShowRationale = shouldShowRequestPermissionRationale(permission) || shouldShowRationale;
        }
        return shouldShowRationale;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_CODE && grantResults.length == PERMISSIONS.length) {
            boolean allGranted = true;
            for (int gr : grantResults) {
                allGranted = allGranted && (gr == PackageManager.PERMISSION_GRANTED);
            }
            if(allGranted){
                continueExecution();
            }
            else {
                boolean showRationale = shouldShowRationaleAllPermissions();
                if (showRationale) {
                    requestPermission();
                } else {
                    final String message = getString(R.string.all_permissions_rationale);
                    MessageBox.build(this, message, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent();
                            intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getPackageName(), null);
                            intent.setData(uri);
                            startActivityForResult(intent, SETTINGS_REQUEST_CODE);
                        }
                    }).show();
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == SETTINGS_REQUEST_CODE){
            requestPermission();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getPermissions() {
        this.requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    private void continueExecution() {
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
        finish();
    }
}
