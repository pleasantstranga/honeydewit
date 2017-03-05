package com.ajbtechnologies.dataaccess;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.ajbtechnologies.Constants;
import com.ajbtechnologies.ImportError;
import com.ajbtechnologies.ListsHomeAdapterData;
import com.ajbtechnologies.pojos.BasicList;
import com.ajbtechnologies.pojos.BasicListLight;
import com.ajbtechnologies.pojos.ImportHeader;
import com.ajbtechnologies.pojos.Links;
import com.ajbtechnologies.pojos.ListEvent;
import com.ajbtechnologies.pojos.ListItem;
import com.ajbtechnologies.pojos.Store;
import com.ajbtechnologies.pojos.Unit;
import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.dao.GenericRawResults;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;
import com.j256.ormlite.stmt.Where;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

public class DbHelperImpl extends DbHelper {


    public DbHelperImpl(Context context) {
        super(context);
    }


	public int addUpdateListItem(ListItem listItem) {
		int id = -1;
		try {
            boolean isUpdate = listItem.isUpdate();
            addAuditData(listItem, isUpdate);
            if (listItem.getRowNumber() == null) {
                int rowNumber = getNextListItemRowNumber(listItem.getList().get_id()).intValue();
                listItem.setRowNumber(rowNumber);
            }

			getListItemDao().createOrUpdate(listItem);

			id = getListItemDao().extractId(listItem);

		}

		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {

			e.printStackTrace();
		}
		finally {
			try {

				getListItemDao().closeLastIterator();
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return id;
	}
	public boolean setListChecked(boolean checked, Integer listId) {
		int update = 0;
		try {
			Integer checkedInt = checked ? Constants.TRUE : Constants.FALSE;
			ContentValues cv = new ContentValues();
			cv.put("CHECKED", checkedInt.toString());

			SQLiteDatabase db = this.getReadableDatabase();
		    update = db.update("LISTS", cv, "_id = " + listId, null);

		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return update > 0;

	}
	public void addUpdateShoppingList(BasicList list, boolean isReplaceAllListWithSameName) {
		try {
			if(isReplaceAllListWithSameName) {
				BasicList listToRemove = getListByName(list.getListName(), list.getListTypeId(), true);
				if(listToRemove != null) {
					getListDao().delete(listToRemove);
				}
			}
			addAuditData(list, null != list.get_id());
		    list.setEnabled(Constants.TRUE);
			if(list.getRowNumber() == null) {
				long rowNumber = getNextListRowNumber(true);
				list.setRowNumber((int)rowNumber);
			}
			getListDao().createOrUpdate(list);

		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				getListDao().closeLastIterator();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void addUpdateShoppingList(BasicList list) {
		try {
			addAuditData(list, null != list.get_id());
			if(list.get_id() == null) {
                list.setEnabled(Constants.TRUE);
			}
			if(list.getRowNumber() == null) {
				long rowNumber = getNextListRowNumber(true);
				list.setRowNumber((int)rowNumber);
			}
			getListDao().createOrUpdate(list);
			if(list.getItems() == null) {
				ForeignCollection<ListItem> items = getListDao().getEmptyForeignCollection(BasicList.ITEMS);
				list.setItems(items);
			}


		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				getListDao().closeLastIterator();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}

	}
    public void deleteErrorListItems(BasicList list)  {

            for(ListItem listItem : list.getItems()) {
                if(listItem.hasErrors()) {
                    deleteImportErrors(listItem);
                    deleteListItem(listItem.get_id());

                }
            }


    }
	public boolean isListExists(String listName, Integer listType, boolean enabledOnly)  {
		boolean isExists = false;
		GenericRawResults<String[]> rawResults = null;
		try {
            int enabled = enabledOnly ? Constants.TRUE : Constants.FALSE;
			String sql= "select count("+ BasicList.LIST_NAME_CLM +") from " + BasicList.TABLE_NAME + " where "+ BasicList.LIST_NAME_CLM + " = '" + listName + "'" +
                    " and " + BasicList.LIST_TYPE_ID_CLM + " = " + listType;

            if(enabledOnly) {
                sql += " and " + BasicList.ENABLED_CLM +  " =  " + enabled;
            }

			rawResults = getListDao().queryRaw(sql);
			// there should be 1 result
			String[] resultArray = rawResults.getResults().get(0);
			isExists = Integer.valueOf(resultArray[0]) > 0;

		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {

			if(rawResults!= null) {
				try {
					getListDao().closeLastIterator();
					rawResults.close();
				}
				catch(Exception e) {
					e.printStackTrace();
				}

			}


		}
	    return isExists;

	}
	public boolean isListErrorsExist(int listId)  {
		boolean isExists = false;
		GenericRawResults<String[]> rawResults = null;
		try {

			rawResults = getListDao().queryRaw("select count("+ BasicList.ID_CMN +") from " + ImportError.TABLE_NAME + " where "+ ImportError.LIST_ID_CLMN + " = '" + listId + "'");
			// there should be 1 result
			String[] resultArray = rawResults.getResults().get(0);
			isExists = Integer.valueOf(resultArray[0]) > 0;

		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				getListDao().closeLastIterator();
				if(rawResults!= null) {
					rawResults.close();
				}
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
	    return isExists;

	}
	public BasicList getListById(Integer listId)  {
		BasicList list = null;
		Where<BasicList, Integer> queryBuilder = null;
		try {
			queryBuilder = getListDao().queryBuilder().where().eq(BasicList.ID_CMN, listId);

			List<BasicList> lists = queryBuilder.query();

			if(lists != null && lists.size() > 0) {
				list = lists.get(0);
			}

		}
		catch(SQLException e) {
			e.printStackTrace();

		}
		finally {
			try {
				getListDao().closeLastIterator();
				queryBuilder.reset();
			}
			catch(Exception e) {
				e.printStackTrace();
			}


		}
		return list;
	}
    public List<BasicList> getLists(boolean getEnabledOnly, Integer listTypeCode)  {
    	List<BasicList> list = new ArrayList<BasicList>();
    	try {
    		QueryBuilder<BasicList, ?> queryBuilder = getListDao().queryBuilder();
        	if(getEnabledOnly) {
    	       queryBuilder.where().eq(BasicList.ENABLED_CLM, Constants.TRUE).and().eq(BasicList.LIST_TYPE_ID_CLM, listTypeCode);

			}
        	queryBuilder.where().eq(BasicList.LIST_TYPE_ID_CLM, listTypeCode);

			list = queryBuilder.query();


		}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    	finally {
    		try {
    			getListDao().closeLastIterator();
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
		return list;
    }
    public List<BasicListLight> getLightLists(boolean getEnabledOnly, Integer listTypeCode)  {
    	List<BasicListLight> list = new ArrayList<BasicListLight>();
    	try {
    		QueryBuilder<BasicListLight, ?> queryBuilder = getLightListDao().queryBuilder();
        	if(getEnabledOnly) {
    	       queryBuilder.where().eq(BasicListLight.ENABLED_CLM, Constants.TRUE).and().eq(BasicListLight.LIST_TYPE_ID_CLM, listTypeCode);

			}
        	queryBuilder.where().eq(BasicListLight.LIST_TYPE_ID_CLM, listTypeCode);

			list = queryBuilder.query();


		}
    	catch(SQLException e) {
    		e.printStackTrace();
    	}
    	finally {
    		try {
    			getListDao().closeLastIterator();
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
		return list;
    }
    public List<ListItem> getShoppingListItems(int shoppingListId)  {
    	List<ListItem> list = new ArrayList<ListItem>();
    	QueryBuilder<ListItem, ?> queryBuilder = null;
    	try {
    		queryBuilder = getListItemDao().queryBuilder();
        	queryBuilder.where().eq(ListItem.LIST_CMN, shoppingListId);
        	list = queryBuilder.query();
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	finally {
    		try {
    			getListItemDao().closeLastIterator();
    			if(queryBuilder != null) {
    				queryBuilder.reset();
    			}
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return list;
    }
    public ListItem getListItemById(int id) {
    	ListItem listItem = null;
    	QueryBuilder<ListItem, ?> queryBuilder = null;
    	try {
    		queryBuilder = getListItemDao().queryBuilder();
        	queryBuilder.where().eq(ListItem.ID_CMN, id);
        	listItem = queryBuilder.queryForFirst();
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	finally {
    		try {
    			getListItemDao().closeLastIterator();
    			if(queryBuilder != null) {
    				queryBuilder.reset();
    			}
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return listItem;
    }

	public List<Unit> getUnitsOfMeasurements(Integer convTypeCde) {
		List<Unit> units = null;
		QueryBuilder<Unit, ?> queryBuilder = null;
		try {
			queryBuilder = getUnitsDao().queryBuilder();
	    	queryBuilder.where().eq(Unit.CONV_TYP_CDE_CMN, convTypeCde);
	    	queryBuilder.orderBy(Unit.TYP_NAM_CMN, true);
			units = queryBuilder.query();
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
    		try {
    			getUnitsDao().closeLastIterator();
    			if(queryBuilder != null) {
    				queryBuilder.reset();
    			}
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
		return units;
	}
	public void deleteList(int listId)  {

		try {
			BasicList list = getListDao().queryForId(listId);
			getListDao().delete(list);
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
    		try {
    			getListDao().closeLastIterator();
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
	}
public void deleteList(BasicList list)  {

	try {
			deleteImportErrors(list);
			getListDao().delete(list);

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
    		try {
    			getListDao().closeLastIterator();
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
	}
	public void deleteListItem(int listItemId)  {
		SQLiteDatabase db = null;
		try {
			db = this.getReadableDatabase();
			ListItem listItem = getListItemDao().queryForId(listItemId);
			if(listItem != null) {
				int currentRowNum = listItem.getRowNumber();
				getListItemDao().delete(listItem);

				String updateRowNum = "UPDATE " + ListItem.TABLE_NAME + " SET " + ListItem.ROW_NUM_CMN + " = " +
				ListItem.ROW_NUM_CMN + " - 1 WHERE " +  ListItem.ROW_NUM_CMN + " > " + currentRowNum;


				db.execSQL(updateRowNum);
			}

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
    		try {
    			getListItemDao().closeLastIterator();
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
	}
	public void deleteImportErrors(final ListItem listItem)  {

		try {
			 getImportErrorsDao().callBatchTasks(new Callable<Integer>() {
				 public Integer call() throws Exception {
					 for (ImportError error : listItem.getErrors()) {

						 getImportErrorsDao().delete(error);
					 }
					 return 1;
				 }
			 });

		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
    		try {
    			getImportErrorsDao().closeLastIterator();
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
	}
	public Long getNextListItemRowNumber(Integer listId) {
		Long rowNumber = 0L;
		try {
			rowNumber = getListItemDao().countOf(getListItemDao().queryBuilder().setCountOf(true).where().eq(ListItem.LIST_CMN, listId).prepare());
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
    		try {
    			getListItemDao().closeLastIterator();
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
		return rowNumber;
	}
	public Long getNextListRowNumber(boolean enabled) {
		Long rowNumber = 0L;
		try {
			rowNumber = getListDao().countOf(getListDao().queryBuilder().setCountOf(true).where().eq(BasicList.ENABLED_CLM, enabled ? Constants.TRUE : Constants.FALSE).prepare());
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			try {
				getListItemDao().closeLastIterator();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return rowNumber;
	}


	public ListEvent saveListEvent(ListEvent listEvent)  {
		try {
			getListEventsDao().createOrUpdate(listEvent);
			listEvent.set_id(getListEventsDao().extractId(listEvent));
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
    		try {
    			getListEventsDao().closeLastIterator();
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
		return listEvent;

	}
	public ListEvent getListEventByListId(Long listId) {
		ListEvent event = null;
		QueryBuilder<ListEvent, ?> queryBuilder = null;
    	try {
    		queryBuilder = getListEventsDao().queryBuilder();
        	queryBuilder.where().eq(ListEvent.LIST_CLMN, listId);
			event = queryBuilder.queryForFirst();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	finally {
    		try {
    			getListEventsDao().closeLastIterator();
    			if(null != queryBuilder) {
    				queryBuilder.reset();
    			}
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return event;
	}
	public List<Links> getLinks(Integer linksTypeCode)  {
		List<Links> links = new ArrayList<Links>();

		Where<Links, Integer> queryBuilder = null;
		try {
			queryBuilder = getLinksDao().queryBuilder().where().eq(Links.LINKS_TYPE_CDE_CLMN, linksTypeCode);

			links = queryBuilder.query();

		}
		catch(SQLException e) {
			e.printStackTrace();

		}
		finally {
			try {
				getLinksDao().closeLastIterator();
				queryBuilder.reset();
			}
			catch(Exception e) {
				e.printStackTrace();
			}


		}
		return links;
	}
	public List<Links> getLinks(Integer linksTypeCode, Integer[] exclusions)  {
		List<Links> links = new ArrayList<Links>();

		Where<Links, Integer> queryBuilder = null;
		try {
			queryBuilder = getLinksDao().queryBuilder().where().eq(Links.LINKS_TYPE_CDE_CLMN, linksTypeCode);
			if(exclusions != null && exclusions.length > 0) {
				queryBuilder.and().notIn("_id", Arrays.asList(exclusions));
			}

			links = queryBuilder.query();

		}
		catch(SQLException e) {
			e.printStackTrace();

		}
		finally {
			try {
				getLinksDao().closeLastIterator();
				queryBuilder.reset();
			}
			catch(Exception e) {
				e.printStackTrace();
			}


		}
		return links;
	}
	public List<ListEvent> getListEvents(Integer calendarId, Integer eventId) {
		List<ListEvent> list = new ArrayList<ListEvent>();
    	QueryBuilder<ListEvent, ?> queryBuilder = null;
    	try {
    		queryBuilder = getListEventsDao().queryBuilder();
        	queryBuilder.where().eq(ListEvent.EVENT_CLMN, eventId).and().eq(ListEvent.CALENDAR_CLMN, calendarId);
			list = queryBuilder.query();
    	}
    	catch (SQLException e) {
			e.printStackTrace();
		}
    	finally {
			try {
    			getListItemDao().closeLastIterator();
    			if(queryBuilder != null) {
    				queryBuilder.reset();
    			}
    		}
    		catch(Exception e) {
    			e.printStackTrace();
    		}
    	}
    	return list;
	}
	public List<ImportHeader> getImportHeaders()  {
		List<ImportHeader> headers = new ArrayList<ImportHeader>();
		try {
			headers = getImportHeadersDao().queryBuilder().query();
		}
		catch(SQLException e) {
			e.printStackTrace();

		}
		finally {
			try {
				getImportHeadersDao().closeLastIterator();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return headers;
	}

	public void addAllListItems(final BasicList parent, final List<ListItem> items) {
		try {
			if (parent.getItems() == null) {
		    	createListItems(parent);
		    }
		    getListItemDao().callBatchTasks(new Callable<Integer>() {
		    	 public Integer call() throws Exception {
		             for (ListItem listItem : items) {
		            	listItem.setList(parent);
		            	addAuditData(listItem, false);
		             	getListItemDao().create(listItem);

					 }
		             return 1;
		         }
			});
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				getListItemDao().clearObjectCache();
				getListItemDao().closeLastIterator();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
	private void createListItems(BasicList list)  throws Exception{
		if(list.getItems() == null) {
			list.items = getListDao().getEmptyForeignCollection(BasicList.ITEMS);
		}
	}
	public void saveImportErrors(final List<ImportError> importErrors)  {
		if(importErrors != null && importErrors.size() > 0) {
			try {
				getImportErrorsDao().callBatchTasks(new Callable<Void>() {
					@Override
					public Void call() throws Exception {
						for (ImportError error : importErrors) {
							addAuditData(error, false);
							getImportErrorsDao().create(error);
						}
						return null;
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public BasicList getListByName(String listName, Integer listTypeCode, boolean enabledOnly)  {
		BasicList list = null;
		Where<BasicList, Integer> queryBuilder = null;
		try {
			queryBuilder = getListDao().queryBuilder().where().eq(BasicList.LIST_NAME_CLM, listName);
			if(null != listTypeCode) {
				queryBuilder.and().eq(BasicList.LIST_TYPE_ID_CLM, listTypeCode);
			}
			if(enabledOnly) {
				queryBuilder.and().eq(BasicList.ENABLED_CLM, Constants.TRUE);
			}
			List<BasicList> lists = queryBuilder.query();

			if(lists != null && lists.size() > 0) {
				list = lists.get(0);
			}

		}
		catch(SQLException e) {
			e.printStackTrace();

		}
		finally {
			try {

					getListDao().closeLastIterator();
                    if(queryBuilder != null) {
                        queryBuilder.reset();
                    }

			}
			catch(Exception e) {
				e.printStackTrace();
			}


		}
		return list;
	}
	public BasicList getListByName(String listName, Integer listTypeCode, boolean enabledOnly, boolean isCloseIterator)  {
		BasicList list = null;
		Where<BasicList, Integer> queryBuilder = null;
		try {
			queryBuilder = getListDao().queryBuilder().where().eq(BasicList.LIST_NAME_CLM, listName);
			if(null != listTypeCode) {
				queryBuilder.and().eq(BasicList.LIST_TYPE_ID_CLM, listTypeCode);
			}
			if(enabledOnly) {
				queryBuilder.and().eq(BasicList.ENABLED_CLM, Constants.TRUE);
			}
			List<BasicList> lists = queryBuilder.query();

			if(lists != null && lists.size() > 0) {
				list = lists.get(0);
			}

		}
		catch(SQLException e) {
			e.printStackTrace();

		}
		finally {
			try {
				if(isCloseIterator) {
					getListDao().closeLastIterator();
					queryBuilder.reset();

				}

			}
			catch(Exception e) {
				e.printStackTrace();
			}


		}
		return list;
	}
	public  ArrayList<ListsHomeAdapterData> getListsHomeAdapterData(int listTypeCode, List<Integer> excludeList, Integer enabled) {
	    ArrayList<ListsHomeAdapterData> map = new ArrayList<ListsHomeAdapterData>();
	    // Select All Query
        String[] args;
        String selectQuery = "SELECT LISTS.LIST_NAME, LISTS._id,  COUNT(LISTS_ITEMS._id),LISTS.LIST_TYPE_ID "
                + "FROM LISTS "
                + "left JOIN LISTS_ITEMS ON LISTS._id = LISTS_ITEMS.LISTS_ID "
                + "WHERE LISTS.LIST_TYPE_ID =  " + listTypeCode + " ";
                if(null != enabled) {
                    selectQuery +="AND LISTS.ENABLED =  " + enabled + " ";
                }
                if(excludeList != null && excludeList.size() > 0) {
                    selectQuery += "AND LISTS._ID NOT IN (" + TextUtils.join(",", excludeList) + ") ";
                }
		selectQuery += "GROUP BY LISTS.LIST_NAME, LISTS._id,LISTS.LIST_TYPE_ID "
				+ "ORDER BY LISTS.ROW_NUM asc";
	    SQLiteDatabase db = this.getReadableDatabase();

	    Cursor mCursor = db.rawQuery(selectQuery, null);
        int countList = 0;
	    // looping through all rows and adding to list
	    if (mCursor.moveToFirst()) {

            do {
                countList +=1;
            	String listName = mCursor.getString(0);
            	Integer id = mCursor.getInt(1);
     	        Integer count = mCursor.getInt(2);
     	        Integer listTypeId = mCursor.getInt(3);

     	        ListsHomeAdapterData data = new ListsHomeAdapterData();
     	        data.setListName(listName);
     	        data.setListId(id);
     	        data.setItemCount(count);
				data.setListTypeId(listTypeId);
                //data.setErrorCount(errorCount);
     	        map.add(data);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
	    // return contact list
	    return map;
	}
	public Map<Integer, Integer> getListItemRowsToFix(int listId)  {
		String sql= "select " + ListItem.ROW_NUM_CMN + " from  " + ListItem.TABLE_NAME + " where "+ ListItem.LIST_CMN + " = " + listId + " ORDER BY CAST(" +  ListItem.ROW_NUM_CMN + " as REAL)";

		@SuppressLint("UseSparseArrays")
		Map<Integer,Integer> rowNumbers = new HashMap<Integer, Integer>();
		SQLiteDatabase db = this.getReadableDatabase();
	    Cursor mCursor = db.rawQuery(sql, null);

		// looping through all rows and adding to list
	    if (mCursor.moveToFirst()) {
            do {

				Integer id = mCursor.getInt(0);
            	Log.d("DbHelperImpl", "Row Number: " + id);
            } while (mCursor.moveToNext());
        }
        if (mCursor != null && !mCursor.isClosed()) {
            mCursor.close();
        }
	    // return contact list
	    return rowNumbers;

	}
    public void toggleEnabledList(int listId, boolean isEnable)  {
        SQLiteDatabase db = null;
        try {
            db = this.getReadableDatabase();
            int enabled = isEnable ? Constants.TRUE : Constants.FALSE;
            String updateRowNum = "UPDATE " + BasicList.TABLE_NAME + " SET " + BasicList.ENABLED_CLM + " = " +
                        enabled + " WHERE " +  BasicList.ID_CMN + " = " + listId;


            db.execSQL(updateRowNum);

        }
        catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally {
            try {
                getListItemDao().closeLastIterator();
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }

    }
	public void deleteImportErrors(BasicList list) {

            try {
                DeleteBuilder<ImportError, Integer> deleteBuilder = getImportErrorsDao().deleteBuilder();
                deleteBuilder.where().eq(ImportError.LIST_ID_CLMN, list.get_id());
                getImportErrorsDao().delete(deleteBuilder.prepare());
            } catch (Exception e) {
                e.printStackTrace();
            }

    }
    public Integer getImportErrorCount(Integer listId)  {
        Where<ImportError, Integer> queryBuilder = null;
        try {
            queryBuilder = getImportErrorsDao().queryBuilder().where().eq(ImportError.LIST_ID_CLMN, listId);

            List<ImportError> errors = queryBuilder.query();

           return errors.size();

        }
        catch(SQLException e) {
            e.printStackTrace();

        }
        finally {
            try {

                    getImportErrorsDao().closeLastIterator();
                    queryBuilder.reset();



            }
            catch(Exception e) {
                e.printStackTrace();
            }


        }
        return 0;
    }

	/**
	 * Updates the row numbers of all list items in an adapter by their position in the adapter;
	 * @param ids The ids of the list items
	 */
	public void updateListItemRowNumbersByListPosition(ArrayList<Integer> ids) {
		try {
			for(int position = 0; position < ids.size(); position++) {

				UpdateBuilder<ListItem, Integer> updateBuilder = getListItemDao().updateBuilder();
				updateBuilder.updateColumnValue(ListItem.ROW_NUM_CMN, position);
				updateBuilder.where().idEq(ids.get(position));
				int update = updateBuilder.update();

			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}

	}
	/**
	 * Updates the row numbers of all lists in an adapter by their position in the adapter;
	 * @param listPositions The positions
	 */
	public void updateListRowNumbersByAdapterPosition(Map<Integer,Integer> listPositions) {
		try {
			for(Map.Entry entry : listPositions.entrySet()) {
				int update = 0;
				UpdateBuilder<BasicList, Integer> updateBuilder = getListDao().updateBuilder();
				updateBuilder.updateColumnValue(BasicList.ROW_NUM_CMN, entry.getValue());
				updateBuilder.where().idEq((Integer)entry.getKey());
				update = updateBuilder.update();
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}

	}
	public Store getStoreInfo(Integer storeId) {
		Store store = null;
		try {
			store = getStoreDao().queryForId(storeId);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return store;
	}
}