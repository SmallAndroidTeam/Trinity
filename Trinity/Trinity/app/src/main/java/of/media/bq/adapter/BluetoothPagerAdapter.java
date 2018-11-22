package of.media.bq.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import of.media.bq.fragment.*;

public class BluetoothPagerAdapter extends FragmentPagerAdapter {

    private final String[] tabTitle = new String[]{ "拨号键盘", "通讯录", "通话记录", "未接来电"};

    private DialpadFragment dialpadFragment = null;
    private ContactsFragment contactsFragment = null;
    private CallLogFragment callLogFragment = null;
    private MissedCallFragment missedCallFragment = null;

    public BluetoothPagerAdapter(FragmentManager fm) {
        super(fm);
    }
 
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
                if (dialpadFragment == null) {
                    dialpadFragment = new DialpadFragment();
                }
                fragment = dialpadFragment;
                break;
            case 1:
                if (contactsFragment == null) {
                    contactsFragment = new ContactsFragment();
                }
                fragment = contactsFragment;
                break;
            case 2:
                if (callLogFragment == null) {
                    callLogFragment = new CallLogFragment();
                }
                fragment = callLogFragment;
                break;
            case 3:
                if (missedCallFragment == null) {
                    missedCallFragment = new MissedCallFragment();
                }
                fragment = missedCallFragment;
                break;
            default:
                break;
        }
        return fragment;
    }
 
    @Override
    public int getCount() {
        return tabTitle.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitle[position];
    }
}
