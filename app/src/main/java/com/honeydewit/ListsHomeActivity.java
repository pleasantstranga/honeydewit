package com.honeydewit;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ArrayAdapter;

import com.honeydewit.adapters.ListsHomeAdapter;
import com.honeydewit.adapters.draganddrop.DragSortListView;
import com.honeydewit.pojos.BasicList;

import java.util.ArrayList;
import java.util.List;

public class ListsHomeActivity extends OptionsMenuActivity{
   
    private ArrayList<ListsHomeAdapterData> lists;
    public static ListsHomeAdapter listAdapter;
    public static Integer listType;

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
	   	MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.action_bar_add_item_menu, menu);
	    
		return super.onCreateOptionsMenu(menu);
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
    	int itemId = item.getItemId();
    	if(itemId == R.id.addItem) {
    		getApplicationContext().setCurrentList(new BasicList());
			addToList();
            return true;
    	} else {
            return super.onOptionsItemSelected(item);
        }
    }
    
    @Override
    protected void onRestart() {
    	super.onRestart();
    	lists.clear();
        List<Integer> excludedListIds = getApplicationContext().getListsIdsToDelete();
        ArrayList<ListsHomeAdapterData> listsToAdd = getApplicationContext().getShoppingListDbHelper().getListsHomeAdapterData(listType,excludedListIds, Constants.TRUE);
    	for(ListsHomeAdapterData list : listsToAdd ) {
    		lists.add(list);
    	}
    	
    	listAdapter.notifyDataSetChanged();
    	
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listshome);
        setTitle(R.string.lists);
        getActionBar().setDisplayHomeAsUpEnabled(false);
        DragSortListView listView = (DragSortListView) findViewById(R.id.list);

        listView.setDropListener(onDrop);
        listView.setRemoveListener(onRemove);
        listType = getIntent().getIntExtra(Constants.SUB_CAT_CODE, Constants.SHOPPING_LIST_TYPE_CDE);
        
              	
        List<Integer> excudedListIds = getApplicationContext().getListsIdsToDelete();

        lists = getApplicationContext().getShoppingListDbHelper().getListsHomeAdapterData(listType,excudedListIds, Constants.TRUE);
        for(ListsHomeAdapterData data : lists) {
            data.setErrorCount(getApplicationContext().getShoppingListDbHelper().getImportErrorCount(data.getListId()));
            Log.d("Error Count",data.getErrorCount().toString());
        }
        listAdapter = new ListsHomeAdapter(this, lists);
        
        listView.setAdapter(listAdapter);
    }
    

    public void addToList() {
    	Intent newListIntent = new Intent(getApplicationContext(), ListInfoActivity.class);
    	newListIntent.putExtra(Constants.LIST_TYPE, listType);
    	startActivity(newListIntent); 

    }
    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
        getApplicationContext().setCurrentList(null);
        Intent intent = new Intent(this, CreateEditListsActivity.class);
        startActivity(intent);
        super.onBackPressed();

    }
    private DragSortListView.DropListener onDrop =
            new DragSortListView.DropListener() {
                @Override
                public void drop(int from, int to) {
                    ListsHomeAdapterData item = listAdapter.getItem(from);

                    listAdapter.remove(item);
                    listAdapter.insert(item, to);

                    Intent intent = new Intent(getBaseContext(),UpdateRowNumbersTask.class);
                    intent.putExtra("adapter", listAdapter);
                    startService(intent);
                }
            };

    private DragSortListView.RemoveListener onRemove =
            new DragSortListView.RemoveListener() {
                @Override
                public void remove(int which) {
                    listAdapter.remove(listAdapter.getItem(which));
                }
            };

        private class UpdateRowNumbersTask extends IntentService {

        public UpdateRowNumbersTask() {
            super("UpdateRowNumbersTask");
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            try {
                ArrayAdapter adapter = (ArrayAdapter)intent.getSerializableExtra("adapter");
                ((HoneyDewApplication)getApplicationContext()).getShoppingListDbHelper().updateListRowNumbersByAdapterPosition(adapter);

            } catch (Exception e) {
                e.printStackTrace();
            } finally {

            }
        }


    }
}
