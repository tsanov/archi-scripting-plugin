/**
 * This program and the accompanying materials
 * are made available under the terms of the License
 * which accompanies this distribution in the file LICENSE.txt
 */
package com.archimatetool.script.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import com.archimatetool.editor.utils.PlatformUtils;
import com.archimatetool.script.ArchiScriptPlugin;



/**
 * Class used to initialize default preference values
 * 
 * @author Phillip Beauvoir
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer
implements IPreferenceConstants {

    @Override
    public void initializeDefaultPreferences() {
		IPreferenceStore store = ArchiScriptPlugin.INSTANCE.getPreferenceStore();
        
		if(PlatformUtils.isWindows()) {
	        store.setDefault(PREFS_EDITOR, "notepad.exe"); //$NON-NLS-1$
		}
		else if(PlatformUtils.isMac()) {
            store.setDefault(PREFS_EDITOR, "TextEdit"); //$NON-NLS-1$
        }
		else if(PlatformUtils.isLinux()) {
            store.setDefault(PREFS_EDITOR, "gedit"); //$NON-NLS-1$
        }
    }
}