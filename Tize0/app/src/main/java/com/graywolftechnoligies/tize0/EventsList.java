package com.graywolftechnoligies.tize0;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;


public class EventsList extends ListFragment {
    ListView listview;
    List<ParseObject> objects;
    ArrayList<Event> eventsList;
    ArrayList<String> eventsIDs;
    ArrayList<Event> tempList;
    ProgressDialog mProgressDialog;
    EventsAdapter adapter;
    EventsDataTask mTask;
    int position=0;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    private class EventsDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setTitle("Tize Events");
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
            mProgressDialog.setCancelable(false);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }

        @Override
        protected Void doInBackground(Void... params) {
            ParseUser currentUser = ParseUser.getCurrentUser();
            String currentUserObjectId = ParseUser.getCurrentUser().getObjectId();

            //Calendar currentDate=new GregorianCalendar().getInstance();
            Date currentDate = new Date();
            Calendar weekAgoDate = new GregorianCalendar().getInstance();
            weekAgoDate.add(Calendar.DAY_OF_YEAR, -7);
            //SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            eventsList = new ArrayList<>();
            eventsIDs=new ArrayList<>();
            try {
                //Query for My Past Events up to a week ago and My Events
                ParseQuery queryMyEvents = ParseQuery.getQuery("Event");
                queryMyEvents.whereEqualTo("hostUser", currentUser);
                //queryMyEvents.whereGreaterThanOrEqualTo("endDate", weekAgoDate);

                //Query for Upcoming Events part 1
                ParseQuery<ParseObject> queryEventUsers = ParseQuery.getQuery("EventUsers");
                queryEventUsers.whereEqualTo("userID", currentUserObjectId);

                //Query for Upcoming Events part 2 - events that haven't ended yet in Events Class
                ParseQuery<ParseObject> queryEventsInvitedTo = ParseQuery.getQuery("Event");
                queryEventsInvitedTo.whereMatchesKeyInQuery("objectId", "eventID", queryEventUsers);
                queryEventsInvitedTo.whereGreaterThanOrEqualTo("endDate", currentDate);

                List<ParseQuery<ParseObject>> allQueries = new ArrayList<>();
                allQueries.add(queryMyEvents);
                allQueries.add(queryEventsInvitedTo);

                ParseQuery<ParseObject> mainQuery = ParseQuery.or(allQueries);

                objects = mainQuery.find();
                Log.d("Update", Integer.toString(objects.size()));
                for (ParseObject parseEvent : objects) {
                    Event event = new Event();
                    event.setEventName((String) parseEvent.get("eventName"));
                    event.setEventID((String) parseEvent.get("objectId"));
                    eventsIDs.add((String) parseEvent.get("objectId"));
                    event.setHost((String) parseEvent.get("host"));
                    event.setHostName((String) parseEvent.get("hostName"));
                    event.setHostUser((ParseUser) parseEvent.get("hostUser"));
                    event.setLocationName((String) parseEvent.get("locationName"));
                    event.setLocation((ParseGeoPoint) parseEvent.get("location"));
                    event.setIcon((String) parseEvent.get("icon"));
                    event.setStartDate((Date) parseEvent.get("startDate"));
                    event.setEndDate((Date) parseEvent.get("endDate"));
                    event.setEventDetails((String) parseEvent.get("eventDetails"));
                    eventsList.add(event);
                }
            } catch (ParseException e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void v){
            GuestList.GuestsDataTask tempTask=new GuestList().new GuestsDataTask();
            tempTask.execute(eventsIDs);
            listview=(ListView) getView().findViewById(android.R.id.list);
            adapter = new EventsAdapter(getActivity(), eventsList);
            listview.setAdapter(adapter);
            mProgressDialog.dismiss();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_events_list, container, false);
        mTask = new EventsDataTask();
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
