package happyeater.com.dateyourfood;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        View.OnClickListener {

    public static final String EXTRA_MESSAGE = "happyeater.com.dateyourfood.MESSAGE";
    Button btnDatePicker, btnAdd;
    EditText FOOD_NAME, EXPIRY_DATE, addData;
    String foodString, expiryDateString1, expiryDateString2;
    EditText EXPIRY_DAYS;
    private int mYear, mMonth, mDay;
    DatabaseHelper db = new DatabaseHelper(this);
    final Calendar c = Calendar.getInstance();

    private static final DateFormat sdf = new SimpleDateFormat("DD/MM/YYYY");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        EXPIRY_DATE=(EditText)findViewById(R.id.expiryDateInput2);
        FOOD_NAME = (EditText) findViewById(R.id.foodNameInput);
        EXPIRY_DAYS = (EditText)findViewById(R.id.expiryDateInput1);
        btnAdd = (Button) findViewById(R.id.submitData);
        //addData = (EditText) findViewById(R.id.submitData);
        btnDatePicker.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
    }

    public void alert(View v){
        Intent intent2 = new Intent(this, PlanExpiringFood.class);
        startActivity(intent2);
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        String m;
                        if (monthOfYear < 9){
                            m = "0"+(monthOfYear+1);
                        }else{
                            m = ""+(monthOfYear+1);
                        }

                        EXPIRY_DATE.setText(dayOfMonth + "/" + m + "/" + year);

                    }
                }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnAdd){
            Intent intent1 = new Intent(this, DisplayMessageActivity.class);
            EditText editText = (EditText) findViewById(R.id.foodNameInput);
            String message = editText.getText().toString();
            intent1.putExtra(EXTRA_MESSAGE, message);
            startActivity(intent1);
            foodString = FOOD_NAME.getText().toString();
            expiryDateString1 = EXPIRY_DAYS.getText().toString();
            expiryDateString2 = EXPIRY_DATE.getText().toString();

            String expiryDateString = "";
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            // Get Current Date in LocalData type
            LocalDate local_current_date = LocalDate.now();
            //if date not chosen from calendar use string 1
            if(EXPIRY_DAYS == null || EXPIRY_DAYS.getText().toString().equals("") || EXPIRY_DAYS.getText().toString().equals(" ") )
            {
                expiryDateString = expiryDateString2;
                //Part 1: We get expiration date from Google Calendar
                // Get Expiry date in local Data type
                //String expiryDateString = "26/10/2018";
                LocalDate local_expiry_date = LocalDate.parse(expiryDateString, formatter);
                // Get the difference between the two dates
                long difference = ChronoUnit.DAYS.between(local_current_date, local_expiry_date);
                int days_left = (int) difference;
                // System.out.println(days_left);
                db.insertFood(foodString, expiryDateString, days_left);
            }
            else {
                expiryDateString = expiryDateString1;
                // Part Two: We get the days left before expiry date
                // String expiry_days_string = "5";
                int expiry_days_int = Integer.parseInt(expiryDateString);
                long expiry_days_long = (long) expiry_days_int;
                LocalDate expiry_date = LocalDate.now().plusDays(expiry_days_long);
                String expiry_date_string = expiry_date.format(formatter);
                db.insertFood(foodString, expiry_date_string, expiry_days_int);
                //System.out.println(expiry_date_string);
            }
            List<Food> data = db.getListContents();
            for (Food d : data ) {
                System.out.println("ID:"+d.getID());
                System.out.println("Name:"+d.getName());
                System.out.println("Date:"+d.getExpiry_date());
                System.out.println("days:"+d.getDays_left());
            }
        }
    }


}
