/* $RCSfile$
 * $Author$
 * $Date$
 * $Revision$
 *
 * Copyright (C) 2003  The Chemistry Development Kit (CDK) project
 *
 * Contact: cdk-devel@lists.sourceforge.net
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * All we ask is that proper credit is given for our work, which includes
 * - but is not limited to - adding the above copyright notice to the beginning
 * of your source code files, and to any copyright notice that you may distribute
 * with programs based on this work.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 */
package org.openscience.cdk.io.cml;

import java.util.*;
import org.xml.sax.*;
import org.openscience.cdk.io.cml.cdopi.*;

/**
 * @author Egon Willighagen <elw38@cam.ac.uk>
 */
public class CMLReactionModule extends CMLCoreModule {

    public CMLReactionModule(CDOInterface cdo) {
        super(cdo);
    };

    public CMLReactionModule(ModuleInterface conv) {
        super(conv);
        logger.debug("New CML-Reaction Module!");
    }

    public CDOInterface returnCDO() {
        return this.cdo;
    };

    public void startDocument() {
        super.startDocument();
    };

    public void endDocument() {
        super.endDocument();
    };
    
    
    public void startElement(Stack xpath, String uri, String local, String raw, Attributes atts) {
        if ("reaction".equals(local)) {
            cdo.startObject("Reaction");
            for (int i = 0; i < atts.getLength(); i++) {
                String att = atts.getQName(i);
                String value = atts.getValue(i);
                if (att.equals("id")) {
                    cdo.setObjectProperty("Reaction", "id", value);
                }
            }
        } else if ("reactionList".equals(local)) {
            cdo.startObject("SetOfReactions");
            for (int i = 0; i < atts.getLength(); i++) {
                String att = atts.getQName(i);
                String value = atts.getValue(i);
                if (att.equals("id")) {
                    cdo.setObjectProperty("SetOfReactions", "id", value);
                }
            }
        } else if ("reactant".equals(local)) {
            cdo.startObject("Reactant");
            for (int i = 0; i < atts.getLength(); i++) {
                String att = atts.getQName(i);
                String value = atts.getValue(i);
                if (att.equals("id")) {
                    cdo.setObjectProperty("Reactant", "id", value);
                }
            }
        } else if ("product".equals(local)) {
            cdo.startObject("Product");
            for (int i = 0; i < atts.getLength(); i++) {
                String att = atts.getQName(i);
                String value = atts.getValue(i);
                if (att.equals("id")) {
                    cdo.setObjectProperty("Product", "id", value);
                }
            }
        } else if ("molecule".equals(local)) {
            // do nothing for now
            super.newMolecule();
        } else {
            super.startElement(xpath, uri, local, raw, atts);
        }
    };

    public void endElement(Stack xpath, String uri, String local, String raw) {
        if ("reaction".equals(local)) {
            cdo.endObject("Reaction");
        } else if ("reactionList".equals(local)) {
            cdo.endObject("SetOfReactions");
        } else if ("reactant".equals(local)) {
            cdo.endObject("Reactant");
        } else if ("product".equals(local)) {
            cdo.endObject("Product");
        } else if ("molecule".equals(local)) {
            logger.debug("Storing Molecule");
            super.storeData();
            // do nothing else but store atom/bond information
        } else {
            super.endElement(xpath, uri, local, raw);
        }
    }

    public void characterData(Stack xpath, char ch[], int start, int length) {
        String s = new String(ch, start, length).trim();
        super.characterData(xpath, ch, start, length);
    }
}
