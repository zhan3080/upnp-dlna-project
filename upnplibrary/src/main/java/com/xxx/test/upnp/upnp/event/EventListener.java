/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002-2003
*
*	File: EventListener.java
*
*	Revision;
*
*	11/18/02
*		- first revision.
*	
******************************************************************/

package com.xxx.test.upnp.upnp.event;

public interface EventListener
{
	public void eventNotifyReceived(String uuid, long seq, String varName, String value);
}
