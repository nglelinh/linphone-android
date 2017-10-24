/*
GroupChatFragment.java
Copyright (C) 2017  Belledonne Communications, Grenoble, France

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
*/

package org.linphone.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.linphone.LinphoneUtils;
import org.linphone.R;
import org.linphone.activities.LinphoneActivity;
import org.linphone.contacts.ContactAddress;
import org.linphone.contacts.LinphoneContact;

import java.util.ArrayList;
import java.util.List;

public class GroupInfoAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ContactAddress> mItems;
    private View.OnClickListener mDeleteListener;
    private boolean mHideAdminFeatures;

    public GroupInfoAdapter(LayoutInflater inflater, List<ContactAddress> items, boolean hideAdminFeatures) {
        mInflater = inflater;
        mItems = items;
        mHideAdminFeatures = hideAdminFeatures;
    }

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = mInflater.inflate(R.layout.chat_infos_cell, null);
        }

        final ContactAddress ca = (ContactAddress)getItem(i);
        LinphoneContact c = ca.getContact();

        TextView name = view.findViewById(R.id.name);
        ImageView avatar = view.findViewById(R.id.contact_picture);
        ImageView delete = view.findViewById(R.id.delete);
        LinearLayout adminLayout = view.findViewById(R.id.adminLayout);
        final LinearLayout isAdmin = view.findViewById(R.id.isAdminLayout);
        final LinearLayout isNotAdmin = view.findViewById(R.id.isNotAdminLayout);

        name.setText(c.getFullName());
        if (c.hasPhoto()) {
            LinphoneUtils.setThumbnailPictureFromUri(LinphoneActivity.instance(), avatar, c.getThumbnailUri());
        }

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDeleteListener != null) {
                    mDeleteListener.onClick(view);
                }
            }
        });
        delete.setTag(ca);

        isAdmin.setVisibility(ca.isAdmin() ? View.VISIBLE : View.GONE);
        isNotAdmin.setVisibility(ca.isAdmin() ? View.GONE : View.VISIBLE);

        isAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNotAdmin.setVisibility(View.VISIBLE);
                isAdmin.setVisibility(View.GONE);
                ca.setAdmin(false);
            }
        });

        isNotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isNotAdmin.setVisibility(View.GONE);
                isAdmin.setVisibility(View.VISIBLE);
                ca.setAdmin(true);
            }
        });

        if (mHideAdminFeatures) {
            delete.setVisibility(View.GONE);
            adminLayout.setVisibility(View.GONE);
        }

        return view;
    }

    public void setOnDeleteClickListener(View.OnClickListener onClickListener) {
        mDeleteListener = onClickListener;
    }

    public void updateDataSet(ArrayList<ContactAddress> mParticipants) {
        mItems = mParticipants;
        notifyDataSetChanged();
    }
}