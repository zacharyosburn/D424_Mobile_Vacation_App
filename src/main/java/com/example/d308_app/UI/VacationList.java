package com.example.d308_app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import androidx.appcompat.widget.SearchView;

import android.widget.Button;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VacationList extends AppCompatActivity {

    private Repository repository;
    private SearchView searchView;
    private VacationAdapter vacationAdapter;
    private List<Vacation> allVacations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        FloatingActionButton fab=findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        RecyclerView recyclerView = findViewById(R.id.vacationListRecyclerView);
        repository = new Repository(getApplication());
        allVacations = repository.getmAllVacations();

        vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

        searchView = findViewById(R.id.searchView);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                filterList(query);
                return true;
            }
        });

        Button generateReportButton = findViewById(R.id.generateReportButton);
        generateReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Vacation> allVacations= repository.getmAllVacations();

                if(allVacations == null || allVacations.isEmpty()){
                    Toast.makeText(VacationList.this, "There are currently no vacations to report on",
                            Toast.LENGTH_LONG).show();
                    return;
                }

                for(Vacation vaca : allVacations){
                    double excursionCost= repository.getTotalExcursionCost(vaca.getVacationId());
                    vaca.setPrice(vaca.getPrice()+ excursionCost);
                }

                File file= new File(getExternalFilesDir(null), "Vacation Report.pdf");
                VacationReportGenerator.generateReport(VacationList.this, allVacations, file);
                VacationReportGenerator.viewReport(VacationList.this, file);
            }
        });

    }

    private void filterList(String text) {
        List<Vacation> filteredList = new ArrayList<>();
        for (Vacation v : allVacations) {
            if (v.getVacationName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(v);
            }
        }

        vacationAdapter.setVacations(filteredList);
    }

    @Override
    protected void onResume() {
        super.onResume();
        allVacations = repository.getmAllVacations();
        vacationAdapter.setVacations(allVacations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == R.id.userLogout) {
            getSharedPreferences("login", MODE_PRIVATE).edit().clear().apply();

            Intent intent = new Intent(VacationList.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();

            Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}