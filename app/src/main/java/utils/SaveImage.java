package utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.widget.Toast;

import org.dev4u.hv.my_diagnostic.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by admin on 16/5/17.
 */

public class SaveImage extends AsyncTask<Bitmap, Void, Boolean> {
    String path;
    String name;
    Context context;
    FloatingActionButton lockButton;
    public SaveImage(Context context, String path, String name,FloatingActionButton lockButton){
        this.context    = context;
        this.path       = path;
        this.name       = name;
        this.lockButton = lockButton;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if(lockButton!=null) lockButton.setEnabled(false);
        Toast.makeText(context, R.string.saving_the_image,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        if(lockButton!=null) lockButton.setEnabled(true);
        Toast.makeText(context, R.string.image_saved_successfully,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected Boolean doInBackground(Bitmap... params) {
        Boolean saved=false;
        for (Bitmap img : params) {
            saved = saveToInternalStorage(img,path,name);
        }
        return saved;
    }

    private Boolean saveToInternalStorage(Bitmap bitmapImage, String Path, String name){
        Boolean saved=false;
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir(Path, Context.MODE_PRIVATE);
        FileOutputStream fos1 = null;
        FileOutputStream fos2 = null;

        Bitmap bitmapPreview = Bitmap.createScaledBitmap (bitmapImage,(int) (bitmapImage.getWidth() * .4), (int) (bitmapImage.getHeight() * .4),true);
        try {
            fos1 = new FileOutputStream(new File(directory,name));
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos1);
            fos2 = new FileOutputStream(new File(directory,"preview_"+name));
            bitmapPreview.compress(Bitmap.CompressFormat.PNG,100,fos2);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos1.close();
                fos2.close();
                saved=true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return saved;
    }
}
