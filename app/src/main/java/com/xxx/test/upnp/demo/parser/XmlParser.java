package com.xxx.test.upnp.demo.parser;

import android.text.TextUtils;
import android.util.Log;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class XmlParser {
    public static final String TAG = "XmlParser";

    public static void parserSSDP(String xmlString) {
        if(TextUtils.isEmpty(xmlString)){
            Log.i(TAG, "parserSSDP xmlString is null");
            return;
        }
        parserSSDP(xmlString.getBytes());
    }

    public static void parserSSDP(byte[] XmlBuf) {
        if(XmlBuf == null || XmlBuf.length <= 0){
            Log.i(TAG, "parserSSDP XmlBuf is null");
            return;
        }
        InputStream inputStream = null;
        try{
            SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
            SAXParser parser = saxParserFactory.newSAXParser();
            inputStream = new ByteArrayInputStream(XmlBuf);
            parser.parse(inputStream, new XMLHandler());
        }catch (Exception e){
            Log.w(TAG, e);
        }finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (Exception e) {
                    Log.w(TAG, e);
                }
                inputStream = null;
            }
        }
    }


    static class XMLHandler extends DefaultHandler {

        private StringBuilder stringBuilder = new StringBuilder();
        private String mCreator;

        public XMLHandler() {
            super();
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            Log.i(TAG,"startElement uri:" + uri + ", localName:" + localName + ", qName:" + qName + ", attributes:" + attributes);
            stringBuilder.setLength(0);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            Log.i(TAG,"endElement uri:" + uri + ", localName:" + localName + ", qName:" + qName);
            String elementValue = stringBuilder.toString();
            Log.i(TAG,"endElement elementValue:" + elementValue);

        }

        @Override
        public void endDocument() throws SAXException {
            super.endDocument();
            Log.i(TAG,"endDocument");
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            Log.i(TAG,"characters ch:" + ch + ", start:" + start + ", length:" + length);
            stringBuilder.append(ch, start, length);
        }
    }
}
