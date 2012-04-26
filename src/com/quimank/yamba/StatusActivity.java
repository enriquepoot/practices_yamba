package com.quimank.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener {
	private static final String TAG = "StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
	
    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        
        //Find views
        editText = (EditText) findViewById(R.id.editText);
        this.updateButton = (Button) this.findViewById(R.id.buttonUpdate);
        
        this.updateButton.setOnClickListener(this);
        
        this.twitter = new Twitter("student","password");
        twitter.setAPIRootUrl("http://yamba.marakana.com/api");
    }

    //Called when button is clicked
	@SuppressWarnings("static-access")
	public void onClick(View v) {
		String status = this.editText.getText().toString();
		new PostToTwitter().execute(status);
		Log.d(this.TAG, "onClicked");
	}

	class  PostToTwitter extends AsyncTask<String, Integer, String>{
	
		@Override
		protected String doInBackground(String... arg0) {
			try{
				winterwell.jtwitter.Status status = twitter.updateStatus(arg0[0]);
				return status.text;
			}catch(TwitterException ex){
				Log.e(TAG, ex.toString());
				ex.printStackTrace();
				return "Failed to post";
			}
		}
		
		@Override
		protected void onProgressUpdate(Integer... values){
			super.onProgressUpdate(values);
			//Not used yet
		}
		
		//Called once the background activity has completed
		@Override
		protected void onPostExecute(String result){
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
	}
}
