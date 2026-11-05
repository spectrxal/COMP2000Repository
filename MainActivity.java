package com.example.roombookingapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    LinearLayout layout;

    ArrayList<Booking> bookings = new ArrayList<>();

    String[] rooms = {
            "SMB101",
            "SMB102",
            "SMB103",
            "SMB104",
            "SMB105",
            "SMB106",
            "Seminar 1",
            "Seminar 2"
    };

    String selectedRoom = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bookings.add(new Booking(
                "SMB102",
                "12 November 2026",
                "11.00"
        ));

        showLoginScreen();
    }

    private void createScreen(String title){
        ScrollView scrollView = new ScrollView(this);
        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(30, 40,30,30);
        layout.setBackgroundColor(Color.WHITE);
        scrollView.addView(layout);
        setContentView(scrollView);
        TextView heading = new TextView(this);
        heading.setText(title);
        heading.setTextSize(26);
        heading.setTextColor(Color.rgb(35,65,90));

        heading.setPadding(0,20,0,40);

        layout.addView(heading);
    }

    private void addText(String text){
        TextView textView = new TextView(this);

        textView.setText(text);
        textView.setTextSize(17);
        textView.setTextColor(Color.DKGRAY);
        textView.setPadding(0,15,0,15);

        layout.addView(textView);
    }

    private EditText addInput(String hint){
        EditText input = new EditText(this);
        input.setHint(hint);
        input.setTextSize(16);

        layout.addView(input);

        return input;
    }

    private void addButton(String text, Runnable action){
        Button button = new Button(this);
        button.setText(text);
        layout.addView(button);

        button.setOnClickListener(v -> action.run());
    }

    private void showLoginScreen() {
        createScreen("Employee Login");
        addText("Please enter your employee details.");
        EditText email = addInput("Email address");
        EditText password = addInput("Password");

        password.setInputType(129);

        addButton("Login", () -> {
            if(email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                Toast.makeText(
                        this,
                        "Please enter your login details",Toast.LENGTH_SHORT
                ).show();
            } else {
            showHomeScreen();
            }
        });
    }



    private void showHomeScreen(){
        createScreen("Welcome Employee!");

        addText("What would you like to do today?");

        addButton("Request to book a room", () ->{
            showAvailableRooms();
        });

        addButton("Request to cancel a room", () -> {
            showBookings();
        });

        addButton("Logout", () -> {
            showLoginScreen();
        });
    }

    private void showAvailableRooms() {
        createScreen("Available Rooms");

        addText("Select a room to continue.");

        for (String room : rooms) {
            boolean booked = isRoomBooked(room, "", "");

            if (!booked) {
                addText(room + " - Free");

                addButton("Book " + room, () -> {
                    selectedRoom = room;

                    showBookingScreen();
                });
            }
        }

        addButton("Back to Home", () -> {
            showHomeScreen();
        });
    }

    private void showBookingScreen() {
        createScreen("Request to book a room");
        addText("Selected room: " + selectedRoom);

        EditText date = addInput("Enter date (e.g. 12 November 2026)");
        EditText time = addInput("Enter time (e.g. 11:00)");

        addButton("Confirm Booking", () -> {
            String bookingDate = date.getText().toString();

            String bookingTime = time.getText().toString();

            if (bookingDate.isEmpty() || bookingTime.isEmpty()) {
                Toast.makeText(
                        this,
                        "Please enter a date and time",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            if (isRoomBooked(selectedRoom, bookingDate, bookingTime)){
                Toast.makeText(
                        this,
                        "This room is already booked at that time",
                        Toast.LENGTH_SHORT
                ).show();

                return;
            }

            bookings.add(new Booking(
                    selectedRoom,
                    bookingDate,
                    bookingTime
            ));

            Toast.makeText(
                    this,
                    "Booking successful!",
                    Toast.LENGTH_SHORT
            ).show();

            showBookings();
        });

        addButton("Back to Available Rooms", () -> {
            showAvailableRooms();
        });
    }

    private void showBookings() {
        createScreen("My Current Bookings");

        if (bookings.isEmpty()){
            addText("You currently have no bookings.");
        } else {
            for(Booking booking: bookings){
                addText(
                        booking.room
                        + "\nDate: " + booking.date
                        + "\nTime: " + booking.time
                );

                addButton("Cancel this booking", () -> {
                    confirmCancellation(booking);
                });
            }
        }

        addButton("Back to Home", () -> {
            showHomeScreen();
        });
    }

    private void confirmCancellation(Booking booking){
        new AlertDialog.Builder(this)
                .setTitle("Cancel Booking")

                .setMessage(
                        "Are you sure you want to cancel "
                        + booking.room + " on "
                        + booking.date + " at "
                        + booking.time + "?"
                )

                .setPositiveButton("Yes, Cancel", (dialog, which) -> {
                    bookings.remove(booking);

                    Toast.makeText(
                            this,
                            "Booking cancelled successfully",
                            Toast.LENGTH_SHORT
                    ).show();

                    showBookings();
                })

                .setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                })

                .show();
    }

    private static class Booking {
        String room;
        String date;
        String time;

        Booking(String room, String date, String time) {
            this.room = room;
            this.date = date;
            this.time = time;
        }
    }

    private boolean isRoomBooked(String room, String date, String time){
        for(Booking booking : bookings){
            if(booking.room.equals(room)
            && booking.date.equals(date)
            && booking.time.equals(time)) {
                return true;
            }
        }

        return false;
    }
}