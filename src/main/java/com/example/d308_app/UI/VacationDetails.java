package com.example.d308_app.UI;

import static android.app.PendingIntent.FLAG_IMMUTABLE;
import static android.widget.Toast.LENGTH_LONG;

import static com.example.d308_app.UI.MainActivity.numAlert;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308_app.R;
import com.example.d308_app.database.Repository;
import com.example.d308_app.entities.Excursion;
import com.example.d308_app.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class VacationDetails extends AppCompatActivity {
    String name;
    String stay;
    String startDate2;
    String endDate2;
    double price;
    int vacaId;
    EditText editName;
    EditText editStay;
    TextView editStartDate;
    TextView editEndDate;
    EditText editPrice;
    Repository repository;
    Vacation currentVacation;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar calendarStart= Calendar.getInstance();
    final Calendar calendarEnd= Calendar.getInstance();
    Random rand= new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab=findViewById(R.id.floatingActionButton2);

        editName= findViewById(R.id.titletext);
        editStay= findViewById(R.id.accomodationtext);

        String dateFormat= "MM/dd/yy";
        SimpleDateFormat sdf= new SimpleDateFormat(dateFormat, Locale.US);

        editStartDate= findViewById(R.id.startdatetext);
        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info= editStartDate.getText().toString();
                if(info.equals("")) Calendar.getInstance();
                try{
                    calendarStart.setTime(sdf.parse(info));
                } catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDate, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                                    calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        editEndDate= findViewById(R.id.enddatetext);
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info= editEndDate.getText().toString();
                if(info.equals("")) Calendar.getInstance();
                try{
                    calendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate, calendarEnd.get(Calendar.YEAR), calendarEnd.get(Calendar.MONTH),
                        calendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        editPrice= findViewById(R.id.pricetext);
        vacaId= getIntent().getIntExtra("id", -1);
        name= getIntent().getStringExtra("name");
        stay= getIntent().getStringExtra("stayName");
        startDate= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelStart();
            }
        };
        endDate= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarEnd.set(Calendar.YEAR, year);
                calendarEnd.set(Calendar.MONTH, month);
                calendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelEnd();
            }
        };
        price= getIntent().getDoubleExtra("price", 0.00);

        startDate2= getIntent().getStringExtra("startDate");
        endDate2= getIntent().getStringExtra("endDate");
        editName.setText(name);
        editStay.setText(stay);
        editStartDate.setText(startDate2);
        editEndDate.setText(endDate2);
        editPrice.setText(Double.toString(price));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacaId", vacaId);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getmAllExcursions()) {
            if (e.getVacationId() == vacaId) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }
    private void updateLabelStart(){
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editStartDate.setText(sdf.format(calendarStart.getTime()));
    }
    private void updateLabelEnd(){
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editEndDate.setText(sdf.format(calendarEnd.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_vacation_details, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId()== android.R.id.home){
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.savevacation) {

            String title = editName.getText().toString().trim();
            String stay = editStay.getText().toString().trim();
            String start = editStartDate.getText().toString().trim();
            String end = editEndDate.getText().toString().trim();
            String priceString = editPrice.getText().toString().trim();

            if (title.isEmpty() || stay.isEmpty() || start.isEmpty() || end.isEmpty() || priceString.isEmpty()) {
                Toast.makeText(this, "All fields are required before saving!", Toast.LENGTH_LONG).show();
                return true;
            }

            double price;
            try {
                price = Double.parseDouble(priceString);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Price must be a valid number!", Toast.LENGTH_LONG).show();
                return true;
            }

            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            try {
                Date startDate = sdf.parse(start);
                Date endDate = sdf.parse(end);

                if (endDate.before(startDate)) {
                    Toast.makeText(this, "End Date must be after the Vacation Start Date!", Toast.LENGTH_LONG).show();
                    return true;
                }

                Vacation vacation;
                if (vacaId == -1) {
                    if (repository.getmAllVacations().size() == 0) {
                        vacaId = 1;
                    } else {
                        vacaId = repository.getmAllVacations()
                                .get(repository.getmAllVacations().size() - 1)
                                .getVacationId() + 1;
                    }
                    vacation = new Vacation(vacaId, title, stay, start, end, price);
                    repository.insert(vacation);

                } else {
                    vacation = new Vacation(vacaId, title, stay, start, end, price);
                    repository.update(vacation);
                }

                Toast.makeText(this, "Vacation has been saved!", Toast.LENGTH_LONG).show();
                finish();
                return true;

            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(this, "Invalid date format!", Toast.LENGTH_LONG).show();
                return true;
            }
        }

        if(item.getItemId()== R.id.sharevacation){
            List<Excursion> excursions = repository.getAssociatedExcursions(vacaId);

            StringBuilder excursionsList = new StringBuilder();
            if (!excursions.isEmpty()) {
                excursionsList.append("\nExcursions:\n");
                for (Excursion e : excursions) {
                    excursionsList.append("• ").append(e.getExcursionName()).append("\n");
                }
            }

            String shareMessage = "Vacation Details:\n\n" + "Vacation Title: " + name + "\n" +
                    "Where we'll be staying: " + stay + "\n" + "Start Date: " + startDate2 + "\n" +
                    "End Date: " + endDate2 + excursionsList.toString();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            sendIntent.putExtra(Intent.EXTRA_TITLE, "Share Vacation Details");
            sendIntent.setType("text/plain");
            Intent shareVacation = Intent.createChooser(sendIntent, null);
            startActivity(shareVacation);
            return true;
        }

        if (item.getItemId()== R.id.notifyVacation) {
            String dateFromScreen = editStartDate.getText().toString();
            String dateFromScreen2= editEndDate.getText().toString();
            String alert = "Your Vacation: " + name + ", is starting! :D";
            notificationSetter(dateFromScreen, alert);
            alert = "Your Vacation: " + name + ", is ending... :(";
            notificationSetter(dateFromScreen2, alert);

            Toast.makeText(this, "A notification for when your Vacation starts and ends has been set!", LENGTH_LONG).show();
            return true;
        }
        if(item.getItemId() == R.id.deletevacation){

            currentVacation = null;

            for (Vacation vaca : repository.getmAllVacations()) {
                if (vaca.getVacationId() == vacaId) {
                    currentVacation = vaca;
                    break;
                }
            }

            if (currentVacation == null) {
                Toast.makeText(VacationDetails.this,"Vacation cannot be deleted if it doesn't exist", Toast.LENGTH_LONG).show();
                return true;
            }

            int numExcursions = 0;
            for (Excursion excursion : repository.getmAllExcursions()) {
                if (excursion.getVacationId() == vacaId) ++numExcursions;
            }

            if (numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, "Vacation: " + currentVacation.getVacationName() + ", has been deleted", Toast.LENGTH_LONG).show();
                finish();
            } else {
                Toast.makeText(VacationDetails.this, "Vacation cannot be deleted until associated excursions are removed", Toast.LENGTH_LONG).show();
            }

            return true;
        }
        return true;
    }
    public void notificationSetter(String dateFromScreen, String alert) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Long trigger = myDate.getTime();
        Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
        intent.putExtra("key", alert);
        PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        System.out.println("numAlert Vacation = " + numAlert);
        numAlert= rand.nextInt(999999);
    }
}