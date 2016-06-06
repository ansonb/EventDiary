package com.example.eventdiary_v2;

public class UserProfileModel {

	String Date;
	String Event;
	String noOfGE;
	String noOfBE;
	int bookmarkVal;
	
	public UserProfileModel()
	{
		//Default constructor
	}
	
	public UserProfileModel(String date, String noOfGE,
			String noOfBE, String event)
	{
		this.Date = date;
		this.Event = event;
		this.noOfGE = noOfGE;
		this.noOfBE = noOfBE;
	}
	
	public UserProfileModel(String date, String noOfGE,
			String noOfBE, String event,int bmVal)
	{
		this.Date = date;
		this.Event = event;
		this.noOfGE = noOfGE;
		this.noOfBE = noOfBE;
		this.bookmarkVal = bmVal;
	}
	
	public void setDate(String date)
	{
		this.Date = date;
	}
	
	public void setEvent(String event)
	{
		this.Event = event;
	}
	
	public void setGE(String GE)
	{
		this.noOfGE = GE;
	}
	
	public void setBE(String BE)
	{
		this.noOfBE = BE;
	}
	
	public void setBookmark(int b)
	{
		this.bookmarkVal = b;
	}
	
	
	public String getDate()
	{
		return this.Date;
	}
	
	public String getEvent()
	{
		return this.Event;
	}
	
	public String getGE()
	{
		return this.noOfGE;
	}
	
	public String getBE()
	{
		return this.noOfBE;
	}
	
	public int getBookmark()
	{
		return this.bookmarkVal;
	}
}
