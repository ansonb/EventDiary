package com.example.eventdiary_v2;

import java.text.SimpleDateFormat;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Date;

import com.example.eventdiary.R;

public class addData extends Activity {

	
	EditText date;
	EditText goodEvents,badEvents;
	EditText Event;
	Button addButton;
	
	String Date,GE,BE,event;
	
	int noOfGE, noOfBE;
	
	int position;
	
	boolean leavingActivityOnIntent = false;
	boolean leavingApp = true;
	
	public addData()
	{
		//Default constructor
	}
	
	@Override
    public void onBackPressed(){
    	new AlertDialog.Builder(addData.this)
        .setMessage("Do you wish to exit without saving?")
        .setCancelable(false)
        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
    	        finish();
            }
        })
        .setNegativeButton("Cancel",null)
        .show();
    }
	
	@Override
	protected void onUserLeaveHint()
	{
		//super.onUserLeaveHint();
		//Log.i("I'm leaving","UserProfile");
		
	    if(!leavingActivityOnIntent)
	    {
            leavingApp = true;
	    }
	    else if(leavingActivityOnIntent){
			leavingActivityOnIntent = false;
	    }
	}

	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_data);
		
		
		
		date = (EditText) findViewById(R.id.date);
		
		goodEvents = (EditText) findViewById(R.id.goodevent);
		badEvents = (EditText) findViewById(R.id.badevent);
		
		Event = (EditText) findViewById(R.id.textView1);
		
		addButton = (Button) findViewById(R.id.button1);
		
		//onEntry();
		Bundle BN;
		BN = getIntent().getExtras();
		position = BN.getInt("pos");
		
		if(position != -1)
		{
			Date = BN.getString("date");
			getDatabaseRow();
		}
		else
		{
			Date dnow = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			Date = sdf.format(dnow);
			date.setText(Date);
		}
		
		Date = date.getText().toString();
		GE = goodEvents.getText().toString();
		BE = badEvents.getText().toString();
		event = Event.getText().toString();
		
		noOfGE = Integer.parseInt(GE);
		noOfBE = Integer.parseInt(BE);
		
		
		
		goodEvents.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				noOfGE ++;
				goodEvents.setText(String.valueOf(noOfGE));
			}
		});
		
        badEvents.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				noOfBE ++;
				badEvents.setText(String.valueOf(noOfBE));
			}
		});
        
        addButton.setOnClickListener(new OnClickListener() {
			
        	
			@Override
			public void onClick(View v) {
				
				if(dataPresent() && position == -1)
				{
					 new AlertDialog.Builder(addData.this)
			           .setMessage("Are you sure you want to save changes to " + Date + " and exit?")
			           .setCancelable(false)
			           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {
			                    
			            	   putInDatabase();
			            	   finish();
			               }
			           })
			           .setNegativeButton("Cancel", null)
			           .show();
				}
				else
				{
					putInDatabase();
	   				finish();
				}
				
			}	
		});
		
	
	}
	
	public void onRestart()
	{
		if(leavingApp){
			leavingApp = false;
			leavingActivityOnIntent = true;
			Intent i= new Intent(addData.this, EventDiary.class);
		    i.putExtra("activityNumber", 2);
			startActivity(i);
		}
	        
        super.onRestart();
	}
	
	
	private boolean dataPresent() {
        profileDatabaseOp DOP = new profileDatabaseOp(this);
		
		String curr_date = date.getText().toString();

			boolean toUpdate = false;
			
			Cursor CR = DOP.getInfo(DOP);
			
			if(CR.moveToFirst())
			{
				do
				{
					if(curr_date.equals(CR.getString(0)))
					{
						toUpdate = true;
					}
				}while(CR.moveToNext());
			}
			
			if(toUpdate)
			{
				return true;
			}
			else
			{
				return false;
			}
	}
	
	public void onEntry()
	{
		Bundle BN;
		BN = getIntent().getExtras();
		position = BN.getInt("pos");
		
		if(position != -1)
		{
			getDatabaseRow();
		}
		
		Date = date.getText().toString();
		GE = goodEvents.getText().toString();
		BE = badEvents.getText().toString();
		event = Event.getText().toString();
		
		noOfGE = Integer.parseInt(GE);
		noOfBE = Integer.parseInt(BE);
	}

	public void putInDatabase() {
		profileDatabaseOp DOP = new profileDatabaseOp(this);
		
		Date = date.getText().toString();
		GE = goodEvents.getText().toString();
		BE = badEvents.getText().toString();
		event = Event.getText().toString();

		{
			boolean toUpdate = false;
			
			Cursor CR = DOP.getInfo(DOP);
			
			if(CR.moveToFirst())
			{
				do
				{
					if(Date.equals(CR.getString(0)))
					{
						toUpdate = true;
					}
				}while(CR.moveToNext());
			}
			
			if(toUpdate)
			{
				DOP.Update(DOP, Date, GE, BE, event);
				Toast.makeText(getBaseContext(), "Successfully Updated", Toast.LENGTH_LONG).show();
			}
			else
			{
				DOP.putInfo(DOP, Date, GE, BE, event);
				Toast.makeText(getBaseContext(), "Successfully Inserted", Toast.LENGTH_LONG).show();
			}
			
		}
		
	}
	
	private void getDatabaseRow() {
		Cursor CR;
		profileDatabaseOp DOP = new profileDatabaseOp(this);
		
		CR = DOP.getInfo(DOP);
		
		int currentPosition = 0;
		
		if(CR.moveToFirst())
		{
			do
			{
				if(Date.equals(CR.getString(0)))
				{
					date.setText(CR.getString(0));
					goodEvents.setText(CR.getString(1));
					badEvents.setText(CR.getString(2));
					Event.setText(CR.getString(3));
				}
				/*if(currentPosition == position)
				{
					date.setText(CR.getString(0));
					goodEvents.setText(CR.getString(1));
					badEvents.setText(CR.getString(2));
					Event.setText(CR.getString(3));
				}
				currentPosition++;*/
				
			}while(CR.moveToNext());
		}
		
	}
	
	
}
