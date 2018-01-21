package homenet.arduino;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.Set;

/**
 * Created by 박주현 on 2017-09-13.
 */

public class BluetoothService extends ListActivity {
    private BluetoothAdapter mBluetoothAdapter2 = null;
    static String ENDERECO_MAC = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mBluetoothAdapter2 = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> pairedDevices = mBluetoothAdapter2.getBondedDevices();
        if(pairedDevices.size() > 0){
            for(BluetoothDevice dispositivo : pairedDevices){
                String nameBT = dispositivo.getName();
                String macBT = dispositivo.getAddress();
                ArrayBluetooth.add(nameBT + "\n" + macBT);
            }
        }
        setListAdapter(ArrayBluetooth);

    }
    @Override
    protected void onListItemClick(ListView l, View v,int position, long id){
        super.onListItemClick(l, v, position, id);

        String informacao = ((TextView) v).getText().toString();

       // Toast.makeText(getApplicationContext(), "Info: " + informacao, Toast.LENGTH_LONG).show();
        String endereMac = informacao.substring(informacao.length() - 17);
        //Toast.makeText(getApplicationContext(), "mac : " + endereMac, Toast.LENGTH_LONG).show();
        Intent retorngMac = new Intent();
        retorngMac.putExtra(ENDERECO_MAC, endereMac);
        setResult(RESULT_OK, retorngMac);
        finish();

    }

}



