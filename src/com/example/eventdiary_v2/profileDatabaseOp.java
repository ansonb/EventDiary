package com.example.eventdiary_v2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract.Columns;
import android.util.Log;

import com.example.eventdiary_v2.EventTable.eventTableInfo;
import com.example.eventdiary_v2.UserTable.userTableInfo;



public class profileDatabaseOp extends SQLiteOpenHelper{

	public static final int databaseVersion=2;
	public String QUERRY=
			"CREATE TABLE "+eventTableInfo.TABLE_NAME+"("
					+eventTableInfo.DATE+" TEXT,"+eventTableInfo.NO_OF_GOOD_EVENTS+
					" TEXT,"+eventTableInfo.NO_OF_BAD_EVENTS+" TEXT,"
					+eventTableInfo.EVENT+" TEXT,"
					+eventTableInfo.MARK_EVENT+" INT);";
	
	public profileDatabaseOp(Context context) {
		super(context, eventTableInfo.DATABASE_NAME, null, databaseVersion);
		Log.d("Profile Database OPerations ", "Database Created");
	}
	
	@Override
	public void onCreate(SQLiteDatabase sdb)
	{
		Log.d("Profile Database OPerations ", "Creating Table");
		sdb.execSQL(QUERRY);
		Log.d("Profile Database OPerations ", "Table Created");
	}
	
	public void putInfo(profileDatabaseOp dob, String date, String GE, String BE, String event)
	{
		Log.d("Profile Database OPerations ", "Putting info");
		SQLiteDatabase SQ=dob.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(eventTableInfo.DATE, date);
		cv.put(eventTableInfo.NO_OF_GOOD_EVENTS, GE);
		cv.put(eventTableInfo.NO_OF_BAD_EVENTS, BE);
		cv.put(eventTableInfo.EVENT, event);
		cv.put(eventTableInfo.MARK_EVENT, 0);
		
		/*Cursor CR=getInfo(dob);
		if(CR.moveToFirst())
		{
			do
			{
				if(date.contentEquals(CR.getString(0)))
				{
					Update(dob,date,GE,BE,event);
					return;
				}
			}while(CR.moveToNext());
		}*/
		
		SQ.insert(eventTableInfo.TABLE_NAME, null, cv);
		Log.d("Profile Database OPerations", "Row Inserted");
	}
	
	public void putInfo(profileDatabaseOp dob, String date, String GE, String BE, int bookmark, String event)
	{
		Log.d("Profile Database OPerations ", "Putting info");
		SQLiteDatabase SQ=dob.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(eventTableInfo.DATE, date);
		cv.put(eventTableInfo.NO_OF_GOOD_EVENTS, GE);
		cv.put(eventTableInfo.NO_OF_BAD_EVENTS, BE);
		cv.put(eventTableInfo.EVENT, event);
		cv.put(eventTableInfo.MARK_EVENT, bookmark);
		
		/*Cursor CR=getInfo(dob);
		if(CR.moveToFirst())
		{
			do
			{
				if(date.contentEquals(CR.getString(0)))
				{
					Update(dob,date,GE,BE,event);
					return;
				}
			}while(CR.moveToNext());
		}*/
		
		SQ.insert(eventTableInfo.TABLE_NAME, null, cv);
		Log.d("Profile Database OPerations", "Row Inserted");
	}
	
