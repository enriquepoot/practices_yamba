package com.quimank.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class StatusActivity extends Activity implements OnClickListener {
	private static final String TAG = "StatusActivity";
	EditText editText;
	Button updateButton;
	Twitter twitter;
	
    /** Called when the activity is first created. */
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

	public void onClick(View v) {
		this.twitter.setStatus(this.editText.getText().toString());
		Log.d(this.TAG, "onClicked");
	}
}