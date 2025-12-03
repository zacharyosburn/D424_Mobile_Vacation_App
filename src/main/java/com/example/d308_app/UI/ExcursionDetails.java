package com.example.d308_app.UI;

import static android.widget.Toast.LENGTH_LONG;
import static com.example.d308_app.UI.MainActivity.numAlert;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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

import com.example.d308_app.R;
import com.example.d308_app.database.Repository;
import com.example.d308_app.entities.Excursion;
import com.example.d308_app.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class ExcursionDetails extends AppCompatActivity {
    String name;
    int excursionId;
    int vacationId;
    DatePickerDialog.OnDateSetListener date1;
    String date2;
    double price;
    Excursion currentExcursion;

    EditText editName;
    TextView editDate;
    final Calendar calendarStart= Calendar.getInstance();
    EditText editPrice;
    Repository repository;
    Random rand= new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_excursion_details);

        repository= new Repository(getApplication());

        String dateFormat= "MM/dd/yy";
        SimpleDateFormat sdf= new SimpleDateFormat(dateFormat, Locale.US);

        name= getIntent().getStringExtra("name");
        editName= findViewById(R.id.excursiontitletext);
        editName.setText(name);

        date2= getIntent().getStringExtra("date");
        editDate= findViewById(R.id.excursiondatetext);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Date date;
                String info= editDate.getText().toString();
                if(info.equals("")) Calendar.getInstance();
                try{
                    calendarStart.setTime(sdf.parse(info));
                } catch (ParseException e){
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, date1, calendarStart.get(Calendar.YEAR), calendarStart.get(Calendar.MONTH),
                        calendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        date1= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendarStart.set(Calendar.YEAR, year);
                calendarStart.set(Calendar.MONTH, month);
                calendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                updateLabelStart();
            }
        };

        editDate.setText(date2);

        price= getIntent().getDoubleExtra("price", 0.00);
        editPrice= findViewById(R.id.excursionpricetext);
        editPrice.setText(Double.toString(price));
        excursionId= getIntent().getIntExtra("id", -1);
        vacationId= getIntent().getIntExtra("vacaId", -1);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void updateLabelStart(){
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(calendarStart.getTime()));
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_excursion_details, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()== android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.saveExcursion) {
            String name = editName.getText().toString().trim();
            String dateStr = editDate.getText().toString().trim();
            String priceStr = editPrice.getText().toString().trim();

            if(name.isEmpty() || dateStr.isEmpty() || priceStr.isEmpty()){
                Toast.makeText(this, "All fields are required before saving!", Toast.LENGTH_LONG).show();
                return true;
            }

            double price;
            try {
                price = Double.parseDouble(priceStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Price must be a valid number!", Toast.LENGTH_LONG).show();
                return true;
            }

            Vacation vacation = null;
            for (Vacation vac : repository.getmAllVacations()) {
                if (vac.getVacationId() == vacationId) {
                    vacation = vac;
                    break;
                }
            }

            if (vacation == null) {
                Toast.makeText(this, "Cannot save excursion: Vacation does not exist.", Toast.LENGTH_LONG).show();
                return true;
            }

            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            String excursionDateString = sdf.format(calendarStart.getTime());
            List<Vacation> vacations = repository.getmAllVacations();
            for (Vacation vac : vacations) {
                if (vac.getVacationId() == vacationId) {
                    vacation = vac;
                }
            }

            Excursion excursion;
            try {
                Date excursionDate = sdf.parse(excursionDateString);
                Date startDate = sdf.parse(vacation.getStartDate());
                Date endDate = sdf.parse(vacation.getEndDate());
                if (excursionDate.before(startDate) || excursionDate.after(endDate)) {
                    Toast.makeText(this, "The date of your Excursion must be between your Vacation's Start and End dates", Toast.LENGTH_LONG).show();
                    return true;
                } else {
                    if (excursionId == -1) {
                        if (repository.getmAllExcursions().size() == 0) {
                            excursionId = 1;
                        } else {
                            excursionId = repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionId() + 1;
                        }
                        excursion = new Excursion(excursionId, editName.getText().toString(), editDate.getText().toString(), Double.parseDouble(editPrice.getText().toString()), vacationId);
                        repository.insert(excursion);
                        this.finish();
                    } else {
                        excursion = new Excursion(excursionId, editName.getText().toString(), editDate.getText().toString(), Double.parseDouble(editPrice.getText().toString()), vacationId);
                        repository.update(excursion);
                        this.finish();
                    }
                    Toast.makeText(this, "Your excursion has been saved!", LENGTH_LONG).show();
                    return true;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (item.getItemId()== R.id.notifyExcursion) {
            String dateFromScreen = editDate.getText().toString();
            String alert = "Get excited! Your excursion: " + name + ", is starting! :D";
            notificationSetter(dateFromScreen, alert);

            Toast.makeText(this, "A notification for when your Excursion starts has been set!", LENGTH_LONG).show();
            return true;
        }
        if(item.getItemId()== R.id.deleteExcursion){
            currentExcursion = null;

            for(Excursion excur: repository.getmAllExcursions()){
                if(excur.getExcursionId()== excursionId) {
                    currentExcursion= excur;
                    break;
                }
            }
            if(currentExcursion == null){
                Toast.makeText(this, "Excursion cannot be deleted if it doesn't exist", LENGTH_LONG).show();
                return true;
            }

            repository.delete(currentExcursion);
            Toast.makeText(this, "Excursion: " + currentExcursion.getExcursionName() + ", has been deleted.", LENGTH_LONG).show();
            this.finish();
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
        Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
        intent.putExtra("key", alert);
        PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
        System.out.println("numAlert Vacation = " + numAlert);
        numAlert= rand.nextInt(999999);
    }

}