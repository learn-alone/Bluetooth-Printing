package com.example.formfill;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.data.printable.Printable;
import com.mazenrashed.printooth.data.printable.RawPrintable;
import com.mazenrashed.printooth.data.printable.TextPrintable;
import com.mazenrashed.printooth.data.printer.DefaultPrinter;
import com.mazenrashed.printooth.ui.ScanningActivity;
import com.mazenrashed.printooth.utilities.Printing;
import com.mazenrashed.printooth.utilities.PrintingCallback;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PrintingCallback {
Printing printing;
Button btn_unpair_pair,btn_print,btn_image;
EditText username,house,road,landmark,pincode,phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_print = (Button)findViewById(R.id.Printbtn);
        btn_unpair_pair = (Button)findViewById(R.id.config);
        username = (EditText)findViewById(R.id.Euser_name);
        house = (EditText)findViewById(R.id.Ehouse);
        road = (EditText)findViewById(R.id.Eroad);
        landmark = (EditText)findViewById(R.id.Elandmark);
        pincode = (EditText)findViewById(R.id.Epincode);
        phone = (EditText)findViewById(R.id.Ephone);

        if (printing != null)
            printing.setPrintingCallback(this);

        btn_unpair_pair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Printooth.INSTANCE.hasPairedPrinter())
                    Printooth.INSTANCE.removeCurrentPrinter();
                else {
                    startActivityForResult(new Intent(MainActivity.this, ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);
                    changePairandUnpair();
                }
            }
        });
        btn_print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Printooth.INSTANCE.hasPairedPrinter())
                    startActivityForResult(new Intent(MainActivity.this,ScanningActivity.class),ScanningActivity.SCANNING_FOR_PRINTER);
                else
                    printTexts();
            }
        });
    }

    private void printTexts() {
        String susername = username.getText().toString();
        String shouse = house.getText().toString();
        String sroad = road.getText().toString();
        String slandmark = landmark.getText().toString();
        String spincode = pincode.getText().toString();
        String sphone = phone.getText().toString();

        ArrayList<Printable> printables = new ArrayList<>();
        printables.add(new RawPrintable.Builder(new byte[]{27,100,41}).build());
        //add text
        printables.add(new TextPrintable.Builder()
                .setText(susername)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1).build());
        printables.add(new TextPrintable.Builder()
                .setText(shouse)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1).build());
        printables.add(new TextPrintable.Builder()
                .setText(sroad)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1).build());
        printables.add(new TextPrintable.Builder()
                .setText(slandmark)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1).build());printables.add(new TextPrintable.Builder()
                .setText(susername)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1).build());
        printables.add(new TextPrintable.Builder()
                .setText(spincode)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1).build());
        printables.add(new TextPrintable.Builder()
                .setText(sphone)
                .setCharacterCode(DefaultPrinter.Companion.getCHARCODE_PC1252())
                .setNewLinesAfter(1).build());

        printing.print(printables);
    }

    private void changePairandUnpair() {
        if (Printooth.INSTANCE.hasPairedPrinter())
            btn_unpair_pair.setText(new StringBuilder("Unpir").append(Printooth.INSTANCE.getPairedPrinter().getName().toString()));
        else
                btn_unpair_pair.setText("pair with printer");

    }

    @Override
    public void connectingWithPrinter() {
        Toast.makeText(this, "Connectng To Printer", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void connectionFailed(String s) {
        Toast.makeText(this, "connection failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onError(String s) {
        Toast.makeText(this, "Error:"+s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onMessage(String s) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void printingOrderSentSuccessfully() {
        Toast.makeText(this, "Printing", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER &&
        resultCode == Activity.RESULT_OK)
            initPrinting();
        changePairandUnpair();
    }

    private void initPrinting() {
        if (!Printooth.INSTANCE.hasPairedPrinter())
            printing = Printooth.INSTANCE.printer();
        if (printing != null)
            printing.setPrintingCallback(this);
    }
}