package com.piotr.chatsms;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class PierwszaAktywnosc extends Activity
{
	// Zmienne globalne
	Button sendSMS;
	EditText numberTxt;
	EditText messageTxt;
	IntentFilter intentFilter;
	
	// Metoda transmisji otrzymania
	private BroadcastReceiver intentReceiver = new BroadcastReceiver()
	{
		@Override
		public void onReceive(Context context, Intent intent)
		{
			TextView inTxt = (TextView) findViewById(R.id.textMsg);
			inTxt.setText(intent.getExtras().getString("sms"));
		}
	};

	// Metoda tworzenia intencji
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pierwsza_aktywnosc);
    
	    intentFilter = new IntentFilter();
	    intentFilter.addAction("SMS_RECEIVED_ACTION");
	    
	    // Przyporz¹dkowanie zmiennych do obiektów
	    sendSMS = (Button) findViewById(R.id.sendBtn);
	    messageTxt = (EditText) findViewById(R.id.messageTxt);
	    numberTxt = (EditText) findViewById(R.id.numberTxt);
	    
	    sendSMS.setOnClickListener(new View.OnClickListener()
	    {
			@Override
			public void onClick(View v)
			{
				String myMsg = messageTxt.getText().toString();
				String theNumber = numberTxt.getText().toString();
				sendMsg(theNumber, myMsg);
			}
		});
    }

    // Metoda wysy³ania wiadomoœci
	protected void sendMsg(String theNumber, String myMsg)
	{
		// £añcuchy znaków wysy³ania i odbierania
		String SENT = "Wiadomoœæ wys³ana";
		String DELIVERED = "Wiadomoœæ dostarczona";
		
		// Sk³adanie po³¹czeñ wysy³anych i otrzymanych
		PendingIntent sentPI = PendingIntent.getBroadcast(this, 0, new Intent(SENT), 0);
		PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0, new Intent(DELIVERED), 0);
		
		// Rejestrowanie transmisji wysy³ania
		registerReceiver(new BroadcastReceiver()
		{
			public void onReceive(Context arg0, Intent arg1)
			{
				switch(getResultCode())
				{
				case Activity.RESULT_OK:
					Toast.makeText(PierwszaAktywnosc.this, "SMS zosta³ wys³any", Toast.LENGTH_LONG).show();
					break;
				case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
					Toast.makeText(getBaseContext(), "Niepowodzenie", Toast.LENGTH_LONG).show();
					break;
				case SmsManager.RESULT_ERROR_NO_SERVICE:
					Toast.makeText(getBaseContext(), "Brak us³ugi", Toast.LENGTH_LONG).show();
					break;
				}
			}
		}, new IntentFilter(SENT));
		
		// Rejestrowanie transmisji dostarczania
		registerReceiver(new BroadcastReceiver()
		{
			public void onReceive(Context arg0, Intent arg1)
			{
				switch(getResultCode())
				{
				case Activity.RESULT_OK:
					Toast.makeText(PierwszaAktywnosc.this, "SMS zosta³ dostarczony", Toast.LENGTH_LONG).show();
					break;
				case Activity.RESULT_CANCELED:
					Toast.makeText(PierwszaAktywnosc.this, "SMS nie zosta³ dostarczony", Toast.LENGTH_LONG).show();
					break;
				}
			}
		}, new IntentFilter(DELIVERED));
		
		// Wynik koñcowy wysy³ania wiadomoœci
		SmsManager sms = SmsManager.getDefault();
		sms.sendTextMessage(theNumber, null, myMsg, sentPI, deliveredPI);
	}
	
	// Metoda wznowienia transmisji
	@Override
	protected void onResume()
	{
		registerReceiver(intentReceiver, intentFilter);
		super.onResume();
	}
	
	// Metoda przerwania transmisji
	@Override
	protected void onPause()
	{
		unregisterReceiver(intentReceiver);
		super.onPause();
	}
}
