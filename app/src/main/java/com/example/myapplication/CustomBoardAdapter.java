package com.example.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CustomBoardAdapter extends BaseAdapter {
    Context context;
    int photos[];
    LayoutInflater inflter;
    private String clientId;
    public CustomBoardAdapter(Context applicationContext, int[] photos,String clientId) {
        this.context = applicationContext;
        this.photos = photos;
        inflter = (LayoutInflater.from(applicationContext));
        this.clientId = clientId;


    }
    @Override
    public int getCount() {
        return photos.length;
    }
    @Override
    public Object getItem(int i) {
        return null;
    }
    @Override
    public long getItemId(int i) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(i==9)
            return view;
        view = inflter.inflate(R.layout.grid_item, null); // inflate the layout
        ImageView photo = (ImageView) view.findViewById(R.id.gridItem_Img_image); // get the reference of ImageView
        photo.setImageResource(photos[i]); // update photos in the board

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ImageView gridItem_Img_image =  view.findViewById(R.id.gridItem_Img_image);

        if(gridItem_Img_image!=null)
        {
            gridItem_Img_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "test click",Toast.LENGTH_LONG).show();
                    ClientListener c=new ClientListener("p");//play
                    c.setId(clientId);
                    c.setIndex(i);//play

                    FutureTask<Object>  futureTask = new FutureTask<>(c);
                    Thread thread = new Thread(futureTask);
                    thread.start();
                    try {
                        Object result  =  futureTask.get();
                        Log.d("DEBUG 66", "onClick: " + result);
                        if(result.equals("O"))
                        {
                            photos[i] = R.drawable.osymbol;
                            photo.setImageResource(photos[i]);



                            //notifyDataSetChanged();
                        }
                        if(result.equals("X"))
                        {
                            photos[i] = R.drawable.xsymbol;
                            photo.setImageResource(photos[i]);
                            //notifyDataSetChanged();
                        }
                        Log.d("DEBUG78", "xsymbol: "+R.drawable.xsymbol);
                        Log.d("DEBUG79", "osymbol: "+R.drawable.osymbol);

                        for(int i=0;i<photos.length;i++)
                        {
                            Log.d("DEBUG80", "photos["+i+"] =" + photos[i]);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            });
        }
        photo.setImageResource(photos[i]);
        return view;
    }
}