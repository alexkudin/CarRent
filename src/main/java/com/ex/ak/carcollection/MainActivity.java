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
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
{
    private ListView        ListOfCars;
    private EditText        editName;
    private EditText        editModel;
    private EditText        editYear;
    private ImageView       FotoView;
    private LayoutInflater  inflater;
    private EditText        dialogCarName;
    private EditText        dialogCarModel;
    private EditText        dialogCarYear;
    private LinearLayout    dialogPictureView;
    public static ArrayList<View> allViews = new ArrayList<>();
    public static int currentCar = -1;


    private View dialogViewAddUpd;              // ----------- dialog Add -----------------------
    private AlertDialog.Builder biulder;        // ------- builder for Add && Update ------------

    private static boolean isUpdate = false;
    private static boolean isMainLayout = true;


    private static File PicDir                      = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);	// directory Pictures on Device
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


    // ---------- fill collection of cars --------
    {
        for(int i = 0 ; i < 67 ; i++)
        {
            //arrYears.add(i + 1950);          // Films Years starts from 1950
        }
    }


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
        Log.d("======", "Files : " + fileNames.length);
        for(File f : files)  {carInPicturesDir.add(f);}  // get ArrayList of files in PICTURES Directory

        // Pesochnitsa------------------------------------------------------------------------------
        //File IntStorDir = this.getFilesDir();
        //File [] InternalStorFiles = IntStorDir.listFiles();
        //Toast.makeText(this,l,Toast.LENGTH_SHORT).show();
        carsAL.add(new Car("Renault",   "Megane",   2007,   fileNames[2]));
        carsAL.add(new Car("Daewoo",    "Lanos",    2011 ,  fileNames[1]));

        carsAL.add(new Car("VAZ",       "2107",     1998 ,  fileNames[9]));
        carsAL.add(new Car("Ford",      "Focus",    2008 ,  fileNames[6]));
        carsAL.add(new Car("ZAZ",       "1102",     2002 ,  fileNames[0]));

        /**
         *      writing v Pesochnicu random file

        Log.d("pesochnica", IntStorDir.toString());


        try
        {
            FileOutputStream FOS = this.openFileOutput("myFile" + ((int) (Math.random() * 100)) + ".txt", Context.MODE_PRIVATE);
            OutputStreamWriter OSW = new OutputStreamWriter(FOS);
            for (int i = 0; i < 2; i++)
            {
                OSW.write("Hello World" + i + "\n");
            }
            OSW.flush();
            OSW.close();
        }
        catch (FileNotFoundException e)     {   e.printStackTrace(); }
        catch (IOException ie)              {   ie.getMessage(); }

        //-----------------------------------------------------------------
        // --------- reading from Pesochnica her content ------------------
        String[] files = this.fileList();
        for (String f : files)
        {
            Log.d("======", "File : " + f);
        }
        //-----------------------------------------------------------------
        // ---------- reading first file from Pesochnica ------------------
        try {
            FileInputStream FIS = this.openFileInput(files[0]);
            LineNumberReader LNR = new LineNumberReader(new InputStreamReader(FIS));
            while (true)
            {
                String S = LNR.readLine();
                if (S == null)
                {
                    break;
                }
                Log.d("========>", S);
            }
            LNR.close();
        }
        catch (Exception e)
        {
        }
        //------------------------------------------------------------------
        // ---------- deleting all files from Pesochnica -------------------

         for (String f : files) {
         if (this.deleteFile(f)) {
         Log.d("====", "File : " + f + " deleted");
         } else {
         Log.d("====", "Error deleting file " + f);
         }
         }
         */
        //End of pesochnitsa -----------------------------------------------------------------------



        /**
         *      Initializing Vidgets ---------------------------------------------
         */
        ListOfCars = (ListView)this.findViewById(R.id.lv1);
        editName   = (EditText)this.findViewById(R.id.etName);
        editModel  = (EditText)this.findViewById(R.id.etModel);
        editYear   = (EditText)this.findViewById(R.id.etYear);
        FotoView   = (ImageView)this.findViewById(R.id.CarImg);

        inflater            = this.getLayoutInflater();

        dialogViewAddUpd    = inflater.inflate(R.layout.actions_layout, null, false);
        biulder             = new AlertDialog.Builder(this);
        //biulder.setView(this.dialogViewAddUpd);

        dialogPictureView   = (LinearLayout)this.dialogViewAddUpd.findViewById(R.id.svImagesDialog);
        dialogCarName       = (EditText)this.dialogViewAddUpd.findViewById(R.id.etCarNameDialog);
        dialogCarModel      = (EditText)this.dialogViewAddUpd.findViewById(R.id.etCarModelDialog);
        dialogCarYear       = (EditText)this.dialogViewAddUpd.findViewById(R.id.etYearDialog);


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
            // ==== A D D ==========================================================================
            case R.id.action_add :

                this.menuAdd();

                return true;

            // ==== U P D A T E ====================================================================
            case R.id.action_upd :

                this.menuUpdate();

                return true;


            // ==== D E L E T E ====================================================================
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
        /**
         this.editName.setText("enter Car Name ");
         this.editModel.setText("enter Car Model");
         this.editYear.setText("enter Year");
         */
        //this.spinnerYear.setAdapter(this.adapterYear);
        //this.dialogPictureView = (LinearLayout)this.findViewById(R.id.svImagesDialog);

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

    public void fillCarsFollection()
    {

    }

}