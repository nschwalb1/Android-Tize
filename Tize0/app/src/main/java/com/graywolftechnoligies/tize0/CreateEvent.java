package com.graywolftechnoligies.tize0;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;


public class CreateEvent extends Fragment {
    FloatingActionButton fab;
    Button inviteFriendsButton;

    EditText eventName;
    EditText eventLocation;
    EditText eventAbout;

    TextView eventStartTime;
    TextView eventEndTime;
    TextView eventStartDate;
    TextView eventEndDate;

    String eventNameTxt;
    String eventLocationTxt;
    String eventAboutTxt;
    String eventIconTxt;

    ArrayList<String> userList;
    ArrayList<String> invitedList;
    List<ParseUser> objects;

    AlertDialog alertDialogInvite;
    AlertDialog.Builder builder;

    MyPagerAdapter mViewPager;
    onDataPass dataPasser;

    public static CreateEvent newInstance() {
        CreateEvent fragment = new CreateEvent();
        return fragment;
    }

    public CreateEvent() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        new PullFriendsTask().execute();
        invitedList=new ArrayList<>();

        inviteFriendsButton=(Button) view.findViewById(R.id.InviteFriendsButton);

        ////Creating Event Handler////
        eventName=(EditText) view.findViewById(R.id.EventNameInput);
        eventLocation=(EditText) view.findViewById(R.id.EventNameInput);
        eventAbout=(EditText) view.findViewById(R.id.EventNameInput);
        //eventStart=(EditText) view.findViewById(R.id.StartTimeInput);
        //eventEnd=(EditText) view.findViewById(R.id.EndTimeInput);
        eventIconTxt="no id";


        ////Invite Friends Button Handler////
        inviteFriendsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.InviteFriendsButton:
                        inviteFriendsPopUp();
                        break;
                }
            }
        });
        ////End Invite Friends Button Handler////
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        menu.clear();
        inflater.inflate(R.menu.menu_create_event, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.saveEvent:
                saveEvent();
                dataPasser.onDataPass(1);
                return true;
            case R.id.cancelEvent:
                dataPasser.onDataPass(1);
                return true;
            default:
                return false;
        }
    }
    public interface onDataPass{
        public void onDataPass(int page);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        Activity a =getActivity();
        dataPasser = (onDataPass) a;
    }

    public void saveEvent(){
        final ParseUser currentUser = ParseUser.getCurrentUser();
        final String host=currentUser.getObjectId();
        final String hostName=currentUser.getUsername();

        //Convert the editText var to String
        eventNameTxt=eventName.getText().toString();
        eventLocationTxt=eventLocation.getText().toString();
        eventAboutTxt=eventAbout.getText().toString();


        //If no Icon Radio Button is selected, default to tize Icon
        if (eventIconTxt.equals("no id")){
            eventIconTxt="tize";
        }
        //Check all fields for input
        //TODO: fix this check after dates are fixed
        if (eventNameTxt=="" || eventLocationTxt==""|| eventStartDate==null || eventEndDate==null){
            Toast.makeText(getActivity(), "Please enter all of the necessary information", Toast.LENGTH_LONG).show();
            return;
        }
        else{
            //Push Event information to Parse
            ParseObject newEvent=new ParseObject("Event");
            newEvent.put("eventName",eventNameTxt);
            newEvent.put("host",host);
            newEvent.put("hostName",hostName);
            newEvent.put("hostUser",currentUser);
            newEvent.put("locationName",eventLocationTxt);
            newEvent.put("icon",eventIconTxt);
            //newEvent.put("startDate",eventStartDate);
            //newEvent.put("endDate",eventEndDate);
            newEvent.put("eventDetails", eventAboutTxt);
            newEvent.saveInBackground();
        }
    }

    public void inviteFriendsPopUp(){
        FriendsAdapter adapter=new FriendsAdapter(getActivity(), R.layout.friend_list_item,userList);
        ListView listViewFriends=new ListView(getActivity());
        listViewFriends.setAdapter(adapter);
        builder = new AlertDialog.Builder(getActivity());
        builder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if(!invitedList.isEmpty()){
                    invitedList.clear();
                }
            }
        });
        builder.setView(listViewFriends);
        builder.setTitle("Friends");
        builder.show();
    }

    public void onUserInviteListener(View view){
        TextView usernameInvited = ((TextView) view.findViewById(R.id.usernameListItem));
        String usernameInvitedText = usernameInvited.getText().toString();
        String usernameInvitedId = usernameInvited.getTag().toString();
        Log.d("invitedList Length: ", Integer.toString(invitedList.size()));
        if (!invitedList.isEmpty()) {
            if(invitedList.contains(usernameInvitedText)){
                invitedList.remove(usernameInvitedText);
                usernameInvited.setTypeface(null, Typeface.NORMAL);
                Log.d("Friend UNinvited: ", usernameInvitedText);
            }
            else{
                invitedList.add(usernameInvitedText);
                usernameInvited.setTypeface(null, Typeface.BOLD);
                Log.d("Friend invited: ", usernameInvitedText);
            }
        }
        else{
            invitedList.add(usernameInvitedText);
            usernameInvited.setTypeface(null, Typeface.BOLD);
            Log.d("Friend invited: ", usernameInvitedText);
        }
    }

    public void iconRadioButtonClicked(View v){
        RadioGroup radioGroup=(RadioGroup) getActivity().findViewById(R.id.iconRadioGroup);
        int idInt=radioGroup.getCheckedRadioButtonId();
        Log.d("idInt",Integer.toString(idInt));
        String idString="no id";
        if(idInt !=View.NO_ID) {
            Resources res = v.getResources();
            if (res != null) {
                idString = res.getResourceEntryName(idInt);
            }
        }
        eventIconTxt=idString.replace("IconButton","");
        Log.d("eventIconTxt",eventIconTxt);
    }

    private class PullFriendsTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params){
            ParseUser currentUser=ParseUser.getCurrentUser();
            String currentUserObjectId = ParseUser.getCurrentUser().getObjectId();
            Log.d("Current User ID: ",currentUserObjectId);
            userList=new ArrayList<>();
            try{
                ParseQuery<ParseObject> queryMyFollowers=ParseQuery.getQuery("Following");
                queryMyFollowers.whereEqualTo("user", currentUserObjectId);

                ParseQuery<ParseUser> queryUsers=ParseUser.getQuery();
                queryUsers.whereMatchesKeyInQuery("objectId", "following", queryMyFollowers);

                objects=queryUsers.find();
                for(ParseUser user:objects){
                    userList.add(user.getUsername());
                }

            }
            catch (ParseException e){
                Log.e("Error",e.getMessage());
                e.printStackTrace();
            }
            Log.d("Friends count: ",Integer.toString(userList.size()));
            return null;
        }
    }

/*    @Override
    public void onDialogPositiveClick(DialogFragment dialog){
        //TODO: Add what happens when user accepts selected date
    }
    @Override
    public void onDialogNegativeClick(DialogFragment dialog){
        //TODO: Add what happens when user cancels date picker
    }*/

}


