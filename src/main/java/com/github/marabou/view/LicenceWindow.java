/**
 * Copyright (C) 2012 - 2015 Jan-Hendrik Peters
 *
 * This file is part of marabou.
 *
 * Marabou is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package com.github.marabou.view;

import com.github.marabou.helper.AvailableImage;
import com.github.marabou.helper.ImageLoader;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.RowLayout;

import static com.github.marabou.helper.I18nHelper._;

public class LicenceWindow extends BaseGuiClass {

	public static void showLicence() {
		shell.setText(_("Licence"));
		shell.setImage(new ImageLoader().getImage(AvailableImage.LOGO_BIG));
		
		RowLayout rowLayout = new RowLayout(1);
		rowLayout.center = true;
		rowLayout.marginBottom = 10;
		rowLayout.marginTop = 10;
		rowLayout.marginLeft = 10;
		rowLayout.marginRight = 10;
		shell.setLayout(rowLayout);
		
		// close window on ESC
		shell.addListener(SWT.Traverse, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.TRAVERSE_ESCAPE) {
					shell.dispose();
				}
			}
		});
		
		// label
		Label label = new Label(shell, SWT.NONE);
		label.setAlignment(SWT.CENTER);
		label.setText(_(
		"Marabou Audio Tagger\n\n" +
		"A cross platform audio tagger using SWT\n\n" + 	
		"Copyright (C) 2009-2012  Jan-Hendrik Peters, Markus Herpich\n\n\n" + 
		
		"This program is free software: you can redistribute it and/or modify\n" + 
		"it under the terms of the GNU General Public License as published by\n" + 
		"the Free Software Foundation, either version 3 of the License, or\n" + 
		"(at your option) any later version.\n\n" +
		
		"This program is distributed in the hope that it will be useful,\n" + 
		"but WITHOUT ANY WARRANTY; without even the implied warranty of\n" + 
		"MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n" + 
		"GNU General Public License for more details.\n\n" +
		
		"You should have received a copy of the GNU General Public License\n" + 
		"along with this program.  If not, see <http://www.gnu.org/licenses/>.")
		);
		label.pack();
		
		// close button
		Button close = new Button(shell, SWT.NONE);
		close.setText(_("&Close"));
		close.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
			shell.dispose();
			}
		});
		close.setFocus();
		close.pack();

		shell.pack();
		shell.open();
		
		// close also if display gets disposed
		while (!shell.isDisposed() && display.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
	}
}