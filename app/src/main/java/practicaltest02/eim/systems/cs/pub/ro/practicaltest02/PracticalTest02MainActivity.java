package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PracticalTest02MainActivity extends AppCompatActivity {

    protected EditText portServer;
    protected Button connectB;

    protected EditText adresa;
    protected EditText portClient;
    protected EditText comanda;
    protected EditText ora;
    protected EditText minut;
    protected Button get;

    protected TextView infoAfisare;

    ServerThread serverThread;
    ClientThread clientThread;

    private ConnectButtonClickListener connectButtonClickListener = new ConnectButtonClickListener();
    private class ConnectButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String serverPort = portServer.getText().toString();
            if (serverPort == null || serverPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Server port should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            serverThread = new ServerThread(Integer.parseInt(serverPort));
            if (serverThread.getServerSocket() == null) {
                Log.e(Constants.TAG, "[MAIN ACTIVITY] Could not create server thread!");
                return;
            }
            serverThread.start();
        }

    }

    private GetButtonClickListener clickGet= new GetButtonClickListener();
    private class GetButtonClickListener implements Button.OnClickListener {

        @Override
        public void onClick(View view) {
            String clientAddress = adresa.getText().toString();
            String clientPort = portClient.getText().toString();
            if (clientAddress == null || clientAddress.isEmpty()
                    || clientPort == null || clientPort.isEmpty()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] Client connection parameters should be filled!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (serverThread == null || !serverThread.isAlive()) {
                Toast.makeText(getApplicationContext(), "[MAIN ACTIVITY] There is no server to connect to!", Toast.LENGTH_SHORT).show();
                return;
            }
            String comandaText = comanda.getText().toString();
            String oraText = ora.getText().toString();
            String minuteText = minut.getText().toString();


            clientThread = new ClientThread(
                    clientAddress, Integer.parseInt(clientPort), comandaText, oraText , minuteText, infoAfisare
            );
            clientThread.start();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practical_test02_main);

        portServer = (EditText)findViewById(R.id.port);
        connectB = (Button)findViewById(R.id.connect);
        connectB.setOnClickListener(connectButtonClickListener);

        adresa = (EditText)findViewById(R.id.adresa);
        portClient = (EditText)findViewById(R.id.port2);
        comanda = (EditText)findViewById(R.id.info);
        ora = (EditText)findViewById(R.id.ora);
        minut = (EditText)findViewById(R.id.minut);
        get = (Button)findViewById(R.id.get);
        get.setOnClickListener(clickGet);

        infoAfisare = (TextView)findViewById(R.id.infoAfisare);

    }

    @Override
    protected void onDestroy() {
        Log.i(Constants.TAG, "[MAIN ACTIVITY] onDestroy() callback method has been invoked");
        if (serverThread != null) {
            serverThread.stopThread();
        }
        super.onDestroy();
    }
}
