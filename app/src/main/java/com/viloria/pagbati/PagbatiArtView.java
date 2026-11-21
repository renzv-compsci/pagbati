package com.viloria.pagbati;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

/**
 * Decorative background inspired by Philippine textile patterns and the
 * BOOKS PHILIPPINES poster: tribal bands, woven side stripes, a sun motif,
 * and leaf corners, all drawn as Canvas shapes.
 */
public class PagbatiArtView extends View {

    private static final int BAND_HEIGHT_DP = 56;
    private static final int SIDE_STRIPE_DP = 10;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path path = new Path();

    private final int cream;
    private final int terracotta;
    private final int red;
    private final int mustard;
    private final int teal;
    private final int green;

    public PagbatiArtView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        cream = ContextCompat.getColor(context, R.color.pagbati_cream);
        terracotta = ContextCompat.getColor(context, R.color.pagbati_terracotta);
        red = ContextCompat.getColor(context, R.color.pagbati_red);
        mustard = ContextCompat.getColor(context, R.color.pagbati_mustard);
        teal = ContextCompat.getColor(context, R.color.pagbati_teal);
        green = ContextCompat.getColor(context, R.color.pagbati_green);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float w = getWidth();
        float h = getHeight();
        float dp = getResources().getDisplayMetrics().density;

        canvas.drawColor(cream);

        drawTribalBand(canvas, w, dp, 0f);
        drawTribalBand(canvas, w, dp, h - BAND_HEIGHT_DP * dp);
        drawSideStripes(canvas, w, h, dp);
        drawSun(canvas, w / 2f, h / 2f, Math.min(w, h) * 0.32f);
        drawLeafCluster(canvas, 26 * dp, 92 * dp, 46 * dp, 1);
        drawLeafCluster(canvas, w - 26 * dp, h - 96 * dp, 46 * dp, -1);
    }

    private void drawTribalBand(Canvas canvas, float w, float dp, float top) {
        float bandH = BAND_HEIGHT_DP * dp;
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(terracotta);
        canvas.drawRect(0f, top, w, top + bandH, paint);

        paint.setColor(cream);
        canvas.drawRect(0f, top + 6 * dp, w, top + 8 * dp, paint);
        canvas.drawRect(0f, top + bandH - 8 * dp, w, top + bandH - 6 * dp, paint);

        float size = 8 * dp;
        float gap = 28 * dp;
        int[] colors = {mustard, red, teal, cream};
        int i = 0;
        for (float x = gap / 2f; x < w; x += gap) {
            paint.setColor(colors[i % colors.length]);
            path.reset();
            path.moveTo(x, top + bandH / 2f - size);
            path.lineTo(x + size, top + bandH / 2f);
            path.lineTo(x, top + bandH / 2f + size);
            path.lineTo(x - size, top + bandH / 2f);
            path.close();
            canvas.drawPath(path, paint);
            i++;
        }
    }

    private void drawSideStripes(Canvas canvas, float w, float h, float dp) {
        float stripeW = SIDE_STRIPE_DP * dp;
        float top = BAND_HEIGHT_DP * dp;
        float bottom = h - BAND_HEIGHT_DP * dp;

        paint.setStyle(Paint.Style.FILL);
        paint.setColor(teal);
        canvas.drawRect(0f, top, stripeW, bottom, paint);
        paint.setColor(red);
        canvas.drawRect(w - stripeW, top, w, bottom, paint);

        paint.setColor(cream);
        for (float y = top + 16 * dp; y < bottom - 8 * dp; y += 28 * dp) {
            canvas.drawRect(2 * dp, y, stripeW - 2 * dp, y + 10 * dp, paint);
            canvas.drawRect(w - stripeW + 2 * dp, y, w - 2 * dp, y + 10 * dp, paint);
        }
    }

    private void drawSun(Canvas canvas, float cx, float cy, float radius) {
        paint.setStyle(Paint.Style.FILL);

        paint.setColor(withAlpha(mustard, 30));
        canvas.drawCircle(cx, cy, radius, paint);

        int petals = 16;
        float petalRadius = radius * 0.16f;
        for (int i = 0; i < petals; i++) {
            double angle = i * 2 * Math.PI / petals;
            float px = cx + (float) Math.cos(angle) * radius * 0.72f;
            float py = cy + (float) Math.sin(angle) * radius * 0.72f;
            paint.setColor(withAlpha(mustard, 40));
            canvas.drawCircle(px, py, petalRadius, paint);
        }

        paint.setColor(withAlpha(terracotta, 34));
        canvas.drawCircle(cx, cy, radius * 0.52f, paint);
    }

    private void drawLeafCluster(Canvas canvas, float x, float y, float length, float flip) {
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(withAlpha(green, 200));
        drawLeaf(canvas, x, y, length, 20f * flip);
        drawLeaf(canvas, x, y, length * 0.78f, 70f * flip);
        drawLeaf(canvas, x, y, length * 0.62f, 120f * flip);
    }

    private void drawLeaf(Canvas canvas, float x, float y, float length, float angleDeg) {
        float width = length * 0.34f;
        canvas.save();
        canvas.rotate(angleDeg, x, y);
        path.reset();
        path.moveTo(x, y);
        path.quadTo(x + length * 0.5f, y - width, x + length, y);
        path.quadTo(x + length * 0.5f, y + width, x, y);
        path.close();
        canvas.drawPath(path, paint);
        canvas.restore();
    }

    private static int withAlpha(int color, int alpha) {
        return (color & 0x00FFFFFF) | (alpha << 24);
    }
}
