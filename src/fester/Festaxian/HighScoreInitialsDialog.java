package fester.Festaxian;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.text.TextUtils;
import android.text.InputFilter;

/**
 * Created by RLester on 08/09/2014.
 */
// Shows a dialog box accepting text input for initials
public class HighScoreInitialsDialog  {

  // Define our custom Listener interface
  public interface OnInitialsInputListener {
    public abstract void onInitialsInput(String initials);
  }

  OnInitialsInputListener onInitialsInputListener = null;

  // This function is called after the initials are entered
  private void OnInitialsInput(String initials){
    // Check if the Listener was set, otherwise we'll get an Exception when we try to call it
    if(onInitialsInputListener!=null) {
      // Only trigger the event, when we have valid initials
      if(!TextUtils.isEmpty(initials)){
        onInitialsInputListener.onInitialsInput(initials);
      }
    }
  }

  // Displays a text box with soft keyboard support
  public void InitialsInput(final Context context) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    final EditText input = new EditText(context) ;
    // Create a text box with a max length of 3 characters
    int maxLength = 3;
    InputFilter[] fArray = new InputFilter[2];
    fArray[0] = new InputFilter.LengthFilter(maxLength);
    fArray[1] = new InputFilter.AllCaps();
    input.setFilters(fArray);
    builder
      .setTitle("High Score")
      .setMessage("Enter your initials")
      .setView(input)
      // Define the handler for the OK button
      .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
          /*
          InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
          // Don't allow empty input
          if (!input.getText().toString().isEmpty()) {
            // hide the soft keyboard
            imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
            // Initials entered, Raise the OnInitialsInput event
            OnInitialsInput(input.getText().toString());
          }
         */
        }
      })
      // Define the handler for the cancel/close button
      .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
          InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
          // hide the soft keyboard
          imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }

      });
    // show the alert dialog
    final AlertDialog dialog = builder.create();
    dialog.show();
    Button okButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
    dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        // Don't allow empty input
        if (!input.getText().toString().isEmpty()) {
          // hide the soft keyboard
          imm.hideSoftInputFromWindow(input.getWindowToken(), 0);
          // Initials entered, Raise the OnInitialsInput event
          OnInitialsInput(input.getText().toString());
          dialog.dismiss();
        }
      }
     }
    );
    // set focus to the input text box
    input.requestFocus();
    // show the soft keyboard
    InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
  }

}
