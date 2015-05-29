package ye.tian.listviewimagemultirow;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    MyAdapter myAdapter;
    ArrayList<SearchResults> searchResults;

    private ScanCallback scanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            int rssi = result.getRssi();
            // do something with RSSI value
            SearchResults srNew = new SearchResults();
            srNew.setName(result.getDevice().getName());
            srNew.setCityState(result.getDevice().getAddress());
            srNew.setPhone(Integer.toString(rssi));
            srNew.setImageNumber(4);
            addItems(srNew);
        }
        @Override
        public void onScanFailed(int errorCode) {
            super.onScanFailed(errorCode);
            // a scan error occurred
        }
    };

    private void addItems(SearchResults newSR) {
        synchronized (searchResults) {
            searchResults.add(newSR);
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchResults = GetSearchResults();
        myAdapter = new MyAdapter(this, searchResults);

        final ListView listView = (ListView) findViewById(R.id.my_list_view);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                SearchResults fullObject = (SearchResults) o;
                Toast.makeText(MainActivity.this, "You have chosen: " + " " + fullObject.getName(), Toast.LENGTH_LONG).show();
            }
        });

        final BluetoothLeScanner bleScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();
        ArrayList<ScanFilter> scanFilters = new ArrayList<>();
        ScanSettings scanSettings;
        ScanSettings.Builder scanSettingsBuilder = new ScanSettings.Builder();
        scanSettingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        scanSettings = scanSettingsBuilder.build();
        bleScanner.startScan(scanFilters, scanSettings, scanCallback);
    }

    private ArrayList<SearchResults> GetSearchResults(){
        ArrayList<SearchResults> results = new ArrayList<>();

        SearchResults sr1 = new SearchResults();
        sr1.setName("John Smith");
        sr1.setCityState("Dallas, TX");
        sr1.setPhone("214-555-1234");
        sr1.setImageNumber(1);
        results.add(sr1);

        sr1 = new SearchResults();
        sr1.setName("Jane Doe");
        sr1.setCityState("Atlanta, GA");
        sr1.setPhone("469-555-2587");
        sr1.setImageNumber(2);
        results.add(sr1);

        sr1 = new SearchResults();
        sr1.setName("Steve Young");
        sr1.setCityState("Miami, FL");
        sr1.setPhone("305-555-7895");
        sr1.setImageNumber(3);
        results.add(sr1);

        return results;
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
}
