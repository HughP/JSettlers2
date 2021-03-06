/**
 * Java Settlers - An online multiplayer version of the game Settlers of Catan
 * Copyright (C) 2003  Robert S. Thomas <thomas@infolab.northwestern.edu>
 * Portions of this file Copyright (C) 2012-2014 Jeremy D Monin <jeremy@nand.net>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * The maintainer of this program can be reached at jsettlers@nand.net
 **/
package soc.client;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Font;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


@SuppressWarnings("serial")
class SOCMonopolyDialog extends Dialog implements ActionListener, Runnable
{
    Button[] rsrcBut;
    /** Prompt message. Text alignment is centered. */
    Label msg;
    SOCPlayerInterface pi;

    /** i18n text strings; will use same locale as SOCPlayerClient's string manager.
     *  @since 2.0.00 */
    private static final soc.util.SOCStringManager strings = soc.util.SOCStringManager.getClientManager();

    /**
     * Creates a new SOCMonopolyDialog object.
     *
     * @param pi Parent window
     */
    public SOCMonopolyDialog(SOCPlayerInterface pi)
    {
        super(pi, strings.get("spec.dcards.monopoly"), true);  // "Monopoly"

        this.pi = pi;
        setBackground(new Color(255, 230, 162));
        setForeground(Color.black);
        setFont(new Font("SansSerif", Font.PLAIN, 12));
        setLayout(null);
        addNotify();
        setSize(280, 160);

        msg = new Label(strings.get("dialog.mono.please.pick.resource"), Label.CENTER);
            // "Please pick a resource to monopolize."
        add(msg);

        rsrcBut = new Button[5];

        rsrcBut[0] = new Button(strings.get("resources.clay"));   // "Clay"
        rsrcBut[1] = new Button(strings.get("resources.ore"));    // "Ore"
        rsrcBut[2] = new Button(strings.get("resources.sheep"));  // "Sheep"
        rsrcBut[3] = new Button(strings.get("resources.wheat"));  // "Wheat"
        rsrcBut[4] = new Button(strings.get("resources.wood"));   // "Wood"

        for (int i = 0; i < 5; i++)
        {
            add(rsrcBut[i]);
            rsrcBut[i].addActionListener(this);
        }
    }

    /**
     * Set this dialog visible or hide it. If visible, request focus on the first resource button.
     */
    public void setVisible(boolean b)
    {
        super.setVisible(b);

        if (b)
            rsrcBut[0].requestFocus();
    }

    /**
     * Do our dialog's custom layout: Prompt message, row of 2 resource buttons, of 3 resource buttons.
     * Put the dialog in the center of the parent game window.
     */
    public void doLayout()
    {
        int width = getSize().width - getInsets().left - getInsets().right;
        int height = getSize().height - getInsets().top - getInsets().bottom;
        int space = 5;

        int pix = pi.getInsets().left;
        int piy = pi.getInsets().top;
        int piwidth = pi.getSize().width - pi.getInsets().left - pi.getInsets().right;
        int piheight = pi.getSize().height - pi.getInsets().top - pi.getInsets().bottom;

        int buttonW = 60;
        int button2X = (width - ((2 * buttonW) + space)) / 2;
        int button3X = (width - ((3 * buttonW) + (2 * space))) / 2;

        /* put the dialog in the center of the game window */
        setLocation(pix + ((piwidth - width) / 2), piy + ((piheight - height) / 2));

        try
        {
            msg.setBounds(getInsets().left, getInsets().top, width, 20);
            rsrcBut[0].setBounds(button2X, (getInsets().bottom + height) - (50 + (2 * space)), buttonW, 25);
            rsrcBut[1].setBounds(button2X + buttonW + space, (getInsets().bottom + height) - (50 + (2 * space)), buttonW, 25);
            rsrcBut[2].setBounds(button3X, (getInsets().bottom + height) - (25 + space), buttonW, 25);
            rsrcBut[3].setBounds(button3X + space + buttonW, (getInsets().bottom + height) - (25 + space), buttonW, 25);
            rsrcBut[4].setBounds(button3X + (2 * (space + buttonW)), (getInsets().bottom + height) - (25 + space), buttonW, 25);
        }
        catch (NullPointerException e) {}
    }

    /**
     * Handle resource button clicks.
     */
    public void actionPerformed(ActionEvent e)
    {
        try {
        Object target = e.getSource();

        for (int i = 0; i < 5; i++)
        {
            if (target == rsrcBut[i])
            {
                /**
                 * Note: This only works if SOCResourceConstants.CLAY == 1
                 */
                pi.getClient().getGameManager().monopolyPick(pi.getGame(), i + 1);
                dispose();

                break;
            }
        }
        } catch (Throwable th) {
            pi.chatPrintStackTrace(th);
        }
    }

    /**
     * Run method, for convenience with {@link java.awt.EventQueue#invokeLater(Runnable)}.
     * This method just calls {@link #setVisible(boolean) setVisible(true)}.
     * @since 2.0.00
     */
    public void run()
    {
        setVisible(true);
    }

}
