/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: DeviceNotifyListener.java
*
*	Revision;
*
*	11/18/02
*		- first revision.
*	
******************************************************************/

package com.xxx.test.upnp.upnp.device;

import com.xxx.test.upnp.http.HTTPRequest;

public interface PresentationListener
{
	public void httpRequestRecieved(HTTPRequest httpReq);
}
