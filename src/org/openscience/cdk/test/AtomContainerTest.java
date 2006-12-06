/* $RCSfile$
 * $Author$    
 * $Date$    
 * $Revision$
 * 
 * Copyright (C) 1997-2006  The Chemistry Development Kit (CDK) project
 * 
 * Contact: cdk-devel@lists.sourceforge.net
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public License
 * as published by the Free Software Foundation; either version 2.1
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA. 
 * 
 */
package org.openscience.cdk.test;


import javax.vecmath.Point2d;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IAtomParity;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.interfaces.IChemObjectChangeEvent;
import org.openscience.cdk.interfaces.IChemObjectListener;
import org.openscience.cdk.interfaces.IElectronContainer;
import org.openscience.cdk.interfaces.ILonePair;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.interfaces.ISingleElectron;

/**
 * Checks the funcitonality of the AtomContainer.
 *
 * @cdk.module test-data
 */
public class AtomContainerTest extends CDKTestCase {

	protected IChemObjectBuilder builder;
	
    public AtomContainerTest(String name) {
        super(name);
    }

    public void setUp() {
    	builder = DefaultChemObjectBuilder.getInstance();
    }

    public static Test suite() {
        return new TestSuite(AtomContainerTest.class);
    }

    public void testSetAtoms_arrayIAtom() {
        IAtom[] atoms = new IAtom[4];
        atoms[0] = builder.newAtom("C");
        atoms[1] = builder.newAtom("C");
        atoms[2] = builder.newAtom("C");
        atoms[3] = builder.newAtom("O");
        IAtomContainer ac = builder.newAtomContainer();
        ac.setAtoms(atoms);
        
        assertEquals(4, ac.getAtomCount());
        //assertEquals(4, ac.getAtoms().length);
    }

    /**
     * Only test wether the atoms are correctly cloned.
     */
	public void testClone() throws Exception {
        IAtomContainer molecule = builder.newAtomContainer();
        Object clone = molecule.clone();
        assertTrue(clone instanceof IAtomContainer);
    }    
        
    public void testClone_IAtom() throws Exception {
		IAtomContainer molecule = builder.newAtomContainer();
		molecule.addAtom(builder.newAtom("C")); // 1
		molecule.addAtom(builder.newAtom("C")); // 2
		molecule.addAtom(builder.newAtom("C")); // 3
		molecule.addAtom(builder.newAtom("C")); // 4

		IAtomContainer clonedMol = (IAtomContainer)molecule.clone();
		assertEquals(molecule.getAtomCount(), clonedMol.getAtomCount());
		for (int f = 0; f < molecule.getAtomCount(); f++) {
			for (int g = 0; g < clonedMol.getAtomCount(); g++) {
				assertNotNull(molecule.getAtom(f));
				assertNotNull(clonedMol.getAtom(g));
				assertNotSame(molecule.getAtom(f), clonedMol.getAtom(g));
			}
		}        
    }
    
	public void testClone_IAtom2() throws Exception {
		IMolecule molecule = builder.newMolecule();
        IAtom carbon = builder.newAtom("C");
        carbon.setPoint2d(new Point2d(2, 4));
		molecule.addAtom(carbon); // 1

        // test cloning of Atoms
		IMolecule clonedMol = (IMolecule)molecule.clone();
        carbon.setPoint2d(new Point2d(3, 1));
		assertEquals(clonedMol.getAtom(0).getPoint2d().x, 2.0, 0.001);
	}

    public void testClone_IBond() throws Exception {
		IAtomContainer molecule = builder.newAtomContainer();
		molecule.addAtom(builder.newAtom("C")); // 1
		molecule.addAtom(builder.newAtom("C")); // 2
		molecule.addAtom(builder.newAtom("C")); // 3
		molecule.addAtom(builder.newAtom("C")); // 4

		molecule.addBond(0, 1, 2.0); // 1
		molecule.addBond(1, 2, 1.0); // 2
		molecule.addBond(2, 3, 1.0); // 3
		IAtomContainer clonedMol = (IAtomContainer)molecule.clone();
		assertEquals(molecule.getElectronContainerCount(), clonedMol.getElectronContainerCount());
		for (int f = 0; f < molecule.getElectronContainerCount(); f++) {
			for (int g = 0; g < clonedMol.getElectronContainerCount(); g++) {
				assertNotNull(molecule.getElectronContainer(f));
				assertNotNull(clonedMol.getElectronContainer(g));
				assertNotSame(molecule.getElectronContainer(f), clonedMol.getElectronContainer(g));
			}
		}
	}

