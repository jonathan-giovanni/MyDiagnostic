package org.dev4u.hv.my_diagnostic;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URI;

/**
 * Created by toni on 07/07/2017.
 */
public class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private static final String TAG = "CustomInfoWindowAdapter";
    private LayoutInflater inflater;
    private Context mContext;
    private Bitmap userImg;
Context mycontext;
    public CustomInfoWindowAdapter(LayoutInflater inflater){
        this.inflater = inflater;
        mContext = this.inflater.getContext();
        userImg = loadImageFromStorage("Profile","preview_profile.png");
    }

    @Override
    public View getInfoContents(final Marker m) {
        //Carga layout personalizado.
        View v = inflater.inflate(R.layout.info_windows_layout, null);
        if(m.getTitle().compareTo("I am Here")==0){
            ((TextView)v.findViewById(R.id.info_window_estado)).setText("I am Here");
            if(userImg!=null)
                ((ImageView)v.findViewById(R.id.info_window_imagen)).setImageBitmap(userImg);
            else
                ((ImageView)v.findViewById(R.id.info_window_imagen)).setImageResource(R.drawable.ic_user);
        }
        else {
            String direccion = String.valueOf(m.getSnippet());
            String separada[] = direccion.split("<");
            ((TextView) v.findViewById(R.id.info_window_nombre)).setText(m.getTitle());
            ((TextView) v.findViewById(R.id.info_window_placas)).setText(separada[0]);
            ((TextView)v.findViewById(R.id.info_window_estado)).setText(" ");
        }
        return v;
    }
    //read the user image
    private Bitmap loadImageFromStorage(String path, String name)
    {
        try {
            ContextWrapper cw = new ContextWrapper(mContext);
            File directory = cw.getDir(path, Context.MODE_PRIVATE);
            File f=new File(directory, name);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public View getInfoWindow(Marker m) {
        return null;
    }

}