# utils

Utils library for different puropses

TextNumberComparator implements a java Comparator interface and performs string comparison.
The comparison is different from the regular string comparison that all numerical parts
of a string are treated as numbers

For example

<code>
'010abc' > '2abc'

'0005text' > '3text'
</code>

and

<code>
'some-string.100' > 'some-string.20'
</code>

while with pure string comparison it will be different

StatefulSaxHandler represents a handler for java SAX parser that accepts tree of states as element processors.
Print state, which only prints XML tag information with indent, is an example of the state usage:

```java
    private class IndentData {
        private String indent = "";

        public String getIndent() {
            return indent;
        }

        public void setIndent(String indent) {
            this.indent = indent;
        }
        
        
    }
    
    
    private class PrintingState extends BaseState {
        
        public PrintingState() {}

        public PrintingState(String elementName) {
            super(elementName);
        }
        

        @Override
        public BaseState startElement(Attributes attributes) {
            IndentData indentData = (IndentData) getData();
            String indent = indentData.getIndent() + "  ";
            indentData.setIndent(indent);
            System.out.println(indent + getElementName());
            for(int i = 0; i < attributes.getLength(); i++) {
                System.out.println(indent + "    " + attributes.getQName(i) + " : " + attributes.getValue(i));
            }
            return super.startElement(attributes); 
        }
        
        @Override
        public BaseState endElement() {
            IndentData indentData = (IndentData)getData();
            String indent = indentData.getIndent();
            indentData.setIndent(indent.substring(0, indent.length()-2));
            return this;
        }
        
    }
```
A state tree example is like

```java
        BaseState state = new BaseState(null)
                .child(new PrintingState("Persons")
                            .child(new PrintingState("Person")
                                    .child(new PrintingState("Name")
                                        .child(new PrintingState("FirstName")
                                        .child(new PrintingState("LastName"))                                         
                                    )
                            )
                            .setData(new IndentData()) // must be after setting children to propagate the data object to them.
                );

```

for the XML 
```xml
<Persons>
	<Person department="Development">
		<Name>
			<FirstName></FirstName>
			<LastName></LastName
		</Name>
	</Person>
	<Person department="HR">
		<Name>
			<FirstName></FirstName>
			<LastName></LastName
		</Name>
	</Person>
</Persons>
```