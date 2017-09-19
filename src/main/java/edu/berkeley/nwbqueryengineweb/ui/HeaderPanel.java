/*
 * Copyright 2013 David Beer.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.berkeley.nwbqueryengineweb.ui;

import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.DropDownButton;
import de.agilecoders.wicket.core.markup.html.bootstrap.button.dropdown.MenuBookmarkablePageLink;
import de.agilecoders.wicket.core.markup.html.bootstrap.image.GlyphIconType;
import de.agilecoders.wicket.core.markup.html.bootstrap.navbar.*;
import de.agilecoders.wicket.extensions.markup.html.bootstrap.icon.FontAwesomeIconType;
import org.apache.wicket.markup.html.link.AbstractLink;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Copyright Petr Jezek.
 */
public class HeaderPanel extends Panel {

    public HeaderPanel(String id) {
        super(id);

        add(navbar());
    }

    private Navbar navbar() {
        Navbar navbar = new Navbar("navbar");
        navbar.setInverted(true);
        navbar.setPosition(Navbar.Position.TOP);
        navbar.setBrandName(Model.of("NWB Query Engine"));

        DropDownButton dropdown = new NavbarDropDownButton(Model.of("DropDown")) {

            @Override
            protected List<AbstractLink> newSubMenuButtons(String buttonMarkupId) {
                final List<AbstractLink> subMenu = new ArrayList<AbstractLink>();
//                subMenu.add(new MenuBookmarkablePageLink(UserAccountPage.class, Model.of("Account"))
//                                .setIconType(FontAwesomeIconType.user));
                return subMenu;
            }

        };

        navbar.addComponents(new ImmutableNavbarComponent(dropdown, Navbar.ComponentPosition.RIGHT));

        navbar.addComponents(NavbarComponents.transform(Navbar.ComponentPosition.RIGHT, new NavbarButton<HomePage>(HomePage.class, Model.of("Home"))
                .setIconType(GlyphIconType.home)));

        return navbar;
    }
}
