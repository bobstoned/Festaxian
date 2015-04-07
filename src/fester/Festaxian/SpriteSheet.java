package fester.Festaxian;

import android.graphics.*;

/**
 * Created with IntelliJ IDEA.
 * User: rlester
 * Date: 18/04/12
 * Time: 23:34
 * To change this template use File | Settings | File Templates.
 */
public class SpriteSheet {

  public Point topLeft;
  public int spriteWidth ;
  public int spriteHeight ;
  public int gapX;
  public int gapY;

  public Rect GetSpriteRectAt(int col, int row) {
    int offSetx =  topLeft.x + (col * (spriteWidth + gapX) ) ;
    int offSety =  topLeft.y + (row * (spriteHeight + gapY) ) ;
    Rect sheetRect = new Rect(offSetx,offSety, offSetx+(spriteWidth), offSety+(spriteHeight));
    return sheetRect;
  }

  public void DrawSprite(Bitmap source, Canvas dest, Rect sourceRect, Point location, double scale)   {
    DrawSprite(source, dest, sourceRect, location, scale, 0, 0, 255,-1);
  }

  public void DrawSprite(Bitmap source, Canvas dest, Rect sourceRect, Point location, double scale, int width, int height)   {
    DrawSprite(source, dest, sourceRect, location, scale, width, height, 255,-1);
  }

  public void DrawSprite(Bitmap source, Canvas dest, Rect sourceRect, Point location, double scale, int width, int height, int alpha, int rotationAngle)   {
    if (width==0) width = 1;
    if (height==0) height = 1;
    int x = location.x;
    int y = location.y;
    Rect destRect ;
    if (width==0 || height ==0) {
      // Use the source rectangle's dimensions
      width = sourceRect.width();
      height = sourceRect.height();
    }
    destRect = new Rect(x, y, x + ((int)(width * scale)), y + ((int)(height * scale))) ;
    Paint paint = new Paint();
    paint.setAlpha(alpha);
    if (rotationAngle != -1) {
      Matrix matrix = new Matrix();
      // Rotate the bitmap the specified number of degrees.
      // Translate the image up and left half the height
      // and width so rotation (below) is around the center.
      float hvw = destRect.width()/2;
      float hvh = destRect.height()/2;
      dest.save();
      dest.rotate(rotationAngle, x + hvw,  y + hvh);
      dest.drawBitmap(source, sourceRect, destRect, paint);
      dest.restore();
    }
    else{
      dest.drawBitmap(source, sourceRect, destRect, paint);
    }
  }

}
