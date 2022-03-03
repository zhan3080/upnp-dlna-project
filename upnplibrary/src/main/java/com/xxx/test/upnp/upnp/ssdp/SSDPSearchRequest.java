/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: SSDPMSearchRequest.java
*
*	Revision;
*
*	11/19/02
*		- first revision.
*	
******************************************************************/

package com.xxx.test.upnp.upnp.ssdp;

import com.xxx.test.upnp.net.*;
import com.xxx.test.upnp.http.*;

import com.xxx.test.upnp.upnp.device.*;

public class SSDPSearchRequest extends SSDPRequest
{
	////////////////////////////////////////////////
	//	Constructor
	////////////////////////////////////////////////

	public SSDPSearchRequest(String serachTarget, int mx)
	{
		setMethod(HTTP.M_SEARCH);
		setURI("*");

		setHeader(HTTP.ST, serachTarget);
		setHeader(HTTP.MX, Integer.toString(mx));
		setHeader(HTTP.MAN, "\"" + MAN.DISCOVER + "\"");
	}
	
	public SSDPSearchRequest(String serachTarget)
	{
		this(serachTarget, SSDP.DEFAULT_MSEARCH_MX);
	}
	
	public SSDPSearchRequest()
	{
		this(ST.ROOT_DEVICE);
	}

	////////////////////////////////////////////////
	//	HOST
	////////////////////////////////////////////////
	
	public void setLocalAddress(String bindAddr)
	{
		String ssdpAddr = SSDP.ADDRESS;
		if (HostInterface.isIPv6Address(bindAddr) == true)
			ssdpAddr = SSDP.getIPv6Address();
		setHost(ssdpAddr, SSDP.PORT);
	}

}
