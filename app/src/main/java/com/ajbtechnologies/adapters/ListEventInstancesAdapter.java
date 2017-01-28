package com.ajbtechnologies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ajbtechnologies.BasicActivity;
import com.ajbtechnologies.Constants;
import com.ajbtechnologies.Application;
import com.ajbtechnologies.DialogFragment;
import com.ajbtechnologies.R;
import com.ajbtechnologies.calendar.DateUtil;
import com.ajbtechnologies.calendar.EventActivity;
import com.ajbtechnologies.calendar.EventInstance;
import com.ajbtechnologies.listeners.OneOffClickListener;
import com.ajbtechnologies.pojos.ListEvent;
import com.ajbtechnologies.utils.StringUtils;

import java.util.List;

public class ListEventInstancesAdapter extends ArrayAdapter<EventInstance> {

        private boolean showEventTitle = true;
        private boolean showDate = true;
        private boolean showTime = true;
        private List<EventInstance> items;
        private final Context context;
        private String currentMonthYear;
        private Integer resourceId = null;

        
        public ListEventInstancesAdapter(Context context, List<EventInstance> items) {
                super(context, R.layout.eventlistrow, items);
                this.resourceId = R.layout.eventlistrow;
                this.items = items;
                this.context = context;
                
        }
        
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
        		LayoutInflater vi = null;
                LinearLayout linearLayout = (LinearLayout)convertView;
                if (linearLayout == null) {
                    vi= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    linearLayout = (LinearLayout)vi.inflate(resourceId, null);
                }

                final EventInstance instance = items.get(position);
                if (instance != null) {
                        TextView eventTitle = (TextView) linearLayout.findViewById(R.id.eventTitle);
                        TextView eventDate = (TextView) linearLayout.findViewById(R.id.eventDate);
                        TextView eventTime = (TextView) linearLayout.findViewById(R.id.eventTime);
                        
                        String dateFrom = DateUtil.timeFormat.format(instance.getDateFrom().getTime());
                        String dateTo = DateUtil.timeFormat.format(instance.getDateTo().getTime());
                        
                        
                        if(isShowEventTitle()) {
                        	eventTitle.setVisibility(View.VISIBLE);
                        	eventTitle.setText(!StringUtils.isEmpty(instance.getEvent().getTitle()) ? instance.getEvent().getTitle() : Constants.NO_SUBJECT);
                        }
                        else {
                        	eventTitle.setVisibility(View.GONE);
                        }
                        if(isShowDate()) {
                        	eventDate.setVisibility(View.VISIBLE);
                        	if(instance.getEvent().isAllDay()) {
                        		eventDate.setText("All Day");
                        	}
                        	else {
                        		eventDate.setText(DateUtil.dayOfWeek.format(instance.getDateFrom().getTime()) + "   " + DateUtil.monthDayYear.format(instance.getDateFrom().getTime()));
                        	}
                        }
                        else {
                        	eventDate.setVisibility(View.GONE);
                        }
                        if(isShowTime()) {
                        	eventTime.setVisibility(View.VISIBLE);
                        	eventTime.setText(dateFrom + "-" + dateTo);
                        }
                        else {
                        	eventTime.setVisibility(View.GONE);
                        }
                }
                linearLayout.setOnClickListener(new OneOffClickListener() {
					
                	
					@Override
					public void onClick(View v) {
						Intent newListIntent = new Intent(context, EventActivity.class);
						newListIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						newListIntent.putExtra("eventInstance",instance);
				    	context.startActivity(newListIntent);	
					}
				});
                ImageButton deleteButton = (ImageButton) linearLayout.findViewById(R.id.deleteButton);
                deleteButton.setOnClickListener(new OneOffClickListener() {
					@SuppressWarnings("deprecation")
					@Override
					public void onClick(View v) {
						
						List<ListEvent> events = ((Application)context.getApplicationContext()).getShoppingListDbHelper().getListEvents(instance.getEvent().getCalendarId(), instance.getEvent().getEventId());
                		
						if(events.size() == 0)  {
							DialogFragment dialog = new DialogFragment();
							dialog.show(((FragmentActivity) getContext()).getSupportFragmentManager(), "listEventInstances");
							boolean isExec = ((FragmentActivity)getContext()).getSupportFragmentManager().executePendingTransactions();


							dialog.setTitle(getContext().getText(R.string.areYouSure).toString());
							dialog.setMessage(getContext().getText(R.string.deleteItemWarning).toString());
							dialog.setPositiveButton(getContext().getText(R.string.ok).toString(), new View.OnClickListener() {

								@Override
								public void onClick(View v) {
									try {
										instance.getEvent().setExDate(DateUtil.getUTCExDateString(instance.getDateFrom(), instance.getEvent().getExDate()));
										//date to and duration both can't be populated when saving a recurring event
										instance.getEvent().setDateTo(null);
										((Application) context.getApplicationContext()).getCalDbHelper().addUpdateCalendarEvent(context, instance.getEvent());
									} catch (Exception e) {
										e.printStackTrace();
									} finally {
										remove(instance);
										notifyDataSetChanged();
									}
								}
							});
							dialog.setNeutralButton(getContext().getText(R.string.cancel).toString(), null);
						}
						else {
							((BasicActivity)context).showDialog(R.string.cantDeleteItem);
						}
					}
				});
                return linearLayout;
        }

		public String getCurrentMonthYear() {
			return currentMonthYear;
		}

		public void setCurrentMonthYear(String currentMonthYear) {
			this.currentMonthYear = currentMonthYear;
		}

		public boolean isShowEventTitle() {
			return showEventTitle;
		}

		public void setShowEventTitle(boolean showEventTitle) {
			this.showEventTitle = showEventTitle;
		}

		public boolean isShowDate() {
			return showDate;
		}

		public void setShowDate(boolean showDate) {
			this.showDate = showDate;
		}

		public boolean isShowTime() {
			return showTime;
		}

		public void setShowTime(boolean showTime) {
			this.showTime = showTime;
		}
}