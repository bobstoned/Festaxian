package fester.Festaxian.BossBug;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import fester.Festaxian.Movement;
import fester.Festaxian.Sprite;
import fester.Festaxian.SpriteSheet;

/**
 * Created by RLester on 18/10/2014.
 */
public class BossBugSprite extends Sprite{

  public SpriteSheet Sheet;
  public SpriteSheet VulnerableAreasSheet;
  public SpriteSheet DeathSheet;
  public Bitmap ShipBitmap;
  public Bitmap AreasBitmap;
  public Bitmap DeathBitmap;
  private Sprite[] vulnerableAreas = new Sprite[2];

  // Draws the current frame image of this sprite
  public void DrawSprite(Canvas canvas) {
    SpriteSheet sheet;
    Bitmap image;
    // set fully opaque drawing as a default
    int alphaValue = 255;
    // Choose a sprite sheet and bitmap
    // one for normal, one for dying
    if (this.Dying) {
      sheet = DeathSheet;
      image = DeathBitmap;
      // Death animation fades out
      alphaValue = this.DeathAnimation.GetAlphaValue();
    } else {
      sheet = Sheet;
      image = ShipBitmap;
    }
    Point frame;
    // get the next animation frame in the sprite sheet grid
    frame = this.GetFrame();
    // get the sprite rectangle for the given frame location
    this.SpriteRect = sheet.GetSpriteRectAt(frame.x, frame.y);
    // draw the sprite frame to the canvas
    sheet.DrawSprite(image, canvas, this.SpriteRect
            , this.Position, this.PixelSize(), this.logicalWidth, this.logicalHeight, alphaValue, 15);
  }

  // Move/update the Boss Bug end of level boss
  public void Move(Canvas canvas) {
    if (!this.Drawing && !this.Dead) {
      this.AdvanceFrame() ;
      this.Move(false);
      MoveAreas(canvas);
      this.DrawSprite(canvas);
    }
  }
  
  public void CreateVulnerableAreas() {
    vulnerableAreas[0] = CreateVulnerableArea(this.Position.x+(this.logicalWidth/5), this.Position.y+(this.logicalHeight/5));
    vulnerableAreas[1] = CreateVulnerableArea(this.Position.x+(this.logicalWidth/5)+32, this.Position.y+(this.logicalHeight/5));
  }

  private void MoveAreas(Canvas canvas) {
    int d = 0;
    for(int i=0;i<vulnerableAreas.length;i++) {
      if (!vulnerableAreas[i].Dead) {
        vulnerableAreas[i].AdvanceFrame();
        //vulnerableAreas[i].Move(false);
        int adjustedWidth = (int) (((float) this.logicalWidth / (float) 100) * (float) vulnerableAreas[i].logicalWidth);
        int adjustedHeight = (int) (((float) this.logicalHeight / (float) 120) * (float) vulnerableAreas[i].logicalHeight);
        vulnerableAreas[i].Position.x = this.Position.x + (int) (((this.logicalWidth / 2.1) + (i * adjustedWidth) - adjustedWidth) * this.PixelSize());
        vulnerableAreas[i].Position.y = this.Position.y + (int) ((this.logicalHeight - (this.logicalHeight / 2.5)) * this.PixelSize());
        Point frame;
        frame = vulnerableAreas[i].GetFrame();
        vulnerableAreas[i].SpriteRect = this.VulnerableAreasSheet.GetSpriteRectAt(frame.x, frame.y);
        if (!vulnerableAreas[i].Dead) {
          this.VulnerableAreasSheet.DrawSprite(AreasBitmap, canvas, vulnerableAreas[i].SpriteRect
                  , vulnerableAreas[i].Position, vulnerableAreas[i].PixelSize(), adjustedWidth, adjustedHeight);
        }
      }
      else {
        d++;
        if (d == vulnerableAreas.length) {
          // All parts dead so boss dies also
          this.Dying = true;
        }
      }
    }
  }
  
  private Sprite CreateVulnerableArea(int offsetX, int offSetY) {
    Sprite area = new Sprite();
    area.setPixelSize(this.PixelSize());
    area.Position = new Point(offsetX, offSetY);
    area.SpriteRect = this.VulnerableAreasSheet.GetSpriteRectAt(0,0);
    // Set the logical width and height of the area before scaling for resolution
    // this is based on the original bitmap dimensions
    area.logicalHeight = 12;
    area.logicalWidth = 12;
    area.Extent = this.Extent;
    area.Movement.delay = this.Movement.delay;
    area.Movement.setMovementStep(this.Movement.getMovementStep());
    area.Movement.setMovementPixelSize((int) this.Movement.getMovementPixelSize());
    area.Movement.Style = fester.Festaxian.Movement.MovementStyle.Cyclic;
    area.Movement.Direction = fester.Festaxian.Movement.MovementDirection.Right;
    area.Animation.FrameDelay = 100;
    area.Animation.Frames = new Point[6];

    area.Animation.Frames[0] = new Point(0,0);
    area.Animation.Frames[1] = new Point(1,0);
    area.Animation.Frames[2] = new Point(2,0);
    area.Animation.Frames[3] = new Point(3,0);
    area.Animation.Frames[4] = new Point(4,0);
    area.Animation.Frames[5] = new Point(5,0);

    area.DeathAnimation.FrameDelay = 100;
    area.DeathAnimation.Loop = false;
    area.DeathAnimation.Frames = new Point[6];
    area.DeathAnimation.Frames[0] = new Point(0,0);
    area.DeathAnimation.Frames[1] = new Point(1,0);
    area.DeathAnimation.Frames[2] = new Point(2,0);
    area.DeathAnimation.Frames[3] = new Point(3,0);
    area.DeathAnimation.Frames[4] = new Point(4,0);
    area.DeathAnimation.Frames[5] = new Point(5,0);
    return area;
  }

  @Override
  // Compares the supplied "rect" value with the bounding rect(s) of
  // this sprite
  public boolean IsCollision(Rect rect) {
    for(int i=0; i<vulnerableAreas.length;i++) {
      if (!vulnerableAreas[i].IsDyingOrDead() && rect.intersect(vulnerableAreas[i].GetBoundingBox())) {
        vulnerableAreas[i].Dying = true;
        return true;
      }
    }
    return false;
  }

  // Reset the areas to initial game start state
  public void Reset() {
    for(int i=0; i<vulnerableAreas.length;i++) {
      vulnerableAreas[i].Dying = false;
      vulnerableAreas[i].Dead = false;
      vulnerableAreas[i].DeathAnimation.Reset();
    }
  }

}