	public void putInfo(profileDatabaseOp dob, String date, String GE, String BE, String bookmark, String event)
	{
		Log.d("Profile Database OPerations ", "Putting info");
		SQLiteDatabase SQ=dob.getWritableDatabase();
		
		ContentValues cv = new ContentValues();
		cv.put(eventTableInfo.DATE, date);
		cv.put(eventTableInfo.NO_OF_GOOD_EVENTS, GE);
		cv.put(eventTableInfo.NO_OF_BAD_EVENTS, BE);
		cv.put(eventTableInfo.EVENT, event);
		
		if(bookmark.equals("1")){
			Log.i("In 1", bookmark);
			cv.put(eventTableInfo.MARK_EVENT, 1);
		}
		else{
			Log.i("In 0", bookmark);
			cv.put(eventTableInfo.MARK_EVENT, 0);
		}
		
		
		/*Cursor CR=getInfo(dob);
		if(CR.moveToFirst())
		{
			do
			{
				if(date.contentEquals(CR.getString(0)))
				{
					Update(dob,date,GE,BE,event);
					return;
				}
			}while(CR.moveToNext());
		}*/
		
		SQ.insert(eventTableInfo.TABLE_NAME, null, cv);
		Log.d("Profile Database OPerations", "Row Inserted");
	}
	
	public void Update(profileDatabaseOp DOP, String date, String GE, String BE, String event) 
	{
		 SQLiteDatabase SQ  = DOP.getWritableDatabase();
		 String selection = eventTableInfo.DATE+ " LIKE ?";
		 //String args[] = {GE, BE, event};
		 String args[] = {date};
		 ContentValues values = new ContentValues();
		 values.put(eventTableInfo.NO_OF_GOOD_EVENTS, GE);
		 values.put(eventTableInfo.NO_OF_BAD_EVENTS, BE);
		 values.put(eventTableInfo.EVENT, event);
		 SQ.update(eventTableInfo.TABLE_NAME, values, selection, args);
		 Log.d("Profile Database OPerations", "Row Updated");
		
	}
	
	public void toggleBookmark(profileDatabaseOp DOP,String date)
	{
		 SQLiteDatabase SQ  = DOP.getWritableDatabase();
		 String selection = eventTableInfo.DATE+ " LIKE ?";
		 String args[] = {date};
		 
		 ContentValues values = new ContentValues();
		 int bookmark = 1;
		 
		 Cursor CR = DOP.getInfo(DOP);
		 if(CR.moveToFirst())
		 {
			 do
			 {
				 if(date.equals(CR.getString(0)))
				 {
					 //toggle bookmark
					 bookmark = 1 - CR.getInt(4);
					 break;
				 }
			 }while(CR.moveToNext());
		 }
		 
		 
		 values.put(eventTableInfo.MARK_EVENT, bookmark);
		 SQ.update(eventTableInfo.TABLE_NAME, values, selection, args);
		 
		 
	}
	
	public void toggleBookmark(profileDatabaseOp DOP,String date
			                   , int bookmark)
	{
		 SQLiteDatabase SQ  = DOP.getWritableDatabase();
		 String selection = eventTableInfo.DATE+ " LIKE ?";
		 String args[] = {date};
		 
		 ContentValues values = new ContentValues();
		 
		 bookmark = 1-bookmark;
		 
		 values.put(eventTableInfo.MARK_EVENT, bookmark);
		 SQ.update(eventTableInfo.MARK_EVENT, values, selection, args);
		 
		 
	}
	
	 public void delete(profileDatabaseOp DOP, String Date)
	 {
	  String selection = eventTableInfo.DATE+ " LIKE ?";
	  String args[] = {Date};
	  SQLiteDatabase SQ = DOP.getWritableDatabase();
	  SQ.delete(eventTableInfo.TABLE_NAME, selection, args);
	  
	 }

	public Cursor getInfo(profileDatabaseOp dob)
	{
		Log.d("Profile Database OPerations ", "Getting info");
		SQLiteDatabase SQ = dob.getReadableDatabase();
		String Columns[]={eventTableInfo.DATE,eventTableInfo.NO_OF_GOOD_EVENTS
				,eventTableInfo.NO_OF_BAD_EVENTS,eventTableInfo.EVENT,eventTableInfo.MARK_EVENT};
		Cursor CR = SQ.query(eventTableInfo.TABLE_NAME, Columns,
				null, null, null, null, null, null);
		return CR;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
