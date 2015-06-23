/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Log;

import Log.LogRecord;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 *
 * @author Mehdi
 */
public class ReadWriteFile {
    /*
     * This method return a list of request which requested within startTime and endTime.usually one hour
     */

    
    public ReadWriteFile() {
    }

    public void writeToExcel(Map<Integer,LogRecord> map, String filename) {
        HSSFWorkbook myWorkBook = new HSSFWorkbook();
        HSSFSheet mySheet = myWorkBook.createSheet();
        HSSFRow myRow = null;
        HSSFCell myCell = null;
        int rowindex = 0;
//        mySheet.setColumnWidth(2, 100);
        myRow = mySheet.createRow(rowindex++);
        myCell = myRow.createCell(0);
        myCell.setCellValue("CloudletId");

        myCell = myRow.createCell(1);
        myCell.setCellValue("Size of Files");

        myCell = myRow.createCell(2);
        myCell.setCellValue("Arrival Time");

        myCell = myRow.createCell(3);
        myCell.setCellValue("Start Time");

        myCell = myRow.createCell(4);
        myCell.setCellValue("CPU Usage Time");

        myCell = myRow.createCell(5);
        myCell.setCellValue("Waiting Time");

        myCell = myRow.createCell(6);
        myCell.setCellValue("Compelition Time");

        myCell = myRow.createCell(7);
        myCell.setCellValue("InSourced");

        myCell = myRow.createCell(8);
        myCell.setCellValue("Compute Server");

        myCell = myRow.createCell(9);
        myCell.setCellValue("Status");

        
//        myCell = myRow.createCell(9);
//        myCell.setCellValue("cloudletTimetoRunApprox (TEMP)");
//        
        

        
        for (Map.Entry<Integer,LogRecord> entry : map.entrySet()) {

            myRow = mySheet.createRow(rowindex++);
            myCell = myRow.createCell(0);
            myCell.setCellValue(entry.getKey());

            myCell = myRow.createCell(1);
            myCell.setCellValue(entry.getValue().getIoFileSize()/1000+"K" );

            myCell = myRow.createCell(2);
            myCell.setCellValue(entry.getValue().getArrivalTime());

            myCell = myRow.createCell(3);
            myCell.setCellValue(entry.getValue().getStartTime());

            myCell = myRow.createCell(4);
            myCell.setCellValue(entry.getValue().getCpuUsageTime());

            myCell = myRow.createCell(5);
            myCell.setCellValue(entry.getValue().getWaitingTime());

            myCell = myRow.createCell(6);
            myCell.setCellValue(entry.getValue().getCompelitionTime());

            myCell = myRow.createCell(7);
            myCell.setCellValue(entry.getValue().isOutsourced());

            myCell = myRow.createCell(8);
            myCell.setCellValue(entry.getValue().getComputeServerId());

            myCell = myRow.createCell(9);
            myCell.setCellValue(cloudletStatus(entry.getValue().getStatus()));

        }
        try {
            FileOutputStream out = new FileOutputStream(filename);
            myWorkBook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeSummeryToExcel(int cloudletNumber,long totalSizeFiles,double totalCpuUsage, double totalWaitingTime,
            double totalCompelitionTime,int numberInsource,int success,int unsuccess, String filename) {
        HSSFWorkbook myWorkBook = new HSSFWorkbook();
        HSSFSheet mySheet = myWorkBook.createSheet();
        HSSFRow myRow = null;
        HSSFCell myCell = null;
        int rowindex = 0;
//        mySheet.setColumnWidth(2, 100);
        myRow = mySheet.createRow(rowindex++);
        myCell = myRow.createCell(0);
        myCell.setCellValue("Cloudlet Number");

        myCell = myRow.createCell(1);
        myCell.setCellValue("Total Size of Files");

        myCell = myRow.createCell(2);
        myCell.setCellValue("Total CPU Usage Time");

        myCell = myRow.createCell(3);
        myCell.setCellValue("Total Waiting Time");

        myCell = myRow.createCell(4);
        myCell.setCellValue("Total Compelition Time");

        myCell = myRow.createCell(5);
        myCell.setCellValue("Number of InSourced");

        myCell = myRow.createCell(6);
        myCell.setCellValue("SUCCESS");

        myCell = myRow.createCell(7);
        myCell.setCellValue("UNSUCCESS");

 
            myRow = mySheet.createRow(rowindex++);
            myCell = myRow.createCell(0);
            myCell.setCellValue(cloudletNumber);

            myCell = myRow.createCell(1);
            myCell.setCellValue(totalSizeFiles);

            myCell = myRow.createCell(2);
            myCell.setCellValue(totalCpuUsage);

            myCell = myRow.createCell(3);
            myCell.setCellValue(totalWaitingTime);

            myCell = myRow.createCell(4);
            myCell.setCellValue(totalCompelitionTime);

            myCell = myRow.createCell(5);
            myCell.setCellValue(numberInsource);

            myCell = myRow.createCell(6);
            myCell.setCellValue(success);

            myCell = myRow.createCell(7);
            myCell.setCellValue(unsuccess);

        try {
            FileOutputStream out = new FileOutputStream(filename);
            myWorkBook.write(out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String cloudletStatus(int status){
        String stat;
        switch(status){
            case 0:
                stat ="CREATED";
                break;
            case 1:
                stat ="READY";
                break;
            case 2:
                stat ="QUEUED";
                break;
            case 3:
                stat ="INEXEC";
                break;
            case 4:
                stat ="SUCCESS";
                break;
            case 5:
                stat ="FAILED";
                break;
            case 6:
                stat ="CANCELED";
                break;
            case 7:
                stat ="PAUSED";
                break;
            case 8:
                stat ="RESUMED";
                break;
            case 9:
                stat ="FRU";
                break;
            default :
                stat ="UnKnown";
        }
        return stat;
    }
 
  
}
