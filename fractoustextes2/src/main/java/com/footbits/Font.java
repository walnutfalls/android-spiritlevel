package com.footbits;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.opengl.GLES20;
import android.util.Log;


public class Font {
    private static final String TAG = "Font";

    //--Constants--//
    public final static int CHAR_START = 32;           // First Character (ASCII Code)
    public final static int CHAR_END = 126;            // Last Character (ASCII Code)
    public final static int CHAR_CNT = ( ( ( CHAR_END - CHAR_START ) + 1 ) + 1 );  // Character Count (Including Character to use for Unknown)

    public final static int CHAR_NONE = 32;            // Character to Use for Unknown (ASCII Code)
    public final static int CHAR_UNKNOWN = ( CHAR_CNT - 1 );  // Index of the Unknown Character

    public final static int FONT_SIZE_MIN = 6;         // Minumum Font Size (Pixels)
    public final static int FONT_SIZE_MAX = 180;       // Maximum Font Size (Pixels)

    private final float[] charWidths;                          // Width of Each Character (Actual; Pixels)

    private int fontPadX, fontPadY;                            // Font Padding (Pixels; On Each Side, ie. Doubled on Both X+Y Axis)

    private float fontHeight;                                  // Font Height (Actual; Pixels)
    private float fontAscent;                                  // Font Ascent (Above Baseline; Pixels)
    private float fontDescent;

    private float charWidthMax;                                // Character Width (Maximum; Pixels)
    private float charHeight;                                  // Character Height (Maximum; Pixels)

    private int cellWidth, cellHeight;                         // Character Cell Width/Height

    private int rowCnt, colCnt;                                // Number of Rows/Columns

    private float scaleX, scaleY;                              // Font Scale (X,Y Axis)
    private float spaceX;                                      // Additional (X,Y Axis) Spacing (Unscaled)

    private Texture2D fontAtlas;
    private int textureSize;
    private TextureRegion[] textureRegions;

    public Font(AssetManager assets, String file, int size, int padX, int padY)
    {
        // initialize remaining members
        fontPadX = padX;                                // Set Requested X Axis Padding
        fontPadY = padY;                                // Set Requested Y Axis Padding

        fontHeight = 0.0f;
        fontAscent = 0.0f;
        fontDescent = 0.0f;

        charWidthMax = 0;
        charHeight = 0;

        cellWidth = 0;
        cellHeight = 0;
        rowCnt = 0;
        colCnt = 0;

        scaleX = 1.0f;                                  // Default Scale = 1 (Unscaled)
        scaleY = 1.0f;                                  // Default Scale = 1 (Unscaled)
        spaceX = 0.0f;

        charWidths = new float[CHAR_CNT];               // Create the Array of Character Widths
        textureRegions = new TextureRegion[CHAR_CNT];   // Create the Array of texture regions.

        load(assets, file, size);
    }

    public void load(AssetManager assets, String file, int size)
    {
        // load the font and setup paint instance for drawing
        Typeface tf = Typeface.createFromAsset( assets, file );  // Create the Typeface from Font File
        Paint paint = new Paint();                      // Create Android Paint Instance
        paint.setAntiAlias( true );                     // Enable Anti Alias
        paint.setTextSize( size );                      // Set Text Size
        paint.setColor( 0xffffffff );                   // Set ARGB (White, Opaque)
        paint.setTypeface( tf );                        // Set Typeface

        setFontMetrics(paint.getFontMetrics());  // Get Font Metrics

        setAtlasMetrics(paint, file);

        Bitmap atlasBitmap = drawAtlas(paint);

        fontAtlas = Texture2D.fromBitmap(atlasBitmap);

        setTextureRegions();

        //TODO: This might be temporary. Should allow user to control which texture unit is used.
        fontAtlas.setTextureUnit(GLES20.GL_TEXTURE0);
    }

    private void setFontMetrics(Paint.FontMetrics fm)
    {
        fontHeight = (float)Math.ceil( Math.abs( fm.bottom ) + Math.abs( fm.top ) );  // Calculate Font Height
        fontAscent = (float)Math.ceil( Math.abs( fm.ascent ) );  // Save Font Ascent
        fontDescent = (float)Math.ceil( Math.abs( fm.descent ) );  // Save Font Descent
    }

