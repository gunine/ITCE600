/*******************************************************************************
 * Project		:	itce600
 * Package		:	kr.ac.postech.dpnm.model
 * Programmer	:	GUNi (yustit@gmail.com)
 * Description	:	Flow Table Entry List POJO Class
 * Create Date	:	2013. 8. 2.
 * Update Date	:	2013. 8. 2.
 * -----------------------------------------------------------------------------
 * TODO
 ******************************************************************************/
package kr.ac.postech.itce.model;

import java.util.ArrayList;
import java.util.List;

public class EntryList
{
	private List< Entry > entryList = new ArrayList< Entry >();
	
	public void addEntry ( Entry entry )
	{
		entryList.add( entry );
	}
	
	public List< Entry > getEntryList()
	{
		return entryList;
	}
}
