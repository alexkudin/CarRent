package com.ex.ak.carcollection;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    private ListView        ListOfCars;
    private LayoutInflater  inflater;
    private EditText        dialogCarName;
    private EditText        dialogCarModel;
    private EditText        dialogCarYear;
    private LinearLayout    dialogPictureView;
    public static ArrayList<View> allViews = new ArrayList<>();
    public static int currentCar = -1;
    private static View curView    = null;
    private static Car tmpCar;

    private View dialogViewAddUpd;              // ----------- dialog Add -----------------------
    private AlertDialog.Builder biulder;        // ------- builder for Add && Update ------------

    private static boolean isUpdate = false;
    private static boolean isMainLayout = true;

    private static File PicDir                       = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);	// directory Pictures on Device
    private static File []      files                = PicDir.listFiles();
    private static String[]     fileNames ;

    //private File                IntStorDir          = MainActivity.this.getFilesDir();
    //private File []             filesInternalStor   = IntStorDir.listFiles();

    public static ArrayList <Car>            carsAL = new ArrayList<>();
    private ArrayList <HashMap<String,Object>> carMapsAL = new ArrayList<>();
    private ArrayList <File>        carInPicturesDir = new ArrayList<>();
    private ArrayList <Map<String,File>>   imgMapsAL = new ArrayList<>();

    private final static String KEY_CAR_NAME    = "CarName";        // key for carName in Adapter
    private final static String KEY_CAR_MODEL   = "CarModel";       // key for carModel in Adapter
    private final static String KEY_CAR_YEAR    = "CarYear";        // key for carYear in Adapter
    private final static String KEY_CAR_FOTO    = "CarFoto";        // key for carImage in Adapter
    private final static String CAR_FOTO_DIALOG = "CarFotoInDialog";// key for carImage in Dialog

    private static File ExtStorDir = Environment.getExternalStorageDirectory(); // ---- getting path to ExternalStorageDirectory
    private static File F = new File(ExtStorDir,"cars.txt");		    		// ---- creating file


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /**
         *      Coping images from Assets to Pictures Directory
         */
        this.putImagesFromAssetsToPictures();
        fileNames = PicDir.list();
        /**
         *      Filling collection carsAL
         */
        fillCarsFollection();

        for(File f : files)  {carInPicturesDir.add(f);}  // get ArrayList of files in PICTURES Directory

        /**
         *      writing v Pesochnicu random file
         *
         //Pesochnitsa------------------------------------------------------------------------------


         //End of pesochnitsa -----------------------------------------------------------------------

         /**
         *      Initializing Vidgets ---------------------------------------------
         */
        this.ListOfCars          = (ListView)this.findViewById(R.id.lv1);
        this.inflater            = this.getLayoutInflater();

        this.dialogViewAddUpd    = inflater.inflate(R.layout.actions_layout, null, false);
        this.biulder             = new AlertDialog.Builder(this);

        this.dialogPictureView   = (LinearLayout)this.dialogViewAddUpd.findViewById(R.id.svImagesDialog);
        this.dialogCarName       = (EditText)this.dialogViewAddUpd.findViewById(R.id.etCarNameDialog);
        this.dialogCarModel      = (EditText)this.dialogViewAddUpd.findViewById(R.id.etCarModelDialog);
        this.dialogCarYear       = (EditText)this.dialogViewAddUpd.findViewById(R.id.etYearDialog);


        /**
         *      Setting pictures to dialog ScrollViewImages
         */

        /**
         *      SimpleAdapter for CarImages in Dialog Layout
         */
        for(int i = 0 ; i < carInPicturesDir.size(); i++)
        {
            HashMap<String,File> tmpImgofCar = new HashMap<>();
            File img = carInPicturesDir.get(i);
            tmpImgofCar.put(MainActivity.CAR_FOTO_DIALOG,img);
            imgMapsAL.add(tmpImgofCar);
        }

        SimpleAdapter adapterDialog =   new SimpleAdapter(this,imgMapsAL,R.layout.actions_layout,
                new String[] {CAR_FOTO_DIALOG},
                new int[] {R.id.CarImg});

        //adapterDialog.setDropDownViewResource(R.layout.actions_layout);
        //dialogViewAddUpd.setAdapter(adapterDialog);

        /**
         *      SimpleAdapter for CarImages in Custom ListView
         */
        for(int i = 0 ; i < carsAL.size(); i++)
        {
            HashMap<String,Object> tmpCar = new HashMap<>();
            Car c = carsAL.get(i);
            tmpCar.put(MainActivity.KEY_CAR_NAME,c.name);
            tmpCar.put(MainActivity.KEY_CAR_MODEL, c.model);
            tmpCar.put(MainActivity.KEY_CAR_YEAR, String.valueOf(c.year));

            Bitmap fotoCar = setImage(c.getFoto());

            tmpCar.put(MainActivity.KEY_CAR_FOTO, fotoCar);
            carMapsAL.add(tmpCar);
        }

        myAdapter adapter = new myAdapter(this,R.layout.custom_view,R.id.etName,carMapsAL);
        ListOfCars.setAdapter(adapter);
        ListOfCars.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                //========== DisSelect Proshliy element ==================
                if (currentCar != -1)
                {
                    for (int i = 0; i < allViews.size(); i++)
                    {
                        View v = allViews.get(i);
                        v.setBackgroundColor(Color.rgb(0xB2,0xDA,0x00));
                    }
                }

                if (currentCar != position)
                {
                    curView = view;
                    currentCar = position;
                    tmpCar = carsAL.get(position);
                    view.setBackgroundColor(Color.CYAN);
                }
                else
                {
                    MainActivity.currentCar = -1;
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        switch(id)
        {
            case R.id.action_add :
                this.menuAdd();
                return true;

            case R.id.action_upd :
                this.menuUpdate();
                return true;

            case R.id.action_del :
                this.menuDelete();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void menuAdd()
    {
        this.biulder.setView(this.dialogViewAddUpd);
        this.biulder.setTitle("Add Car");
        this.dialogCarName.setText("enter Car Name ");
        this.dialogCarModel.setText("enter Car Model");
        this.dialogCarYear.setText("enter Year");
        //this.spinnerYear.setAdapter(this.adapterYear);
        try
        {
            for(File f : files)
            {
                //carInPicturesDir.add(f);
                FileInputStream FIS = new FileInputStream(f);
                Bitmap bmp = BitmapFactory.decodeStream(FIS);
                FIS.close();
                ImageView IV = new ImageView(this);
                IV.setImageBitmap(bmp);
                dialogPictureView.addView(IV);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        isUpdate = false;
        AlertDialog dialog = biulder.create();
        dialog.show();
    }

    public void menuUpdate()
    {

    }

    public void menuDelete()
    {

    }

    public void putImagesFromAssetsToPictures()
    {
        try
        {
            //PicDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);	// directory Pictures on Device
            AssetManager AM = this.getAssets();
            String[]     arr = AM.list("");     													    // current directory of Assets

            for (String filename : arr)
            {
                try
                {
                    InputStream IS      = AM.open(filename);
                    File              D = new File (PicDir,filename);
                    FileOutputStream OS = new FileOutputStream(D);
                    byte[]            b = new byte[2048];
                    while(true)
                    {
                        int cnt = IS.read(b,0,b.length);
                        if(cnt == -1 || cnt ==0)
                        {
                            break;
                        }
                        OS.write(b,0,cnt);
                    }
                    OS.close();
                    IS.close();
                }
                catch (Exception e) {}
                Log.d("========", filename);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        /**
         *      list of Files in Pictures directory
         */
        String[] fileNames = PicDir.list();
        for(String f : fileNames)
        {
            Log.d(">>>>>>>>>", f);
        }
    }

    public static Bitmap setImage(String fileName)
    {
        Bitmap bmp = null;
        try
        {
            for (File f : files)
            {
                if (String.valueOf(f.getName()).compareTo(fileName) == 0)
                {
                    FileInputStream FIS = new FileInputStream(f);
                    bmp = BitmapFactory.decodeStream(FIS);
                    FIS.close();
                }
            }
        }
        catch (Exception e)  { System.out.println(e.getMessage()); }

        return bmp;
    }
    public static String getImage(String fileN)
    {
        String file = null;
        Bitmap bmp = null;
        try
        {
            for (File f : files)
            {
                if (String.valueOf(f.getName()).compareTo(fileN) == 0)
                {
                    FileOutputStream FOS = new FileOutputStream(f);
                    //bmp = BitmapFactory.decodeStream(FOS);
                    FOS.close();
                }
            }
        }
        catch (Exception e)  { System.out.println(e.getMessage()); }

        return file;
    }

    public static void fillCarsFollection()
    {
        carsAL.add(new Car("Renault", "Megane",     2007,   fileNames[2]));
        carsAL.add(new Car("Daewoo",    "Lanos",    2011 ,  fileNames[1]));
        carsAL.add(new Car("VAZ",       "2107",     1998 ,  fileNames[9]));
        carsAL.add(new Car("Ford",      "Focus",    2008 ,  fileNames[6]));
        carsAL.add(new Car("ZAZ",       "1102",     2002 ,  fileNames[0]));
        carsAL.add(new Car("Hyundai",   "Accent",   1998 ,  fileNames[15]));
        carsAL.add(new Car("Nissan",    "Maxima",   2008 ,  fileNames[11]));
        carsAL.add(new Car("Honda",     "Legend",   2002 ,  fileNames[8]));
    }

    /**
     *   ----- check for Writing to external storage
     */
    private boolean isExstStorageAvailableForWriting()
    {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     *	------ save to File ----------------
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        try
        {
            if(isExstStorageAvailableForWriting())
            {
                FileOutputStream    FOS = new FileOutputStream(F);
                ObjectOutputStream OOS = new ObjectOutputStream(FOS);

                //----- for all Products -------------------------------------

                    for(int j = 0 ; j < carMapsAL.size(); j++)
                    {
                        HashMap <String , Object> HashMapCar = carMapsAL.get(j);
                        String Name  = (String) HashMapCar.get(KEY_CAR_NAME);
                        String Model = (String) HashMapCar.get(KEY_CAR_MODEL);
                        String Year = (String) HashMapCar.get(KEY_CAR_YEAR);
                        //Bitmap imgCar = getFilesDir()Image(HashMapCar.get(KEY_CAR_FOTO));

                        //String FotoCar = (String)

                        //Car toWrite = new Car(Name,Model,Integer.parseInt(Year),FotoCar);
                        //OOS.writeObject(toWrite);
                    }
                OOS.flush();
                OOS.close();
            }
        }
        catch (IOException e)
        {
            System.out.println("Error writing to File : " + e.getMessage());
        }
    }
}