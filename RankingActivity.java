package com.ponggan.phishing;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RankingActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

     // Fragment managing the behaviors, interactions and presentation of the navigation drawer.
    private NavigationDrawerFragment mNavigationDrawerFragment;
    // Used to store the last screen title. For use in {@link #restoreActionBar()}.
    private CharSequence mTitle;

    SQLiteDatabase db = null;
    Cursor cursor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        db= (new com.ponggan.phishing.DatabaseHandler(this)).getWritableDatabase();
        cursor =db.rawQuery("SELECT _id,name,figure,avg_score from Files ORDER BY _id", null);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        if (cursor != null) {
            cursor.moveToPosition(number - 1);
            try {
                mTitle = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            } catch (IllegalArgumentException e) {
                Toast.makeText(this, "記得先新增目標喔！", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.ranking, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
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

    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        return RankingActivity.PlaceholderFragment.newInstance(position + 1);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        //TextView tv;
        com.ponggan.phishing.DatabaseHandler dbHandler;
        Cursor cursor;
        SQLiteDatabase db;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_ranking, container, false);
            // 獲取目前的目標ID
            dbHandler = new com.ponggan.phishing.DatabaseHandler(getActivity());
            Bundle args = getArguments();
            /*tv = (TextView) rootView.findViewById(R.id.section_label);
            try {
                tv.setText(dbHandler.getDatingTargetName(args.getInt(ARG_SECTION_NUMBER)));
            } catch (Exception e) {
                Toast.makeText(getActivity(), "記得先新增目標喔！", Toast.LENGTH_SHORT).show();
            }*/

            //獲取目標約會總數
            int datingCount = 0;
            db = (new com.ponggan.phishing.DatabaseHandler(getActivity())).getWritableDatabase();
            cursor =db.rawQuery("SELECT _id,date,content,scoreSelf,scoreTarget,target_id FROM Datings WHERE target_id = " +
                    args.getInt(ARG_SECTION_NUMBER) + " ORDER BY date", null);
            if (cursor != null){
                cursor.moveToFirst();
                datingCount = cursor.getCount();
            }

            String[] titles = new String[] { "自己的好感度", "對方的好感度" }; // 定義折線的名稱

            //獲取所有約會日期
            //並且輸入變成座標
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
            List<Date[]> dates = new ArrayList<Date[]>();
            int lengthTopics = titles.length;
            for (int i = 0; i < lengthTopics; i++) {
                dates.add(new Date[datingCount]);

                if(cursor != null){
                    cursor.moveToPosition(-1);
                    while(cursor.moveToNext()){
                        Date convertedCurrentDate = null;
                        try {
                            convertedCurrentDate = simpleDateFormat.parse(cursor.getString(cursor.getColumnIndexOrThrow("date")));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        dates.get(i)[cursor.getPosition()] = convertedCurrentDate;
                    }
                }
            }

            // 約會分數坐標值輸入
            //第一條
            List<double[]> values = new ArrayList<double[]>();
            double scoreSelf[] = new double[datingCount];
            if(cursor != null){
                //cursor.moveToFirst();
                cursor.moveToPosition(-1);
                while(cursor.moveToNext()){
                    scoreSelf[cursor.getPosition()] = (double)cursor.getInt(cursor.getColumnIndexOrThrow("scoreSelf"));
                }
            }
            values.add(scoreSelf);
            //第二條
            double scoreTarget[] = new double[datingCount];
            if(cursor != null){
                //cursor.moveToFirst();
                cursor.moveToPosition(-1);
                while(cursor.moveToNext()){
                    scoreTarget[cursor.getPosition()] = (double)cursor.getInt(cursor.getColumnIndexOrThrow("scoreTarget"));
                }
                cursor.close();
            }
            values.add(scoreTarget);

            int lengthValues = values.get(0).length;
            int[] colors = new int[] { Color.parseColor("#6495ED"), Color.GREEN };// 折線的顏色
            PointStyle[] styles = new PointStyle[] { PointStyle.DIAMOND, PointStyle.DIAMOND }; // 折線點的形狀
            XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);

            try {
                setChartSettings(renderer, "好感度趨勢", "日期", "分數", dates.get(0)[0].getTime(),
                        dates.get(0)[datingCount - 1].getTime(), -10, 110, Color.GRAY, Color.LTGRAY, 50.f, 50.f);// 定義折線圖
            } catch (ArrayIndexOutOfBoundsException e) {
                Toast.makeText(getActivity(), "你和此人沒有約會，無法繪圖！", Toast.LENGTH_SHORT).show();
            }

            renderer.setXLabels(10);
            renderer.setYLabels(10);
            //renderer.addYTextLabel(100, "test");
            lengthValues = renderer.getSeriesRendererCount();


            for (int i = 0; i < lengthValues; i++) {
                XYSeriesRenderer seriesRenderer = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
                seriesRenderer.setLineWidth(10.f);
                seriesRenderer.setChartValuesTextSize(50.f);
                seriesRenderer.setChartValuesSpacing(70.f);
                seriesRenderer.setDisplayChartValues(true);
            }

            renderer.setXRoundedLabels(true);

            View chart = ChartFactory.getTimeChartView(getActivity(), buildDateDataset(titles, dates, values),
                    renderer, "yyyy/MM/dd");

            //return rootView;
            cursor.close();
            return chart;
        }

        // 定義折線圖名稱
        /**
         * Sets a few of the series renderer settings.
         *
         * @param renderer the renderer to set the properties to
         * @param title the chart title
         * @param xTitle the title for the X axis
         * @param yTitle the title for the Y axis
         * @param xMin the minimum value on the X axis
         * @param xMax the maximum value on the X axis
         * @param yMin the minimum value on the Y axis
         * @param yMax the maximum value on the Y axis
         * @param axesColor the axes color
         * @param labelsColor the labels color
         */
        protected void setChartSettings(XYMultipleSeriesRenderer renderer, String title, String xTitle,
                                        String yTitle, double xMin, double xMax, double yMin, double yMax, int axesColor,
                                        int labelsColor, float titleTextSize, float pointSize) {
            renderer.setChartTitle(title);
            renderer.setXTitle(xTitle);
            renderer.setYTitle(yTitle);
            renderer.setXAxisMin(xMin);
            renderer.setXAxisMax(xMax);
            renderer.setYAxisMin(yMin);
            renderer.setYAxisMax(yMax);
            renderer.setAxesColor(axesColor);
            renderer.setLabelsColor(labelsColor);
            renderer.setAxisTitleTextSize(titleTextSize);
            renderer.setPointSize(pointSize);
        }

        /**
         * Builds an XY multiple series renderer.
         *
         * @param colors the series rendering colors
         * @param styles the series point styles
         * @return the XY multiple series renderers
         */
        protected XYMultipleSeriesRenderer buildRenderer(int[] colors, PointStyle[] styles) {
            XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
            setRenderer(renderer, colors, styles);
            return renderer;
        }
        protected void setRenderer(XYMultipleSeriesRenderer renderer, int[] colors, PointStyle[] styles) {
            renderer.setAxisTitleTextSize(16);
            renderer.setChartTitleTextSize(20);
            renderer.setLabelsTextSize(15);
            renderer.setLegendTextSize(15);
            renderer.setPointSize(5f);
            renderer.setMargins(new int[] { 20, 30, 15, 20 });
            int length = colors.length;
            for (int i = 0; i < length; i++) {
                XYSeriesRenderer r = new XYSeriesRenderer();
                r.setColor(colors[i]);
                r.setPointStyle(styles[i]);
                renderer.addSeriesRenderer(r);
            }
        }

        /**
         * Builds an XY multiple time dataset using the provided values.
         *
         * @param titles the series titles
         * @param xValues the values for the X axis
         * @param yValues the values for the Y axis
         * @return the XY multiple time dataset
         */
        protected XYMultipleSeriesDataset buildDateDataset(String[] titles, List<Date[]> xValues,
                                                           List<double[]> yValues) {
            XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
            int length = titles.length; // 折線數量
            for (int i = 0; i < length; i++) {
                // TimeSeries對象,用於提供繪製的點集合的資料
                TimeSeries series = new TimeSeries(titles[i]); // 依據每條線的名稱新增
                Date[] xV = xValues.get(i); // 獲取第i條線的資料
                double[] yV = yValues.get(i);
                int seriesLength = xV.length; // 有幾個點
                for (int k = 0; k < seriesLength; k++) { // 每條線裡有幾個點
                    series.add(xV[k], yV[k]);
                }
                dataset.addSeries(series);
            }
            return dataset;
        }


        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
                ((RankingActivity) activity).onSectionAttached(
                        getArguments().getInt(ARG_SECTION_NUMBER));
            } catch (Exception e) {
                Toast.makeText(activity, "記得先新增目標喔！", Toast.LENGTH_SHORT).show();
            }
        }

    }

}
