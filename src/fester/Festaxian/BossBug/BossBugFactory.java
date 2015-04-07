package fester.Festaxian.BossBug;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.Rect;
import fester.Festaxian.Movement;
import fester.Festaxian.R;
import fester.Festaxian.Sprite;
import fester.Festaxian.SpriteSheet;
import android.graphics.*;

import java.util.ArrayList;

/**
 * Created by RLester on 16/10/2014.
 */
// Factory for creating boss bugs
public class BossBugFactory {

  public BossBugFactory(Context context) {
    init(context);
  }

  public Rect GameArea = new Rect();
  public double PixelSize = 1;
  private Bitmap ShipBitmap;
  private Bitmap AreasBitmap;
  private Bitmap deathBitmap;
  public SpriteSheet Sheet = new SpriteSheet();
  public SpriteSheet VulnerableAreaSheet = new SpriteSheet();
  public SpriteSheet deathSheet = new SpriteSheet();
  public ArrayList<BossBugSprite> Ships = new  ArrayList<BossBugSprite>();

  private void init(Context context) {
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inScaled = false;
    options.inDensity = 100;
    ShipBitmap =  BitmapFactory.decodeResource(context.getResources(), R.drawable.boss_bug_sheet, options);
    Sheet.topLeft = new Point(0,0);
    Sheet.gapX =0;
    Sheet.gapY =0;
    Sheet.spriteHeight = 400;
    Sheet.spriteWidth = 400;

    AreasBitmap =  BitmapFactory.decodeResource(context.getResources(), R.drawable.pulseball_sheet, options);
    VulnerableAreaSheet.topLeft = new Point(0,0);
    VulnerableAreaSheet.gapX =0;
    VulnerableAreaSheet.gapY =0;
    VulnerableAreaSheet.spriteHeight = 32;
    VulnerableAreaSheet.spriteWidth = 32;

    deathBitmap =  BitmapFactory.decodeResource(context.getResources(), R.drawable.explosion_sheet, options);
    deathSheet.topLeft = new Point(0,0);
    deathSheet.gapX =0;
    deathSheet.gapY =0;
    deathSheet.spriteHeight = 64;
    deathSheet.spriteWidth = 64;
  }

  public void AddShip(int resizePercent, long moveDelay, int yPos) {
    Ships.add(CreateBossBug(resizePercent, moveDelay, yPos));
  }

  public void DrawSprites(Canvas canvas) {
    for(int i=0;i<Ships.toArray().length-1;i++) {
      Ships.get(i).DrawSprite(canvas);
    }
  }

  public void MoveSprites(Canvas canvas) {
    for(int i=0;i<=Ships.toArray().length-1;i++) {
      Ships.get(i).Move(canvas);
    }
  }

  public void ResetShips() {
    for(int i=0;i<=Ships.toArray().length-1;i++) {
      Ships.get(i).Reset();
    }
  }

  public BossBugSprite CreateBossBug(int resizePercent, long moveDelay, int yPos) {
    BossBugSprite bossBugShip = new BossBugSprite();
    bossBugShip.setPixelSize(PixelSize);
    bossBugShip.Sheet = this.Sheet;
    bossBugShip.ShipBitmap = this.ShipBitmap;
    bossBugShip.VulnerableAreasSheet = this.VulnerableAreaSheet;
    bossBugShip.AreasBitmap = this.AreasBitmap;
    bossBugShip.DeathSheet = this.deathSheet;
    bossBugShip.DeathBitmap = this.deathBitmap;
    bossBugShip.Dying = false;
    bossBugShip.Dead = false;
    bossBugShip.logicalWidth = 100;
    bossBugShip.logicalHeight = 120;
    bossBugShip.logicalWidth =  (int)(bossBugShip.logicalWidth * ((double)resizePercent / 100));
    bossBugShip.logicalHeight = (int)(bossBugShip.logicalHeight * ((double)resizePercent / 100));
    bossBugShip.Extent =  new Rect(GameArea.left,GameArea.bottom-220,GameArea.right,GameArea.bottom);
    bossBugShip.SpriteRect = Sheet.GetSpriteRectAt(0, 0);

    bossBugShip.Movement.Direction = Movement.MovementDirection.Right;
    bossBugShip.Movement.Style = Movement.MovementStyle.Limit;
    bossBugShip.Movement.delay = moveDelay;
    bossBugShip.Movement.setMovementStep(0.3 * PixelSize);
    bossBugShip.Movement.Style = Movement.MovementStyle.Cyclic;

    bossBugShip.Animation.Frames = new Point[2] ;
    bossBugShip.Animation.Frames[0] = new Point(0,0);
    bossBugShip.Animation.Frames[1] = new Point(1,0);
    bossBugShip.Animation.FrameDelay = 400;
    bossBugShip.Animation.Loop = true;
    bossBugShip.Position = new Point( (GameArea.width() - 60)/2, GameArea.top + (int)(yPos * PixelSize) ) ;

    bossBugShip.DeathAnimation.FrameDelay = 100;
    bossBugShip.DeathAnimation.Loop = false;
    bossBugShip.DeathAnimation.Frames = new Point[20];
    bossBugShip.DeathAnimation.Frames[0] = new Point(0,0);
    bossBugShip.DeathAnimation.Frames[1] = new Point(1,0);
    bossBugShip.DeathAnimation.Frames[2] = new Point(2,0);
    bossBugShip.DeathAnimation.Frames[3] = new Point(3,0);
    bossBugShip.DeathAnimation.Frames[4] = new Point(4,0);
    bossBugShip.DeathAnimation.Frames[5] = new Point(0,1);
    bossBugShip.DeathAnimation.Frames[6] = new Point(1,1);
    bossBugShip.DeathAnimation.Frames[7] = new Point(2,1);
    bossBugShip.DeathAnimation.Frames[8] = new Point(3,1);
    bossBugShip.DeathAnimation.Frames[9] = new Point(4,1);
    bossBugShip.DeathAnimation.Frames[10] = new Point(0,2);
    bossBugShip.DeathAnimation.Frames[11] = new Point(1,2);
    bossBugShip.DeathAnimation.Frames[12] = new Point(2,2);
    bossBugShip.DeathAnimation.Frames[13] = new Point(3,2);
    bossBugShip.DeathAnimation.Frames[14] = new Point(4,2);
    bossBugShip.DeathAnimation.Frames[15] = new Point(0,3);
    bossBugShip.DeathAnimation.Frames[16] = new Point(1,3);
    bossBugShip.DeathAnimation.Frames[17] = new Point(2,3);
    bossBugShip.DeathAnimation.Frames[18] = new Point(3,3);
    bossBugShip.DeathAnimation.Frames[19] = new Point(4,3);
    // Create the sprites which define vulnerable areas
    // within the main ship
    bossBugShip.CreateVulnerableAreas();
    return bossBugShip;
  }

}
