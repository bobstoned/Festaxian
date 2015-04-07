package fester.Festaxian;

import android.content.Context;
import android.graphics.*;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import fester.Festaxian.R;

/**
 * Created with IntelliJ IDEA.
 * User: RLester
 * Date: 07/06/12
 * Time: 22:37
 * To change this template use File | Settings | File Templates.
 */
public class FestaxianTitleScreen {

  public GalaxianGameEngine gameEngine = new GalaxianGameEngine();
  Galaxian[] scoreShips = new Galaxian[4] ;
  Galaxian attackShip ;
  Sprite playerShip;
  Bitmap pointsTextBitmap ;
  Sprite pointsTextSprite ;
  Bitmap titleLogoBitmap;
  SpriteTimer[] scoreTimers = new SpriteTimer[4];
  Rect logoRect;
  Rect logoDestRect;
  SpriteTimer screenAlternateTimer = new SpriteTimer(10000); // alternate title screens, 10 seconds
  int pageNo = 1; // title page index
  String[] highScores ;

  public void DrawTitleScreen(Canvas canvas, Context context) {
    canvas.drawBitmap(titleLogoBitmap, logoRect, logoDestRect, null);
    attackShip.Update();
    attackShip.MoveFormation();
    attackShip.Move(false);
    // Update the current page of the title screen
    UpdatePage(canvas);
    DrawSprite(attackShip, canvas);
    DisplayInstructions(canvas);
    //DrawText(pointsTextSprite, canvas);
  }

  private void UpdatePage(Canvas canvas) {
    if (screenAlternateTimer.Expired()) {
      if (pageNo == 2) {
        pageNo = 1;
      } else {
        pageNo++;
      }
    }
    switch(pageNo){
      case 1: DrawPage1(canvas);
        break;
      case 2: DrawPage2(canvas);
        break;
    }
  }

  // draw page 1, scores
  private void DrawPage1(Canvas canvas) {
    // Update the ships displaying scoring to the user
    for(int i=0; i<4; i++) {
      if (scoreTimers[i].Expired())
        scoreTimers[i].Enabled = false;
      if (!scoreTimers[i].Enabled)  {
        scoreShips[i].Update();
        scoreShips[i].MoveStandard();
        DrawSprite( scoreShips[i], canvas);
        if (scoreShips[i].Movement.Complete)
          DisplayScore(canvas, scoreShips[i], i) ;
      }
    }
  }

  private void DrawPage2(Canvas canvas) {
    Paint paint = new Paint();
    paint.setColor(Color.YELLOW);
    paint.setStyle(Paint.Style.FILL);
    paint.setTypeface(gameEngine.retroFont);
    paint.setTextSize( (int)(10 * gameEngine.pixelSize));
    canvas.drawText("HIGH SCORES", logoDestRect.left, logoDestRect.bottom+30, paint);
    paint.setColor(Color.GREEN);
    for (int i=0;i<10;i++){
      if (highScores[i] != null) {
        String[] pair = highScores[i].split(":");
        if (pair.length==2) {
          String initials = pair[0];
          String score = pair[1];
          canvas.drawText(initials, logoDestRect.left, logoDestRect.bottom + 30 + (int) ((i + 2) * 12 * gameEngine.pixelSize), paint);
          canvas.drawText(score, logoDestRect.left + (int) (10 * gameEngine.pixelSize * 5), logoDestRect.bottom + 30 + (int) ((i + 2) * 12 * gameEngine.pixelSize), paint);
        }
      }
    }
  }

