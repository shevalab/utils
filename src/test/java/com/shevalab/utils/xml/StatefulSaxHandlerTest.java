
package com.shevalab.utils.xml;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;


public class StatefulSaxHandlerTest {
    
    public static final String xmlString = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n"
            + "<OfflineSyncPackage MinimumClientVersion=\"5.8.0.2678\" ProtocolVersion=\"1.0\" PackageId=\"d4abc7fc-9461-4084-a565-87f187d7d6\n"
            + "e3\" SourceId=\"cc56dcba-9026-4399-8535-7a3c9bed7086\" CreationDate=\"2019-05-14T03:13:34Z\" PackageVersion=\"1.1\">\n"
            + "  <Updates>\n"
            + "    <Update CreationDate=\"2019-05-14T17:02:06Z\" DefaultLanguage=\"en\" UpdateId=\"e34205a2-3739-4b7c-b792-22bc71890ca9\" RevisionNumber=\"201\" RevisionId=\"29010705\" IsLeaf=\"true\" IsBundle=\n"
            + "\"true\">\n"
            + "      <Categories>\n"
            + "        <Category Type=\"Company\" Id=\"56309036-4c77-4dd9-951a-99ee9c246a94\"/>\n"
            + "        <Category Type=\"Product\" Id=\"a3c2375d-0c8a-42f9-bce0-28333e198407\"/>\n"
            + "        <Category Type=\"ProductFamily\" Id=\"6964aab4-c5b5-43bd-a17d-ffb4346a8e1d\"/>\n"
            + "        <Category Type=\"UpdateClassification\" Id=\"0fa1201d-4330-4fa8-8ae9-b877473b6441\"/>\n"
            + "      </Categories>\n"
            + "      <Prerequisites>\n"
            + "        <UpdateId Id=\"3e0afb10-a9fb-4c16-a60e-5790c3803437\"/>\n"
            + "        <UpdateId Id=\"36712b28-21ab-499d-8cc4-2f517ce878ac\"/>\n"
            + "        <UpdateId Id=\"1280a650-3e8d-4358-81b9-3e65aa14cd35\"/>\n"
            + "        <UpdateId Id=\"0fa1201d-4330-4fa8-8ae9-b877473b6441\"/>\n"
            + "        <UpdateId Id=\"a3c2375d-0c8a-42f9-bce0-28333e198407\"/>\n"
            + "      </Prerequisites>\n"
            + "    </Update>\n"
            + "    <Update CreationDate=\"2019-05-14T00:27:55Z\" DefaultLanguage=\"en\" UpdateId=\"02e0b67f-5977-4182-a04b-065298a7fd40\" RevisionNumber=\"201\" RevisionId=\"29010704\" DeploymentAction=\"Bundl\n"
            + "e\" IsLeaf=\"true\">\n"
            + "      <PayloadFiles>\n"
            + "        <File Id=\"MNix321xUZqK4uzbb+o9TzSjhcI=\"/>\n"
            + "      </PayloadFiles>\n"
            + "      <Prerequisites>\n"
            + "        <UpdateId Id=\"3e0afb10-a9fb-4c16-a60e-5790c3803437\"/>\n"
            + "        <UpdateId Id=\"36712b28-21ab-499d-8cc4-2f517ce878ac\"/>\n"
            + "        <UpdateId Id=\"1280a650-3e8d-4358-81b9-3e65aa14cd35\"/>\n"
            + "        <UpdateId Id=\"a3c2375d-0c8a-42f9-bce0-28333e198407\"/>\n"
            + "      </Prerequisites>\n"
            + "      <BundledBy>\n"
            + "        <Revision Id=\"29010705\"/>\n"
            + "      </BundledBy>\n"
            + "    </Update>"
            + "</OfflineSyncPackage>";
    
    
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
    
    @Test
    public void testHandler() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(false);
        SAXParser saxParser = factory.newSAXParser();
        BaseState state = new BaseState()
                .child(new PrintingState("OfflineSyncPackage")
                                .child(new PrintingState("Updates")
                                    .child(new PrintingState("Update")
                                        .child(new PrintingState("Categories")
                                                .child(new PrintingState("Category"))
                                        )
                                        .child(new PrintingState("Prerequisites")
                                                .child(new PrintingState("UpdateId"))
                                                .child(new PrintingState("Or")
                                                        .child(new PrintingState("UpdateId"))
                                                )
                                                .setAllowMissingChild(true)
                                        )
                                        .child(new PrintingState("BundledBy")
                                                .child(new PrintingState("Revision"))
                                        )
                                        .child(new PrintingState("SupersededBy")
                                                .child(new PrintingState("Revision"))
                                        )                                            
                                        .child(new PrintingState("PayloadFiles")
                                                .child(new PrintingState("File"))
                                        )  
                                        .child(new PrintingState("EulaFiles")
                                                .child(new PrintingState("File")
                                                    .child(new PrintingState("Language"))
                                                )
                                        ) 
                                        .child(new PrintingState("Languages")
                                                .child(new PrintingState("Language"))
                                        )                                            
                                    )
                                )
                                .child(new PrintingState("FileLocations")
                                        .child(new PrintingState("FileLocation"))
                                )
                                .setData(new IndentData()) // must be after setting children to propagate the data object to them.
                );
        BaseState stubState = new BaseState()
                .setAllowMissingChild(true)
                .setStubSupplier(() -> new PrintingState())
                .setData(new IndentData());
        saxParser.parse(new File("/home/vadim/tmp/WSUS-less/1/extracted/package.xml"), new StatefulSaxHandler().setRootParserState(state));
    }

}