    public void testClone_IBond2() throws Exception {
		IAtomContainer molecule = builder.newAtomContainer();
        IAtom atom1 = builder.newAtom("C");
        IAtom atom2 = builder.newAtom("C");
		molecule.addAtom(atom1); // 1
		molecule.addAtom(atom2); // 2
		molecule.addBond(builder.newBond(atom1, atom2, 2.0)); // 1
        
        // test cloning of atoms in bonds
		IAtomContainer clonedMol = (IAtomContainer)molecule.clone();
        assertNotSame(atom1, clonedMol.getBond(0).getAtom(0));
        assertNotSame(atom2, clonedMol.getBond(0).getAtom(1));
	}

    public void testClone_IBond3() throws Exception {
		IAtomContainer molecule = builder.newAtomContainer();
        IAtom atom1 = builder.newAtom("C");
        IAtom atom2 = builder.newAtom("C");
		molecule.addAtom(atom1); // 1
		molecule.addAtom(atom2); // 2
		molecule.addBond(builder.newBond(atom1, atom2, 2.0)); // 1
        
        // test that cloned bonds contain atoms from cloned atomcontainer
		IAtomContainer clonedMol = (IAtomContainer)molecule.clone();
        assertTrue(clonedMol.contains(clonedMol.getBond(0).getAtom(0)));
        assertTrue(clonedMol.contains(clonedMol.getBond(0).getAtom(1)));
	}

    public void testClone_ILonePair() throws Exception {
		IAtomContainer molecule = builder.newAtomContainer();
        IAtom atom1 = builder.newAtom("C");
        IAtom atom2 = builder.newAtom("C");
		molecule.addAtom(atom1); // 1
		molecule.addAtom(atom2); // 2
		molecule.addLonePair(0); 
        
        // test that cloned bonds contain atoms from cloned atomcontainer
		IAtomContainer clonedMol = (IAtomContainer)molecule.clone();
        assertEquals(1, clonedMol.getLonePairCount(clonedMol.getAtom(0)));
	}

    public void testGetConnectedElectronContainersList_IAtom() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(1, acetone.getConnectedElectronContainersList(o).size());
        assertEquals(3, acetone.getConnectedElectronContainersList(c1).size());
        assertEquals(1, acetone.getConnectedElectronContainersList(c2).size());
        assertEquals(1, acetone.getConnectedElectronContainersList(c3).size());
        
        // add lone pairs on oxygen
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);

        assertEquals(3, acetone.getConnectedElectronContainersList(o).size());
        assertEquals(3, acetone.getConnectedElectronContainersList(c1).size());
        assertEquals(1, acetone.getConnectedElectronContainersList(c2).size());
        assertEquals(1, acetone.getConnectedElectronContainersList(c3).size());

    }

    public void testGetConnectedBondsList_IAtom() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(1, acetone.getConnectedBondsList(o).size());
        assertEquals(3, acetone.getConnectedBondsList(c1).size());
        assertEquals(1, acetone.getConnectedBondsList(c2).size());
        assertEquals(1, acetone.getConnectedBondsList(c3).size());
        
        // add lone pairs on oxygen
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);

        assertEquals(1, acetone.getConnectedBondsList(o).size());
        assertEquals(3, acetone.getConnectedBondsList(c1).size());
        assertEquals(1, acetone.getConnectedBondsList(c2).size());
        assertEquals(1, acetone.getConnectedBondsList(c3).size());
    }

