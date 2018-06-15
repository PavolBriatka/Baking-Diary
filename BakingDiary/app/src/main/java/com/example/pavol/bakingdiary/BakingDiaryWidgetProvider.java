package com.example.pavol.bakingdiary;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;

/**
 * Implementation of App Widget functionality.
 */
public class BakingDiaryWidgetProvider extends AppWidgetProvider {


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {


        // Construct the RemoteViews object
        RemoteViews remoteViews;

        remoteViews = getListRemoteView(context);


        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId,
                R.id.widget_list_view);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void updateBakingDiaryWidgets(Context context, AppWidgetManager appWidgetManager,
                                                int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    private static RemoteViews getListRemoteView(Context context) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String recipeName = sharedPreferences.getString(context.getString(R.string.recipe_name_key),
                context.getString(R.string.widget_recipe_name));

        Intent intent = new Intent(context, ListWidgetService.class);
        remoteViews.setRemoteAdapter(R.id.widget_list_view, intent);

        Intent openMainActivity = new Intent(context, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0,
                openMainActivity, 0);


        remoteViews.setTextViewText(R.id.widget_recipe_name, recipeName);
        remoteViews.setEmptyView(R.id.widget_list_view, R.id.empty_view);

        remoteViews.setOnClickPendingIntent(R.id.widget_recipe_name, pendingIntent);

        return remoteViews;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

