package cn.timebusker.zookeeper.center.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import cn.timebusker.zookeeper.center.systemconst.CommandConstEnum;

public class SocketClient {

    private String host;
    private int port;
    private Socket socket;


    public SocketClient(String host, int port) throws UnknownHostException, IOException {
        this.host = host;
        this.port = port;
        socket = new Socket(this.host, this.port);
    }

    public void clostSocket() throws IOException {
        socket.close();
    }

    public List<String> sendCommand(CommandConstEnum command) throws IOException {
        if (command == null) {
            return null;
        }
        List<String> resultList = new ArrayList<String>();
        BufferedReader reader = null;
        try {
            OutputStream outstream = socket.getOutputStream();
            outstream.write(command.getVal().getBytes());
            outstream.flush();
            socket.shutdownOutput();
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                resultList.add(line);
            }
        } catch (Exception e) {
            this.clostSocket();
        } finally {
            if (reader != null) {
                reader.close();
            }
            socket.close();
        }
        return resultList;
    }
}
