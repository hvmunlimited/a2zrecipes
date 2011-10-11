package net.potterpcs.recipebook;

import java.util.ArrayList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

public class TagsEditor extends Fragment {
	GridView gridview;
	ArrayList<String> tags;
	private ArrayAdapter<String> adapter;
	private RecipeEditor activity;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		activity = (RecipeEditor) getActivity();
		tags = new ArrayList<String>();
		
		long rid = activity.getRecipeId();
			
		if (rid > 0) {
			RecipeBook app = (RecipeBook) activity.getApplication();
			Cursor c = app.getData().getRecipeTags(rid);
			
			c.moveToFirst();
			while (!c.isAfterLast()) {
				tags.add(c.getString(c.getColumnIndex(RecipeData.TT_TAG)));
				c.moveToNext();
			}
			
			c.close();
				
		} else {

		}
		
		adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1, tags);
		return inflater.inflate(R.layout.tagsedit, container, false);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getActivity().getMenuInflater();
		inflater.inflate(R.menu.tagscontext, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
		case R.id.ctxdeletetag:
			adapter.remove(adapter.getItem(info.position));
			return true;
			
		case R.id.ctxedittag:
			
			return true;
			
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onStart() {
		super.onStart();

		gridview = (GridView) activity.findViewById(R.id.tagsgrid);
		gridview.setAdapter(adapter);
		
		ImageButton add = (ImageButton) getActivity().findViewById(R.id.addtag);
		add.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditText edit = (EditText) getActivity().findViewById(R.id.tagsedit);
				if (edit.getText().length() > 0) {
					adapter.add(edit.getText().toString());
				}
				edit.setText("");
				edit.requestFocus();
			}
		});
		
		registerForContextMenu(gridview);
	}
	
	public String[] getTags() {
		String[] insts = new String[adapter.getCount()];
		int l = insts.length;
		for (int i = 0; i < l; i++) {
			insts[i] = adapter.getItem(i);
		}
		return insts;
	}
}