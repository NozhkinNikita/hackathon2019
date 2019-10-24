package com.sb.wifistart.utils;

import android.content.Context;
import android.view.Gravity;
import android.widget.TableRow;
import android.widget.TextView;

public class TableContentHelper {
    public static TableRow getRowWithEmptyMessage(Context context, int colspan) {
        TableRow row = new TableRow(context);
        TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(lp);

        TextView emptyMessage = new TextView(context);
        emptyMessage.setText("Отсутствуют данные");
        emptyMessage.setTextAppearance(android.R.style.TextAppearance_Medium);
        emptyMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        emptyMessage.setEnabled(false);

        TableRow.LayoutParams emptyMessageLP = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
        emptyMessageLP.span = colspan;
        emptyMessage.setLayoutParams(emptyMessageLP);

        row.addView(emptyMessage);
        return row;
    }
}
