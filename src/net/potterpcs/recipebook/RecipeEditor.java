package net.potterpcs.recipebook;

import java.util.Date;

import net.potterpcs.recipebook.RecipeData.Recipe;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

public class RecipeEditor extends FragmentActivity {
	public static final String TAG = "RecipeEditor";
	static final int GALLERY_ACTIVITY = 1;
	private static final int METADATA = 0;
	private static final int INGREDIENTS = 1;
	private static final int DIRECTIONS = 2;
	private static final int TAGS = 3;
	private static final String[] ALL_FRAGMENT_NAMES = { "meta", "ingredients", "directions", "tags" };
	
	private Recipe recipe;
	private long recipeId;
	FragmentManager manager;
	MetadataEditor meta;
	IngredientsEditor ingredients;
	DirectionsEditor directions;
	TagsEditor tags;
	RecipeData recipeData;
	ListView sslistview;
	String[] fragmentnames;
	FrameLayout ssfragmentframe;
	Fragment nextFragment;
	Fragment lastFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.recipeeditor);
		
		sslistview = (ListView) findViewById(R.id.ssfragmentlist);
		ssfragmentframe = (FrameLayout) findViewById(R.id.ssfragment);
		fragmentnames = getResources().getStringArray(R.array.fragmentnames);
		
		recipeId = Long.parseLong(getIntent().getData().getLastPathSegment());
		manager = getSupportFragmentManager();
		meta = (MetadataEditor) manager.findFragmentById(R.id.metafragment);
		ingredients = (IngredientsEditor) manager.findFragmentById(R.id.ingredientsfragment);
		directions = (DirectionsEditor) manager.findFragmentById(R.id.directionsfragment);
		tags = (TagsEditor) manager.findFragmentById(R.id.tagsfragment);
		recipeData = ((RecipeBook) getApplication()).getData();
		
		if (recipeId != -1) {
			recipe = recipeData.getSingleRecipeObject(recipeId);
		} else {
			recipe = new Recipe();
		}
		
		if (sslistview != null) {
			FragmentTransaction setup = manager.beginTransaction();
			Log.v(TAG, "creating fragments");

			meta = (MetadataEditor) manager.findFragmentByTag(ALL_FRAGMENT_NAMES[0]);
			if (meta == null) {
				Log.v(TAG, "creating meta");
				meta = new MetadataEditor();
				setup.add(R.id.ssfragment, meta, "meta");
			}
			Log.v(TAG, meta.toString());
			setup.hide(meta);

			ingredients = (IngredientsEditor) manager.findFragmentByTag(ALL_FRAGMENT_NAMES[1]);
			if (ingredients == null) {
				Log.v(TAG, "creating ingredients");
				ingredients = new IngredientsEditor();
				setup.add(R.id.ssfragment, ingredients, "ingredients");
			}
			Log.v(TAG, ingredients.toString());
			setup.hide(ingredients);

			directions = (DirectionsEditor) manager.findFragmentByTag(ALL_FRAGMENT_NAMES[2]);
			if (directions == null) {
				Log.v(TAG, "creating directions");
				directions = new DirectionsEditor();
				setup.add(R.id.ssfragment, directions, "directions");
			}
			Log.v(TAG, directions.toString());
			setup.hide(directions);

			tags = (TagsEditor) manager.findFragmentByTag(ALL_FRAGMENT_NAMES[3]);
			if (tags == null) {
				Log.v(TAG, "creating tags");
				tags = new TagsEditor();
				setup.add(R.id.ssfragment, tags, "tags");
			}
			Log.v(TAG, tags.toString());
			setup.hide(tags);
			
			setup.commit();
			
			sslistview.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					String itemText = ((TextView) view).getText().toString();
					
					lastFragment = nextFragment;
					
					if (itemText.equals(fragmentnames[METADATA])) {
						nextFragment = meta;
					} else if (itemText.equals(fragmentnames[INGREDIENTS])) {
						nextFragment = ingredients;
					} else if (itemText.equals(fragmentnames[DIRECTIONS])) {
						nextFragment = directions;
					} else if (itemText.equals(fragmentnames[TAGS])) {
						nextFragment = tags;
					} else {
						nextFragment = null;
					}
					
					if (nextFragment != null && !nextFragment.equals(lastFragment)) {
						FragmentTransaction ft = manager.beginTransaction();
						if (lastFragment != null) {
							ft.hide(lastFragment);
						}
						ft.show(nextFragment);
						ft.commit();
					}
				}
			});
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		if (sslistview != null) {
//			FragmentTransaction ft = manager.beginTransaction();
//			for (String s : ALL_FRAGMENT_NAMES) {
//				Fragment f = manager.findFragmentByTag(s);
//				Log.i(TAG, f.toString() + f.isVisible());
//				if (f != null) {
//					if (f.isVisible()) {
//						nextFragment = f;
//					}
//					ft.hide(f);
//				}
//				Log.i(TAG, f.toString() + f.isVisible());
//			}
//			ft.commit();
//		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
//		FragmentTransaction ft = manager.beginTransaction();
//		for (String s: ALL_FRAGMENT_NAMES) {
//			Fragment f = manager.findFragmentByTag(s);
//			if (f != null && sslistview != null) {
//				// if the fragment exists and is not the one that should be shown
//				// (two-pane mode only, large-screen mode can show all fragments at once)
//				Log.i(TAG, f.toString() + f.isVisible());
//				if (f.equals(nextFragment)) {
//					ft.show(f);
//				} else {
//					ft.hide(f);
//				}
//				Log.i(TAG, f.toString() + f.isVisible());
//			}
//		}
//		ft.commit();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.editormenu, menu);
		MenuCompat.setShowAsAction(menu.findItem(R.id.editormenusave), 
				MenuItem.SHOW_AS_ACTION_ALWAYS | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		MenuCompat.setShowAsAction(menu.findItem(R.id.editormenuattach), 
				MenuItem.SHOW_AS_ACTION_IF_ROOM | MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.editormenusave:
			onSaveItem(item);
			return true;
		case R.id.editormenuattach:
			onAttachPhoto(item);
			return true;
		case android.R.id.home:
			Intent intent = new Intent(this, RecipeBookActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	public long getRecipeId() {
		return recipeId;
	}
	
	public Recipe getRecipe() {
		return recipe;
	}
	
	public void onSaveItem(MenuItem item) {
		recipe = meta.getRecipeMetadata();
		recipe.id = recipeId;
		recipe.date = new Date().toString();
		recipe.ingredients = ingredients.getIngredients();
		recipe.directions = directions.getDirections();
		recipe.tags = tags.getTags();
		
		if (recipe.id == -1) {
			// an insert of a new recipe
			recipeId = recipeData.insertRecipe(recipe);
//			recipeId = recipeData.getLastInsertRecipeId();
		} else {
			// an edit of an existing recipe
			recipeId = recipeData.updateRecipe(recipe);
		}
		
		if (recipeId == -1) {
			// Conflict error
			AlertDialogFragment adf = AlertDialogFragment.newInstance(this);
			adf.show(getSupportFragmentManager(), "alert");
		}
	}
	
	public void onAttachPhoto(MenuItem item) {
		// TODO
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, GALLERY_ACTIVITY);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == GALLERY_ACTIVITY) {
				Uri selectedImage = data.getData();
				Log.i(TAG, selectedImage.toString());
				recipe.photo = selectedImage.toString();
			}
		}
	}
	
	void alertDismissed() {
		// do nothing for now
	}
	
	private static class AlertDialogFragment extends DialogFragment {
		RecipeEditor editor;
		
		public static AlertDialogFragment newInstance(RecipeEditor editor) {
			AlertDialogFragment adf = new AlertDialogFragment(editor);
			return adf;
		}
		
		public AlertDialogFragment(RecipeEditor e) {
			super();
			editor = e;
		}
		
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(R.string.recipeeditoralert)
				.setPositiveButton(android.R.string.ok, 
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								editor.alertDismissed();
							}
						})
				.create();
		}
	}
}
