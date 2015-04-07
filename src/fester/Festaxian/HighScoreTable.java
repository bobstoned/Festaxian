package fester.Festaxian;

import android.content.Context;

import java.io.*;
import java.lang.reflect.Array;
import android.app.AlertDialog;
import android.widget.EditText;
import android.content.DialogInterface;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by RLester on 31/08/2014.
 */
// Maintains high score table for Festaxian
public class HighScoreTable {

  String scoreFileName = "festaxianHighScores.txt";
  public String[] Scores = new String[10];

  // constructor requires context
  public HighScoreTable(Context ctx) {
    try {
      // create an input stream for the file
      FileInputStream file = ctx.openFileInput(scoreFileName);
      // buffer the reads
      BufferedReader bfr = new BufferedReader(new InputStreamReader(file));
      boolean eof = false;
      int i=0;
      String scoreLine = "";
      // keep reading from the file until no more lines
      while(!eof&&scoreLine!=null) {
        try {
          scoreLine = bfr.readLine(); // read the next line from the file
          Scores[i] = scoreLine;      // save in the array
          i++;
        }
        catch(Exception e) {
          eof = true;
          bfr.close();
        }
      }
    }
    catch(Exception e) {
      FileOutputStream outputStream;
      try {
        outputStream = ctx.openFileOutput(scoreFileName, Context.MODE_PRIVATE);
        outputStream.close();
      }
      catch(Exception ex) {
        e.printStackTrace();
      }
    }
  }

  //Add a score to the list
  public void Add(String item) {
    int insertPos = -1;
    // find the insert position
    for (int i = 0; i < 10; i++) {
      if (GetScore(item) > GetScore(Scores[i])) {
        insertPos = i;
        break;
      }
    }
    if (insertPos!=-1){
      for (int i=8;i>=insertPos;i--) {
        if (GetScore(Scores[i])!=-1) {
          Scores[i+1] = Scores[i];
        }
      }
      Scores[insertPos] = item;
    }
  }

  //Extracts the score from a table item e.g. "RPL:56570" = 56570
  //returns -1 if item is empty
  private int GetScore(String item) {
    if (item != null && item.contains(":"))
      return Integer.parseInt(item.split(":")[1]);
    else
      return -1;
  }

  //
  public void Save(Context ctx) {
    FileOutputStream outputStream;
    try {
      outputStream = ctx.openFileOutput(scoreFileName, Context.MODE_PRIVATE);
      BufferedWriter bfw = new BufferedWriter(new OutputStreamWriter(outputStream));
      for(int i=0;i<10;i++) {
        if (Scores[i] != null) {
          bfw.write(Scores[i]);
        }
        bfw.newLine();
      }
      bfw.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void SaveScore(String initials, int score) {

  }




}
