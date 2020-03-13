package ankit.com.timetable.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class NetworkUtil {
    public static boolean isNetworkConnected(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);

        // Check for network connections
        if (connectivityManager != null) {
            if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED) {

                // if connected with internet

                Toast.makeText(context.getApplicationContext(), " Connected ", Toast.LENGTH_LONG).show();
                return true;

            } else if (connectivityManager.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                    connectivityManager.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED) {

                Toast.makeText(context.getApplicationContext(), " Not Connected ", Toast.LENGTH_LONG).show();
                return false;
            }
        }
        return false;
    }
}
