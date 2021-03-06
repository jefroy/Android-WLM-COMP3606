package com.example.fileswithobjects;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.util.Size;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class PizzaScreen3Activity extends Activity {
    private HashMap<Integer, Person> persons;
    private ArrayList<Person> personsData;
    private int SIZE;
    private static final String FILENAME = "customer_file2";
    private Button buttonOpen, buttonSave, buttonUpdate, btnLV;
    private EditText fname, lname, newOrders, ID;
    private ListView lv;
    private File testFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pizza_screen3);

        testFile = new File("test");
        System.out.println("test file: " + testFile.exists());

        persons = new HashMap<Integer, Person>(); // data to write to the file
        personsData = new ArrayList<>();

        initStuff();
    }

    public void processTestFile(){
    }

    public void initStuff(){
        lv = findViewById(R.id.list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String s = lv.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
            }
        });

        buttonOpen = (Button) findViewById(R.id.buttonOpen);
        buttonOpen.setOnClickListener(new Button_Clicker());
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonSave.setOnClickListener(new Button_Clicker());
        buttonUpdate= (Button) findViewById(R.id.buttonUpdate);
        buttonUpdate.setOnClickListener(new Button_Clicker());
        fname = (EditText) findViewById(R.id.editTextFname);
        lname = (EditText) findViewById(R.id.editTextLastname);
        newOrders = (EditText) findViewById(R.id.editTextOrders);
        ID = (EditText) findViewById(R.id.editID);

        btnLV = findViewById(R.id.btn_lv);
        btnLV.setOnClickListener(new Button_Clicker());

        fname.setOnClickListener(new EditText_Clicker());
        lname.setOnClickListener(new EditText_Clicker());
        ID.setOnClickListener(new EditText_Clicker());
        newOrders.setOnClickListener(new EditText_Clicker());
