package com.quimank.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener, TextWatcher, OnSharedPreferenceChangeListener {
	private static final String TAG = "StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
	TextView textCount;
	SharedPreferences prefs;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.status);
        
        //Find views
        this.editText = (EditText) findViewById(R.id.editText);
        
        this.updateButton = (Button) this.findViewById(R.id.buttonUpdate);        
        this.updateButton.setOnClickListener(this);
        
        this.textCount = (TextView) this.findViewById(R.id.textCount);
        this.textCount.setText(Integer.toString(140));
        this.textCount.setTextColor(Color.GREEN);
        
        this.editText.addTextChangedListener(this);
        
        //Setup preferences
        this.prefs = PreferenceManager.getDefaultSharedPreferences(this);
        this.prefs.registerOnSharedPreferenceChangeListener(this);        
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
		protected String doInBackground(String... statuses) {
			try{
				winterwell.jtwitter.Status status = getTwitter().updateStatus(statuses[0]);
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

	public void afterTextChanged(Editable statusText) {
		int count = 140 - statusText.length();
		this.textCount.setText(Integer.toString(count));
		this.textCount.setTextColor(Color.GREEN);
		if(count < 10)
			this.textCount.setTextColor(Color.YELLOW);
		if(count < 0)
			this.textCount.setTextColor(Color.RED);
	}

	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item){
		switch(item.getItemId()){
		case R.id.itemPrefs:
			startActivity(new Intent(this, PrefsActivity.class));
			break;
		}
		return true;
	}

	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		// TODO Auto-generated method stub
		//Invalidate twitter object
		this.twitter = null;
	}
	
	@SuppressWarnings("deprecation")
	private Twitter getTwitter(){
		if(this.twitter == null){
			String username, password, apiRoot;
			username = prefs.getString("username", "studenttwitter");
			password = prefs.getString("password", "password");
			apiRoot = prefs.getString("apiRoot", "http://identi.ca/api");
			
			//Connect to twitter.com
			this.twitter = new Twitter(username, password);
			this.twitter.setAPIRootUrl(apiRoot);
		}
		return this.twitter;
	}
}
