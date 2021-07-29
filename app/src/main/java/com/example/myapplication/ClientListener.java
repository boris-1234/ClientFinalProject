package com.example.myapplication;

import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.Callable;

public class ClientListener implements Callable<Object> {

    private int index;
    private String command;
    private String result;
    private String id;

    public ClientListener(String command) {
        this.command = command;
    }

    public void setIndex(int index) {
        this.index = index;

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCommand(String command) {
        this.command = command;
    }
    //    public  void  listen(int index) throws IOException {
//
//    }



    @Override
    public Object call() throws Exception {
        try {
            Socket socket =new Socket();

            socket.connect(new InetSocketAddress("10.0.2.2",8010),69999);


            Log.e("DEBUG-BORIS","client: Created Socket");

            ObjectOutputStream toServer=new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream fromServer=new ObjectInputStream(socket.getInputStream());


                toServer.writeObject(command);
                switch (command)
                {
                    case "p"://play
                        toServer.writeObject(id);//send id of user to server
                        toServer.writeObject(index);//send where to put the plyer sign ( x or o)
                        break;
                    case "r"://read from server the board and who wins
                        int[] resultObject = new int[9];
                        String debug = "";
                        for(int i=0;i<9;i++)
                        {
                            String s = (String)fromServer.readObject().toString(); //read index and value from server by the form "index,value" value can be 0, 1,-1, and index can be 0 - 8 include
                            int index = Integer.parseInt(s.split(",")[0]);//the first number is index
                            int value = Integer.parseInt(s.split(",")[1]);//the second number is the value
                            resultObject[index] = value;//save the value in index
                            debug+= "("+i+")+"+ resultObject[index]+",";//debug
                        }
                        Integer winner = (int)fromServer.readObject();//read the winner from the server -          2-None, -1 , tiko, 0 - x , 1 -circle

                        if(winner == 1 || winner == 0)//if someone sin return the id of the player
                         return winner;

                            Log.d("6767", debug);

                         return resultObject; // return the winner


                }
                result = fromServer.readObject().toString();//return X when can put X, return Y when can put Y and return '-' when can't play because turn or invalid place
            Log.d("DEBUG 64", "call: "+ result);


           // Log.e("DEBUG-BORIS","client: Close all streams");
        fromServer.close();
        toServer.close();
        socket.close();
         //   Log.e("DEBUG-BORIS","client: Closed operational socket");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;// return the array in size 9 of board
    }
}


