package com.shevalab.utils.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class StatefulSaxHandler extends DefaultHandler {

    BaseState state;

    /**
     * Set root parser state for the handler
     * @param rootState root parser state
     * @return this object
     */
    public DefaultHandler setRootParserState(BaseState rootState) {
        state = rootState;
        return this;
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        super.characters(ch, start, length);
        state.processChars(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        super.endElement(uri, localName, qName);
        state.endElement();
        state = state.getParentState();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        super.startElement(uri, localName, qName, attributes);
        BaseState childState = state.getChild(qName);
        if (childState != null) {
            state = childState.startElement(attributes);
        } else {
            throw new IllegalStateException("Cannot find child node '" + qName + "' for the parent node '" + state.getElementName() + "'");
        }
    }

    @Override
    public void endDocument() throws SAXException {
        super.endDocument();
    }

    @Override
    public void startDocument() throws SAXException {
        super.startDocument();
    }
}
