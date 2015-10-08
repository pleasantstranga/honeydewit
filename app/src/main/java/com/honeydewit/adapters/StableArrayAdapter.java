/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.honeydewit.adapters;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.honeydewit.pojos.ListItem;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StableArrayAdapter extends ArrayAdapter<ListItem>  {

    final int INVALID_ID = -1;

    Map<ListItem, Integer> mIdMap = new HashMap<ListItem, Integer>();


    public StableArrayAdapter(Context context, int textViewResourceId, List<ListItem> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i), objects.get(i).getRowNumber());
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        ListItem item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getPosition(ListItem item) {
        return item.getRowNumber();
    }
    public List<ListItem> getItems() {
        return getItems();
    }
}
