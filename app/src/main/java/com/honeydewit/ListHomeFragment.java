package com.honeydewit;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
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

import java.util.ArrayList;
import java.util.List;


public class ListHomeFragment extends Fragment {

    RelativeLayout layout;
    public ArrayAdapter<ListItem> adapter;
    public DragSortListView listView;

    static final int REQUEST_IMAGE_CAPTURE = 1234;

    private HoneyDewApplication hdewContext;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        hdewContext = (HoneyDewApplication)getActivity().getApplicationContext();
        layout = (RelativeLayout) inflater.inflate(R.layout.listhome, container, false);
        listView =(DragSortListView) layout.findViewById(R.id.list);
        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadList();


    }

    public void loadList() {
        int listId = getActivity().getIntent().getIntExtra("listId", -1);
        new LoadListAsyncTask().execute(listId);


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

    protected class LoadListAsyncTask extends AsyncTask<Integer, Void, Boolean> {


        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pd = new ProgressDialog(getActivity());
            pd.setMessage("Please wait while your data loads");
            pd.show();
        }

        protected Boolean doInBackground(Integer... listIds) {

            if(hdewContext.getCurrentList() == null) {
                Integer subTypeCatCode = getActivity().getIntent().getIntExtra(Constants.SUB_CAT_CODE, -1);
                if(null != subTypeCatCode && subTypeCatCode == Constants.NOTES_LIST_TYP_CODE) {
                    Log.d(ListHomeActivity.class.getName(), "Notes: currentlist null : subTypeCatCode = notes");

                    hdewContext.setCurrentList(hdewContext.getShoppingListDbHelper().getListByName("Notes", Constants.NOTES_LIST_TYP_CODE, true, false));
                }
                else {

                    int listId = listIds[0];
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


            createListAdapter(hdewContext.getCurrentList().isShowErrorInd());

            return true;
        }

        protected void onPostExecute(Boolean result) {
            listView.setAdapter(adapter);
            listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            listView.setDropListener(onDrop);
            listView.setRemoveListener(onRemove);
            getActivity().setTitle(hdewContext.getCurrentList().getListName());
            pd.dismiss();

            if(hdewContext.getCurrentList().getListTypeId() == Constants.SHOPPING_LIST_TYPE_CDE) {
                RelativeLayout totalPriceLayout = (RelativeLayout) layout.findViewById(R.id.totalPriceLayout);
                totalPriceLayout.setVisibility(View.VISIBLE);
            }

        }

        private void createListAdapter(boolean showErrors) {
            //reload data here
            //if(getActivity().)
            if(hdewContext.getCurrentList().getListTypeId() == Constants.SHOPPING_LIST_TYPE_CDE) {
                adapter = new ListItemAdapter(getActivity(), R.layout.listrow, hdewContext.getCurrentList().getItems(showErrors));
            }
            else if(hdewContext.getCurrentList().getListTypeId() == Constants.TODO_LIST_TYPE_CDE) {
                adapter = new ListItemAdapter(getActivity(),R.layout.listrow,  hdewContext.getCurrentList().getItems(showErrors));
            }
            else {
                adapter = new NoteAdapter((BasicActivity)getActivity(),R.layout.notesrow,  hdewContext.getCurrentList().getItems(false));
            }

        }



    }

}