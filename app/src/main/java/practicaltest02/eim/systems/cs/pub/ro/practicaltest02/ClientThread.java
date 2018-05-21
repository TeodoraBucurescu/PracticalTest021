package practicaltest02.eim.systems.cs.pub.ro.practicaltest02;

import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by student on 21.05.2018.
 */

public class ClientThread extends Thread {

    protected String adresa;
    protected int port;
    protected String ora;
    protected String minut;
    protected String comanda;

    protected TextView infoAfisare;
    protected Socket socket;



    public ClientThread(String address, int port, String comanda, String ora , String minut, TextView infoAfisare) {
        this.adresa = address;
        this.port = port;
        this.comanda = comanda;
        this.ora = ora;
        this.minut = minut;
        this.infoAfisare = infoAfisare;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(adresa, port);
            if (socket == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Could not create socket!");
                return;
            }

            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);
            if (bufferedReader == null || printWriter == null) {
                Log.e(Constants.TAG, "[CLIENT THREAD] Buffered Reader / Print Writer are null!");
                return;
            }

            Log.e(Constants.TAG, "[CLIENt THREAD] Comanda: " + comanda);

            printWriter.println(comanda);
            printWriter.flush();
            printWriter.println(ora);
            printWriter.flush();
            printWriter.println(minut);
            printWriter.flush();
            String oraMinut;

            while ((oraMinut = bufferedReader.readLine()) != null) {
                final String finalizedOraMinut = oraMinut;
                infoAfisare.post(new Runnable() {
                    @Override
                    public void run() {
                        infoAfisare.setText(finalizedOraMinut);
                    }
                });
            }
        } catch (IOException ioException) {
            Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
            if (Constants.DEBUG) {
                ioException.printStackTrace();
            }
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ioException) {
                    Log.e(Constants.TAG, "[CLIENT THREAD] An exception has occurred: " + ioException.getMessage());
                    if (Constants.DEBUG) {
                        ioException.printStackTrace();
                    }
                }
            }
        }
    }
}