//    public void testGetConnectedBonds_IAtom() {
//        // acetone molecule
//        IMolecule acetone = builder.newMolecule();
//        
//        IAtom c1 = builder.newAtom("C");
//        IAtom c2 = builder.newAtom("C");
//        IAtom o = builder.newAtom("O");
//        IAtom c3 = builder.newAtom("C");
//        acetone.addAtom(c1);
//        acetone.addAtom(c2);
//        acetone.addAtom(c3);
//        acetone.addAtom(o);
//        IBond b1 = builder.newBond(c1, c2,1);
//        IBond b2 = builder.newBond(c1, o, 2);
//        IBond b3 = builder.newBond(c1, c3,1);
//        acetone.addBond(b1);
//        acetone.addBond(b2);
//        acetone.addBond(b3);
//        
//        assertEquals(1, acetone.getConnectedBondsVector(o).size());
//        assertEquals(3, acetone.getConnectedBondsVector(c1).size());
//        assertEquals(1, acetone.getConnectedBondsVector(c2).size());
//        assertEquals(1, acetone.getConnectedBondsVector(c3).size());
//        
//        // add lone pairs on oxygen
//        ILonePair lp1 = builder.newLonePair(o);
//        ILonePair lp2 = builder.newLonePair(o);
//        acetone.addElectronContainer(lp1);
//        acetone.addElectronContainer(lp2);
//
//        assertEquals(1, acetone.getConnectedBondsVector(o).size());
//        assertEquals(3, acetone.getConnectedBondsVector(c1).size());
//        assertEquals(1, acetone.getConnectedBondsVector(c2).size());
//        assertEquals(1, acetone.getConnectedBondsVector(c3).size());
//    }

    public void testGetLonePairs_IAtom() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(0, acetone.getLonePairs(o).length);
        assertEquals(0, acetone.getLonePairs(c1).length);
        assertEquals(0, acetone.getLonePairs(c2).length);
        assertEquals(0, acetone.getLonePairs(c3).length);

        // add lone pairs on oxygen
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);

        assertEquals(2, acetone.getLonePairs(o).length);
        assertEquals(0, acetone.getLonePairs(c1).length);
        assertEquals(0, acetone.getLonePairs(c2).length);
        assertEquals(0, acetone.getLonePairs(c3).length);

    }

    
    public void testRemoveAtomAndConnectedElectronContainers_IAtom() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        // add lone pairs on oxygen
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
        // remove the oxygen
        acetone.removeAtomAndConnectedElectronContainers(o);
        assertEquals(3, acetone.getAtomCount());
        assertEquals(2, acetone.getBondCount());
        assertEquals(0, acetone.getLonePairCount());
    }

    public void testGetElectronContainer_int() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        // add lone pairs on oxygen
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
		for (int i = 0; i < acetone.getElectronContainerCount(); i++) {
            try {
            	org.openscience.cdk.interfaces.IElectronContainer ec = acetone.getElectronContainer(i);
                if (ec == null) {
                    fail("ElectronContainer is unexpectedly null!");
                }
            } catch (Exception e) {
                fail();
            }
		}
    }
    
    public void testGetAtomCount() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        assertEquals(0, acetone.getAtomCount());
        
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        
        assertEquals(4, acetone.getAtomCount());
    }
    
    public void testGetBondCount() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        assertEquals(0, acetone.getBondCount());
        
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getBondCount());
    }
    
    public void testAtomContainer_int_int() {
        // create an empty container with predefined
        // array lengths
        IAtomContainer ac = new org.openscience.cdk.AtomContainer(5,6);
        
        assertEquals(0, ac.getAtomCount());
        assertEquals(0, ac.getElectronContainers().length);
        
        // test wether the ElectronContainer is correctly initialized
        ac.addBond(builder.newBond(builder.newAtom("C"), builder.newAtom("C"), 2));
        ac.addElectronContainer(builder.newLonePair(builder.newAtom("N")));
    }

    public void testAtomContainer() {
        // create an empty container with in the constructor defined array lengths
        IAtomContainer container = builder.newAtomContainer();
        
        assertEquals(0, container.getAtomCount());
        assertEquals(0, container.getElectronContainers().length);
        
        // test wether the ElectronContainer is correctly initialized
        container.addBond(builder.newBond(builder.newAtom("C"), builder.newAtom("C"), 2));
        container.addElectronContainer(builder.newLonePair(builder.newAtom("N")));
    }

    public void testAtomContainer_IAtomContainer() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        IAtomContainer container = new org.openscience.cdk.AtomContainer(acetone);
        assertEquals(4, container.getAtomCount());
        assertEquals(3, container.getBondCount());
    }
    
    public void testAdd_IAtomContainer() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        IAtomContainer container = builder.newAtomContainer();
        container.add(acetone);
        assertEquals(4, container.getAtomCount());
        assertEquals(3, container.getBondCount());
    }
    
    public void testRemove_IAtomContainer() throws Exception {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        IAtomContainer container = builder.newAtomContainer();
        container.add(acetone);
        assertEquals(4, container.getAtomCount());
        assertEquals(3, container.getBondCount());
        container.remove((IAtomContainer)acetone.clone());
        assertEquals(4, container.getAtomCount());
        assertEquals(3, container.getBondCount());
        container.remove(acetone);
        assertEquals(0, container.getAtomCount());
        assertEquals(0, container.getBondCount());
    }
    
    public void testRemoveAllElements() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        IAtomContainer container = builder.newAtomContainer();
        container.add(acetone);
        assertEquals(4, container.getAtomCount());
        assertEquals(3, container.getBondCount());
        container.removeAllElements();
        assertEquals(0, container.getAtomCount());
        assertEquals(0, container.getBondCount());
    }
    
    public void testRemoveAtom_int() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);

        assertEquals(4, acetone.getAtomCount());
        acetone.removeAtom(1);
        assertEquals(3, acetone.getAtomCount());
        assertEquals(c1, acetone.getAtom(0));
        assertEquals(c3, acetone.getAtom(1));
        assertEquals(o, acetone.getAtom(2));
    }
    
    public void testRemoveAtom_IAtom() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);

        assertEquals(4, acetone.getAtomCount());
        acetone.removeAtom(c3);
        assertEquals(3, acetone.getAtomCount());
        assertEquals(c1, acetone.getAtom(0));
        assertEquals(c2, acetone.getAtom(1));
        assertEquals(o, acetone.getAtom(2));
    }
    
    public void testSetAtom_int_IAtom() {
        IAtomContainer container = builder.newAtomContainer();
        IAtom c = builder.newAtom("C");
        container.setAtom(0, c);
        
        assertNotNull(container.getAtom(0));
        assertEquals("C", container.getAtom(0).getSymbol());
    }
    
    public void testGetAtom_int() {
        IAtomContainer acetone = builder.newAtomContainer();
        
        IAtom c = builder.newAtom("C");
        IAtom n = builder.newAtom("N");
        IAtom o = builder.newAtom("O");
        IAtom s = builder.newAtom("S");
        acetone.addAtom(c);
        acetone.addAtom(n);
        acetone.addAtom(o);
        acetone.addAtom(s);
        
        org.openscience.cdk.interfaces.IAtom a1 = acetone.getAtom(0);
        assertNotNull(a1);
        assertEquals("C", a1.getSymbol());
        org.openscience.cdk.interfaces.IAtom a2 = acetone.getAtom(1);
        assertNotNull(a2);
        assertEquals("N", a2.getSymbol());
        org.openscience.cdk.interfaces.IAtom a3 = acetone.getAtom(2);
        assertNotNull(a3);
        assertEquals("O", a3.getSymbol());
        org.openscience.cdk.interfaces.IAtom a4 = acetone.getAtom(3);
        assertNotNull(a4);
        assertEquals("S", a4.getSymbol());
    }
    
    public void testGetBond_int() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        assertEquals(0, acetone.getBondCount());
        
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,3.0);
        IBond b2 = builder.newBond(c1, o, 2.0);
        IBond b3 = builder.newBond(c1, c3,1.0);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3.0, acetone.getBond(0).getOrder(), 0.00001);
        assertEquals(2.0, acetone.getBond(1).getOrder(), 0.00001);
        assertEquals(1.0, acetone.getBond(2).getOrder(), 0.00001);
    }
    
    public void testSetElectronContainer_int_IElectronContainer() {
        IAtomContainer container = builder.newAtomContainer();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        container.addAtom(c1);
        container.addAtom(c2);
        IBond b = builder.newBond(c1, c2, 3);
        container.setElectronContainer(3, b);
        
        assertTrue(container.getElectronContainer(3) instanceof org.openscience.cdk.interfaces.IBond);
        IBond bond = (IBond)container.getElectronContainer(3);
        assertEquals(3.0, bond.getOrder(), 0.00001);
    }
    
    public void testGetElectronContainerCount() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
        assertEquals(3, acetone.getBondCount());
        assertEquals(2, acetone.getLonePairCount());
        assertEquals(5, acetone.getElectronContainerCount());
    }
    
    public void testRemoveAllBonds() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getBondCount());
	
	acetone.removeAllBonds();
	assertEquals(0, acetone.getBondCount());
    }
    
    public void testRemoveAllElectronContainers() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getElectronContainerCount());
	
        acetone.removeAllElectronContainers();
        assertEquals(0, acetone.getElectronContainerCount());
    }
    
    public void testSetElectronContainerCount_int() {
        IAtomContainer container = builder.newAtomContainer();
        container.setElectronContainerCount(2);
        
        assertEquals(2, container.getElectronContainerCount());
    }
    
