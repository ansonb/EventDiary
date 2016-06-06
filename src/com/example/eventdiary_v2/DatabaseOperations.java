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



public class DatabaseOperations extends SQLiteOpenHelper{

	public static final int databaseVersion=1;
	public String QUERRY="CREATE TABLE "+userTableInfo.TABLE_NAME+"("
			+userTableInfo.USER_NAME+" TEXT,"+userTableInfo.USER_PASS+
			" TEXT);";
	public String USER_QUERRY=
			"CREATE TABLE "+eventTableInfo.TABLE_NAME+"("
					+eventTableInfo.DATE+" TEXT,"+eventTableInfo.NO_OF_GOOD_EVENTS+
					" TEXT,"+eventTableInfo.NO_OF_BAD_EVENTS+" TEXT,"
					+eventTableInfo.EVENT+" TEXT);";
	
	public DatabaseOperations(Context context) {
		super(context, userTableInfo.DATABASE_NAME, null, databaseVersion);
		//super(context, eventTableInfo.DATABASE_NAME, null, databaseVersion);
		Log.d("Database Operations ", "Database Created");
	}
	
	@Override
	public void onCreate(SQLiteDatabase sdb)
	{
		Log.d("Database OPerations ", "Creating Table");
		sdb.execSQL(QUERRY);
		Log.d("Database OPerations ", "Table Created");
	}
	
	public void putInfo(DatabaseOperations dob, String user, String pass)
	{
		Log.d("Database OPerations ", "Putting info");
		SQLiteDatabase SQ=dob.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(userTableInfo.USER_NAME, user);
		cv.put(userTableInfo.USER_PASS, pass);
		SQ.insert(userTableInfo.TABLE_NAME, null, cv);
		Log.d("Database Operator", "Row Inserted");
	}
	
	public Cursor getInfo(DatabaseOperations dob)
	{
		Log.d("Database OPerations ", "Getting info");
		SQLiteDatabase SQ = dob.getReadableDatabase();
		String Columns[]={userTableInfo.USER_NAME,userTableInfo.USER_PASS};
		Cursor CR = SQ.query(userTableInfo.TABLE_NAME, Columns,
				null, null, null, null, null, null);
		return CR;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}

}
