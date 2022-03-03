/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: SearchResponseListener.java
*
*	Revision;
*
*	11/18/02
*		- first revision.
*	
******************************************************************/

package com.xxx.test.upnp.upnp.device;

import com.xxx.test.upnp.upnp.ssdp.*;

public interface SearchResponseListener
{
	public void deviceSearchResponseReceived(SSDPPacket ssdpPacket);
}
