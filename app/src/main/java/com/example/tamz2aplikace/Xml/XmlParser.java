package com.example.tamz2aplikace.Xml;

import android.content.res.XmlResourceParser;

import com.example.tamz2aplikace.Model.ZebricekXml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class XmlParser {

    public List<ZebricekXml> parseToObjects(XmlResourceParser myXml) {
        List<ZebricekXml> zebricekXmlList = new ArrayList<ZebricekXml>();

        try {
            int eventType = myXml.getEventType();

            ZebricekXml zebricekXml = null;
            String text = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {

                String tagname = myXml.getName();
                switch (eventType) {

                    case XmlPullParser.START_TAG: {
                        if (tagname.equalsIgnoreCase("skore")) {
                            zebricekXml = new ZebricekXml();
                        }
                        break;
                    }

                    case XmlPullParser.TEXT: {
                        text = myXml.getText();
                        break;
                    }

                    case XmlPullParser.END_TAG: {
                        if (tagname.equalsIgnoreCase("skore")) {
                            int value = Integer.parseInt(text);
                            zebricekXml.setSkore(value);
                            zebricekXmlList.add(zebricekXml);
                        }
                        break;
                    }

                    default:
                        break;
                }
                eventType = myXml.next();
            }

        } catch (XmlPullParserException xmlPullParserException) {
            xmlPullParserException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        return zebricekXmlList;
    }
}
