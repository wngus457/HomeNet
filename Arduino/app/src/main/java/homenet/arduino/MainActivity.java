package homenet.arduino;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_ENABLE_BT = 1;
    private static final int REQUEST_CONECT = 2;

    ConnectedThread connectedThread;                   //연결 스레드 변수 선언
    BluetoothAdapter mBluetoothAdapter;                //블루투스 어뎁터 변수 선언
    BluetoothDevice mBluetoothdevice = null;
    BluetoothSocket mBluetoothSocket = null;
    Button bluetoothbtn;
    ToggleButton btnled1, btnled2, btnled3;
    boolean bluetoothcon = false;
    private static String MAC = null;
    UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TabHost tab = (TabHost)findViewById(R.id.tabHost);               //탭호스트 사용
        tab.setup();
        TabHost.TabSpec spec1 = tab.newTabSpec("HOME");
        spec1.setIndicator("블루투스");
        spec1.setContent(R.id.contentmain);
        tab.addTab(spec1);

        TabHost.TabSpec spec2 = tab.newTabSpec("LAMP");
        spec2.setIndicator("전등");
        spec2.setContent(R.id.contentlamp);
        tab.addTab(spec2);

        bluetoothbtn = (Button)findViewById(R.id.btn_bluetooth);           //블루투스 버튼 선언
        btnled1 = (ToggleButton) findViewById(R.id.btnled1);                //led1번 버튼
        btnled2 = (ToggleButton) findViewById(R.id.btnled2);                //led2번 버튼
        btnled3 = (ToggleButton) findViewById(R.id.btnled3);                //led3번 버튼


        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
                // 장치가 블루투스 지원하지 않는 경우
                Toast.makeText(getApplicationContext(),"기기가 블루투스를 사용할 수 없습니다",Toast.LENGTH_LONG).show();
            } else if (!mBluetoothAdapter.isEnabled()) {
            // 장치가 블루투스 지원하는 경우
            // 블루투스를 지원하지만 비활성 상태인 경우
            // 블루투스를 활성 상태로 바꾸기 위해 사용자 동의 요첨
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        }

        bluetoothbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(bluetoothcon){
                    //장치연결없음
                    try{
                        mBluetoothSocket.close();
                        bluetoothcon = false;
                        Toast.makeText(getApplicationContext(), "블루투스 연결이 끊어졌습니다.", Toast.LENGTH_LONG).show();
                    }catch (IOException erro){
                        Toast.makeText(getApplicationContext(), "오류가 발생했습니다. :" + erro, Toast.LENGTH_LONG).show();

                    }
                }else {
                    //연결할장치목록
                    Intent conintent = new Intent(MainActivity.this, BluetoothService.class);
                    startActivityForResult(conintent, REQUEST_CONECT);
                }

            }
        });

        btnled1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnled1.isChecked()){
                    btnled1.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.bathon)
                    );
                    if(bluetoothcon){
                        connectedThread.enviar("led1");

                    }else{
                        Toast.makeText(getApplicationContext(), "블루투스가 꺼져있습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                else{
                    btnled1.setBackgroundDrawable(
                            getResources().
                            getDrawable(R.drawable.bathoff)
                    );
                    if(bluetoothcon){
                        connectedThread.enviar("led1");

                    }else{
                        Toast.makeText(getApplicationContext(), "블루투스가 꺼져있습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
            }
        });
        btnled2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnled2.isChecked()){
                    btnled2.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.livingon)
                    );
                    if(bluetoothcon){
                        connectedThread.enviar("led2");

                    }else{
                        Toast.makeText(getApplicationContext(), "블루투스가 꺼져있습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                else{
                    btnled2.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.livingoff)
                    );
                    if(bluetoothcon){
                        connectedThread.enviar("led2");

                    }else{
                        Toast.makeText(getApplicationContext(), "블루투스가 꺼져있습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
            }
        });
        btnled3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnled3.isChecked()){
                    btnled3.setBackgroundDrawable(
                            getResources().getDrawable(R.drawable.kitchenon)
                    );
                    if(bluetoothcon){
                        connectedThread.enviar("led3");

                    }else{
                        Toast.makeText(getApplicationContext(), "블루투스가 꺼져있습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
                else{
                    btnled3.setBackgroundDrawable(
                            getResources().
                                    getDrawable(R.drawable.kitchenoff)
                    );
                    if(bluetoothcon){
                        connectedThread.enviar("led3");

                    }else{
                        Toast.makeText(getApplicationContext(), "블루투스가 꺼져있습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }


                }
            }
        });

    }
        @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            switch (requestCode) {
                case REQUEST_ENABLE_BT:
                    if (resultCode == RESULT_OK) {
                        // 블루투스가 활성 상태로 변경됨
                        Toast.makeText(getApplicationContext(), "블루투스가 활성되었습니다.", Toast.LENGTH_LONG).show();
                    } else {
                        // 블루투스가 비활성 상태임
                        Toast.makeText(getApplicationContext(), "블루투스가 비활성되어 기능을 사용할 수 없습니다.", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
                case REQUEST_CONECT:
                    if (resultCode == Activity.RESULT_OK) {
                        MAC = data.getExtras().getString(BluetoothService.ENDERECO_MAC);
                        //Toast.makeText(getApplicationContext(),"MAC FINAL : " + MAC, Toast.LENGTH_LONG).show();
                        mBluetoothdevice = mBluetoothAdapter.getRemoteDevice(MAC);
                        try {
                            mBluetoothSocket = mBluetoothdevice.createRfcommSocketToServiceRecord(MEU_UUID);

                            mBluetoothSocket.connect();

                            bluetoothcon = true;

                            connectedThread = new ConnectedThread(mBluetoothSocket);
                            connectedThread.start();
                            Toast.makeText(getApplicationContext(), "장치연결이 되었습니다. : " + MAC, Toast.LENGTH_LONG).show();
                        } catch (IOException erro) {
                            bluetoothcon = false;
                            Toast.makeText(getApplicationContext(), "오류가 발생했습니다. : " + erro, Toast.LENGTH_LONG).show();

                        }


                    } else {
                        Toast.makeText(getApplicationContext(), "맥주소를 얻지 못했습니다.", Toast.LENGTH_LONG).show();

                    }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }

    private class ConnectedThread extends Thread {
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private int MESSAGE_READ;

        public ConnectedThread(BluetoothSocket socket) {
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) { }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            String humi, temp;

            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    int byteAvailable = mmInStream.available();

                            // Read from the InputStream

                    bytes = mmInStream.read(buffer);
                    // Send the obtained bytes to the UI activity
                    String mbuffer = new String(buffer, 0, bytes);
                    //mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                        //    .sendToTarget();

                } catch (IOException e) {
                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void enviar(String dadosenviar) {
            byte[] mbuffer = dadosenviar.getBytes();
            try {
                mmOutStream.write(mbuffer);
            } catch (IOException e) {

            }
        }

    }

}
