package com.exampl.hamdi.businesscard;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.SyncStateContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.exampl.hamdi.businesscard.Model.IBusinessCard;
import com.exampl.hamdi.businesscard.Util.Config;
import com.getbase.floatingactionbutton.*;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.exampl.hamdi.businesscard.DataBaseHandler.DBHandler;
import com.exampl.hamdi.businesscard.Model.BusinessCard;
import com.google.gson.Gson;

import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView bCListView;
    MaterialSearchView searchView;
    List<IBusinessCard> businessCardList;
    List<IBusinessCard> filterList;
    TextView tv_user_name;
    static final int PICK_CONTACT_REQUEST = 1;  // The request code
    static  final int SCAN_CODE_REQUEST = 2;
    DBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = (MaterialSearchView) findViewById(R.id.search_view);
        searchView.setVoiceSearch(true);


        db = new DBHandler(this);
        //db.addBusinessCard(new BusinessCard(1,"aaaa","gazzah","06158733","hamdi.g@gmail.com","25 rue robert latouche, 06200 Nice","Etudiant","sdcard/"));
        businessCardList = db.getAllBusinessCards();

        bCListView = (ListView) findViewById(R.id.bclistview);
        final BCAdapter adapter = new BCAdapter(businessCardList);
        bCListView.setAdapter(adapter);
        bCListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this,BusinessCardActivity.class);
                i.putExtra("bc_id",id);
                startActivity(i);
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });

        com.getbase.floatingactionbutton.FloatingActionButton fabContact = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_from_contact);
        fabContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickContact();
            }
        });

        fabContact.setContentDescription("Pick Contact");
        com.getbase.floatingactionbutton.FloatingActionButton fabQRCode = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.add_from_qr_code);
        fabQRCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //scanQRCode();
                startActivity(new Intent(MainActivity.this,ScanActivity.class));
            }
        });
        fabQRCode.setContentDescription("Scanner");



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        for (IBusinessCard bc : businessCardList){
            Log.d("name ",String.valueOf(bc.getId()));
            Log.d("name ",bc.getFirstName());
            Gson gson = new Gson();
            String json = gson.toJson(bc);
            Log.d("JSON ",json);
        }
        /*TextDrawable drawable = TextDrawable.builder()
                .buildRect("A", Color.RED);

        ImageView image = (ImageView) findViewById(R.id.image_view);
        image.setImageDrawable(drawable);*/
        //BusinessCard bc1 = db.getBusinessCard(1);
        //Log.d("nameazerr ",bc1.getFirstName());



    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        }else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        MenuItem item2 = menu.findItem(R.id.action_settings);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(MainActivity.this,QRAppActivity.class);
            startActivity(i);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_view_card) {
            Intent i = new Intent(MainActivity.this,BusinessCardActivity.class);
            i.putExtra("bc_id", Long.parseLong(Config.ID_SHARED_PREF));
            startActivity(i);
        } else if (id == R.id.nav_edit_card) {
            Intent i = new Intent(MainActivity.this,EditBusinessCardActivity.class);
            i.putExtra("bc_id", Long.parseLong(Config.ID_SHARED_PREF));
            startActivity(i);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public class BCAdapter extends BaseAdapter implements Filterable {

        public BCAdapter(List<IBusinessCard> list) {
            filterList = list;
        }

        @Override
        public int getCount() {
            return businessCardList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return businessCardList.get(position).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.item_business_card,null);

            ColorGenerator generator = ColorGenerator.MATERIAL;
            // generate color based on a key (same key returns the same color), useful for list/grid views
            int color = generator.getColor(businessCardList.get(position).getNumbers());

            TextDrawable drawable = TextDrawable.builder().buildRound(businessCardList.get(position).getFirstName().charAt(0)+"", color);

            ImageView image = (ImageView) convertView.findViewById(R.id.image_view);
            TextView text_view_name = (TextView)convertView.findViewById(R.id.text_view_name);
            TextView text_view_function = (TextView)convertView.findViewById(R.id.text_view_function);

            File imgFile = new  File("/data/user/0/com.exampl.hamdi.businesscard/app_hello/img"+String.valueOf(businessCardList.get(position).getId())+".jpg");
            if(imgFile.exists()){
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                image.setImageBitmap(myBitmap);
            }else {
                image.setImageDrawable(drawable);
            }
            text_view_name.setText(businessCardList.get(position).getFirstName());
            text_view_function.setText(businessCardList.get(position).getFunction());
            return convertView;
        }

        @Override
        public Filter getFilter() {
            return new Filter() {
                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    //Log.d(Constants.TAG, "**** PUBLISHING RESULTS for: " + constraint);
                    businessCardList = (List<IBusinessCard>) results.values;
                    BCAdapter.this.notifyDataSetChanged();
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    // TODO Auto-generated method stub
                    FilterResults results= new FilterResults();
                    if(constraint != null && constraint.length()>0)
                    {
                        //CONSTARINT TO UPPER
                        constraint=constraint.toString().toUpperCase();
                        ArrayList<IBusinessCard> filters=new ArrayList<IBusinessCard>();
                        //get specific items
                        for(int i=0;i<filterList.size();i++)
                        {
                            if(filterList.get(i).getFirstName().toUpperCase().contains(constraint))
                            {
                                IBusinessCard bc = new BusinessCard(filterList.get(i).getId(),filterList.get(i).getFirstName(),
                                        filterList.get(i).getLastName(),filterList.get(i).getNumbers(),
                                        filterList.get(i).getMail(),filterList.get(i).getAddress(),
                                        filterList.get(i).getFunction(),filterList.get(i).getImagePath());
                                filters.add(bc);
                            }
                        }
                        results.count=filters.size();
                        results.values=filters;
                    }else
                    {
                        results.count=filterList.size();
                        results.values=filterList;
                    }
                    return results;
                }
            };
        }
    }

    private void pickContact() {
        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, Uri.parse("content://contacts"));
        pickContactIntent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE); // Show user only contacts w/ phone numbers
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);
    }

    private void scanQRCode(){


        /*try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes

            startActivityForResult(intent, SCAN_CODE_REQUEST);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
            startActivity(marketIntent);
        }*/

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request it is that we're responding to
        if (requestCode == PICK_CONTACT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // Get the URI that points to the selected contact
                Uri contactUri = data.getData();

                Cursor cursor = getContentResolver()
                        .query(contactUri, null, null, null, null);
                cursor.moveToFirst();

                int numberColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                String number = cursor.getString(numberColumn);

                int nameColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
                String name = cursor.getString(nameColumn);

                int mailColumn = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                String mail = cursor.getString(mailColumn);
                Log.d("email",mail);
                db.addBusinessCard(new BusinessCard(1,name,"",number,mail,"","",""));
                Intent i = new Intent(MainActivity.this,EditBusinessCardActivity.class);
                i.putExtra("bc_id", getIdLastBusinessCard(db));
                startActivity(i);


            }
        }/*else     if (requestCode == SCAN_CODE_REQUEST) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                Log.i("scanResult",contents);
            }
        }*/

    }

    public static long getIdLastBusinessCard(DBHandler db){
        long id = 0;
        List<IBusinessCard> businessCardList;
        businessCardList = db.getAllBusinessCards();
        for (IBusinessCard bc : businessCardList){
            if (bc.getId()>id){
                id = (long) bc.getId();

            }
        }
        Log.i("max id",String.valueOf(id));
        return id;
    }
}