    private void setAtlasMetrics(Paint paint, String file)
    {
        // determine the width of each character (including unknown character)
        // also determine the maximum character width
        char[] s = new char[2];                         // Create Character Array
        charWidthMax = charHeight = 0;                  // Reset Character Width/Height Maximums
        float[] w = new float[2];                       // Working Width Value
        int cnt = 0;                                    // Array Counter
        for ( char c = CHAR_START; c <= CHAR_END; c++ )  {  // FOR Each Character
            s[0] = c;                                    // Set Character
            paint.getTextWidths( s, 0, 1, w );           // Get Character Bounds
            charWidths[cnt] = w[0];                      // Get Width
            if ( charWidths[cnt] > charWidthMax )        // IF Width Larger Than Max Width
                charWidthMax = charWidths[cnt];           // Save New Max Width
            cnt++;                                       // Advance Array Counter
        }
        s[0] = CHAR_NONE;                               // Set Unknown Character
        paint.getTextWidths( s, 0, 1, w );              // Get Character Bounds
        charWidths[cnt] = w[0];                         // Get Width
        if ( charWidths[cnt] > charWidthMax )           // IF Width Larger Than Max Width
            charWidthMax = charWidths[cnt];              // Save New Max Width

        // set character height to font height
        charHeight = fontHeight;                        // Set Character Height

        // find the maximum size, validate, and setup cell sizes
        cellWidth = (int)charWidthMax + ( 2 * fontPadX );  // Set Cell Width
        cellHeight = (int)charHeight + ( 2 * fontPadY );  // Set Cell Height
        int maxSize = Math.max(cellWidth, cellHeight);  // Save Max Size (Width/Height)

        if ( maxSize < FONT_SIZE_MIN)   // IF Maximum Size Outside Valid Bounds
            Log.e(TAG, "Character size of font " + file + " too small.");
        else if(maxSize > FONT_SIZE_MAX)
            Log.e(TAG, "Character size of font " + file + " too big.");

        // set texture size based on max font size (width or height)
        // NOTE: these values are fixed, based on the defined characters. when
        // changing start/end characters (CHAR_START/CHAR_END) this will need adjustment too!
        if ( maxSize <= 24 )                            // IF Max Size is 18 or Less
            textureSize = 256;                           // Set 256 Texture Size
        else if ( maxSize <= 40 )                       // ELSE IF Max Size is 40 or Less
            textureSize = 512;                           // Set 512 Texture Size
        else if ( maxSize <= 80 )                       // ELSE IF Max Size is 80 or Less
            textureSize = 1024;                          // Set 1024 Texture Size
        else                                            // ELSE IF Max Size is Larger Than 80 (and Less than FONT_SIZE_MAX)
            textureSize = 2048;                          // Set 2048 Texture Size
    }

    public Bitmap drawAtlas(Paint paint)
    {
        char[] s = new char[2];                         // Create Character Array

        // create an empty bitmap (alpha only)
        Bitmap bitmap = Bitmap.createBitmap( textureSize, textureSize, Bitmap.Config.ALPHA_8 );  // Create Bitmap
        Canvas canvas = new Canvas( bitmap );           // Create Canvas for Rendering to Bitmap
        bitmap.eraseColor( 0x00000000 );                // Set Transparent Background (ARGB)

        // calculate rows/columns
        // NOTE: while not required for anything, these may be useful to have :)
        colCnt = textureSize / cellWidth;               // Calculate Number of Columns
        rowCnt = (int)Math.ceil( (float)CHAR_CNT / (float)colCnt );  // Calculate Number of Rows

        // render each of the characters to the canvas (ie. build the font map)
        float x = fontPadX;                             // Set Start Position (X)
        float y = ( cellHeight - 1 ) - fontDescent - fontPadY;  // Set Start Position (Y)
        for ( char c = CHAR_START; c <= CHAR_END; c++ )  {  // FOR Each Character
            s[0] = c;                                    // Set Character to Draw
            canvas.drawText( s, 0, 1, x, y, paint );     // Draw Character
            x += cellWidth;                              // Move to Next Character
            if ( ( x + cellWidth - fontPadX ) > textureSize )  {  // IF End of Line Reached
                x = fontPadX;                             // Set X for New Row
                y += cellHeight;                          // Move Down a Row
            }
        }
        s[0] = CHAR_NONE;                               // Set Character to Use for NONE
        canvas.drawText( s, 0, 1, x, y, paint );        // Draw Character

        return bitmap;
    }

    public void setTextureRegions()
    {
        // setup the array of character texture regions
        float x = 0;
        float y = 0;
        for ( int c = 0; c < CHAR_CNT; c++ )  {         // FOR Each Character (On Texture)
            textureRegions[c] = new TextureRegion( textureSize, textureSize, x, y, cellWidth-1, cellHeight-1 );  // Create Region for Character
            x += cellWidth;                              // Move to Next Char (Cell)
            if ( x + cellWidth > textureSize )  {
                x = 0;                                    // Reset X Position to Start
                y += cellHeight;                          // Move to Next Row (Cell)
            }
        }
    }


    public TextureRegion getTextureRegion(char asciiCharacter)
    {
        int c = (int) asciiCharacter - CHAR_START;  // Calculate Character Index (Offset by First Char in Font)
        if (c < 0 || c >= CHAR_CNT)                // IF Character Not In Font
            c = CHAR_UNKNOWN;                         // Set to Unknown Character Index

        return textureRegions[c];
    }

    public float getCharWidth(char asciiCharacter)
    {
        int c = (int) asciiCharacter - CHAR_START;  // Calculate Character Index (Offset by First Char in Font)
        return charWidths[c];           // Add Scaled Character Width to Total Length
    }

    /**
     * If the character is unknown, returns a know replacement. Otherwise, returns the character
     * its self
     */
    public char checkIfKnown(char asciiCharacter)
    {
        int c = (int)asciiCharacter - CHAR_START;

        if (c < 0 || c >= CHAR_CNT)
            return CHAR_UNKNOWN;

        return asciiCharacter;
    }

    //////////////////////
    //getters and setters
    public int getCellHeight()
    {
        return cellHeight;
    }

    public int getCellWidth()
    {
        return cellWidth;
    }

    public int getFontPadX()
    {
        return fontPadX;
    }

    public int getFontPadY()
    {
        return fontPadY;
    }

    public float getCharHeight()
    {
        return charHeight;
    }

    public Texture2D getFontAtlas()
    {
        return fontAtlas;
    }
}
