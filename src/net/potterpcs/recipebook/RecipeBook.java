package net.potterpcs.recipebook;

import android.app.Application;

public class RecipeBook extends Application {
	private RecipeData recipeData;
	static final String SEARCH_EXTRA = "search-query";
	static final String TAG_EXTRA = "tag-search";

	public void onCreate() {
		super.onCreate();
		recipeData = new RecipeData(this);
	}

    public RecipeData getData() {
    	return recipeData;
    }
}
