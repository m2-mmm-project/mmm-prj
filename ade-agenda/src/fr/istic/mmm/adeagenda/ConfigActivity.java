package fr.istic.mmm.adeagenda;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import fr.istic.mmm.adeagenda.calendar.CalendarReader;
import fr.istic.mmm.adeagenda.model.Event;

public class ConfigActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_config);
		
		Button loadBtn = (Button) findViewById(R.id.btn_load);
		loadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				new Thread(new Runnable() {
					public void run() {
						try {
							// Téléchargement du fichier iCal
							showToast("Downloading calendar...");
							URL url = new URL("http://plannings.univ-rennes1.fr/ade/custom/modules/plannings/direct_cal.jsp?calType=ical&login=cal&password=visu&resources=129&firstDate=2012-08-22&lastDate=2013-12-31&projectId=31");
					        URLConnection conexion = url.openConnection();
					        conexion.connect();
					        int lenghtOfFile = conexion.getContentLength();
					        InputStream is = url.openStream();
					        File testDirectory = new File(Environment.getDataDirectory() + "/ADECalendar");
					        if (!testDirectory.exists()) {
					        	Log.v("Calendar", "Création dossier ...");
					            if(testDirectory.mkdir()){
					            	Log.v("Calendar", "Création dossier ok");
					            }
					            Log.v("Calendar", testDirectory.getAbsolutePath());
					        }
					        
					        FileOutputStream fos = new FileOutputStream(testDirectory + "/ADECal.ics"); // TODO FNF
					        byte data[] = new byte[1024];
					        int count = 0;
					        long total = 0;
					        int progress = 0;
					        while ((count = is.read(data)) != -1) {
					            total += count;
					            int progress_temp = (int) total * 100 / lenghtOfFile;
					            if (progress_temp % 10 == 0 && progress != progress_temp) {
					                progress = progress_temp;
					            }
					            fos.write(data, 0, count);
					        }
					        is.close();
					        fos.close();
					        
							// Chargement fichier ics
							InputStream in = new FileInputStream(new File(Environment.getDataDirectory() + "/ADECalendar", "ADECal.ics"));
							CalendarReader reader = new CalendarReader(in);
							showToast("Reading calendar...");
							List<Event> events = reader.allEvents();

							for (Event event : events) {
								Log.v("Calendar", " " + event.getName() + " " + event.getDescription());
							}
							showToast("Done !");
						} catch (IOException e) {
							Log.e("Calendar", "IO Error", e);
							showToast("IO Error..." + e.getLocalizedMessage());
						}
					}
				}).start();
			}
		});
	}

	public void showToast(final String toast) {
		runOnUiThread(new Runnable() {
			public void run() {
				Toast.makeText(ConfigActivity.this, toast, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

}