  public void Init(Context context, int width, int height, String[] scores) {
    this.highScores = scores;
    screenAlternateTimer.Enabled = true;
    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(context);
    String graphicsStyle = sharedPref.getString("pref_graphics_style", "");
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inScaled = false;
    options.inDensity = 100;
    playerShip = new Sprite();
    gameEngine.SetScreenDimensions(width, height);
    gameEngine.init(context);
    // Define the sprite sheet for the galaxians
    gameEngine.sheet.gapX = 0;
    gameEngine.sheet.gapY = 0;
    gameEngine.sheet.topLeft = new Point(0,0);
    gameEngine.sheet.spriteHeight = 80;
    gameEngine.sheet.spriteWidth = 80;

    logoRect = new Rect(0,0,400,126);
    int logoWidth = (int)(200 * gameEngine.pixelSize);
    int logoHeight = (int)(63 * gameEngine.pixelSize);
    int logoxBorder = (gameEngine.screenWidth-logoWidth)/2;
    int logoyBorder = (int)(gameEngine.screenHeight * 0.01);
    logoDestRect = new Rect(logoxBorder,logoyBorder,logoWidth + logoxBorder,logoyBorder+logoHeight);

    if (graphicsStyle.equals("1"))  {
        // Define the sprite sheet for the classic galaxian graphics
        gameEngine.sheet.gapX = 8;
        gameEngine.sheet.gapY = 8;
        gameEngine.sheet.topLeft = new Point(111,0);
        gameEngine.sheet.spriteHeight = 12;
        gameEngine.sheet.spriteWidth = 12;
        //pixelSize = 2;
        gameEngine.sheetBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.galaxian_sheet, options)  ;
    }
    else {
        // Define the sprite sheet for the modern galaxian graphics
        gameEngine.sheet.gapX = 0;
        gameEngine.sheet.gapY = 0;
        gameEngine.sheet.topLeft = new Point(0,0);
        gameEngine.sheet.spriteHeight = 80;
        gameEngine.sheet.spriteWidth = 80;
        //pixelSize = 0.5;
        gameEngine.sheetBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.galaxian_sheet_modern, options) ;
    }

    attackShip = gameEngine.CreateGalaxian(0, 1, 1.5);
    attackShip.Position.x = 100;
    attackShip.Position.y = 150;
    attackShip.Player = playerShip;

    attackShip.Movement.Direction = Movement.MovementDirection.Right;
    attackShip.Movement.Style = Movement.MovementStyle.Cyclic;
    attackShip.Movement.delay = 10;
    attackShip.attackMovement.delay = 5;
    attackShip.attackMovement.setMovementPixelSize(4);
    attackShip.AttackTimer.delay = 5000;
    attackShip.AttackTimer.Enabled = true;
    attackShip.AttackTimer.Expired();

    scoreShips[0] = CreateScoreGalaxian(0, 0) ;
    scoreShips[1] = CreateScoreGalaxian(1, 1) ;
    scoreShips[2] = CreateScoreGalaxian(2, 2) ;
    scoreShips[3] = CreateScoreGalaxian(3, 3) ;


    pointsTextSprite = new Sprite();
    pointsTextBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.points, options)  ;
    Rect pointsTextRect =  new Rect(0,0,69,22);
    pointsTextSprite.SpriteRect = pointsTextRect;
    pointsTextSprite.Position =  new Point(200,200);

    titleLogoBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.title_logo, options)  ;
    playerShip.Position = new Point(250, gameEngine.gameArea.bottom-50);
  }

  private Galaxian CreateScoreGalaxian(int shipType, int row) {
    Galaxian attackShip;
    attackShip = gameEngine.CreateGalaxian(shipType, 5, 1);
    attackShip.Extent = new Rect(logoDestRect.left, 0, gameEngine.gameArea.width()+ 120, gameEngine.gameArea.height()) ;
    attackShip.Movement.Direction = Movement.MovementDirection.Left;
    attackShip.Movement.Style = Movement.MovementStyle.Limit;
    attackShip.Movement.delay = 5;
    attackShip.Movement.setMovementPixelSize((int)gameEngine.pixelSize);

    attackShip.attackMovement.delay = 5;
    attackShip.attackMovement.Direction = Movement.MovementDirection.Left;
    attackShip.attackMovement.Style = Movement.MovementStyle.Limit;
    attackShip.attackMovement.setMovementPixelSize(4);
    attackShip.AttackTimer.delay = 5000 + (row * 2000);
    attackShip.AttackTimer.Enabled = false;
    attackShip.AttackTimer.Expired();

    attackShip.Position.x = gameEngine.gameArea.width()+ 100;
    attackShip.Position.y = logoDestRect.bottom + (int)(row * ( ((double)gameEngine.TitlePageShipSize*gameEngine.pixelSize)+ (5 * gameEngine.pixelSize)) );
    attackShip.Player = playerShip;

    scoreTimers[row] = new SpriteTimer();
    scoreTimers[row].Enabled = true;
    scoreTimers[row].Expired();
    scoreTimers[row].delay = 2000 + (row * 4000);

    return attackShip;
  }

  private void DisplayScore(Canvas canvas, Galaxian ship, int row) {
    Paint paint = new Paint();
    paint.setColor(Color.YELLOW);
    paint.setStyle(Paint.Style.FILL);
    paint.setTypeface(gameEngine.retroFont);
    paint.setTextSize( (int)(10 * gameEngine.pixelSize));
    int x = logoDestRect.left + (int)(32 *  gameEngine.pixelSize) + 10;
    int y = logoDestRect.bottom + (int)((32 * gameEngine.pixelSize) /2)
            + (row * (int)( ((double)gameEngine.TitlePageShipSize*gameEngine.pixelSize)+(5 * gameEngine.pixelSize)) );
    canvas.drawText(Integer.toString(ship.getScore())  + " points", x, y, paint);
  }

  // Simply displays the start instructions at the bottom
  // of the screen
  private void DisplayInstructions(Canvas canvas) {
    Paint paint = new Paint();
    paint.setColor(Color.BLUE);
    paint.setStyle(Paint.Style.FILL);
    paint.setTextSize((int)(20 * gameEngine.pixelSize));
    int textWidth = GetTextWidth("Touch screen to start", paint);
    canvas.drawText("Touch screen to start", (gameEngine.screenWidth - textWidth)/2, gameEngine.screenHeight - (paint.getTextSize() * 2), paint);
    paint.setColor(Color.MAGENTA);
    paint.setStyle(Paint.Style.FILL);
    paint.setTextSize((int)(10 * gameEngine.pixelSize));
    textWidth = GetTextWidth("Rob Lester (2014)", paint);
    canvas.drawText("Rob Lester (2014)", (gameEngine.screenWidth - textWidth)/2, gameEngine.screenHeight - paint.getTextSize(), paint);
  }

  private int GetTextWidth(String text, Paint paint) {
    Rect bounds = new Rect();
    paint.getTextBounds(text,0,text.length(),bounds);
    return bounds.width();
  }

  private void DrawText(Sprite sprite, Canvas canvas) {
    canvas.drawBitmap(pointsTextBitmap, sprite.SpriteRect, sprite.GetBoundingBox(), null);
  }

  private void DrawSprite(Galaxian sprite, Canvas canvas) {
    if (sprite.Position == null)
      sprite.Position = new Point(20,20);

    if (!sprite.Drawing && !sprite.Dead) {
      Point frame;
      frame = sprite.GetFrame();
      sprite.SpriteRect = gameEngine.sheet.GetSpriteRectAt(frame.x, frame.y);
      // Draw the sprite using a sprite(Rect) from the sprite sheet
      gameEngine.sheet.DrawSprite(gameEngine.sheetBitmap, canvas, sprite.SpriteRect
              , sprite.Position, gameEngine.pixelSize, gameEngine.TitlePageShipSize, gameEngine.TitlePageShipSize, 255, -1);
    }
  }

}
