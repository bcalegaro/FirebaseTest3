package com.bcalegaro.firebasetest3.ui.ration;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ItemDivider extends RecyclerView.ItemDecoration{
    private final Drawable divider;

    // o construtor carrega a divisória de itens de lista interna
    ItemDivider (Context context) {
        int[] attrs = {android.R.attr.listDivider};
        divider = context.obtainStyledAttributes(attrs).getDrawable(0);
    }

    // desenha as divisórias de itens de lista na RecylerView

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent,
                           RecyclerView.State state) {
        super.onDrawOver(c, parent, state);

        // calcula as coordenadas x esquerda/direita de todas as divisórias
        int left = parent.getPaddingLeft();
        int right = parent.getWidth() - parent.getPaddingRight();

        // para todos os itens, menos o último, desenha uma linha abaixo dele
        for (int i = 0; i < parent.getChildCount() - 1; i++) {
            View item = parent.getChildAt(i);

            // calcula as coodenadas y superior/inferior da divisória atual
            int top = item.getBottom() + ((RecyclerView.LayoutParams)
                    item.getLayoutParams()).bottomMargin;
            int bottom = top + divider.getIntrinsicHeight();

            // desenha a divisória com os limites calculados
            divider.setBounds(left, top, right, bottom);
            divider.draw(c);
        }
    }
}