//
//        fname.setFocusableInTouchMode();
//        lname.setFocusableInTouchMode(false);
//        ID.setFocusableInTouchMode(false);
//        newOrders.setFocusableInTouchMode(false);

    }


    public void exitApp () {
        finish();
    }

    public void writeToFileFromMap(HashMap<Integer, Person> list){
        Log.d("MyApp", "Attempting to write to file....");
        //Person per = new Person(fName, lName, ord); //create Person object
        try {
            FileOutputStream fout; // write bytes to a file (from obj in this case)
            fout = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fout); // object stream to write an object to the file in bytes
            for (Map.Entry<Integer, Person> integerPersonEntry : list.entrySet()) {
                int key = integerPersonEntry.getKey();
                Person p1 = list.get(key);
                oos.writeObject(p1);
            }
            oos.close();
            System.out.println("Finished writing person objects to file " + FILENAME);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("MyApp", "File write successful.");
        Toast.makeText(this, "Saved File! el: " + list.size(), Toast.LENGTH_SHORT).show();
        buttonSave.setText("Save File");
    }



    public void saveFile () {
        buttonSave.setText("Saving File...");
        determineSIZE();
        if(SIZE <= 0) initPersons();

        Log.d("MyApp", "Data loaded in array. Printing from array....");
        printFromMap(persons);

        writeToFileFromMap(persons);

    }

    private void initPersons() {
        Person p = new Person("Bill", "Smith", 20);
        persons.put(p.getID(), p);
        Person p1 = new Person("Fred", "Bloggs", 30);
        persons.put(p1.getID(), p1);
        Person p2 = (new Person("Joe", "Phillips", 40) );
        persons.put(p2.getID(), p2);
        Person p3 = (new Person("Ria", "Maharaj", 50) );
        persons.put(p3.getID(), p3);
        Person p4 = (new Person("Jo", "Jo", 10) );
        persons.put(p4.getID(), p4);
        determineSIZE();
    }

    public void determineSIZE(){
        this.SIZE = persons.size();
        System.out.println("SIZE: " + SIZE);
    }

    public void printFromMap(HashMap<Integer, Person> p) {
        // print persons
        System.out.println("MAP: Size: " + p.size());
        for (Map.Entry<Integer, Person> integerPersonEntry : p.entrySet()) {
            int key = integerPersonEntry.getKey();
            Person p1 = persons.get(key);
            System.out.println("\n" + p1.toString());
        }
    }
    public void printFromArray (ArrayList<Person> p) {
        // print persons
        System.out.println("ARRAY: Size: " + p.size());
        for (Person i :
                p) {
            System.out.println(i.toString());
        }
    }

    public void openFile () {
        // grab the data from the file and store it in personsData
        determineSIZE();
        if(SIZE <= 0) saveFile();
        if(SIZE <= 0) return;
        FileInputStream inputStream; // for reading streams of byte data
        personsData = new ArrayList<>();
        try{
            inputStream = openFileInput(FILENAME); // open file to read from
            Log.d("MyApp", "getting persons from file for list!!");
            ObjectInputStream ois = new ObjectInputStream(inputStream); // for reading objects from the file

            int i = 0;
            while(i <= SIZE){
                Object temp; // temp person obj for reading file
                temp = ois.readObject();
                personsData.add((Person)temp);
                i++;
            }
            ois.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        // we should now have personsData without null at the end
        Log.d("MyApp" ,"got persons[]");
        printFromArray(personsData);
    }

    public void updateFile () {
        // TODO: 10-Nov-19 dapowdklopaw 
        // Required: ID, orders (this amount will be added to the existing amount for that person
        // run openFile(), then saveFile() - populate persons and personsData
        // navigate through each person and check look for the id - p.getID() == id
        // if found, increment order amount
        // else print toast error and return
        boolean updated = false;
        String reg = "^[a-zA-z]*$";
        if(persons.size() <= 0) saveFile();
        if(persons.size() <= 0){
            Toast.makeText(this, "error in updating, no persons found!!, plase check saveFile()", Toast.LENGTH_SHORT).show();
            return;
        }
        if(personsData.size() <= 0) openFile();

        String idStr, orderStr;
        idStr = ID.getText().toString();
        orderStr = newOrders.getText().toString();
        if(idStr == null || idStr.matches(reg)){
            Toast.makeText(this, "invalid id", Toast.LENGTH_SHORT).show();
            return;
        }
        if(orderStr == null || orderStr.matches(reg)){
            Toast.makeText(this, "invalid order amount", Toast.LENGTH_SHORT).show();
            return;
        }

        int id = Integer.parseInt(idStr);
        int updateAmt = Integer.parseInt(orderStr);

        // at this point we have the personsData and valid numbers for ID and Orders

        if(!persons.containsKey(id)){
            Toast.makeText(this, "cannot find customer!", Toast.LENGTH_SHORT).show();
            return;
        }

        for (Map.Entry<Integer, Person> integerPersonEntry : persons.entrySet()) {
            int key = integerPersonEntry.getKey();
            Person p = persons.get(key);
            if (p.getID() == id) {
                updated = true;
                p.addOrders(updateAmt);
            }
        }

        if(!updated){
            Toast.makeText(this, "cannot find customer!", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "customer updated!", Toast.LENGTH_SHORT).show();
        printFromMap(persons);

        updateMapFromArray();
        saveFile();
        openFile(); // update personsData
        initList();

    }

    public void updateMapFromArray(){
        for (Person p :
                personsData) {
            p = persons.get(p.getID());
        }
    }

    class EditText_Clicker implements EditText.OnClickListener{

        @Override
        public void onClick(View v) {
            if(v == ID){
//                Toast.makeText(PizzaScreen3Activity.this, "inside id click listener", Toast.LENGTH_SHORT).show();
                ID.setText("");
                ID.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            if(v == newOrders){
                newOrders.setText("");
                newOrders.setInputType(InputType.TYPE_CLASS_NUMBER);
            }
            if(v == fname){
                fname.setText("");
                fname.setInputType(InputType.TYPE_CLASS_TEXT);
            }
            if(v == lname){
                lname.setText("");
                lname.setInputType(InputType.TYPE_CLASS_TEXT);
            }
        }
    }

    class Button_Clicker implements Button.OnClickListener
    {
        @Override
        public void onClick(View v) {
            if (v == btnLV){
//                Intent i = new Intent(getApplicationContext(), ListActivity.class);
//                startActivity(i);
                initList();
            }
            if(v == buttonOpen) openFile();
            if(v == buttonSave) saveFile();
            if(v == buttonUpdate) updateFile();
        }

    }

    private void initList() {
        // update the list data from personsData
        System.out.println("initing List");
        determineSIZE(); // account for the end of list
        if(SIZE <= 0){
            Toast.makeText(this, "Array Empty! try opening file!", Toast.LENGTH_SHORT).show();
            return;
        }
        int i = 0;
        String[] s = new String[SIZE];
        while(i < SIZE){
            s[i] = personsData.get(i).toString();
            i++;
        }

        ListAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, s);
        lv.setAdapter(adapter);

    }


}
/*
03-15 12:04:15.196    1571-1583/example.com.userinterface11 I/art﹕ Background sticky concurrent mark sweep GC freed 6494(290KB) AllocSpace objects, 0(0B) LOS objects, 16% free, 2MB/2MB, paused 1.130ms total 201.273ms
03-15 12:04:36.414    1571-1571/example.com.userinterface11 D/MyApp﹕ Data loaded in array. Printing from array....
03-15 12:04:36.415    1571-1571/example.com.userinterface11 I/System.out﹕ Bill Smith 20
03-15 12:04:36.415    1571-1571/example.com.userinterface11 I/System.out﹕ Fred Bloggs 30
03-15 12:04:36.415    1571-1571/example.com.userinterface11 I/System.out﹕ Joe Phillips 40
03-15 12:04:36.415    1571-1571/example.com.userinterface11 I/System.out﹕ Ria Maharaj 50
03-15 12:04:36.415    1571-1571/example.com.userinterface11 D/MyApp﹕ Attempting to write to file....
03-15 12:04:36.587    1571-1571/example.com.userinterface11 I/System.out﹕ Finished writing person objects to file ' + FILENAME
03-15 12:04:36.587    1571-1571/example.com.userinterface11 D/MyApp﹕ File write successful.
03-15 12:04:56.017    1571-1571/example.com.userinterface11 D/MyApp﹕ Opened the file.
03-15 12:04:56.068    1571-1571/example.com.userinterface11 D/MyApp﹕ Loaded the array from the file.
03-15 12:04:56.068    1571-1571/example.com.userinterface11 D/MyApp﹕ Printing the array contents.
03-15 12:04:56.069    1571-1571/example.com.userinterface11 I/System.out﹕ Bill Smith 20
03-15 12:04:56.069    1571-1571/example.com.userinterface11 I/System.out﹕ Fred Bloggs 30
03-15 12:04:56.069    1571-1571/example.com.userinterface11 I/System.out﹕ Joe Phillips 40
03-15 12:04:56.077    1571-1571/example.com.userinterface11 I/System.out﹕ Ria Maharaj 50
03-15 12:05:04.951    1571-1571/example.com.userinterface11 D/MyApp﹕ Attempting to update to file....
03-15 12:05:04.953    1571-1571/example.com.userinterface11 D/MyApp﹕ Opened the file.
03-15 12:05:05.084    1571-1571/example.com.userinterface11 D/MyApp﹕ Loaded the array from the file.
03-15 12:05:05.085    1571-1571/example.com.userinterface11 D/MyApp﹕ Printing the array contents.
03-15 12:05:05.085    1571-1571/example.com.userinterface11 I/System.out﹕ Bill Smith 20
03-15 12:05:05.085    1571-1571/example.com.userinterface11 I/System.out﹕ Fred Bloggs 30
03-15 12:05:05.085    1571-1571/example.com.userinterface11 I/System.out﹕ Joe Phillips 40
03-15 12:05:05.085    1571-1571/example.com.userinterface11 I/System.out﹕ Ria Maharaj 50
03-15 12:05:19.903    1571-1571/example.com.userinterface11 D/InputEventConsistencyVerifier﹕ KeyEvent: ACTION_UP but key was not down.
    in android.widget.EditText{155430da VFED..CL .F....ID 32,597-32,680 #7f080014 app:id/custOrders}
    0: sent at 6947298000000, KeyEvent { action=ACTION_UP, keyCode=KEYCODE_TAB, scanCode=15, metaState=0, flags=0x8, repeatCount=0, eventTime=6947298, downTime=6947152, deviceId=0, source=0x101 }
03-15 12:05:30.756    1571-1571/example.com.userinterface11 D/MyApp﹕ Attempting to update to file....
03-15 12:05:30.758    1571-1571/example.com.userinterface11 D/MyApp﹕ Opened the file.
03-15 12:05:30.903    1571-1571/example.com.userinterface11 D/MyApp﹕ Loaded the array from the file.
03-15 12:05:30.903    1571-1571/example.com.userinterface11 D/MyApp﹕ Printing the array contents.
03-15 12:05:30.903    1571-1571/example.com.userinterface11 I/System.out﹕ Bill Smith 20
03-15 12:05:30.903    1571-1571/example.com.userinterface11 I/System.out﹕ Fred Bloggs 30
03-15 12:05:30.903    1571-1571/example.com.userinterface11 I/System.out﹕ Joe Phillips 40
03-15 12:05:30.903    1571-1571/example.com.userinterface11 I/System.out﹕ Ria Maharaj 50
03-15 12:05:30.904    1571-1571/example.com.userinterface11 D/MyApp﹕ Record was updated in the array Bloggs
03-15 12:05:30.904    1571-1571/example.com.userinterface11 D/MyApp﹕ Printing the UPDATED array contents.
03-15 12:05:30.990    1571-1571/example.com.userinterface11 I/System.out﹕ Bill Smith 20
03-15 12:05:30.991    1571-1571/example.com.userinterface11 I/System.out﹕ Fred Bloggs 130
03-15 12:05:31.013    1571-1571/example.com.userinterface11 I/System.out﹕ Joe Phillips 40
03-15 12:05:31.013    1571-1571/example.com.userinterface11 I/System.out﹕ Ria Maharaj 50
03-15 12:05:31.014    1571-1571/example.com.userinterface11 D/MyApp﹕ Attempting to update file with changes....
03-15 12:05:31.064    1571-1571/example.com.userinterface11 I/System.out﹕ Finished writing person objects to file customer_file2
03-15 12:05:31.064    1571-1571/example.com.userinterface11 D/MyApp﹕ Finished attempting write changes to file...
03-15 12:05:31.298    1571-1583/example.com.userinterface11 I/art﹕ Background partial concurrent mark sweep GC freed 5557(247KB) AllocSpace


NOTE: Exercise caution if running this app on your actual phone.

EXERCISES
1. Try to figure out where the data file is located.
2. Add and ID field for each customer (unique integer)
3. Display all the data written to the file and all the data retrieved from the file
4. Accept an ID an integer "update" and locate the customer in the file. ADd "update"
   to his orders. If not found, print a message.


 */