package hu.bme.aut.androidwallet;

import android.content.Context;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends AppCompatActivity {
    private EditText nameEditText;
    private EditText amountEditText;
    private ToggleButton typeChooserButton;
    private Button saveButton;
    private LinearLayout listOfRows;
    private LayoutInflater inflater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nameEditText = findViewById(R.id.salary_name);
        amountEditText = findViewById(R.id.salary_amount);
        typeChooserButton = findViewById(R.id.expense_or_income);
        saveButton = findViewById(R.id.save_button);
        listOfRows = findViewById(R.id.list_of_rows);

        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (nameEditText.getText().toString().isEmpty() || amountEditText.getText().toString().isEmpty()){
                    Toast.makeText(MainActivity.this, R.string.warn_message, Toast.LENGTH_LONG).show();
                    return;
                }
                View rowItem = inflater.inflate(R.layout.salary_row, null);
                ImageView icon = rowItem.findViewById(R.id.salary_direction_icon);
                TextView rowItemSalaryName = rowItem.findViewById(R.id.row_salary_name);
                TextView rowItemSalaryAmount = rowItem.findViewById(R.id.row_salary_amount);
                icon.setImageResource(typeChooserButton.isChecked() ? R.drawable.expense : R.drawable.income);
                rowItemSalaryName.setText(nameEditText.getText().toString());
                rowItemSalaryAmount.setText(amountEditText.getText().toString());
                listOfRows.addView(rowItem);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_delete_all) {
            listOfRows.removeAllViews();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}