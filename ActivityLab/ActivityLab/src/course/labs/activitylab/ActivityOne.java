package course.labs.activitylab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class ActivityOne extends Activity {

	private static final String RESTART_KEY = "restart";
	private static final String RESUME_KEY = "resume";
	private static final String START_KEY = "start";
	private static final String CREATE_KEY = "create";

	// String for LogCat documentation
	private final static String TAG = "Lab-ActivityOne";
	
	// Lifecycle counters
	private long mCreate = 0;
	private long mStart = 0;
	private long mResume = 0;
	private long mRestart =0;
	
	// TODO:
	// Create counter variables for onCreate(), onRestart(), onStart() and
	// onResume(), called mCreate, etc.
	// You will need to increment these variables' values when their
	// corresponding lifecycle methods get called

	private TextView mTvCreate;
	private TextView mTvRestart;
	private TextView mTvResume;
	private TextView mTvStart;
	

	// TODO: Create variables for each of the TextViews, called
    // mTvCreate, etc. 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_one);
		
		// TODO: Assign the appropriate TextViews to the TextView variables
		// Hint: Access the TextView by calling Activity's findViewById()
		// textView1 = (TextView) findViewById(R.id.textView1);

		mTvCreate = (TextView) findViewById(R.id.create);
		mTvRestart = (TextView) findViewById(R.id.restart);
		mTvResume = (TextView) findViewById(R.id.resume);
		mTvStart = (TextView) findViewById(R.id.start);

		Button launchActivityTwoButton = (Button) findViewById(R.id.bLaunchActivityTwo); 
		launchActivityTwoButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO:
				// Launch Activity Two
				// Hint: use Context's startActivity() method

				// Create an intent stating which Activity you would like to start
				Intent intent = new Intent(getApplicationContext(), ActivityTwo.class);
				startActivity(intent);
				
				// Launch the Activity using the intent

			
			}
		});
		
		// Check for previously saved state
		if (savedInstanceState != null) {

			// TODO:
			// Restore value of counters from saved state
			// Only need 4 lines of code, one for every count variable
			Log.i("COURSERA","Restoring savedInstanceState");
			mStart = savedInstanceState.getLong(START_KEY);
			mCreate = savedInstanceState.getLong(CREATE_KEY);
			mResume = savedInstanceState.getLong(RESUME_KEY);
			mRestart = savedInstanceState.getLong(RESTART_KEY);
		
		}

		
		// TODO: Emit LogCat message
				Log.i("COURSERA","Entered the onCreate method");

				// TODO:
				// Update the appropriate count variable
				// Update the user interface via the displayCounts() method
				mCreate++;
				displayCounts();
	}

	// Lifecycle callback overrides

	@Override
	public void onStart() {
		super.onStart();

		// TODO: Emit LogCat message
				Log.i("COURSERA","Entered the onStart method");

				// TODO:
				// Update the appropriate count variable
				// Update the user interface
				mStart++;
				displayCounts();

	}

	@Override
	public void onResume() {
		super.onResume();

		// TODO: Emit LogCat message
		Log.i("COURSERA","Entered the onResume method");

		// TODO:
		// Update the appropriate count variable
		// Update the user interface
		mResume++;
		displayCounts();



	}

	@Override
	public void onPause() {
		super.onPause();

		// TODO: Emit LogCat message

		Log.i("COURSERA","Entered the onPause method");

	}

	@Override
	public void onStop() {
		super.onStop();

		// TODO: Emit LogCat message

		Log.i("COURSERA","Entered the onStop method");

	}

	@Override
	public void onRestart() {
		super.onRestart();

		// TODO: Emit LogCat message
		Log.i("COURSERA","Entered the onRestart method");

		// TODO:
		// Update the appropriate count variable
		// Update the user interface

		mRestart++;
		displayCounts();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		// TODO: Emit LogCat message
		Log.i("COURSERA","Entered the onDestroy method");
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		// TODO:
		// Save state information with a collection of key-value pairs
		// 4 lines of code, one for every count variable

		super.onSaveInstanceState(savedInstanceState);
		Log.i("COURSERA","Entered the onSaveInstanceState method");

		savedInstanceState.putLong(START_KEY, mStart);
		savedInstanceState.putLong(CREATE_KEY, mCreate);
		savedInstanceState.putLong(RESUME_KEY, mResume);
		savedInstanceState.putLong(RESTART_KEY, mRestart);
	}
	
	// Updates the displayed counters
	public void displayCounts() {

		mTvCreate.setText("onCreate() calls: " + mCreate);
		mTvStart.setText("onStart() calls: " + mStart);
		mTvResume.setText("onResume() calls: " + mResume);
		mTvRestart.setText("onRestart() calls: " + mRestart);
	
	}
}
