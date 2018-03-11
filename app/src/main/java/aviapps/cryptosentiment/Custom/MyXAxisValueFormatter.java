package aviapps.cryptosentiment.Custom;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*
 * Created by Avijeet on 11-Mar-18.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {

    private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.US);

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        Date s = new Date((long) (value));
        return sdf.format(s);
    }
}