package in.websuite.web.listview;

import android.Manifest;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

   /* String name[]={"Gyaneshwar","Vishwas","Vishal","Harshit","Deepanshu","Krishna","Nikhil","Mani"};
    String mobile[]={"7266880340","9140492884","8765495847","985454841454","87824584512","78454845154","9148484121","89897488441"};
    */

    ArrayList<String> name=new ArrayList<String>();
    ArrayList<String> number=new ArrayList<String>();
    ArrayList<String> type=new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 23)
        {

            if(checkSelfPermission(Manifest.permission.READ_CONTACTS)== PackageManager.PERMISSION_DENIED)
            {
               ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS}, 1
                );
            }
            if(checkSelfPermission(Manifest.permission.WRITE_CONTACTS)== PackageManager.PERMISSION_DENIED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_CONTACTS}, 1
                );
            }
            if(checkSelfPermission(Manifest.permission.INTERNET)== PackageManager.PERMISSION_DENIED)
            {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.INTERNET}, 1
                );
            }
            if(checkSelfPermission(Manifest.permission.WRITE_CONTACTS)== PackageManager.PERMISSION_GRANTED)
            {
                setContentView(R.layout.activity_main);
                MyList ml=new MyList();
                ListView lv=(ListView) findViewById(R.id.list_view);

                lv.setAdapter(ml);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.WRITE_CONTACTS)== PackageManager.PERMISSION_GRANTED) {
                setContentView(R.layout.activity_main);
                MyList ml = new MyList();
                ListView lv = (ListView) findViewById(R.id.list_view);

                lv.setAdapter(ml);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add("Add Account");
        menu.add("Import contact");
        menu.add("Filter");
        menu.add("Setting");
        menu.add("Exit");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title=item.getTitle().toString();
        if(title.equals("Exit"))
        {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Do you want to Cancel ? ");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            builder.setNegativeButton("No",null);
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    public class MyList extends ArrayAdapter
    {
        MyList()
        {
            super(MainActivity.this,R.layout.activity_cust_list_view);
            getContactList();
        }
        @Override
        public int getCount()
        {
            return name.size();
        }
        @Override
        public View getView(int pos, View view, ViewGroup viewGroup)
        {
            LayoutInflater liv=getLayoutInflater();
            View v=liv.inflate(R.layout.activity_cust_list_view,viewGroup,false);
            TextView txt1=(TextView)v.findViewById(R.id.txt_name);
            TextView txt2=(TextView)v.findViewById(R.id.txt_mobile);


            txt1.setText(name.get(pos));
            txt2.setText(number.get(pos));
            return v;
        }
    }
    private void getContactList() {
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME));

                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER));
                        this.name.add(name);
                        this.number.add(phoneNo);
//                        Log.i(TAG, "Name: " + name);
//                        Log.i(TAG, "Phone Number: " + phoneNo);
                    }
                    pCur.close();
                }
            }
        }
        if(cur!=null){
            cur.close();
        }
    }
}