//    public void testSetAtomCount_int() {
//        IAtomContainer container = builder.newAtomContainer();
//        container.setAtomCount(2);
//        
//        assertEquals(2, container.getAtomCount());
//    }
    
    public void testGetAtoms() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        
        assertEquals(4, acetone.getAtomCount());
    }
    
    public void testAddAtom_IAtom() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        
        java.util.Iterator atomIter = acetone.atoms();
        int counter = 0;
        while (atomIter.hasNext()) {
            atomIter.next();
            counter++;
        }
        assertEquals(4, counter);
        
        // test force growing of default arrays
        for (int i=0; i<11; i++) {
        	acetone.addAtom(builder.newAtom());
        	acetone.addBond(builder.newBond());
        }
    }

    public void testAtoms() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        
        java.util.Iterator atomIter = acetone.atoms();
        assertNotNull(atomIter);
        assertTrue(atomIter.hasNext());
        IAtom next = (IAtom)atomIter.next();
        assertTrue(next instanceof IAtom);
        assertEquals(c1, next);
        assertTrue(atomIter.hasNext());
        next = (IAtom)atomIter.next();
        assertTrue(next instanceof IAtom);
        assertEquals(c2, next);
        assertTrue(atomIter.hasNext());
        next = (IAtom)atomIter.next();
        assertTrue(next instanceof IAtom);
        assertEquals(c3, next);
        assertTrue(atomIter.hasNext());
        next = (IAtom)atomIter.next();
        assertTrue(next instanceof IAtom);
        assertEquals(o, next);
        
        assertFalse(atomIter.hasNext());
    }

    public void testBonds() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);

        IBond bond1 = builder.newBond(c1,c2,1);
        IBond bond2 = builder.newBond(c2,o,2);
        IBond bond3 = builder.newBond(c2,c3,1);
        acetone.addBond(bond1);
        acetone.addBond(bond2);
        acetone.addBond(bond3);

        java.util.Iterator bonds = acetone.bonds();
        assertNotNull(bonds);
        assertTrue(bonds.hasNext());

        IBond next =  (IBond) bonds.next();
        assertTrue(next instanceof IBond);
        assertEquals(bond1, next);

        next = (IBond) bonds.next();
        assertTrue(next instanceof IBond);
        assertEquals(bond2, next);

        next = (IBond) bonds.next();
        assertTrue(next instanceof IBond);
        assertEquals(bond3, next);

        assertFalse(bonds.hasNext());
    }

    public void testContains_IAtom() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        
        assertTrue(acetone.contains(c1));
        assertTrue(acetone.contains(c2));
        assertTrue(acetone.contains(o));
        assertTrue(acetone.contains(c3));
    }

    public void testAddLonePair_int() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        acetone.addLonePair(2);
        acetone.addLonePair(2);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getBondCount());
        assertEquals(5, acetone.getElectronContainerCount());
    }

    public void testGetMaximumBondOrder_IAtom() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        acetone.addLonePair(2);
        acetone.addLonePair(2);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(2.0, acetone.getMaximumBondOrder(o) , 0.0001);
        assertEquals(2.0, acetone.getMaximumBondOrder(c1), 0.0001);
        assertEquals(1.0, acetone.getMaximumBondOrder(c2), 0.0001);
        assertEquals(1.0, acetone.getMaximumBondOrder(c3), 0.0001);
    }

    public void testGetMinimumBondOrder_IAtom() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        acetone.addLonePair(2);
        acetone.addLonePair(2);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(2.0, acetone.getMinimumBondOrder(o) , 0.0001);
        assertEquals(1.0, acetone.getMinimumBondOrder(c1), 0.0001);
        assertEquals(1.0, acetone.getMinimumBondOrder(c2), 0.0001);
        assertEquals(1.0, acetone.getMinimumBondOrder(c3), 0.0001);
    }

    public void testRemoveElectronContainer_int() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        acetone.addLonePair(2);
        acetone.addLonePair(2);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getBondCount());
        assertEquals(5, acetone.getElectronContainerCount());
        acetone.removeElectronContainer(0);
        assertEquals(3, acetone.getBondCount());
        assertEquals(4, acetone.getElectronContainerCount());
        acetone.removeElectronContainer(1); // first bond now
        assertEquals(2, acetone.getBondCount());
        assertEquals(3, acetone.getElectronContainerCount());
    }

    public void testRemoveElectronContainer_IElectronContainer() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        ILonePair firstLP = builder.newLonePair(o);
        acetone.addElectronContainer(firstLP);
        acetone.addElectronContainer(builder.newLonePair(o));
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getBondCount());
        assertEquals(5, acetone.getElectronContainerCount());
        acetone.removeElectronContainer(firstLP);
        assertEquals(3, acetone.getBondCount());
        assertEquals(4, acetone.getElectronContainerCount());
        acetone.removeElectronContainer(b1); // first bond now
        assertEquals(2, acetone.getBondCount());
        assertEquals(3, acetone.getElectronContainerCount());
    }

    public void testAddBond_IBond() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getBondCount());
        org.openscience.cdk.interfaces.IBond[] bonds = acetone.getBonds();
        for (int i=0; i<bonds.length; i++) {
            assertNotNull(bonds[i]);
        }
        assertEquals(b1, bonds[0]);
        assertEquals(b2, bonds[1]);
        assertEquals(b3, bonds[2]);
        
        // test readding a bond
        acetone.addBond(b3);
        assertEquals(3, acetone.getBondCount());
    }

    public void testSetElectronContainers_arrayIElectronContainer() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IElectronContainer[] electronContainers = new IElectronContainer[3];
        electronContainers[0] = builder.newBond(c1, c2,1);
        electronContainers[1] = builder.newBond(c1, o, 2);
        electronContainers[2] = builder.newBond(c1, c3,1);
        acetone.setElectronContainers(electronContainers);
        
        assertEquals(3, acetone.getBondCount());
        org.openscience.cdk.interfaces.IBond[] bonds = acetone.getBonds();
        for (int i=0; i<bonds.length; i++) {
            assertNotNull(bonds[i]);
        }
        assertEquals(electronContainers[0], bonds[0]);
        assertEquals(electronContainers[1], bonds[1]);
        assertEquals(electronContainers[2], bonds[2]);
    }

    public void testAddElectronContainers_IAtomContainer() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IElectronContainer[] electronContainers = new IElectronContainer[3];
        electronContainers[0] = builder.newBond(c1, c2,1);
        electronContainers[1] = builder.newBond(c1, o, 2);
        electronContainers[2] = builder.newBond(c1, c3,1);
        acetone.setElectronContainers(electronContainers);
        
        IAtomContainer tested = builder.newAtomContainer();
        tested.addBond(builder.newBond(c2, c3));
        tested.addElectronContainers(acetone);
        
        assertEquals(0, tested.getAtomCount());
        assertEquals(4, tested.getBondCount());
        org.openscience.cdk.interfaces.IBond[] bonds = tested.getBonds();
        for (int i=0; i<bonds.length; i++) {
            assertNotNull(bonds[i]);
        }
        assertEquals(electronContainers[0], bonds[1]);
        assertEquals(electronContainers[1], bonds[2]);
        assertEquals(electronContainers[2], bonds[3]);
    }

    public void testAddElectronContainer_IElectronContainer() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        acetone.addAtom(c);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c, o, 2.0);
        acetone.addElectronContainer(b1);
        acetone.addElectronContainer(builder.newLonePair(o));
        acetone.addElectronContainer(builder.newSingleElectron(c));

        assertEquals(3, acetone.getElectronContainerCount());
        assertEquals(1, acetone.getBondCount());
        assertEquals(1, acetone.getLonePairCount());
    }

    public void testGetSingleElectron_IAtom() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        acetone.addAtom(c);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c, o, 2.0);
        acetone.addElectronContainer(b1);
        acetone.addElectronContainer(builder.newLonePair(o));
        ISingleElectron single = builder.newSingleElectron(c);
        acetone.addElectronContainer(single);

        assertEquals(1, acetone.getSingleElectron(c).length);
        assertEquals(single, acetone.getSingleElectron(c)[0]);
    }

    public void testRemoveBond_IAtom_IAtom() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getBondCount());
        acetone.removeBond(c1, o);
        assertEquals(2, acetone.getBondCount());
        assertEquals(b1, acetone.getBond(0));
        assertEquals(b3, acetone.getBond(1));
    }

    public void testAddBond_int_int_double() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        acetone.addBond(0, 1, 1);
        acetone.addBond(1, 3, 2);
        acetone.addBond(1, 2, 1);
        
        assertEquals(3, acetone.getBondCount());
        org.openscience.cdk.interfaces.IBond[] bonds = acetone.getBonds();
        for (int i=0; i<bonds.length; i++) {
            assertNotNull(bonds[i]);
        }
        assertEquals(c1, bonds[0].getAtom(0));
        assertEquals(c2, bonds[0].getAtom(1));
        assertEquals(1.0, bonds[0].getOrder(), 0.001);
        assertEquals(c2, bonds[1].getAtom(0));
        assertEquals(o, bonds[1].getAtom(1));
        assertEquals(2.0, bonds[1].getOrder(), 0.001);
        assertEquals(c2, bonds[2].getAtom(0));
        assertEquals(c3, bonds[2].getAtom(1));
        assertEquals(1.0, bonds[2].getOrder(), 0.001);
    }

    public void testAddBond_int_int_double_int() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        acetone.addBond(0, 1, 1, CDKConstants.STEREO_BOND_UP); // yes this is crap
        acetone.addBond(1, 3, 2, CDKConstants.STEREO_BOND_DOWN);
        acetone.addBond(1, 2, 1, CDKConstants.STEREO_BOND_NONE);
        
        assertEquals(3, acetone.getBondCount());
        org.openscience.cdk.interfaces.IBond[] bonds = acetone.getBonds();
        for (int i=0; i<bonds.length; i++) {
            assertNotNull(bonds[i]);
        }
        assertEquals(c1, bonds[0].getAtom(0));
        assertEquals(c2, bonds[0].getAtom(1));
        assertEquals(1.0, bonds[0].getOrder(), 0.001);
        assertEquals(CDKConstants.STEREO_BOND_UP, bonds[0].getStereo());
        assertEquals(c2, bonds[1].getAtom(0));
        assertEquals(o, bonds[1].getAtom(1));
        assertEquals(2.0, bonds[1].getOrder(), 0.001);
        assertEquals(CDKConstants.STEREO_BOND_DOWN, bonds[1].getStereo());
        assertEquals(c2, bonds[2].getAtom(0));
        assertEquals(c3, bonds[2].getAtom(1));
        assertEquals(1.0, bonds[2].getOrder(), 0.001);
        assertEquals(CDKConstants.STEREO_BOND_NONE, bonds[2].getStereo());
    }

    public void testContains_IElectronContainer() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
        assertTrue(acetone.contains(b1));
        assertTrue(acetone.contains(b2));
        assertTrue(acetone.contains(b3));
        assertTrue(acetone.contains(lp1));
        assertTrue(acetone.contains(lp2));
    }

    public void testGetElectronContainers() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
        assertEquals(5, acetone.getElectronContainers().length);
    }
    
    public void testGetLonePairs() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
        assertNotNull(acetone.getLonePairs());
        assertEquals(2, acetone.getLonePairs().length);
    }
    
    public void testGetBonds() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getBonds().length);
    }
    
    public void testGetFirstAtom() {
        IAtomContainer container = builder.newAtomContainer();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("O");
        IAtom o = builder.newAtom("H");
        container.addAtom(c1);
        container.addAtom(c2);
        container.addAtom(o);
        
        assertNotNull(container.getFirstAtom());
        assertEquals("C", container.getFirstAtom().getSymbol());
    }

    public void testGetLastAtom() {
        IAtomContainer container = builder.newAtomContainer();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("O");
        IAtom o = builder.newAtom("H");
        container.addAtom(c1);
        container.addAtom(c2);
        container.addAtom(o);
        
        assertNotNull(container.getLastAtom());
        assertEquals("H", container.getLastAtom().getSymbol());
    }
    
    public void testGetAtomNumber_IAtom() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        
        assertEquals(0, acetone.getAtomNumber(c1));
        assertEquals(1, acetone.getAtomNumber(c2));
        assertEquals(2, acetone.getAtomNumber(c3));
        assertEquals(3, acetone.getAtomNumber(o));
    }
    
    public void testGetBondNumber_IBond() {
        // acetone molecule
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(0, acetone.getBondNumber(b1));
        assertEquals(1, acetone.getBondNumber(b2));
        assertEquals(2, acetone.getBondNumber(b3));
        
        // test the default return value
        assertEquals(-1, acetone.getBondNumber(builder.newBond()));
    }
    
    public void testGetBondNumber_IAtom_IAtom() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(0, acetone.getBondNumber(c1, c2));
        assertEquals(1, acetone.getBondNumber(c1, o));
        assertEquals(2, acetone.getBondNumber(c1, c3));
    }
    
    public void testGetBond_IAtom_IAtom() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertTrue(b1.equals(acetone.getBond(c1, c2)));        
        assertTrue(b2.equals(acetone.getBond(c1, o)));        
        assertTrue(b3.equals(acetone.getBond(c1, c3)));
        
        // test the default return value
        assertNull(acetone.getBond(builder.newAtom(), builder.newAtom()));
    }
    
