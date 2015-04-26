package com.piotr.mojakamerka;

import java.io.File;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
/*
import android.view.Menu;
import android.view.MenuItem;
*/

public class Kamerka extends Activity {

	private static String logtag = "Kamerka";
    private static int TAKE_PICTURE = 1;
    private Uri imageUri;
	
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kamerka);
        
        Button przyciskKamerki = (Button) findViewById(R.id.przycisk_kamerki);
        przyciskKamerki.setOnClickListener(cameraListener);
    }
    
    private OnClickListener cameraListener = new OnClickListener()
    {
    	public void onClick(View v)
    	{
    		takePhoto(v);
    	}
    };
    
    private void takePhoto(View v)
    {
    	Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
    	File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "picture.jpg");
    	imageUri = Uri.fromFile(photo);
    	intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
    	startActivityForResult(intent, TAKE_PICTURE);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
    	super.onActivityResult(requestCode, resultCode, intent);
    	
    	if(resultCode == Activity.RESULT_OK)
    	{
    		Uri selectedImage = imageUri;
    		getContentResolver().notifyChange(selectedImage, null);
    		
    		ImageView imageView = (ImageView) findViewById(R.id.obraz_kamerki);
    		ContentResolver cr = getContentResolver();
    		Bitmap bitmap;
    		
    		try
    		{
    			bitmap = MediaStore.Images.Media.getBitmap(cr, selectedImage);
    			imageView.setImageBitmap(bitmap);
    			Toast.makeText(Kamerka.this, selectedImage.toString(), Toast.LENGTH_LONG).show();
    		}
    		catch(Exception e)
    		{
    			Log.e(logtag, e.toString());
    		}
    		
    	}
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.kamerka, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/
}
