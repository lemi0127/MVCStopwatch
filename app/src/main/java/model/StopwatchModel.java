package model;

import java.text.NumberFormat;
import java.util.Observable;
import java.util.Timer;
import java.util.TimerTask;

/**
 *  Stopwatch assignment for Android Development Class at Algonquin College
 *  Teaching OO Design Patterns such as Observable/Observer & MVC Architecture
 *  Stopwatch Model: Stopwatch timer content and states
 *  @author Alice Lee (lemi0127)
 */

public class StopwatchModel extends Observable {

    private static final NumberFormat NF;

    private int Hours;
    private int Minutes;
    private int Seconds;
    private int TenthOfASecond;
    private Boolean IsRunning;
    private TimerTask mTimerTask;
    private Timer mTimer;

    public int getHours() {
        return Hours;
    }

    public int getMinutes() {
        return Minutes;
    }

    public int getSeconds() {
        return Seconds;
    }

    public int getTenthOfASecond() {
        return TenthOfASecond;
    }

    static {
        NF = NumberFormat.getInstance();
        NF.setMinimumIntegerDigits(2);
        NF.setMaximumIntegerDigits(2);
    }

    public StopwatchModel() {
        this(0, 0, 0, 0);
    }

    public StopwatchModel(int hour, int minute, int second, int tenthOfASec) {
        super();

        this.Hours = hour;
        this.Minutes = minute;
        this.Seconds = second;
        this.TenthOfASecond = tenthOfASec;

        IsRunning = false;

        mTimer = new Timer();
    }

    public Boolean stopwatchRunning() {
        return IsRunning;
    }

    public void reset() {
        Hours = Minutes = Seconds = TenthOfASecond = 0;
        this.updateObservers();
    }

    public void start() {
        if (this.stopwatchRunning() == false) {
            mTimerTask = new StopwatchTask();
            mTimer.scheduleAtFixedRate(mTimerTask, 0L, 100L);
            IsRunning = true;
        }
        this.updateObservers();
    }

    public void stop() {
        if (this.stopwatchRunning() == true) {
            mTimerTask.cancel();
            IsRunning = false;
        }
        this.updateObservers();
    }

    public String toString() {
        return (NF.format(Hours) + ":" +
                NF.format(Minutes) + ":" +
                NF.format(Seconds) + ":" +
                TenthOfASecond);
    }

    private void updateObservers() {
        this.setChanged();
        this.notifyObservers();
    }

    private class StopwatchTask extends TimerTask {
        @Override
        public void run() {
            TenthOfASecond++;

            if (TenthOfASecond == 10) {
                TenthOfASecond = 0;
                Seconds++;

                if (Seconds >= 60) {
                    Seconds = 0;
                    Minutes++;

                    if (Minutes >= 60) {
                        Minutes = 0;
                        Hours++;
                    }
                }
            }
            updateObservers();
        }

        ;
    }

}
