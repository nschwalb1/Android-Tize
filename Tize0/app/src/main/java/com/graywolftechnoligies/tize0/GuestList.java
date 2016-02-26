package com.graywolftechnoligies.tize0;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashSet;
import java.util.List;


public class GuestList extends ListFragment {
    ListView listview;
    List<ParseObject> objects;
    List<ParseUser> queriedUsers;
    ArrayList<Guests> guestList;
    ArrayList<String> eventsIDs;
    ArrayList<String> guestIDs;
    ArrayList<String> guestUsernames;
    ArrayList<Integer> guestAttendingStatus;
    ArrayList<String> masterGuestIDs;
    ArrayList<String> masterUsernames;
    GuestsAdapter adapter;
    GuestsDataTask mTask;
    int position=0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public class GuestsDataTask extends AsyncTask<ArrayList<String>, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(ArrayList<String>... tempList) {
            eventsIDs=tempList[0];
            guestList = new ArrayList<>();
            guestIDs=new ArrayList<>();
            guestAttendingStatus=new ArrayList<>();
            masterGuestIDs=new ArrayList<>();
            try {
                //Query for guests to each event
                ParseQuery queryEventGuests = ParseQuery.getQuery("EventUsers");
                queryEventGuests.whereContainedIn("eventID", Arrays.asList(tempList));

                objects = queryEventGuests.find();
                Log.d("Update", Integer.toString(objects.size()));
                for(int i=0;i<eventsIDs.size();i++){
                    Guests guests = new Guests();
                    guests.setEvent(eventsIDs.get(i));
                    for (ParseObject parseGuests : objects) {
                        if (eventsIDs.get(i).equals(parseGuests.get("eventID"))){
                            guestIDs.add((String) parseGuests.get("userID"));
                            masterGuestIDs.add((String) parseGuests.get("userID"));
                            guestAttendingStatus.add((Integer) parseGuests.get("attendingStatus"));
                        }
                    }
                    guests.setUserIDs(guestIDs);
                    guests.setGuestAttendingStatus(guestAttendingStatus);
                    guestList.add(guests);
                    guestIDs.clear();
                    guestAttendingStatus.clear();
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void v){
            new iDsToUsernamesDataTask().execute();
        }

    }
    public class iDsToUsernamesDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            masterGuestIDs=new ArrayList<String>(new LinkedHashSet<String>(masterGuestIDs));
            guestUsernames=new ArrayList<>();
            try {
                //Query for guests to each event
                ParseQuery queryUsernames = ParseQuery.getQuery("User");
                queryUsernames.whereContainedIn("objectId",Arrays.asList(masterGuestIDs));

                queriedUsers = queryUsernames.find();
                Log.d("Update", Integer.toString(queriedUsers.size()));
                for(int j=0;j<guestList.size();j++){
                    for(int k=0;k<guestList.get(j).getUserIDs().size();k++){
                        for(ParseObject user : queriedUsers){
                            if(user.get("objectId").equals(guestList.get(j).getUserIDs().get(k))){
                                guestUsernames.add((String) user.get("username"));
                            }
                        }
                    }
                    guestList.get(j).setGuestUsernames(guestUsernames);
                    guestUsernames.clear();
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void v){
            listview=(ListView) getView().findViewById(android.R.id.list);
            adapter = new GuestsAdapter(getActivity(), guestList.get(0));
            listview.setAdapter(adapter);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_events_list, container, false);
        mTask = new GuestsDataTask();
        mTask.execute();

        return v;
    }

    public static EventsList newInstance() {
        EventsList eList = new EventsList();
        return eList;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        inflater.inflate(R.menu.menu_main, menu);
    }

}
