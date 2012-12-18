package s0n1c.example.pingtest;

import java.io.IOException;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class PingActivity extends Activity {
	
	// UI View Elements
	TextView txtStatus;				// TextView that displays the current ping status
	Button btnRefresh;				// Button used to call the ping request
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ping);

		// Set UI View Elements
		txtStatus = (TextView)findViewById(R.id.txtStatus);
		btnRefresh = (Button)findViewById(R.id.btnRefresh);
		btnRefresh.setText("Refresh");
		
		// OnClick listener for Refresh button
		btnRefresh.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// Start AsyncTask, which will start the ping request to the host
				new pingHostTask().execute();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_ping, menu);
		return true;
	}

	////
	// AsyncTask class to ping a remote host
	////
	private class pingHostTask extends AsyncTask<Void, Void, Boolean> {
		
		// This method is what performs the tasks you wish to perform in the background. In this case, it is the ping request.
		@Override
		protected Boolean doInBackground(Void... params) {
			
			// before pinging, publish the progress.
			// In this case, there is no progress, but this allows for the class to update the status textView
			publishProgress();
			// Return the boolean value of pingHost
			return pingHost();
		}
		
		// This method is called automatically after the 'doinBackground' tasks have finished
		@Override
		protected void onPostExecute(Boolean result) {
			
			// Update the status TextView depending on the result of the ping
			if(result) {
				txtStatus.setText("host found");
			} else { 
				txtStatus.setText("no host found");
			}
		}
		
		// This method is automatically called when the 'publishProgress' method is called within the 'doInBackground'
		@Override
		protected void onProgressUpdate(Void... values) {
			// Simply update the status TextView, to inform the user that the app is currently performing the ping
			txtStatus.setText("Checking for host");
		}
		
		// This method is what actually pings the host
		private boolean pingHost() {
			
			Process p1 = null;		// Create a process object, which will be used to perform the ping
			int returnVal = 0;		// Set ping return status to 0, which automatically declares it as fail

			try {
				// Since Android is Unix base, we can perform a unix ping command. This will return 0 if the ping was unsuccessful, or 1 if the ping returned true
				p1 = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");
			} catch (IOException e) {
				Toast.makeText(getBaseContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
			}

			try {
				returnVal = p1.waitFor();
			} catch (InterruptedException e) {
				Toast.makeText(getBaseContext(), e.getMessage().toString(), Toast.LENGTH_LONG).show();
			}
			
			// return true or false, depending on the status of ping
			if (returnVal == 0) {
				return true;

			} else {
				return false;
			}
		}
	}

}
