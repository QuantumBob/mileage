package com.smalldoor.rwk.mileage;

import android.graphics.Color;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.BarLineScatterCandleBubbleDataSet;
import com.github.mikephil.charting.data.DataSet;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quant on 02/01/2017.
 * Bar dataset with extras that can be referenced
 */

class BarDataSetWithMileageDetails extends BarLineScatterCandleBubbleDataSet<BarEntry> implements IBarDataSet {

    private List<MileageDetails> mMileageDetailsList = null;
//    private ArrayList<String> mLabels;
    /**
     * the maximum number of bars that are stacked upon each other, this value
     * is calculated from the Entries that are added to the DataSet
     */
    private int mStackSize = 1;

    /**
     * the color used for drawing the bar shadows
     */
    private int mBarShadowColor = Color.rgb(215, 215, 215);

    private float mBarBorderWidth = 0.0f;

    private int mBarBorderColor = Color.BLACK;

    /**
     * the alpha value used to draw the highlight indicator bar
     */
    private int mHighLightAlpha = 120;

    /**
     * the overall entry count, including counting each stack-value individually
     */
    private int mEntryCountStacks = 0;

    /**
     * array of labels used to describe the different values of the stacked bars
     */
    private String[] mStackLabels = new String[]{
            "Stack"
    };

    BarDataSetWithMileageDetails(List<BarEntry> yVals, String label, List<MileageDetails> mileageDetailsList) {
        super(yVals, label);

        this.mMileageDetailsList = mileageDetailsList;

        if (mMileageDetailsList == null)
            mMileageDetailsList = new ArrayList<>();

        mHighLightColor = Color.rgb(0, 0, 0);

        calcStackSize(yVals);
        calcEntryCountIncludingStacks(yVals);
    }

//    public void setLabels(ArrayList<String> labels){
//
//        mLabels = labels;
//    }

    String getLabel(int value){

        return mMileageDetailsList.get(value).getLabel();
    }

    int getIdAtIndex(int index){

        return mMileageDetailsList.get(index).getID();
    }

    void removeMileageDetail(int index){

        mMileageDetailsList.remove(index);
    }

    public MileageDetails getMileageDetailAtIndex(int index){

        return mMileageDetailsList.get(index);
    }

    public List<MileageDetails> getMileageDetailsList(){
        return mMileageDetailsList;
    }

    @Override
    public DataSet<BarEntry> copy() {

        List<BarEntry> yVals = new ArrayList<>();
        yVals.clear();

        for (int i = 0; i < mValues.size(); i++) {
            yVals.add(mValues.get(i).copy());
        }

        BarDataSetWithMileageDetails copied = new BarDataSetWithMileageDetails(yVals, getLabel(), mMileageDetailsList);
        copied.mColors = mColors;
        copied.mStackSize = mStackSize;
        copied.mBarShadowColor = mBarShadowColor;
        copied.mStackLabels = mStackLabels;
        copied.mHighLightColor = mHighLightColor;
        copied.mHighLightAlpha = mHighLightAlpha;

        return copied;
    }

    /**
     * Calculates the total number of entries this DataSet represents, including
     * stacks. All values belonging to a stack are calculated separately.
     */
    private void calcEntryCountIncludingStacks(List<BarEntry> yVals) {

        mEntryCountStacks = 0;

        for (int i = 0; i < yVals.size(); i++) {

            float[] vals = yVals.get(i).getYVals();

            if (vals == null)
                mEntryCountStacks++;
            else
                mEntryCountStacks += vals.length;
        }
    }

    /**
     * calculates the maximum stacksize that occurs in the Entries array of this
     * DataSet
     */
    private void calcStackSize(List<BarEntry> yVals) {

        for (int i = 0; i < yVals.size(); i++) {

            float[] vals = yVals.get(i).getYVals();

            if (vals != null && vals.length > mStackSize)
                mStackSize = vals.length;
        }
    }

    @Override
    protected void calcMinMax(BarEntry e) {

        if (e != null && !Float.isNaN(e.getY())) {

            if (e.getYVals() == null) {

                if (e.getY() < mYMin)
                    mYMin = e.getY();

                if (e.getY() > mYMax)
                    mYMax = e.getY();
            } else {

                if (-e.getNegativeSum() < mYMin)
                    mYMin = -e.getNegativeSum();

                if (e.getPositiveSum() > mYMax)
                    mYMax = e.getPositiveSum();
            }

            calcMinMaxX(e);
        }
    }

    @Override
    public int getStackSize() {
        return mStackSize;
    }

    @Override
    public boolean isStacked() {
        return mStackSize > 1;
    }

    /**
     * returns the overall entry count, including counting each stack-value
     * individually
     *
     * @return int representing number of stacked entires
     */
    public int getEntryCountStacks() {
        return mEntryCountStacks;
    }

    /**
     * Sets the color used for drawing the bar-shadows. The bar shadows is a
     * surface behind the bar that indicates the maximum value. Don't for get to
     * use getResources().getColor(...) to set this. Or Color.rgb(...).
     *
     * @param color
     */
    public void setBarShadowColor(int color) {
        mBarShadowColor = color;
    }

    @Override
    public int getBarShadowColor() {
        return mBarShadowColor;
    }

    /**
     * Sets the width used for drawing borders around the bars.
     * If borderWidth == 0, no border will be drawn.
     *
     * @return
     */
    public void setBarBorderWidth(float width) {
        mBarBorderWidth = width;
    }

    /**
     * Returns the width used for drawing borders around the bars.
     * If borderWidth == 0, no border will be drawn.
     *
     * @return
     */
    @Override
    public float getBarBorderWidth() {
        return mBarBorderWidth;
    }

    /**
     * Sets the color drawing borders around the bars.
     *
     * @return
     */
    public void setBarBorderColor(int color) {
        mBarBorderColor = color;
    }

    /**
     * Returns the color drawing borders around the bars.
     *
     * @return
     */
    @Override
    public int getBarBorderColor() {
        return mBarBorderColor;
    }

    /**
     * Set the alpha value (transparency) that is used for drawing the highlight
     * indicator bar. min = 0 (fully transparent), max = 255 (fully opaque)
     *
     * @param alpha
     */
    public void setHighLightAlpha(int alpha) {
        mHighLightAlpha = alpha;
    }

    @Override
    public int getHighLightAlpha() {
        return mHighLightAlpha;
    }

    /**
     * Sets labels for different values of bar-stacks, in case there are one.
     *
     * @param labels
     */
    public void setStackLabels(String[] labels) {
        mStackLabels = labels;
    }

    @Override
    public String[] getStackLabels() {
        return mStackLabels;
    }
}
