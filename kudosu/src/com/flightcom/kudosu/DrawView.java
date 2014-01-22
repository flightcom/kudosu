package com.flightcom.kudosu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.util.Log;

public class DrawView extends View {

	Paint paint = new Paint();
	int dim;
	int caseDim;

	public DrawView(Context context, int dimension) {
		super(context);
		dim = (int) ((Integer)dimension*0.95);
		caseDim = (int)this.dim/9;

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dim, dim);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER;
        this.setLayoutParams(params);

		paint.setColor(Color.WHITE);
		paint.setStrokeWidth(3);
	}

	@Override
	public void onDraw(Canvas canvas) {
		canvas.drawLine(0, 0, 0, dim, paint);
		canvas.drawLine(0, dim, dim, dim, paint);
		canvas.drawLine(dim, dim, dim, 0, paint);
		canvas.drawLine(dim, 0, 0, 0, paint);
		for(int i = 1; i < 9; i++) {
			canvas.drawLine(0, caseDim*i, dim, caseDim*i, paint);
			canvas.drawLine(caseDim*i, 0, caseDim*i, dim, paint);
		}
	}

}