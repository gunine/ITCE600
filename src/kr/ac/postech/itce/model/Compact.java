/*******************************************************************************
 * Project		:	sdnproject
 * Package		:	kr.ac.postech.dpnm.model
 * Programmer	:	GUNi (yustit@gmail.com)
 * Description	:	
 * Create Date	:	2013. 9. 16.
 * Update Date	:	2013. 9. 16.
 * -----------------------------------------------------------------------------
 * TODO
 ******************************************************************************/
package kr.ac.postech.itce.model;


public class Compact
{
	private String name;
	private String mac;
	private String actions;
	private Integer priority;
	private String src_ip;
	private String dst_ip;
	
	public Compact( String name, String mac, String actions, 
	                			Integer priority, String dst_ip )
	{
		this.name = name;
		this.mac = mac;
		this.actions = actions;
		this.priority = priority;
		this.src_ip = src_ip;
		this.dst_ip = dst_ip;		
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}
	
	public String getMac()
	{
		return mac;
	}

	public void setMac( String mac )
	{
		this.mac = mac;
	}

	public String getActions()
	{
		return actions;
	}

	public void setActions( String actions )
	{
		this.actions = actions;
	}

	public Integer getPriority()
	{
		return priority;
	}

	public void setPriority( Integer priority )
	{
		this.priority = priority;
	}

	public String getSrc_ip()
	{
		return src_ip;
	}

	public void setSrc_ip( String src_ip )
	{
		this.src_ip = src_ip;
	}

	public String getDst_ip()
	{
		return dst_ip;
	}

	public void setDst_ip( String dst_ip )
	{
		this.dst_ip = dst_ip;
	}
}
