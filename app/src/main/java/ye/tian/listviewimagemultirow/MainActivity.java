package ye.tian.listviewimagemultirow;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    private static final int RESULT_SETTINGS = 1, REQUEST_ENABLE_BT = 2;
    private BluetoothAdapter btAdapter;
    MyAdapter myAdapter;
    ArrayList<SearchResults> searchResults;
    private ArrayList<String> macList;
    private TextView textView1;
    private TextView textView2;
    private Intent filterIntent;

    private ScanCallback newScanCallback = new ScanCallback() {
        @Override
        public void onScanResult(int callbackType, ScanResult result) {
            super.onScanResult(callbackType, result);
            // do something
            SearchResults srNew = new SearchResults();
            srNew.setName(result.getDevice().getName());
            srNew.setCityState(result.getDevice().getAddress());
            srNew.setPhone(Integer.toString(result.getRssi()));
            srNew.setImageNumber(4);

            if (!macList.contains(result.getDevice().getAddress())) {
                macList.add(result.getDevice().getAddress());
                addItems(srNew);
            } else {
                resItems(macList.indexOf(result.getDevice().getAddress()), srNew);
            }
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

    private void resItems(int ind, SearchResults newSR) {
        synchronized (searchResults) {
            searchResults.set(ind, newSR);
            myAdapter.notifyDataSetChanged();
        }
    }

    private void remItems(int ind) {
        synchronized (searchResults) {
            searchResults.remove(ind);
            myAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView1 = (TextView) this.findViewById(R.id.txt1);
        textView2 = (TextView) this.findViewById(R.id.txt2);

        macList = new ArrayList<>();
        searchResults = new ArrayList<>();
        myAdapter = new MyAdapter(this, searchResults);

        final ListView listView = (ListView) findViewById(R.id.my_list_view);
        listView.setAdapter(myAdapter);

        filterIntent = new Intent(this, FilterActivity.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                Object o = listView.getItemAtPosition(position);
                SearchResults fullObject = (SearchResults) o;
//                Toast.makeText(MainActivity.this, "You have chosen: " + " " + fullObject.getName(), Toast.LENGTH_LONG).show();
                filterIntent.putExtra("firstKeyName","FirstKeyValue");
                filterIntent.putExtra("secondKeyName","SecondKeyValue");
                startActivity(filterIntent);
            }
        });

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        final BluetoothLeScanner bleScanner = BluetoothAdapter.getDefaultAdapter().getBluetoothLeScanner();

        checkBLE();
        if (enableBLE()) {
            textView2.setText(getResources().getString(R.string.ble_state) + "On");
        } else {
            textView2.setText(getResources().getString(R.string.ble_state) + "Off");
        }

        ArrayList<ScanFilter> scanFilters = new ArrayList<>();
        ScanSettings scanSettings;
        ScanSettings.Builder scanSettingsBuilder = new ScanSettings.Builder();
        scanSettingsBuilder.setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY);
        scanSettings = scanSettingsBuilder.build();

        bleScanner.startScan(scanFilters, scanSettings, newScanCallback);
    }

    private void checkBLE() {
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private boolean enableBLE() {
        boolean ret = true;
        if (btAdapter == null || !btAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            ret = false;
        }
        return ret;
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
