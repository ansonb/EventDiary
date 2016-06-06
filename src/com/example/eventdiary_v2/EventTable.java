package com.example.eventdiary_v2;

import android.provider.BaseColumns;

public class EventTable {

	public EventTable()
	{
		//Default Constructor
	}
	
	public static abstract  class eventTableInfo implements BaseColumns
	{
		 public static final String DATE = "date" ;
		 public static final String NO_OF_GOOD_EVENTS = "good" ;
		 public static final String NO_OF_BAD_EVENTS = "bad";
		 public static final String EVENT = "event";
		 public static final String MARK_EVENT = "bookmark";
		 public static final String TABLE_NAME = "eventTable";
		 public static final String DATABASE_NAME = "profiledb";
		  
     }
	
}
