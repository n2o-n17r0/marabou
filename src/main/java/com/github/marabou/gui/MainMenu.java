/*
	Marabou Audio Tagger - A cross platform audio tagger using SWT
	
	Copyright (C) 2009-2010  Jan-Hendrik Peters, Markus Herpich
	
	This program is free software: you can redistribute it and/or modify
	it under the terms of the GNU General Public License as published by
	the Free Software Foundation, either version 3 of the License, or
	(at your option) any later version.
	      
	This program is distributed in the hope that it will be useful,
	but WITHOUT ANY WARRANTY; without even the implied warranty of
	MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
	GNU General Public License for more details.
	
	You should have received a copy of the GNU General Public License
	along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.marabou.gui;

import static com.github.marabou.helper.I18nHelper._;

import com.github.marabou.controller.EditorController;
import com.github.marabou.controller.MainMenuController;
import com.github.marabou.helper.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.*;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

/**
 * This class creates and fills (if init is invoked) a menu object which will be
 * used as SWT.BAR menu in the main window.
 */
public class MainMenu extends BaseGuiClass {
    private final ImageLoader imageLoader;
	private Menu menu;
    private EditorController editorController;
    private MainMenuController mainMenuController;


    public MainMenu(EditorController editorController, MainMenuController mainMenuController) {
        this.editorController = editorController;
        this.mainMenuController = mainMenuController;
        this.menu = new Menu(shell, SWT.BAR);
        this.imageLoader = new ImageLoader();
	}

	public Menu getMenu() {
		return menu;
	}

	public void init() {
		// File
		MenuItem file = new MenuItem(menu, SWT.CASCADE);
		file.setText(_("&File"));

		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		file.setMenu(fileMenu);

		// File -> Open File
		MenuItem openFileItem = new MenuItem(fileMenu, SWT.PUSH);
		openFileItem.setText(_("Open &file\t Ctrl+F"));

		openFileItem.setAccelerator(SWT.CTRL + 'F');
		openFileItem.setImage(imageLoader.getImage(AvailableImage.AUDIO_ICON));

		// listener for File -> open file
		openFileItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event e) {
                mainMenuController.handleOpenFileEvent();
            }
		});

		// File -> open directory
		MenuItem openDirectoryItem = new MenuItem(fileMenu, SWT.PUSH);
		openDirectoryItem.setText(_("Open &directory\t Ctrl+D"));
		openDirectoryItem.setAccelerator(SWT.CTRL + 'D');

		openDirectoryItem.setImage(imageLoader.getImage(AvailableImage.FOLDER_ICON));

		// listener for File -> open directory
		openDirectoryItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
                mainMenuController.handleOpenDirectoryEvent();
				}
		});

		// File -> save current file
		MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
		saveItem.setText(_("&Save\t Ctrl+S"));
		saveItem.setAccelerator(SWT.CTRL + 'S');
		saveItem.setImage(imageLoader.getImage(AvailableImage.SAVE_ICON));

		saveItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
					editorController.saveSelectedFiles();
			}
		});

		// File -> Exit
		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setImage(imageLoader.getImage(AvailableImage.EXIT_ICON));
		exitItem.setText(_("&Exit\t Alt+F4"));

		// listener for File -> Exit
		exitItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event e) {
				System.exit(0);
			}
		});

		// Help
		MenuItem help = new MenuItem(menu, SWT.CASCADE);
		help.setText(_("&Help"));

		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		help.setMenu(helpMenu);

		MenuItem aboutItem = new MenuItem(helpMenu, SWT.PUSH);
		aboutItem.setText(_("&About\t F1"));
		aboutItem.setAccelerator(SWT.F1);

		aboutItem.setImage(imageLoader.getImage(AvailableImage.HELP_ICON));

		// listener for Help -> About
		aboutItem.addListener(SWT.Selection, new Listener() {
			public void handleEvent(final Event e) {
                mainMenuController.handleShowAboutWindow();
			}
		});
	}
}
