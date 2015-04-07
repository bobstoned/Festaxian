package fester.Festaxian;

import android.graphics.Point;

/**
 * Created with IntelliJ IDEA.
 * User: rlester
 * Date: 20/04/12
 * Time: 19:08
 * To change this template use File | Settings | File Templates.
 */
public class DeathAnimation extends Animation {

  int alphaValue = 255;

  @Override
  public boolean AdvanceFrame() {
    boolean updated = super.AdvanceFrame();
    if (updated) {
      if (alphaValue <= 0) {
        alphaValue = 255;
      } else {
        alphaValue -= (int) (255 / Frames.length);
      }
    }
    return updated;
  }

  public int GetAlphaValue() {
    return alphaValue;
  }

  public void ResetAlphaValue() {
    alphaValue = 255;
  }

}
