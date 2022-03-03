/******************************************************************
*
*	CyberUPnP for Java
*
*	Copyright (C) Satoshi Konno 2002
*
*	File: ActionListener.java
*
*	Revision;
*
*	01/16/03
*		- first revision.
*	
******************************************************************/

package com.xxx.test.upnp.upnp.control;

import com.xxx.test.upnp.upnp.*;

public interface ActionListener
{
	public boolean actionControlReceived(Action action);
}
