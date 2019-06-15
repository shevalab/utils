
package com.shevalab.utils.xml;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import org.xml.sax.Attributes;


public class BaseState {
    private String elementName;
    private BaseState parent;
    private Set<BaseState> childStates;
    private Object data;
    private boolean allowMissingChild = false;
    private Supplier<BaseState> stubSupplier = () -> new BaseState();
    
    public BaseState() {}
    
    /**
     * Constructor setting the parser state name according to an XML tag name
     * @param elementName XML tag name or null for a root (document) parser state
     */
    public BaseState(String elementName) {
        this.elementName = elementName;
    }

    /**
     * Set XML tag name, parser state name
     * @param name XML tag name as parser state name
     * @return this object
     */
    public BaseState setElementName(String name) {
        this.elementName = name;
        return this;
    }

    /**
     * Add child parser state 
     * @param childState parser state to be added as child state to the current state
     * @return this object
     */
    public BaseState child(BaseState childState) {
        if(childStates == null) childStates = new HashSet();
        childState.setParent(this);
        childState.setData(getData());
        childStates.add(childState);
        return this;
    }

    
    /**
     * Retrieve the parent parser state
     * @return parent parser state
     */
    public BaseState getParentState() {
        return parent;
    }
    
    /**
     * Retrieve a child parser state by element name
     * @param name element/state name
     * @return parser state with the name or either null of stub state if not found
     * the stab state is retrieved if the setAllowMissingChild is set to true
     */
    public BaseState getChild(String name) {
        
        if(childStates == null) return createStubParser(name);
        
        return childStates.stream()
                .filter(state -> state.getElementName().equals(name))
                .findFirst()
                .orElseGet(() -> createStubParser(name));
    }

    /**
     * Callback method being called on start element event of SAX parser
     * Called when a start element event occurs for the parser state
     * @param attributes element attributes
     * @return this object
     */
    public BaseState startElement(Attributes attributes) {    
        return this;
    }

    /**
     * Callback method being called on end element event of SAX parser
     * @return 
     */
    public BaseState endElement() {
       return this;
    }
    
    /**
     * Retrieves XML element name the parser state is bound to
     * @return element name
     */

    public String getElementName() {
        return elementName;
    }
    
    /**
     * Process character data callback. 
     * Invoked by a SAX parser when character data arrived
     * @param ch array of input characters
     * @param start start index of the data
     * @param length length of the data
     * @return this object
     */
    public BaseState processChars(char[] ch, int start, int length) {
        return this;
    }

    /**
     * Set parent parser state
     * @param parent parent parser state
     * @return 
     */
    private BaseState setParent(BaseState parent) {
        this.parent = parent;
        return this;
    }
    
    /**
     * Return data object of the parser state
     * @return data object
     */
    public Object getData() {
        return data;
    }
    
    /**
     * Set common data object to a chain of parser states. 
     * Applied usually on an upper state of the chain. Has to be applied after setting of childs
     * in order to properly propagate the data object to all child parser states
     * @param data data object
     * @return this object
     */
    public BaseState setData(Object data) {
        if(data != null && this.data == null) {
            this.data = data;
            if (childStates != null) for (BaseState child : childStates) {
                    child.setData(data);
            }
        }

        return this;
    }

    /**
     * Allow to the parser state skipping a child tag/node with setting a stub parser state
     * @param allowMissingChild - setting to true will create a stub child parser state if
     * a properly named child state is not found
     * @return this object
     */
    public BaseState setAllowMissingChild(boolean allowMissingChild) {
        this.allowMissingChild = allowMissingChild;
        return this;
    }
    
    /**
     * Override the default parser state stub supplier with a custom one
     * Allows create objects other then BaseState as stub parser states
     * @param supplier
     * @return 
     */
    public BaseState setStubSupplier(Supplier<BaseState> supplier) {
        stubSupplier = supplier;
        return this;
    }

    private BaseState createStubParser(String name) {
        return allowMissingChild ?
                stubSupplier.get()
                        .setElementName(name)
                        .setAllowMissingChild(true)
                        .setParent(this)
                        .setData(getData())
                        .setStubSupplier(stubSupplier)
                : null;
    }    
}
