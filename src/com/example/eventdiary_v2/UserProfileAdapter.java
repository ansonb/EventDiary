package com.example.eventdiary_v2;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.eventdiary.R;
import com.example.eventdiary_v2.UserProfileModel;

public class UserProfileAdapter extends ArrayAdapter<UserProfileModel> {
	Activity context;
	ArrayList<UserProfileModel> userArray;

	public UserProfileAdapter(Activity context,ArrayList<UserProfileModel> userArray)
	{
		super(context,R.layout.user_profile,R.id.listview,userArray);

		this.context=context;
		this.userArray=userArray;
		
		for(int i=0;i<userArray.size();i++)
		{
			//Log.d("Full userArray", userArray.get(i).getEvent());
		}

	}

	@Override
	public UserProfileModel getItem(int position)
	{
		return userArray.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ViewHolder holder;
		UserProfileModel Model =userArray.get(position);
		if(convertView == null)
		{
			LayoutInflater inflater=context.getLayoutInflater();
			convertView=inflater.inflate(R.layout.user_profile_child_view,null);

			holder = new ViewHolder();

			holder.date = (TextView) convertView.findViewById(R.id.childDate);
			holder.event = (TextView) convertView.findViewById(R.id.childEvent);
			holder.GE = (TextView) convertView.findViewById(R.id.childGE);
			holder.BE = (TextView)convertView.findViewById(R.id.childBE);
			holder.Mark = (Button) convertView.findViewById(R.id.bookmark);

			convertView.setTag(holder);
		}
		else
		{
			holder=(ViewHolder)convertView.getTag();
		}

		/*Log.v("user Array", "pos: "+position);
		Log.v("user Array", "Event: "+Model.getEvent());*/
		
		holder.date.setText(Model.getDate());
		holder.event.setText(Model.getEvent());
		holder.GE.setText(Model.getGE());
		holder.BE.setText(Model.getBE());
		if( userArray.get(position).bookmarkVal == 1)
		{
			holder.Mark.setBackgroundResource(R.drawable.bookmark_black);
		}
		else
		{
			holder.Mark.setBackgroundResource(R.drawable.bookmark_white);
		}

		return convertView;
	}

	static class ViewHolder
	{
		TextView date;
		TextView event;
		TextView GE;
		TextView BE;
		Button Mark;
	}
}
