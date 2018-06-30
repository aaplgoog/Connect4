package com.fehaja.connect4;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import java.util.Locale;


public class MainActivity extends ActionBarActivity //implements GestureDetector.OnDoubleTapListener
{
/*
    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onDoubleTap(MotionEvent e) {
        Log.d("DOUBLETAP", e.toString());
        undo();

        return true;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        Log.d("DOUBLETAPEVENT", e.toString());

        return true;
    }
*/

    ImageAdapter m_ia;
    //boolean m_bBlue = true;
    boolean m_bBlueFirst = true;
    GridView gridview;
    TextView m_tv;
    boolean m_bGameOver = false;
    Button m_btNew;
    Button m_btUndo;
    TextToSpeech tts;
    GestureDetectorCompat m_detector;
    boolean m_bUseAI = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_new);

        gridview = (GridView) findViewById(R.id.gridview);
        int nLayoutParam =  (Math.min (getResources().getDisplayMetrics().widthPixels, getResources().getDisplayMetrics().heightPixels) - 100) / 7;
        gridview.setColumnWidth(nLayoutParam);
        //gridview.setColumnWidth((int) (nLayoutParam / getResources().getDisplayMetrics().density));


    /*    Log.d ("blahblah", ""+getResources().getDisplayMetrics() +
                " width: " + getResources().getDisplayMetrics().widthPixels +
                "height: " + getResources().getDisplayMetrics().heightPixels +
                "density: " + getResources().getDisplayMetrics().density +
                "dpi: " + getResources().getDisplayMetrics().densityDpi +
                "\n imageSize: " + nLayoutParam + "\n columnWidth = " + (int) (nLayoutParam / getResources().getDisplayMetrics().density)
        );
*/
        Log.d("blahblah", "MeasuredHeight: " + gridview.getMeasuredHeight() + "\nMeasuredWidth;" + gridview.getMeasuredWidth());





                m_ia = new ImageAdapter(this, nLayoutParam );
        gridview.setAdapter(m_ia);
        m_detector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener(){
            public boolean onDoubleTap(MotionEvent e) {
      //          Toast.makeText(MainActivity.this, "double tap", Toast.LENGTH_SHORT).show();
                undo();
                return true;
            }
        });
        //m_detector.setOnDoubleTapListener(this);
        m_tv = (TextView) findViewById(R.id.textView);
        boolean bBlue = m_bBlueFirst;
        m_tv.setTextColor(bBlue ? Color.BLUE : Color.RED);
        m_tv.setText(bBlue ? "Blue turn" : "Red turn");

        m_btNew= (Button) findViewById(R.id.button);
        m_btNew.setVisibility(View.GONE);

        m_btNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //restart the game.
                // switch start with every new game
                m_bBlueFirst = !m_bBlueFirst;
                m_bGameOver = false;


                m_ia.reset(m_bBlueFirst);
                gridview.setAdapter(m_ia);
                m_ia.notifyDataSetChanged();
                gridview.invalidateViews();

                m_btNew.setVisibility(View.GONE);

                m_tv.setTextColor(m_bBlueFirst? Color.BLUE:Color.RED);
                m_tv.setText(m_bBlueFirst ? "Blue turn" : "Red turn");
                //gameOn();
            }
        });
        if (m_bUseAI && !bBlue)
        {
            int aiCol = ConnectFour.aiMove(m_ia.cf);
            m_ia.cf.drop(aiCol);
            m_ia.notifyDataSetChanged();
            gridview.invalidateViews();
        }

        m_btUndo= (Button) findViewById(R.id.undo);
        m_btUndo.setVisibility(View.GONE); // hide undo button
        m_btUndo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //undo last step
                undo();
                // using AI? then undo computer move and then undo human move.
                if (m_bUseAI) {undo();}
                //gameOn();
            }
        });


        gameOn();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {


            tts = new TextToSpeech(getApplicationContext(),
                    new TextToSpeech.OnInitListener() {
                        @Override
                        public void onInit(int status) {
                            if (status != TextToSpeech.ERROR) {
                                tts.setLanguage(new Locale("spa", "US"));
                                //tts.setLanguage(Locale.US);
                                tts.setSpeechRate(0.8f);
                                tts.setPitch(1.1f);
                                //Toast.makeText(getApplicationContext(), tts.isLanguageAvailable(Locale.CHINESE), Toast.LENGTH_SHORT);
                                //spinner.setVisibility(View.GONE);

                            }
                        }
                    });
        }
    }
    public boolean onTouchEvent(MotionEvent event){
        this.m_detector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
    void undo ()
    {

        boolean bBlue = m_ia.cf.undo();

        m_ia.notifyDataSetChanged();
        gridview.invalidateViews();

        m_bGameOver = false;
        m_tv.setTextColor(bBlue? Color.BLUE:Color.RED);
        m_tv.setText("Undone..." + (bBlue ? "Blue turn" : "Red turn"));

    }
    void gameOn ()
    {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //Toast.makeText(MainActivity.this, "" + position,
                //Toast.LENGTH_SHORT).show();
                if (!MainActivity.this.m_bGameOver) {
                    while (!MainActivity.this.m_ia.cf.drop(position % 7))
                    {
                        // off board? still your turn
                        MainActivity.this.m_tv.setText("Still your turn.");
                        return;

                    };

                    MainActivity.this.m_ia.notifyDataSetChanged();
                    MainActivity.this.gridview.invalidateViews();
                    boolean bBlue = MainActivity.this.m_ia.cf.isBlueNext();

                    MainActivity.this.m_tv.setTextColor(bBlue? Color.BLUE:Color.RED);
                    MainActivity.this.m_tv.setText(bBlue ? "Blue turn " : "Red turn ");
                    //Toast.makeText(MainActivity.this, MainActivity.this.m_bBlue ? "Blue turn " : "Red turn ",
                    //Toast.LENGTH_LONG).show();



                    char cDone = MainActivity.this.m_ia.cf.done();
                    System.out.println (cDone);


                    if (cDone != '-') {
                        if (cDone == 'O' || cDone == 'o') {
                            MainActivity.this.m_tv.setTextColor(Color.BLUE);
                            MainActivity.this.m_tv.setText("Blue won.");

                            //Toast.makeText(MainActivity.this, "Blue won" ,
                            //Toast.LENGTH_SHORT).show();

                        }
                        else if (cDone == 'X' || cDone == 'x') {
                            MainActivity.this.m_tv.setTextColor(Color.RED);
                            MainActivity.this.m_tv.setText("Red won.");
                            //Toast.makeText(MainActivity.this, "Red won",
                            //Toast.LENGTH_SHORT).show();

                        } else if (cDone == '+') {
                            MainActivity.this.m_tv.setText("It is a tie.");
                        }
                        //m_tv.setTextAppearance(MainActivity.this, R.style.big);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            tts.speak(MainActivity.this.m_tv.getText(), TextToSpeech.QUEUE_FLUSH, null, null);
                        }
                        YoYo.with(Techniques.Tada)
                                .duration(5000)
                                .playOn(MainActivity.this.m_tv);

                        MainActivity.this.m_bGameOver = true;

                        m_bBlueFirst = !m_bBlueFirst; // take turn starting

                        m_btNew.setVisibility(View.VISIBLE);

                    }
                    if (MainActivity.this.m_bGameOver)
                    {
                        //Toast.makeText(MainActivity.this, "Game Over",
                        //Toast.LENGTH_SHORT).show();

                    } else {
                        MainActivity.this.m_ia.cf.print();
//                        MainActivity.this.m_tv.setTextColor(m_bBlue ? Color.BLUE : Color.RED);
//                        MainActivity.this.m_tv.setText(MainActivity.this.m_bBlue ? "Blue turn " : "Red turn ");
                        /// if game is not yet over
                        if (m_bUseAI && !bBlue)
                        {
                            int aiCol = ConnectFour.aiMove(m_ia.cf);
                            m_ia.cf.drop(aiCol);
                            m_ia.notifyDataSetChanged();
                            gridview.invalidateViews();
                        }
                    }
                }
                else
                {
                    MainActivity.this.m_tv.setText("Game is over");
                    //Toast.makeText(MainActivity.this, "Game Over",
                    //Toast.LENGTH_SHORT).show();



                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
