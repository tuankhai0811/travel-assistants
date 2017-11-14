package com.tuankhai.travelassistants.library.slideractivity;

import android.animation.ArgbEvaluator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

import com.tuankhai.travelassistants.R;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderConfig;
import com.tuankhai.travelassistants.library.slideractivity.model.SliderInterface;
import com.tuankhai.travelassistants.library.slideractivity.widget.SliderPanel;

/**
 * This attacher class is used to attach the sliding mechanism to any {@link android.app.Activity}
 * that lets the user slide (or swipe) the activity away as a form of back or up action. The action
 * causes {@link android.app.Activity#finish()} to be called.
 *
 *
 * Created by r0adkll on 8/18/14.
 */
public class Slider {


    public static SliderInterface attach(Activity activity){
        return attach(activity, -1, -1);
    }


    public static SliderInterface attach(final Activity activity, final int statusBarColor1, final int statusBarColor2){

		// Setup the slider panel and attach it to the decor
		final SliderPanel panel = initSliderPanel(activity, null);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new SliderPanel.OnPanelSlideListener() {

            private final ArgbEvaluator mEvaluator = new ArgbEvaluator();

            @Override
            public void onStateChanged(int state) {

            }

            @Override
            public void onClosed() {
                activity.finish();
                activity.overridePendingTransition(0, 0);
            }

            @Override
            public void onOpened() {

			}

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSlideChange(float percent) {
                // Interpolate the statusbar color
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                        statusBarColor1 != -1 && statusBarColor2 != -1){
                    int newColor = (int) mEvaluator.evaluate(percent, statusBarColor1, statusBarColor2);
                    activity.getWindow().setStatusBarColor(newColor);
                }
            }
        });

		// Return the lock interface
		return initInterface(panel);
    }

    public static SliderInterface attach(final Activity activity, final SliderConfig config){

        // Setup the slider panel and attach it to the decor
        final SliderPanel panel = initSliderPanel(activity, config);

        // Set the panel slide listener for when it becomes closed or opened
        panel.setOnPanelSlideListener(new SliderPanel.OnPanelSlideListener() {

            private final ArgbEvaluator mEvaluator = new ArgbEvaluator();

            @Override
            public void onStateChanged(int state) {
                if(config.getListener() != null){
                    config.getListener().onSlideStateChanged(state);
                }
            }

            @Override
            public void onClosed() {
                if(config.getListener() != null){
                    config.getListener().onSlideClosed();
                }

                activity.finish();
                activity.overridePendingTransition(0, 0);
            }

            @Override
            public void onOpened() {
                if(config.getListener() != null){
                    config.getListener().onSlideOpened();
                }
            }

            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onSlideChange(float percent) {
                // Interpolate the statusbar color
                // TODO: Add support for KitKat
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                        config.areStatusBarColorsValid()){

                    int newColor = (int) mEvaluator.evaluate(percent, config.getPrimaryColor(),
                            config.getSecondaryColor());

                    activity.getWindow().setStatusBarColor(newColor);
                }

                if(config.getListener() != null){
                    config.getListener().onSlideChange(percent);
                }
            }
        });

        // Return the lock interface
        return initInterface(panel);
    }

	private static SliderPanel initSliderPanel(final Activity activity, final SliderConfig config) {
		// Hijack the decorview
		ViewGroup decorView = (ViewGroup)activity.getWindow().getDecorView();
		View oldScreen = decorView.getChildAt(0);
		decorView.removeViewAt(0);

		// Setup the slider panel and attach it to the decor
		SliderPanel panel = new SliderPanel(activity, oldScreen, config);
		panel.setId(R.id.slidable_panel);
		oldScreen.setId(R.id.slidable_content);
		panel.addView(oldScreen);
		decorView.addView(panel, 0);
		return panel;
	}

	private static SliderInterface initInterface(final SliderPanel panel) {
		// Setup the lock interface
		SliderInterface slidrInterface = new SliderInterface() {
			@Override
			public void lock() {
				panel.lock();
			}

			@Override
			public void unlock() {
				panel.unlock();
			}
		};

		// Return the lock interface
		return slidrInterface;
	}

}