//    public void testGetConnectedAtoms_IAtom() {
//        IMolecule acetone = builder.newMolecule();
//        IAtom c1 = builder.newAtom("C");
//        IAtom c2 = builder.newAtom("C");
//        IAtom o = builder.newAtom("O");
//        IAtom c3 = builder.newAtom("C");
//        acetone.addAtom(c1);
//        acetone.addAtom(c2);
//        acetone.addAtom(c3);
//        acetone.addAtom(o);
//        IBond b1 = builder.newBond(c1, c2,1);
//        IBond b2 = builder.newBond(c1, o, 2);
//        IBond b3 = builder.newBond(c1, c3,1);
//        acetone.addBond(b1);
//        acetone.addBond(b2);
//        acetone.addBond(b3);
//        
//        assertEquals(3, acetone.getConnectedAtomsList(c1).length);
//        assertEquals(1, acetone.getConnectedAtoms(c2).length);
//        assertEquals(1, acetone.getConnectedAtoms(c3).length);
//        assertEquals(1, acetone.getConnectedAtoms(o).length);
//    }
    
    public void testGetConnectedAtomsList_IAtom() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getConnectedAtomsList(c1).size());
        assertEquals(1, acetone.getConnectedAtomsList(c2).size());
        assertEquals(1, acetone.getConnectedAtomsList(c3).size());
        assertEquals(1, acetone.getConnectedAtomsList(o).size());
    }
    
    public void testGetConnectedAtomsCount_IAtom() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        assertEquals(3, acetone.getConnectedAtomsCount(c1));
        assertEquals(1, acetone.getConnectedAtomsCount(c2));
        assertEquals(1, acetone.getConnectedAtomsCount(c3));
        assertEquals(1, acetone.getConnectedAtomsCount(o));
    }
    
    public void testGetLonePairCount() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        // add lone pairs on oxygen
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
        assertEquals(2, acetone.getLonePairCount());
    }

    public void testGetLonePairCount_IAtom() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        // add lone pairs on oxygen
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
        assertEquals(2, acetone.getLonePairCount(o));
        assertEquals(0, acetone.getLonePairCount(c2));
        assertEquals(0, acetone.getLonePairCount(c3));
        assertEquals(0, acetone.getLonePairCount(c1));
    }

    public void testGetBondOrderSum_IAtom() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        // add lone pairs on oxygen
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
        assertEquals(4.0, acetone.getBondOrderSum(c1), 0.00001);
        assertEquals(1.0, acetone.getBondOrderSum(c2), 0.00001);
        assertEquals(1.0, acetone.getBondOrderSum(c3), 0.00001);
        assertEquals(2.0, acetone.getBondOrderSum(o), 0.00001);
    }
    
    public void testGetBondCount_IAtom() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        // add lone pairs on oxygen
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
        assertEquals(3, acetone.getBondCount(c1));
        assertEquals(1, acetone.getBondCount(c2));
        assertEquals(1, acetone.getBondCount(c3));
        assertEquals(1, acetone.getBondCount(o));
    }
    
    public void testGetBondCount_int() {
        IMolecule acetone = builder.newMolecule();
        IAtom c1 = builder.newAtom("C");
        IAtom c2 = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        IAtom c3 = builder.newAtom("C");
        acetone.addAtom(c1);
        acetone.addAtom(c2);
        acetone.addAtom(c3);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c1, c2,1);
        IBond b2 = builder.newBond(c1, o, 2);
        IBond b3 = builder.newBond(c1, c3,1);
        acetone.addBond(b1);
        acetone.addBond(b2);
        acetone.addBond(b3);
        
        // add lone pairs on oxygen
        ILonePair lp1 = builder.newLonePair(o);
        ILonePair lp2 = builder.newLonePair(o);
        acetone.addElectronContainer(lp1);
        acetone.addElectronContainer(lp2);
        
        assertEquals(3, acetone.getBondCount(0));
        assertEquals(1, acetone.getBondCount(1));
        assertEquals(1, acetone.getBondCount(2));
        assertEquals(1, acetone.getBondCount(3));
    }
    
    public void testGetAtomParity_IAtom() {
        IAtom carbon = builder.newAtom("C");
        carbon.setID("central");
        IAtom carbon1 = builder.newAtom("C");
        carbon1.setID("c1");
        IAtom carbon2 = builder.newAtom("C");
        carbon2.setID("c2");
        IAtom carbon3 = builder.newAtom("C");
        carbon3.setID("c3");
        IAtom carbon4 = builder.newAtom("C");
        carbon4.setID("c4");
        int parityInt = 1;
        IAtomParity parity = builder.newAtomParity(carbon, carbon1, carbon2, carbon3, carbon4, parityInt);
        IAtomContainer container = builder.newAtomContainer();
        container.addAtomParity(parity);
        org.openscience.cdk.interfaces.IAtomParity copy = container.getAtomParity(carbon);
        assertNotNull(copy);
        assertEquals(parity, copy);
    }

    /** Test for RFC #9 */
    public void testToString() {
        IAtomContainer container = builder.newAtomContainer();
        String description = container.toString();
        for (int i=0; i< description.length(); i++) {
            assertTrue(description.charAt(i) != '\n');
            assertTrue(description.charAt(i) != '\r');
        }
    }

    public void testStateChanged_IChemObjectChangeEvent() {
        ChemObjectListenerImpl listener = new ChemObjectListenerImpl();
        IAtomContainer chemObject = builder.newAtomContainer();
        chemObject.addListener(listener);
        
        chemObject.addAtom(builder.newAtom());
        assertTrue(listener.changed);
        
        listener.reset();
        assertFalse(listener.changed);
        chemObject.addBond(builder.newBond(builder.newAtom(), builder.newAtom()));
        assertTrue(listener.changed);
    }

    private class ChemObjectListenerImpl implements IChemObjectListener {
        private boolean changed;
        
        private ChemObjectListenerImpl() {
            changed = false;
        }
        
        public void stateChanged(IChemObjectChangeEvent e) {
            changed = true;
        }
        
        public void reset() {
            changed = false;
        }
    }
    
    public void testAddAtomParity_IAtomParity() {
        testGetAtomParity_IAtom();
    }
    
    public void testGetSingleElectronSum_IAtom() {
        // another rather artifial example
        IMolecule acetone = builder.newMolecule();
        IAtom c = builder.newAtom("C");
        IAtom o = builder.newAtom("O");
        acetone.addAtom(c);
        acetone.addAtom(o);
        IBond b1 = builder.newBond(c, o, 2.0);
        acetone.addElectronContainer(b1);
        ISingleElectron single1 = builder.newSingleElectron(c);
        ISingleElectron single2 = builder.newSingleElectron(c);
        ISingleElectron single3 = builder.newSingleElectron(o);
        acetone.addElectronContainer(single1);
        acetone.addElectronContainer(single2);
        acetone.addElectronContainer(single3);

        assertEquals(2, acetone.getSingleElectron(c).length);
        assertEquals(1, acetone.getSingleElectron(o).length);
        assertEquals(single1, acetone.getSingleElectron(c)[0]);
        assertEquals(single2, acetone.getSingleElectron(c)[1]);
        assertEquals(single3, acetone.getSingleElectron(o)[0]);
        
        assertEquals(2, acetone.getSingleElectronSum(c));
        assertEquals(1, acetone.getSingleElectronSum(o));
    }
}
