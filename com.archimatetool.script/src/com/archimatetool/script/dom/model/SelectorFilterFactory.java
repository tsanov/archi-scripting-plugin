/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.script.dom.model;

import org.eclipse.emf.ecore.EObject;

import com.archimatetool.model.IArchimateConcept;
import com.archimatetool.model.IArchimateElement;
import com.archimatetool.model.IArchimatePackage;
import com.archimatetool.model.IArchimateRelationship;
import com.archimatetool.model.IDiagramModel;
import com.archimatetool.model.IDiagramModelArchimateComponent;
import com.archimatetool.model.IDiagramModelComponent;
import com.archimatetool.model.IFolder;
import com.archimatetool.model.IIdentifier;
import com.archimatetool.model.INameable;

/**
 * Selector Filter Factory
 * 
 * @author Phillip Beauvoir
 */
class SelectorFilterFactory {
    
    public static interface ISelectorFilter {
        boolean accept(EObject object);
        
        default boolean isSingle() {
            return false;
        }
    }
    
    private SelectorFilterFactory() {}
    
    static SelectorFilterFactory INSTANCE = new SelectorFilterFactory();

    public ISelectorFilter getFilter(String selector) {
        if(selector == null) {
            return null;
        }
        
        // All
        if(selector.equals("*")) { //$NON-NLS-1$
            return new ISelectorFilter() {
                public boolean accept(EObject object) {
                    return (object instanceof IArchimateConcept || object instanceof IDiagramModel
                            || object instanceof IFolder || object instanceof IDiagramModelComponent);
                }
            };
        }
        
        // All concepts
        else if(selector.equals("concepts")) { //$NON-NLS-1$
            return new ISelectorFilter() {
                public boolean accept(EObject object) {
                    return object instanceof IArchimateConcept;
                }
            };
        }
        
        // All elements
        else if(selector.equals("elements")) { //$NON-NLS-1$
            return new ISelectorFilter() {
                public boolean accept(EObject object) {
                    return object instanceof IArchimateElement;
                }
            };
        }
        
        // All relationships
        else if(selector.equals("relations") || selector.equals("relationships")) { //$NON-NLS-1$ //$NON-NLS-2$
            return new ISelectorFilter() {
                public boolean accept(EObject object) {
                    return object instanceof IArchimateRelationship;
                }
            };
        }

        // All views
        else if(selector.equals("views")) { //$NON-NLS-1$
            return new ISelectorFilter() {
                public boolean accept(EObject object) {
                    return object instanceof IDiagramModel;
                }
            };
        }

        // Find single unique object by its ID
        else if(selector.startsWith("#") && selector.length() > 1) { //$NON-NLS-1$
            String id = selector.substring(1);
            
            return new ISelectorFilter() {
                public boolean accept(EObject object) {
                    return object instanceof IIdentifier && id.equals(((IIdentifier)object).getId());
                }
                
                public boolean isSingle() {
                    return true;
                }
            };
        }
        
        // Find all objects with given name
        else if(selector.startsWith(".") & selector.length() > 1) { //$NON-NLS-1$
            String name = selector.substring(1);
            
            return new ISelectorFilter() {
                public boolean accept(EObject object) {
                    object = getReferencedConcept(object);
                    return (object instanceof IArchimateConcept || object instanceof IDiagramModel || object instanceof IFolder)
                            && name.equals(((INameable)object).getName());
                }
            };
        }
        
        // Find all objects with given type and name
        else if(selector.contains(".") && selector.length() > 2) { //$NON-NLS-1$
            String[] s = selector.split("\\."); //$NON-NLS-1$
            if(s.length != 2) {
                return null;
            }

            return new ISelectorFilter() {
                public boolean accept(EObject object) {
                    object = getReferencedConcept(object);
                    return object.eClass().getName().equals(s[0]) &&
                            ((INameable)object).getName().equals(s[1]) &&
                            (object instanceof IArchimateConcept || object instanceof IDiagramModel || object instanceof IFolder);
                }
            };
        }

        // Class type of concept
        else if(IArchimatePackage.eINSTANCE.getEClassifier(selector) != null) {
            return new ISelectorFilter() {
                public boolean accept(EObject object) {
                    object = getReferencedConcept(object);
                    return object.eClass().getName().equals(selector) &&
                            (object instanceof IArchimateConcept || object instanceof IDiagramModel || object instanceof IFolder);
                }
            };
        }

        return null;
    }
    
    private EObject getReferencedConcept(EObject object) {
        if(object instanceof IDiagramModelArchimateComponent) {
            return ((IDiagramModelArchimateComponent)object).getArchimateConcept();
        }
        
        return object;
    }
}
