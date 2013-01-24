package fr.istic.mmm.adeagenda;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import net.fortuna.ical4j.model.DateTime;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
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

				final DatePicker start_picker = (DatePicker)findViewById(R.id.date_start);
				final DatePicker end_picker = (DatePicker)findViewById(R.id.date_end);
				
				new Thread(new Runnable() {
					public void run() {
						try {
							
					        // Création du dossier de destination
					        File downloadDirectory = new File(getFilesDir().getAbsolutePath() + "/ADECalendar");
					        if (!downloadDirectory.exists()) {
					        	Log.v("Calendar", "Création dossier ...");
					            if(downloadDirectory.mkdir()){
					            	Log.v("Calendar", "Création dossier ok");
					            }
					            Log.v("Calendar", downloadDirectory.getAbsolutePath());
					        }
							
							// Téléchargement du fichier iCal
							showToast("Connexion ...");

							String firstDate = formatDate(start_picker.getDayOfMonth(), start_picker.getMonth()+1, start_picker.getYear());
							String lastDate = formatDate(end_picker.getDayOfMonth(), end_picker.getMonth()+1, end_picker.getYear());
							String resources = "129";
							String calType = "ical";
							String login = "cal";
							String password = "visu";
							String projectId = "31";
							
							String cal_url = "http://plannings.univ-rennes1.fr/ade/custom/modules/plannings/direct_cal.jsp?calType="+calType
									+"&login="+login
									+"&password="+password
									+"&resources="+resources
									+"&firstDate="+firstDate
									+"&lastDate="+lastDate
									+"&projectId="+projectId;

							showToast("Téléchargement du fichier ...");
							showToast(cal_url);
							URL url = new URL(cal_url);
					        URLConnection conexion = url.openConnection();
					        InputStream is = url.openStream();
					        conexion.connect();
					        
					        // Téléchargement du fichier
					        showToast("Téléchargement du fichier ...");
					        FileOutputStream destFile = new FileOutputStream(downloadDirectory + "/ADECal.ics");
					        
					        double lenghtOfFile = conexion.getContentLength();
					        byte data[] = new byte[1024];
					        int count = 0;
					        double downloaded = 0;
					        int progress = 0;
					        while ((count = is.read(data)) != -1) {
					            downloaded += count;
					            int progress_temp = (int) ((downloaded / (lenghtOfFile*100)) * 100) ;

					            if (progress_temp % 10 == 0 && progress != progress_temp) {
					                progress = progress_temp;
					                showToast("Progress " +progress_temp);
					            }
					            destFile.write(data, 0, count);
					        }
					        is.close();
					        destFile.close();

					        showToast("Téléchargement du fichier terminé");
							// Chargement fichier ics
							InputStream in = new FileInputStream(new File(getFilesDir().getAbsolutePath() + "/ADECalendar", "ADECal.ics"));
							CalendarReader reader = new CalendarReader(in);
							showToast("Récupération des events du jour...");
							List<Event> events = reader.eventsOfTheDay();
							
							showToast("Affichage des event ...");
							for (Event event : events) {
								showToast(event.getName());
								Log.v("Event name", "[" + event.getName() + "]");
								Log.v("Event date", event.getStart().toGMTString() +" - "+ (event.getDuration() / (1000*60*60)) +" Heures");
								Log.v("Event description", event.getDescription());
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
				Log.v("Toast", toast);
				Toast.makeText(ConfigActivity.this, toast, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	private String formatDate(int day, int month, int year){
		
		String date_str = year+"-";
		
		if(month<10){
			date_str += "0"+month+"-";
		}
		else{
			date_str += month+"-";
		}
		
		if(day<10){
			date_str += "0"+day+"-";
		}
		else{
			date_str += day+"-";
		}
		return date_str;
	}
	
}
