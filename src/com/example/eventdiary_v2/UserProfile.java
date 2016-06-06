package com.example.eventdiary_v2;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Vector;

import com.example.eventdiary.R;
import com.example.eventdiary_v2.EventTable.eventTableInfo;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.MenuItemCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class UserProfile extends Activity{

    String values[];	
    int noOfUsers;
    
    boolean cancel = false;
    boolean longClick = false;
    boolean displayingBookmarks = false;
    //boolean createdBefore = false;
    Menu myMenu;
    MenuInflater myInflater;
    int position;
    long ID;
    
    ListView listview ;
    
    ActionBar actionbar;
    EditText searchSpace ;
    
    int arrayOfPosToDelete[];
    
    boolean leavingActivityOnIntent;
    boolean leavingApp;
	
	int counter = 0;
	
	Vector mIDs;

    
    UserProfileAdapter myAdapter;
    
    String fileName = "Backup.DIARY";
	
    public UserProfile()
	{
	    //Default Constructor
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
	
	public void bookmarkToggle(View v)
	{
		if(!longClick)
		{
			final int position = listview.getPositionForView((View) v.getParent()); 
			String d = myAdapter.getItem(position).getDate();
			profileDatabaseOp DOP = new profileDatabaseOp(UserProfile.this);
			DOP.toggleBookmark(DOP, d);
			
			if (!displayingBookmarks)
			{
				displayNewProfile();	
			}
			else
			{
				displayBookmarkedItems();
			}
					
		}
	}
	
	@Override
	public void onBackPressed() {
	   /* if( displayingBookmarks)
	    {
	    	displayNewProfile();
	    	showStartMenu();
	    	displayingBookmarks = false;
	    }
	    else*/
	    {
	    	new AlertDialog.Builder(UserProfile.this)
	           .setMessage("You are about to log out and exit...Continue?")
	           .setCancelable(false)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               @SuppressLint("NewApi") public void onClick(DialogInterface dialog, int id) {
	       	    	
	            	   //getFragmentManager().popBackStackImmediate();
	            	   finish();
	               }
	           })
	           .setNegativeButton("Cancel",null)
	           .show();
	    }
	    return;
	}
	
	@SuppressLint("NewApi") 
	public void onCreate(Bundle savedInstanceState)
	{
		/*if(createdBefore)
		{
			Intent intent = new Intent(UserProfile.this, EventDiary.class);
			startActivity(intent);
			finish();
		}
		
		createdBefore = true;*/
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_profile);
		
		mIDs = new Vector(1,1);
		
		leavingActivityOnIntent = false;
		leavingApp = false;
		
		actionbar = getActionBar();
		actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
		
		
		listview = (ListView)findViewById(R.id.listview);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		
		displayNewProfile();
		
		arrayOfPosToDelete = new int[listview.getCount()];
		Initialize(arrayOfPosToDelete, listview.getCount());
		
		 listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		      @Override
		      public void onItemClick(AdapterView<?> parent, final View view,
		          int position, long id) {
		    	  
		    	  //final String s=getString(position +1);
			      //Toast.makeText(getBaseContext(), "Clicked " , Toast.LENGTH_LONG).show();
			        
		    	  UserProfile.this.position = position;
			      UserProfile.this.ID = id;
		    	  
		    	  if (!longClick) {
					Intent intent = new Intent(UserProfile.this, addData.class);
					intent.putExtra("date", myAdapter.getItem(position).getDate());
					//Log.i("pos on click",myAdapter.getItem(position).getDate());
					intent.putExtra("pos", myAdapter.getCount()-1 - position);
					//Log.i("pos on click",Integer.toString(myAdapter.getCount()-1 - position));
					
					leavingActivityOnIntent = true;
					startActivity(intent);
					//finish();
				 }
		    	  else
		    	  {
		    		  if(mIDs.contains(id)){
		    			  mIDs.remove(id);
		    		  }else{
		    			  mIDs.add(id);
		    		  }
		    		  addPositionToBeDeleted(view);
		    		  //highlightSelectedPositions();
		    	  }
		    	  
		      }

		    });
		 
		 listview.setLongClickable(true);
		 listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

	            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
	                    int pos, long id) {
	                // TODO Auto-generated method stub

	                Log.v("long clicked","pos: " + pos);
	                
	                showDeleteMenu();
	                //deleteItems();
	                
	                return true;
	            }
	        });
		 
			//Toast.makeText(getBaseContext(), "Created" , Toast.LENGTH_LONG).show();
		
	}
	
	@Override
	public void onRestart()
	{
		counter ++;
		super.onRestart();
		if(leavingApp){
			leavingApp = false;
			Intent i= new Intent(UserProfile.this, EventDiary.class);
		    i.putExtra("activityNumber", 2);
		    leavingActivityOnIntent = true;
			startActivity(i);
		}
		
		
		//Toast.makeText(getBaseContext(), "Restarted", Toast.LENGTH_LONG).show();
		Log.d("userprofile restart", "restarting....");
			
		if(displayingBookmarks){
			displayBookmarkedItems();
		}
		else{
			displayNewProfile();
		}
		
		arrayOfPosToDelete = new int[listview.getCount()];
		Initialize(arrayOfPosToDelete, listview.getCount());
		
		
		//Intent intent = new Intent(UserProfile.this, EventDiary.class);
		//startActivity(intent);
		//finish();
		
	}
	
	@SuppressLint("NewApi") private void exportToText(){
		new AlertDialog.Builder(UserProfile.this)
        .setMessage("Exporting will replace the existing backup, continue?")
        .setCancelable(false)
        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi") public void onClick(DialogInterface dialog, int id) {
    	    	
         	   //getFragmentManager().popBackStackImmediate();
         	   return;
            }
        })
        .setNegativeButton("Yes",new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi") public void onClick(DialogInterface dialog, int id) {
            	profileDatabaseOp DOP = new profileDatabaseOp(UserProfile.this);
        		Cursor CR = DOP.getInfo(DOP);
          	   
        		try{
        			String date;
        			Date dnow = new Date();
        			SimpleDateFormat sdf = new SimpleDateFormat("dd_MM_yyyy");
        			date = sdf.format(dnow);
        			
        			
        			File dirToSave = new File(Environment.getExternalStorageDirectory().toString()+"/ed_backup/");
        		    dirToSave.mkdir();
        		    Log.d("dirToSave", "dir made");
        		    File sd = Environment.getExternalStoragePublicDirectory("/ed_backup");
        			
        			Toast.makeText(getBaseContext(), "Exporting to "+ sd.toString()+"/"+fileName, Toast.LENGTH_LONG).show();
        			OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(sd.toString()+"/"+fileName), "UTF-16");
        			if(CR.moveToFirst()){
        				do{
        					osw.write(CR.getString(0));
        					osw.write("\n");
        					osw.write(CR.getString(1));
        					osw.write("\n");
        					osw.write(CR.getString(2));
        					osw.write("\n");
        					osw.write(CR.getString(4));
        					osw.write("\n");		
        					osw.write(CR.getString(3));
        					osw.write("$");
        					osw.write("\n");
        					/*osw.write(CR.getString(0).getBytes());
        					osw.write("\n".getBytes());
        					osw.write(CR.getString(1).getBytes());
        					osw.write("\n".getBytes());
        					osw.write(CR.getString(2).getBytes());
        					osw.write("\n".getBytes());
        					osw.write(CR.getString(4).getBytes());
        					osw.write("\n".getBytes());		
        					osw.write(CR.getString(3).getBytes());
        					osw.write("$".getBytes());
        					osw.write("\n".getBytes());*/
        				}while(CR.moveToNext());
        			}
        			osw.close();
        			Toast.makeText(UserProfile.this, "Successfully exported", Toast.LENGTH_LONG).show();
             }catch(Exception e){
        			Toast.makeText(UserProfile.this, "error: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        		}
         }
            })
        .show();	
		
		
	}
	
	private void importFromText(){
		new AlertDialog.Builder(UserProfile.this)
        .setMessage("Importing will overwrite the current database, continue?")
        .setCancelable(false)
        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi") public void onClick(DialogInterface dialog, int id) {
    	    	
         	   //getFragmentManager().popBackStackImmediate();
         	   return;
            }
        })
        .setNegativeButton("Yes",new DialogInterface.OnClickListener() {
            @SuppressLint("NewApi") public void onClick(DialogInterface dialog, int id) {
            	File sd = Environment.getExternalStoragePublicDirectory("/ed_backup");
        		Toast.makeText(getBaseContext(), "Importing from "+ sd.toString()+"/"+fileName, Toast.LENGTH_LONG).show();
        		Cursor CR;
        		profileDatabaseOp DOP = new profileDatabaseOp(UserProfile.this);
        		CR = DOP.getInfo(DOP);
        		DOP.getWritableDatabase().delete(eventTableInfo.TABLE_NAME, null, null);
        		String date, noOfGE, noOfBE, event, bookmark; 
        		date="";
        		noOfGE="";
        		noOfBE="";
        		bookmark="";
        		event="";
        		try {
        			InputStreamReader fis = new InputStreamReader(new FileInputStream(sd.toString()+"/"+fileName), "UTF-16") ;
        			Log.d("file address",sd.toString()+"/"+fileName);
        			int c;
        			int prev_c=0;
        			int row=0;
        			int col=0;
        			while((c=fis.read())!=-1){
        				Log.d("inside while",Character.toString((char)c));
        				
        				if(!((c=='$'||(c=='\n'&&col<4)) ||
        						prev_c=='$')){
        					switch(col){
        					case 0:
        						date = date + Character.toString((char)c);
        						break;
        					case 1:
        						noOfGE = noOfGE + Character.toString((char)c);
        						break;
        					case 2:
        						noOfBE = noOfBE + Character.toString((char)c);
        						break;
        					case 3:
        						bookmark = bookmark + Character.toString((char)c);
        						break;
        					case 4:
        						event = event + Character.toString((char)c);
        						break;
        				    default:
        				    	break;
        					}
        				}
        				
        				if(c=='\n'){
        					if(prev_c=='$'){
        						DOP.putInfo(DOP, date, noOfGE, noOfBE, bookmark, event);
        						
        						Log.d("row " + Integer.toString(row), date+noOfGE+
        								noOfBE+bookmark+event);
        						date="";
        						noOfGE="";
        						noOfBE="";
        						bookmark="";
        						event="";
        		
        						row++;
        						col=0;
        					}
        					else if(col<4){
        						col++;
        					}
        				}
        				prev_c = c;
        			}
        			
        			fis.close();
        			displayNewProfile();
        			Toast.makeText(UserProfile.this, "Successfully imported", Toast.LENGTH_LONG).show();
        			
        		} catch (Exception e) {
        			// TODO Auto-generated catch block
        			e.printStackTrace();
        			Toast.makeText(UserProfile.this, "error: "+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        		}
          	   
             }
         })
        .show();
		
		
	}
	
	public void setAllViewsTransparent()
	{
		for(int i=0;i<listview.getChildCount();i++)
		{
			listview.getChildAt(i).setBackgroundColor(Color.TRANSPARENT);
		}
	}
	
	private void highlightSelectedPositions()
	{
		setAllViewsTransparent();
		
		int index = 0;
		while(index < listview.getCount()
				&& arrayOfPosToDelete[index] != -1 )
		{
			//myAdapter.getView(index, null, null).setBackgroundColor(Color.CYAN);
			if(mIDs.contains(listview.getChildAt(arrayOfPosToDelete[index]).getId()))
			listview.getChildAt(arrayOfPosToDelete[index]).setBackgroundColor(Color.CYAN);
			
			index++;
			
		}
	}
	
	private void addPositionToBeDeleted(View v)
	{
		int index;
		
		index = find(arrayOfPosToDelete, listview.getCount(),  position);
	
		if(index == -1)
		{
			insert(arrayOfPosToDelete, listview.getCount(),position);
			//v.setBackgroundColor(Color.CYAN);
		}
		else
		{
			remove(arrayOfPosToDelete, listview.getCount(), position);
			//v.setBackgroundColor(Color.TRANSPARENT);
		}
	
		highlightSelectedPositions();
		for(int i=0;i<listview.getCount();i++)
		{
			Log.d("arrayOfPos", Integer.toString(arrayOfPosToDelete[i]));
			//Toast.makeText(getBaseContext(),Integer.toString(arrayOfPosToDelete[i]), Toast.LENGTH_SHORT).show();

		}
		
	}
	
	
	private int find(int arr[], int length, int pos)
	{
		int index = -1;
		for(int i=0;i<length;i++)
		{
			if(arr[i] == pos)
			{
				index = i;
				break;
			}
		}
		
		return index;
	}
	
	private void Initialize(int arr[], int length)
	{
		for(int i=0;i<length;i++)
		{
			arr[i] = -1;
		}
	}
	
	private void insert(int arr[], int length, int value)
	{
		int index = 0;
		for(int i=0;i<length;i++)
		{
			if(arr[i] == -1)
			{
				index = i;
				break;
			}
		}
		
		arr[index] = value;
	}
	
	private void remove(int arr[], int length, int value)
	{
		int index = length ;
		
		for(int i=0;i<length;i++)
		{
			if(arr[i] == value)
			{
				index = i;
				break;
			}
		}
		
		for(int i=index;i<length-1;i++)
		{
			arr[i] = arr[i+1];
		}
		arr[length-1] = -1;
	}
	
	

	private void displayNewProfile() {
		profileDatabaseOp DOP = new profileDatabaseOp(this);
		
		Cursor CR=DOP.getInfo(DOP);
		
		
		//ListView listview = (ListView)findViewById(R.id.listview);
		
		final ArrayList<UserProfileModel> list = new ArrayList<UserProfileModel>();
		final ArrayList<String> listEvent = new ArrayList<String>();
		
		if(CR.moveToLast())
		{
			do
			{
				UserProfileModel Model = new UserProfileModel();
				
				Model.setDate(CR.getString(0));
				Model.setGE(CR.getString(1));
				Model.setBE(CR.getString(2));
				Model.setEvent(CR.getString(3));
				Model.setBookmark(CR.getInt(4));
				
				list.add(Model);
				
			} while (CR.moveToPrevious());
		}
		
		else
		{
			listEvent.add("This is an Event Diary. Please add events.");
			final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    	        android.R.layout.simple_list_item_1, listEvent);
	        listview.setAdapter(adapter);
	        
	        return;
		}
		
		/*for(int i=0;i<list.size();i++)
		{
			Log.d("Full list", list.get(i).getGE() + " " + list.get(i).getBE());
		}*/
		
		final UserProfileAdapter adapter =
		      new UserProfileAdapter (this,list);
		
		
		
		myAdapter = adapter;
		
        listview.setAdapter(adapter);
       
       
	}
	
	public void displayBookmarkedItems()
	{
        profileDatabaseOp DOP = new profileDatabaseOp(this);
		
		Cursor CR=DOP.getInfo(DOP);
		
		
		ListView listview = (ListView)findViewById(R.id.listview);
		
		final ArrayList<UserProfileModel> list = new ArrayList<UserProfileModel>();
		final ArrayList<String> listEvent = new ArrayList<String>();
		
		if(CR.moveToLast())
		{
			do
			{
				if (CR.getInt(4) == 1) {
					UserProfileModel Model = new UserProfileModel();
					Model.setDate(CR.getString(0));
					Model.setGE(CR.getString(1));
					Model.setBE(CR.getString(2));
					Model.setEvent(CR.getString(3));
					Model.setBookmark(CR.getInt(4));
					list.add(Model);
				}
				
			} while (CR.moveToPrevious());
		}
		
		else
		{
			listEvent.add("This is an Event Diary. Please add events.");
			final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
	    	        android.R.layout.simple_list_item_1, listEvent);
	        listview.setAdapter(adapter);
	        
	        return;
		}
		
		final UserProfileAdapter adapter =
		      new UserProfileAdapter (this,list);
		
		
		
		myAdapter = adapter;
		
        listview.setAdapter(adapter);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.event_diary, menu);
	    
	    myMenu = menu;
        myInflater = inflater;
        
        showStartMenu();
        
	    return true;
	}
	
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
        case R.id.action_import:
        	importFromText();
        	displayNewProfile();
        	return true;
        case R.id.action_export:
        	exportToText();
        	return true;
        case R.id.action_back_to_all:
        	displayNewProfile();
        	displayingBookmarks = false;
        	showStartMenu();
        	return true;
        case R.id.action_add:
        	addNewData();
            return true;
        case R.id.action_logOut:
        	logOut();
            return true;
        case R.id.action_delete:
        	new AlertDialog.Builder(UserProfile.this)
	           .setMessage("Are you sure you want to Delete selected items?")
	           .setCancelable(true)
	           .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    
	            	   deleteSelections();
	            	   showStartMenu();
	               	   displayNewProfile();
	               	   Initialize(arrayOfPosToDelete, listview.getCount());
	               }
	           })
	           .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                    
	            	   showStartMenu();
	               	   displayNewProfile();
	               	   Initialize(arrayOfPosToDelete, listview.getCount());
	               }
	           })
	           .show();
        	
        	//showStartMenu();
        	//displayNewProfile();
        	//Initialize(arrayOfPosToDelete, listview.getCount());
        	return true;
        case R.id.action_cancel:
        	displayNewProfile();
        	showStartMenu();
        	cancel = true;
    		Initialize(arrayOfPosToDelete, listview.getCount());
        	return true;
        case R.id.action_search:
        	showSearchMenu();
        	return true;
        case R.id.action_show_bookmark:
        	displayBookmarkedItems();
        	displayingBookmarks = true;
        	showBookmarkMenu();
        	return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
    
    private void deleteSelections()
    {
    	profileDatabaseOp DOP = new profileDatabaseOp(UserProfile.this);
    	
    	String date;
    	UserProfileModel eventToDelete;
    	    	
    	int index = 0;
    	while(index < listview.getCount() 
    			&& arrayOfPosToDelete[index] != -1)
    	{
    		
    		eventToDelete = myAdapter.getItem(arrayOfPosToDelete[index]);
    		Log.d("In deleteSelections", "Deleting value"+eventToDelete.getDate());
    		DOP.delete(DOP, eventToDelete.getDate());
    		
    		index++;
    	}
    }
    
    
    public void addNewData()
    {
    	Intent intent = new Intent(UserProfile.this, addData.class);
    	intent.putExtra("pos", -1);
    	leavingActivityOnIntent = true;
    	startActivity(intent);
    	//finish();
    }
    
    public void logOut()
    {
    	finish();
    }
    
    public void displaySearchResult()
    {
    	String querry = searchSpace.getText().toString();
    	
    	ArrayList<UserProfileModel> list;
    	list = new ArrayList<UserProfileModel>();
    	
    	profileDatabaseOp DOP = new profileDatabaseOp(UserProfile.this);
    	Cursor CR;
    	CR = DOP.getInfo(DOP);
    	
    	if(CR.moveToLast())
    	{
    		do
    		{
    			if(CR.getString(0).contains(querry) ||
    			   CR.getString(1).contains(querry) ||
    			   CR.getString(2).contains(querry) ||
    			   CR.getString(3).contains(querry) )
    			{
    				UserProfileModel Model;
    				Model = new UserProfileModel(CR.getString(0),
    						CR.getString(1),CR.getString(2),
    						CR.getString(3));
    				
    				list.add(Model);
    			}
    		}while(CR.moveToPrevious());
    	}
    	
    	UserProfileAdapter adapter = new UserProfileAdapter(UserProfile.this, list);
        myAdapter = adapter;
        
        listview.setAdapter(adapter);
    }
    
    @SuppressLint("NewApi")
    private void showSearchMenu()
    {
    	actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
    	
    	actionbar.setCustomView(R.layout.search_view);
    	
    	
    	hideOption(R.id.action_search);
    	hideOption(R.id.action_logOut);
    	hideOption(R.id.action_add);
    	hideOption(R.id.action_delete);
    	hideOption(R.id.action_forward);
    	showOption(R.id.action_cancel);
    	hideOption(R.id.action_back_to_all);
    	
		searchSpace = (EditText) findViewById(R.id.searchfield);
		searchSpace.addTextChangedListener(new TextWatcher()
    	{
    		@Override
    	    public void afterTextChanged(Editable s) {}

    		@Override    
    		public void beforeTextChanged(CharSequence s, int start,
    		  int count, int after) {
    		}
    		
    		@Override
			public void onTextChanged(CharSequence s, 
					int start, int before, int count)
    		{
    			//Toast.makeText(getBaseContext(), "text changed", Toast.LENGTH_LONG).show();
    		
    			displaySearchResult();
    		}
    	}
    	);
    	
    }
    
    @SuppressLint("NewApi")  
    private void showBookmarkMenu()
    {
        actionbar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME);
    	
    	setAllViewsTransparent();
    	
    	longClick = false;
    	
    	hideOption(R.id.action_export);
    	hideOption(R.id.action_import);
    	hideOption(R.id.action_search);
    	hideOption(R.id.action_delete);
    	hideOption(R.id.action_cancel);
    	hideOption(R.id.action_logOut);
    	hideOption(R.id.action_add);
    	hideOption(R.id.action_forward);
    	hideOption(R.id.action_show_bookmark);
    	showOption(R.id.action_back_to_all);
    }
    
    @SuppressLint("NewApi") 
    private void showNothing()
    {
        actionbar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME);
    	
    	setAllViewsTransparent();
    	
    	longClick = false;
    	
    	hideOption(R.id.action_export);
    	hideOption(R.id.action_import);
    	hideOption(R.id.action_search);
    	hideOption(R.id.action_delete);
    	hideOption(R.id.action_cancel);
    	hideOption(R.id.action_logOut);
    	hideOption(R.id.action_add);
    	hideOption(R.id.action_forward);
    	hideOption(R.id.action_show_bookmark);
    	hideOption(R.id.action_back_to_all);
    }
    
    @SuppressLint("NewApi") 
    private void showStartMenu()
    {
    	actionbar.setDisplayOptions( ActionBar.DISPLAY_SHOW_HOME);
    	
    	setAllViewsTransparent();
    	
    	longClick = false;
    	
    	showOption(R.id.action_search);
    	showOption(R.id.action_export);
    	showOption(R.id.action_import);
    	hideOption(R.id.action_delete);
    	hideOption(R.id.action_cancel);
    	showOption(R.id.action_logOut);
    	showOption(R.id.action_add);
    	hideOption(R.id.action_forward);
    	showOption(R.id.action_show_bookmark);
    	hideOption(R.id.action_back_to_all);
    }
    
    @SuppressLint("NewApi") 
    private void showDeleteMenu()
    {
    	actionbar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME);
    	
    	longClick = true;
    	hideOption(R.id.action_export);
    	hideOption(R.id.action_import);
    	hideOption(R.id.action_show_bookmark);
    	hideOption(R.id.action_search);
    	hideOption(R.id.action_logOut);
    	hideOption(R.id.action_add);
    	showOption(R.id.action_delete);
    	showOption(R.id.action_cancel);
    	hideOption(R.id.action_forward);
    	hideOption(R.id.action_back_to_all);
    	
    }
    
    private void hideOption(int id)
    {
        MenuItem item = myMenu.findItem(id);
        item.setVisible(false);
    }

    private void showOption(int id)
    {
        MenuItem item = myMenu.findItem(id);
        item.setVisible(true);
    }

    private void setOptionTitle(int id, String title)
    {
        MenuItem item = myMenu.findItem(id);
        item.setTitle(title);
    }

    private void setOptionIcon(int id, int iconRes)
    {
        MenuItem item = myMenu.findItem(id);
        item.setIcon(iconRes);
    }
    
}
