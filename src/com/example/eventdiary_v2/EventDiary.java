package com.example.eventdiary_v2;

import com.example.eventdiary.R;

import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class EventDiary extends ActionBarActivity {

	Context CTX = this;
	
	EditText username,password,confirmPass;
	Button login,register;
	String name,pass,conpass;
	
	int prevActivity = 0;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_diary);
        
        Log.d("event diary onCreate", "creating...");
        
        prevActivity = 0;
        Bundle B;
        B = getIntent().getExtras();
        if(B!=null)
        {
        	prevActivity = B.getInt("activityNumber");
        }
        Log.i("prevActivity",Integer.toString(prevActivity));
        
        
        username=(EditText)findViewById(R.id.editTextName);
        password=(EditText)findViewById(R.id.editTextPass);
        confirmPass=(EditText)findViewById(R.id.editTextConPass);
        
        login=(Button)findViewById(R.id.loginButton);
        //login.setOnClickListener((OnClickListener) this);
        register=(Button)findViewById(R.id.regButton);
        //register.setOnClickListener((OnClickListener) this);
        
        
        login.setOnClickListener(new OnClickListener()
        {
        	@Override
        	public void onClick(View view)
        	{
        		loginPressed(view);
        	}
        });
        
        register.setOnClickListener(new OnClickListener()
        {
        	public void onClick(View view)
        	{
        		registerPressed(view);
        	}
        });
        
        
    }
    
    /*@Override
    public void onRestart()
    {
    	super.onRestart();
    	Log.d("event diary restart", "restarting...");
    	onCreate(null);
    }*/
    
    


    public void loginPressed(View view)
    {
    	name=username.getText().toString();
    	pass=password.getText().toString();
    	//conpass=confirmPass.getText().toString();
    	
    	DatabaseOperations DOP = new DatabaseOperations(this);
    	Cursor CR = DOP.getInfo(DOP);
    	
    	int noOfUsers = 0;
    	
    	boolean loginStatus = false;
    	if(CR.moveToFirst())
    	{
    		do
    		{
    			noOfUsers++;
    			
    			if(name.equals(CR.getString(0)) 
    					&& pass.equals(CR.getString(1)))
        		{
        			loginStatus = true;
        		}
        		Log.d("Database ",CR.getString(0)+" "+CR.getString(1) );
    		}while(CR.moveToNext());
    	}
    	
    	if(loginStatus == true)
    	{
    		loginStatus=false;
    		Toast.makeText(getBaseContext(), "Login Successfull", Toast.LENGTH_LONG).show();
    		if(prevActivity!=0)
    		{
    			finish();
    			//return; why the hell is it giving double login error
    		}
    		else
    		{
    			goToUserProfile(CR, noOfUsers);
    		}
    		
    	}
    	else
    	{
    		Toast.makeText(getBaseContext(), "Login Unsuccessfull", Toast.LENGTH_LONG).show();
    		//Clear the Text fields
        	username.setText("");
        	password.setText("");
        	confirmPass.setText("");
    	}
    	
    	//Clear the Text fields
    	//username.setText("");
    	//password.setText("");
    	//confirmPass.setText("");
    }
    
    public void registerPressed(View view)
    {
    	name=username.getText().toString();
    	pass=password.getText().toString();
    	conpass=confirmPass.getText().toString();
    	
    	DatabaseOperations DOP = new DatabaseOperations(CTX);
    	
        Cursor CR = DOP.getInfo(DOP);
    	
    	boolean userAlreadyExists = false;
    	if(CR.moveToFirst())
    	{
    		do
    		{
    			if(name.equals(CR.getString(0)))
        		{
        			userAlreadyExists = true;
        		}
        		Log.d("Database ",CR.getString(0)+" "+CR.getString(1) );
    		}while(CR.moveToNext());
    	}
    	
    	if(userAlreadyExists)
    	{
    		userAlreadyExists=false;
    		Toast.makeText(getBaseContext(), "User Already Exists", Toast.LENGTH_LONG).show();
    	}
    	
    	else
    	{
    		if(!pass.equals(conpass))
    		{
    			Toast.makeText(getBaseContext(), "Passwords donot match", Toast.LENGTH_LONG).show();
    		}
    		else
    		{
    			Log.d("Register", "Reached Here");
    			DOP.putInfo(DOP, name, pass);
    			Toast.makeText(getBaseContext(), "Registration Succeddfull", Toast.LENGTH_LONG).show();
    		}
    		
    	}
    	
    	//Clear the Text fields
    	username.setText("");
    	password.setText("");
    	confirmPass.setText("");
    }
    
    public void goToUserProfile(Cursor CR, int noOfUsers)
    {
    	String values[] = new String[noOfUsers];
    	
    	int index = 0;
    	if(CR.moveToFirst())
    	{
    		do
    		{
    			values[index] = CR.getString(0);
    			index++;
    		}while(CR.moveToNext());
    	}
    	
    	Intent i = new Intent(EventDiary.this, UserProfile.class);
    	i.putExtra("NoOfUsers", noOfUsers);
    	i.putExtra("UserNames", values);
    	startActivity(i);
    	//finish();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_diary, menu);
        return true;
    }

    
    
}
