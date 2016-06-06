package com.example.eventdiary_v2;

import android.provider.BaseColumns;

public class UserTable {

	public UserTable()
	{
		//Default Constructor
	}
	
	public static abstract  class userTableInfo implements BaseColumns
	{
		 public static final String USER_NAME = "user_name" ;
		 public static final String USER_PASS = "user_pass" ;
		 public static final String DATABASE_NAME = "userdb";
		 public static final String TABLE_NAME = "loginTable";
		  
     }
	
}