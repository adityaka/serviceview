package com.pluralsight.servicesviewapp;

import android.app.ActivityManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        AppIndex.AppIndexApi.start(client, getIndexApiAction());
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//
//        // ATTENTION: This was auto-generated to implement the App Indexing API.
//        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        AppIndex.AppIndexApi.end(client, getIndexApiAction());
//        client.disconnect();
//    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements AdapterView.OnItemClickListener {

        private ArrayAdapter<RunningServiceWrapper> mServices;
        private ListView mServiceList;

        public PlaceholderFragment() {
        }

        //@Override
        public View onCreateview(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mServices = new ArrayAdapter<RunningServiceWrapper>(getActivity(), android.R.layout.simple_list_item_1);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            mServiceList = (ListView) rootView.findViewById(R.id.service_list);
            mServiceList.setAdapter(mServices);
            mServiceList.setOnItemClickListener(this);
            return rootView;
        }

        @Override
        public void onResume() {
            super.onResume();
            Context ctx = getActivity().getApplicationContext();
            ActivityManager mgr = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);
            java.util.List<ActivityManager.RunningServiceInfo> srvInfo = mgr.getRunningServices(Integer.MAX_VALUE);
            mServices.clear();
            for (ActivityManager.RunningServiceInfo cursrv : srvInfo){
                RunningServiceWrapper wrap = new RunningServiceWrapper(cursrv);
                mServices.add(wrap);
            }
            mServices.notifyDataSetChanged();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            RunningServiceWrapper curItem = mServices.getItem(position);
            ServiceDetailFragment frag = ServiceDetailFragment.newInstance(curItem.getInfo());
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            FragmentTransaction replace = ft.replace(R.id.container, frag);
            ft.addToBackStack(frag.getClass().getSimpleName());
            ft.commit();

        }

        private class RunningServiceWrapper {
            private ActivityManager.RunningServiceInfo mInfo;

            public RunningServiceWrapper(ActivityManager.RunningServiceInfo info) {
                mInfo = info;
            }

            public ActivityManager.RunningServiceInfo getInfo() {
                return mInfo;
            }

            public String toString() {
                return mInfo.service.flattenToShortString();
            }
        }
    }
}
