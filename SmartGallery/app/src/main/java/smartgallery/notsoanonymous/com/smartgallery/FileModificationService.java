package smartgallery.notsoanonymous.com.smartgallery;

import java.io.File;
import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class FileModificationService extends Service{
    private MyFileObserver fileOb;
    public static String root = Environment.getExternalStorageDirectory() + "/DCIM/Camera/";
    //private List<MyFileObserver> fileOb_list = new ArrayList<MyFileObserver>();
    @Override
    public void onCreate() {
        File sdcard = new File(root);
        Log.e("path", sdcard.getAbsolutePath());
        if (sdcard == null) {
            return;
        } else {
            //fileOb_list.clear();
            num_of_fos = 0;
            createFileObs(sdcard);
        }
    }

    //only create fileobserver for folders
    int num_of_fos = 0;
    private void createFileObs(File f) {
		/*if (num_of_fos > MAX_FO) {
			return;
		}*/
        if (!f.isDirectory()) {
            //MyFileObserver aFileOb = new MyFileObserver(f.getAbsolutePath());
            //fileOb_list.add(aFileOb);
        } else {
            fileOb = new MyFileObserver(f.getAbsolutePath());
			/*MyFileObserver aFileOb = new MyFileObserver(f.getAbsolutePath());
			fileOb_list.add(aFileOb);
			num_of_fos++;
			try {
				for (File currentFile : f.listFiles()) {
					createFileObs(currentFile);
				}
			} catch (Exception e) {
				Log.e("Error", e.toString());
			}*/
        }
    }

    @Override
    public void onStart(Intent intent, int startid) {
        fileOb.startWatching();
		/*for (int i = 0; i < fileOb_list.size(); ++i) {
			fileOb_list.get(i).startWatching();
		}*/
        Log.e("SERVICE", " STARTED");
//        Toast.makeText(this.getApplicationContext(), "start monitoring file modification", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onDestroy() {
        fileOb.stopWatching();
		/*for (int i = 0; i < fileOb_list.size(); ++i) {
			fileOb_list.get(i).stopWatching();
		}*/
//        Toast.makeText(this.getApplicationContext(), "stop monitoring file modification", Toast.LENGTH_SHORT).show();
    }

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }
}
