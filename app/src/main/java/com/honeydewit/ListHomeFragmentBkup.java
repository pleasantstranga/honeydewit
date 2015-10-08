package com.honeydewit;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.honeydewit.adapters.ListItemAdapter;
import com.honeydewit.adapters.NoteAdapter;
import com.honeydewit.adapters.draganddrop.DragSortListView;
import com.honeydewit.pojos.BasicList;
import com.honeydewit.pojos.ListItem;
import com.honeydewit.services.UpdateRowNumService;

import java.util.concurrent.Callable;


public class ListHomeFragmentBkup extends Fragment {


    private ArrayAdapter<ListItem> adapter;
    public DragSortListView listView;
    public Integer position = null;
    static final int REQUEST_IMAGE_CAPTURE = 1234;
    public boolean isShowImportErrorDialog = false;
    private HoneyDewApplication hdewContext;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        hdewContext = (HoneyDewApplication)getActivity().getApplicationContext();
        setRetainInstance(true);


        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.listhome, container, false);

        try {
            listView =(DragSortListView) layout.findViewById(R.id.list);

            listView.setDropListener(onDrop);
            listView.setRemoveListener(onRemove);
            //check first if user is going to notes screen
            loadList();
            isShowImportErrorDialog = true;

            if(hdewContext.getCurrentList().getListTypeId() == Constants.SHOPPING_LIST_TYPE_CDE) {
                RelativeLayout totalPriceLayout = (RelativeLayout) layout.findViewById(R.id.totalPriceLayout);
                totalPriceLayout.setVisibility(View.VISIBLE);
            }
        }
        catch(NullPointerException e) {
            ((BasicActivity)getActivity()).showError(R.string.error, new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    if (null != getActivity()) {
                        Intent intent = new Intent(getActivity(), MainMenuActivity.class);
                        startActivity(intent);
                    }
                    getActivity().finish();
                    return null;
                }
            });

        }
        return layout;
    }
    public void loadList() {

        if(hdewContext.getCurrentList() == null) {
            Integer subTypeCatCode = getActivity().getIntent().getIntExtra(Constants.SUB_CAT_CODE, -1);
            if(null != subTypeCatCode && subTypeCatCode == Constants.NOTES_LIST_TYP_CODE) {
                Log.d(ListHomeActivity.class.getName(), "Notes: currentlist null : subTypeCatCode = notes");

                hdewContext.setCurrentList(hdewContext.getShoppingListDbHelper().getListByName("Notes", Constants.NOTES_LIST_TYP_CODE, true, false));
            } else {
                int listId = getActivity().getIntent().getIntExtra("listId", -1);
                Log.d(ListHomeActivity.class.getName(), "Notes: getListById " + listId);
                hdewContext.setCurrentList(hdewContext.getShoppingListDbHelper().getListById(listId));
                if(hdewContext.getCurrentList() == null) {
                    Toast.makeText(getActivity(), getText(R.string.commonErrorRedirect), Toast.LENGTH_LONG).show();
                    startActivity(new Intent(getActivity(),MainMenuActivity.class));
                    getActivity().finish();
                }
            }


        }
        if(null == hdewContext.getCurrentList()) {
            Toast.makeText(getActivity(), "3: " + getText(R.string.commonErrorRedirect), Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(),MainMenuActivity.class));
            getActivity().finish();
        }
        getActivity().setTitle(hdewContext.getCurrentList().getListName());

        setupListAdapter(hdewContext.getCurrentList().isShowErrorInd());

    }

    private void setupListAdapter(boolean showErrors) {
        BasicList reloadedList = hdewContext.getShoppingListDbHelper().getListById(hdewContext.getCurrentList().get_id());
        hdewContext.setCurrentList(reloadedList);
        if(hdewContext.getCurrentList().getListTypeId() == Constants.SHOPPING_LIST_TYPE_CDE) {
            adapter = new ListItemAdapter(getActivity(), R.layout.listrow, hdewContext.getCurrentList().getItems(showErrors));
        }
        else if(hdewContext.getCurrentList().getListTypeId() == Constants.TODO_LIST_TYPE_CDE) {
            adapter = new ListItemAdapter(getActivity(),R.layout.listrow,  hdewContext.getCurrentList().getItems(showErrors));
        }
        else {
            adapter = new NoteAdapter((BasicActivity)getActivity(),R.layout.notesrow,  hdewContext.getCurrentList().getItems(false));
        }
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

    }


    private DragSortListView.DropListener onDrop = new DragSortListView.DropListener() {
        @Override
        public void drop(int from, int to) {
            try {
                ListItem item = adapter.getItem(from);
                adapter.remove(item);
                adapter.insert(item, to);

                SerializableArrayList<Integer> ids = new SerializableArrayList<>();
                for(int i=0 ; i<adapter.getCount() ; i++){
                    ids.add(adapter.getItem(i).get_id());
                }
                Intent intent = new Intent(getActivity(), UpdateRowNumService.class);
                intent.putExtra("ids",ids);
                getActivity().startService(intent);
            }
            catch(Exception e) {
                e.printStackTrace();
            }

        }
    };

    private DragSortListView.RemoveListener onRemove = new DragSortListView.RemoveListener() {
        @Override
        public void remove(int which) {
            adapter.remove(adapter.getItem(which));
        }
    };
    




}