package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class ForecastFragment extends Fragment {

    ArrayAdapter<String> mForecastAdapter;

    public ForecastFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Add this line in order for this fragment to handle menu events.
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(com.example.android.sunshine.app.R.menu.forecastfragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == com.example.android.sunshine.app.R.id.action_refresh) {
            updateWeather();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // ArrayAdapter will take the data from the weekForecast list and use it
        // to populate the ListView it's attached to.
        mForecastAdapter =
                new ArrayAdapter<String>(getActivity(),
                        com.example.android.sunshine.app.R.layout.list_item_forecast,
                        com.example.android.sunshine.app.R.id.list_item_forecast_textview,
                        new ArrayList<String>());

        View rootView = inflater.inflate(com.example.android.sunshine.app.R.layout.fragment_main, container, false);

        // Attach the forecast adapter to the ListView
        ListView listView = (ListView) rootView.findViewById(com.example.android.sunshine.app.R.id.listview_forecast);
        listView.setAdapter(mForecastAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String forecast = mForecastAdapter.getItem(i);
                Intent intent = new Intent(getActivity(), DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, forecast);
                startActivity(intent);
            }
        });

        return rootView;
    }

    private void updateWeather() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(com.example.android.sunshine.app.R.string.pref_location_key),
                getString(com.example.android.sunshine.app.R.string.pref_location_default));
        FetchWeatherTask weatherTask = new FetchWeatherTask(getActivity(), mForecastAdapter);
        weatherTask.execute(location);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateWeather();
    }
}
