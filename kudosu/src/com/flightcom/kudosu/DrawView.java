package com.flightcom.kudosu;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.util.Log;

public class DrawView extends View {

	Paint paint = new Paint();
	int dim;
	int caseDim;

	public DrawView(Context context, int dimension) {
		super(context);
		dim = (int) ((Integer)dimension*0.9);
		caseDim = (int)this.dim/9;
		paint.setColor(Color.WHITE);
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