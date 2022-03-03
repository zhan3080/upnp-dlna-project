/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: SearchListener.java
*
*	Revision;
*
*	11/18/02b
*		- first revision.
*	
******************************************************************/

package com.xxx.test.upnp.upnp.device;

import com.xxx.test.upnp.upnp.ssdp.*;

public interface SearchListener
{
	public void deviceSearchReceived(SSDPPacket ssdpPacket);
}
