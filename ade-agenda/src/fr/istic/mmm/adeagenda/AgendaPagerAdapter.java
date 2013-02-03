package fr.istic.mmm.adeagenda;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

import fr.istic.mmm.adeagenda.utils.DateFormater;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class AgendaPagerAdapter extends FragmentStatePagerAdapter {

	private Context context;
	
	public AgendaPagerAdapter(FragmentManager fm, Context context) {
		super(fm);
		this.context = context;
	}

	 @Override
	    public Fragment getItem(int i) {
	        Fragment fragment = new AgendaFragment();
	        Bundle args = new Bundle();
	        // On donne l'information du jour au fragment
	        Calendar c = Calendar.getInstance();
	        Date now = new Date();
	        c.setTime(now);	        
	        c.add(Calendar.DAY_OF_YEAR, i);
	        c.set(Calendar.AM_PM, Calendar.AM);
	        c.set(Calendar.HOUR, 0);
	        c.set(Calendar.MINUTE, 0);
	        c.set(Calendar.SECOND, 0);
	        c.set(Calendar.MILLISECOND, 0);
	        args.putLong(AgendaFragment.ARG_DATE, c.getTimeInMillis());
	        fragment.setArguments(args);
	        return fragment;
	    }

	    @Override
	    public int getCount() {
	        return 7;
	    }

	    @Override
	    public CharSequence getPageTitle(int position) {
	    	String tabName = null;
	    	Calendar c = Calendar.getInstance();
	    	Date now = new Date();
	    	c.setTime(now);
	    	
	    	switch (position) {
	    	default:
	    	case 0:
	    		tabName = context.getString(R.string.today);
	    		break;
	    		
	    	case 1:
	    		tabName = context.getString(R.string.tomorrow);
	    		break;
	    		
	    	case 2:
	    		c.add(Calendar.DAY_OF_YEAR, 2);
	    		tabName = DateFormater.getDayString(context, c.get(Calendar.DAY_OF_WEEK));
	    		break;
	    		
	    	case 3:
	    		c.add(Calendar.DAY_OF_YEAR, 3);
	    		tabName = DateFormater.getDayString(context, c.get(Calendar.DAY_OF_WEEK));
	    		break;
	    		
	    	case 4:
	    		c.add(Calendar.DAY_OF_YEAR, 4);
	    		tabName = DateFormater.getDayString(context, c.get(Calendar.DAY_OF_WEEK));
	    		break;
	    		
	    	case 5:
	    		c.add(Calendar.DAY_OF_YEAR, 5);
	    		tabName = DateFormater.getDayString(context, c.get(Calendar.DAY_OF_WEEK));
	    		break;
	    		
	    	case 6:
	    		c.add(Calendar.DAY_OF_YEAR, 6);
	    		tabName = DateFormater.getDayString(context, c.get(Calendar.DAY_OF_WEEK));
	    		break;
	    	}
	    	
	        return tabName;
	    }
}
