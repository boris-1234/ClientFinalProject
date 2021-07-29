package com.example.myapplication;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Activity_Game extends AppCompatActivity {

    private GridView game_GRIDVIEW_gameBoard;
    private int contentOfBoard[];
    private CustomBoardAdapter customBoardAdapter;
    private String clientId;
    private Handler handler;
    private HandlerThread handlerThread;
    private Runnable runnable;
    int delay = 5000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        initContentOfBoard();
        findViews();
        getClientId();
        try {
            initViews();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Create an object of CustomAdapter and set Adapter to GirdView


    }

    private void getClientId() {
        ClientListener c=new ClientListener("i");//get id
        FutureTask<Object> futureTask = new FutureTask<>(c);
        Thread thread = new Thread(futureTask);
        thread.start();
        try {
            clientId =  futureTask.get().toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void initContentOfBoard() {
        contentOfBoard = new int[9];
        for (int i=0;i<contentOfBoard.length;i++)
        {
            contentOfBoard[i] = R.drawable.e;
        }
    }

    private void initViews() throws ExecutionException, InterruptedException {
        customBoardAdapter = new CustomBoardAdapter(getApplicationContext(), contentOfBoard,clientId);

        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);




        game_GRIDVIEW_gameBoard.setAdapter(customBoardAdapter);

        handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, delay);
                Toast.makeText(Activity_Game.this, "This method is run every 5 seconds",
                        Toast.LENGTH_SHORT).show();
                Log.d("DEBUG 78" ,"run: ");
                ClientListener c=new ClientListener("r");//read

                FutureTask<Object>  futureTask = new FutureTask<>(c);
                Thread thread = new Thread(futureTask);
                thread.start();
                try {
                    Object result  = futureTask.get();
                    int theWinner = 2;
                    if(result instanceof  Integer)
                    {
                        theWinner = (int)result;
                        String theWinnerStr="";
                        if(theWinner ==1)
                        {
                            theWinnerStr = "O";
                        }
                        if(theWinner == 0)
                        {
                            theWinnerStr = "X";
                        }
                        final String theWinnerStrFinal =  theWinnerStr;
                        if(theWinner==0 || theWinner==1) {
                            handler.removeCallbacks(this);
                            runOnUiThread(new Runnable() {

                                @Override
                                public void run() {

                                    new AlertDialog.Builder(Activity_Game.this)
                                            .setTitle("the winner is:")
                                            .setMessage("The winner is " + theWinnerStrFinal)

                                            // Specifying a listener allows you to take an action before dismissing the dialog.
                                            // The dialog is automatically dismissed when a dialog button is clicked.
                                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int which) {
                                                    // Continue with delete operation
                                                }
                                            })

                                            // A null listener allows the button to dismiss the dialog and take no further action.
                                            .setNegativeButton(android.R.string.no, null)
                                            .setIcon(android.R.drawable.ic_dialog_alert)
                                            .show();

                                }
                            });

                        }
                    }
                    else
                    {
                        int[] arr = (int[])result;

                    if(result ==null )
                        return;
                    String debug = "";
                    for (int i=0;i<arr.length;i++)
                    {

                        debug+= "("+i+")+"+ arr[i]+",";
                    }
                    Log.e("6767",  debug);
                    for(int i =0;i<arr.length;i++) {
                        if (arr[i] == 0) {
                            contentOfBoard[i] = R.drawable.xsymbol;
                        }
                        if (arr[i] == 1) {
                            contentOfBoard[i] = R.drawable.osymbol;
                        }
                        if (arr[i] == -1) {
                            contentOfBoard[i] = R.drawable.e;
                        }
                        Log.d("DEBUG 95", "run: " + contentOfBoard[i]);
                    }

                    }
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {

                            customBoardAdapter.notifyDataSetChanged();

                        }
                    });
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }, delay);





//        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
//            public void run() {
//
//            }
//        }, 0, 5, TimeUnit.SECONDS);

    }


    private void findViews() {
        game_GRIDVIEW_gameBoard = (GridView) findViewById(R.id.game_GRIDVIEW_gameBoard); // init GridView
    }
}
