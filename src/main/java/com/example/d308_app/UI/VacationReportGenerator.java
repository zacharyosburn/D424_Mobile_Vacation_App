package com.example.d308_app.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;

import androidx.core.content.FileProvider;

import com.example.d308_app.entities.Vacation;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationReportGenerator {

    public static void generateReport(Context context, List<Vacation> vacations, File file){
        PdfDocument pdf = new PdfDocument();
        Paint paint = new Paint();
        Paint headerPaint = new Paint();
        Paint linePaint = new Paint();

        int pageWidth = 600;
        int pageHeight = 850;
        int margin = 40;
        int x = 80;
        int rowHeight = 25;
        int pageNumber = 1;
        String reportTitle= "Vacation Report";
        String timeStamp= new SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.US).format(new Date());

        headerPaint.setColor(Color.BLACK);
        headerPaint.setFakeBoldText(true);
        headerPaint.setTextSize(12);

        paint.setColor(Color.BLACK);
        paint.setTextSize(11);

        linePaint.setColor(Color.BLACK);
        linePaint.setStrokeWidth(1);

        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create();
        PdfDocument.Page page = pdf.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Paint titlePaint = new Paint();
        titlePaint.setTextSize(18);
        titlePaint.setFakeBoldText(true);
        titlePaint.setColor(Color.BLACK);
        float titleWidth= titlePaint.measureText(reportTitle);
        canvas.drawText(reportTitle, ((pageWidth-titleWidth) / 2) - 5, x, titlePaint);
        x += 40;

        int[] colX = {margin, 90, 200, 300, 400, 480};
        String[] headers = {"ID", "Name", "Stay", "Start", "End", "Price"};

        for (int i = 0; i < headers.length; i++) {
            canvas.drawText(headers[i], colX[i], x, headerPaint);
        }

        x += 10;
        canvas.drawLine(margin, x, pageWidth - margin, x, linePaint);
        x += 15;

        double totalPrice = 0.0;

        for (Vacation v : vacations) {
            if (x > pageHeight - 100) {
                pdf.finishPage(page);
                pageNumber++;
                pageInfo = new PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create();
                page = pdf.startPage(pageInfo);
                canvas = page.getCanvas();
                x = 80;
            }

            canvas.drawText(String.valueOf(v.getVacationId()), colX[0], x, paint);
            canvas.drawText(v.getVacationName(), colX[1], x, paint);
            canvas.drawText(v.getVacationStay(), colX[2], x, paint);
            canvas.drawText(v.getStartDate(), colX[3], x, paint);
            canvas.drawText(v.getEndDate(), colX[4], x, paint);
            canvas.drawText(String.format("$%.2f", v.getPrice()), colX[5], x, paint);

            x += rowHeight;
            totalPrice += v.getPrice();
        }

        x += 20;
        Paint summaryPaint = new Paint();
        summaryPaint.setFakeBoldText(true);
        summaryPaint.setTextSize(13);
        canvas.drawLine(margin, x, pageWidth - margin, x, linePaint);
        x += 25;
        canvas.drawText("Total Vacations: " + vacations.size(), margin, x, summaryPaint);
        x += 20;
        canvas.drawText("Total Cost of Vacations: $" + String.format("%.2f", totalPrice), margin, x, summaryPaint);

        addFooterTimestamp(canvas, pageWidth, pageHeight, margin, timeStamp, pageNumber);

        pdf.finishPage(page);

        try(FileOutputStream out= new FileOutputStream(file)){
            pdf.writeTo(out);
        }catch (IOException e){
            e.printStackTrace();
        }
        pdf.close();
    }

    private static void addFooterTimestamp(Canvas canvas, int pageWidth, int pageHeight, int margin, String timestamp, int pageNumber) {
        Paint footerPaint = new Paint();
        footerPaint.setTextSize(10);
        footerPaint.setColor(Color.DKGRAY);

        String footerText = "Report generated on " + timestamp + "  |  Page " + pageNumber;
        float textWidth = footerPaint.measureText(footerText);

        canvas.drawText(footerText, (pageWidth - textWidth) / 2, pageHeight - 30, footerPaint);
    }

    public static void viewReport(Context context, File file){
        Uri uri= FileProvider.getUriForFile(context, "com.example.d308_app.fileprovider", file);

        Intent intent= new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "application/pdf");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

        context.startActivity(Intent.createChooser(intent, "Open Report"));
    }
}
