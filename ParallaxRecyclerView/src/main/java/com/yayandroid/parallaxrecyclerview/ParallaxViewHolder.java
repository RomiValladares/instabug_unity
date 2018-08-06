package com.yayandroid.parallaxrecyclerview;

import android.graphics.Rect;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by yahyabayramoglu on 15/04/15.
 */
public abstract class ParallaxViewHolder extends RecyclerView.ViewHolder implements ParallaxImageView.ParallaxImageListener {

    private ParallaxImageView backgroundImage;

    public abstract int getParallaxImageId();

    public ParallaxViewHolder(View itemView) {
        super(itemView);

        backgroundImage = (ParallaxImageView) itemView.findViewById(getParallaxImageId());
        backgroundImage.setListener(this);
    }

    @Override
    public int[] requireValuesForTranslate() {
        if (itemView.getParent() == null) {
            // Not added to parent yet!
            return null;
        } else {
            int[] itemPosition = new int[2];
            itemView.getLocationOnScreen(itemPosition);

            int[] recyclerPosition = new int[2];
            ((RecyclerView) itemView.getParent()).getLocationOnScreen(recyclerPosition);

            return new int[]{itemView.getMeasuredHeight(), itemPosition[1], ((RecyclerView) itemView.getParent()).getMeasuredHeight(), recyclerPosition[1]};
        }
    }

    public void animateImage() {
        getBackgroundImage().doTranslate();
        processOpaqueView(this);
    }

    public ParallaxImageView getBackgroundImage() {
        return backgroundImage;
    }

    public abstract View getOpaqueView();


    private void processOpaqueView(ParallaxViewHolder holder) {
        Rect visibleRect = new Rect();
        // r will be populated with the coordinates of     your view
        // that area still visible.
        holder.itemView.getGlobalVisibleRect(visibleRect);
        float visibleHeight = visibleRect.height();
        float minVisibleHeight = holder.itemView.getMeasuredHeight() / 2;

        float alpha = 1;

        //less than half is visible
        if (visibleHeight < minVisibleHeight) {
            float visibleHeightPercent = (visibleHeight * 100) / minVisibleHeight;

            alpha = visibleHeightPercent / 100;

            holder.getOpaqueView().setAlpha(alpha);
        } else {
            holder.getOpaqueView().setAlpha(255);
        }
    }
}
