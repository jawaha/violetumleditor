/*
 Violet - A program for editing UML diagrams.

 Copyright (C) 2007 Cay S. Horstmann (http://horstmann.com)
 Alexandre de Pellegrin (http://alexdp.free.fr);

 This program is free software; you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 2 of the License, or
 (at your option) any later version.

 This program is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 GNU General Public License for more details.

 You should have received a copy of the GNU General Public License
 along with this program; if not, write to the Free Software
 Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package com.horstmann.violet.product.diagram.state.nodes;

import java.awt.Color;

import com.horstmann.violet.framework.graphics.Separator;
import com.horstmann.violet.framework.graphics.content.*;
import com.horstmann.violet.framework.graphics.shape.ContentInsideRoundRectangle;
import com.horstmann.violet.product.diagram.abstracts.edge.IEdge;
import com.horstmann.violet.product.diagram.abstracts.node.ColorableNode;
import com.horstmann.violet.product.diagram.abstracts.node.INode;
import com.horstmann.violet.product.diagram.abstracts.property.string.LineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.MultiLineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.SingleLineText;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.BoldDecorator;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.LargeSizeDecorator;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.OneLineString;
import com.horstmann.violet.product.diagram.abstracts.property.string.decorator.PrefixDecorator;

/**
 * A node_old in a state diagram.
 */
public class StateNode extends ColorableNode
{
    /**
     * Construct a state node_old with a default size
     */
    public StateNode()
    {
        super();
        name = new SingleLineText(nameConverter);
        name.setAlignment(LineText.CENTER);
        onEntry = new MultiLineText(entryConverter);
        onExit = new MultiLineText(exitConverter);
        createContentStructure();
    }

    protected StateNode(StateNode node) throws CloneNotSupportedException
    {
        super(node);
        name = node.name.clone();
        onEntry = node.onEntry.clone();
        onExit = node.onExit.clone();
        createContentStructure();
    }

    @Override
    public void deserializeSupport()
    {
        name.deserializeSupport(nameConverter);
        onEntry.deserializeSupport(entryConverter);
        onExit.deserializeSupport(exitConverter);

        super.deserializeSupport();
    }

    @Override
    protected INode copy() throws CloneNotSupportedException
    {
        return new StateNode(this);
    }

    @Override
    protected void createContentStructure()
    {
        TextContent nameContent = new TextContent(name);
        nameContent.setMinHeight(20);
        nameContent.setMinWidth(DEFAULT_WIDTH);
        nameContent.setMinHeight(DEFAULT_HEIGHT);
        TextContent onEntryContent = new TextContent(onEntry);
        TextContent onExitContent = new TextContent(onExit);

        VerticalLayout verticalGroupContent = new VerticalLayout();
        verticalGroupContent.add(nameContent);

        VerticalLayout verticalInsideGroupContent = new VerticalLayout();

        verticalInsideGroupContent.add(onEntryContent);
        verticalInsideGroupContent.add(onExitContent);
        verticalGroupContent.add(verticalInsideGroupContent);

        separator = new Separator.LineSeparator(getBorderColor());
        verticalGroupContent.setSeparator(separator);
        updateSeparator();

        setTextColor(super.getTextColor());

        ContentInsideShape contentInsideShape = new ContentInsideRoundRectangle(verticalGroupContent, ARC_SIZE);

        setBorder(new ContentBorder(contentInsideShape, getBorderColor()));
        setBackground(new ContentBackground(getBorder(), getBackgroundColor()));
        setContent(getBackground());

        setTextColor(super.getTextColor());
    }

    @Override
    public void setTextColor(Color textColor)
    {
        name.setTextColor(textColor);
        onEntry.setTextColor(textColor);
        onExit.setTextColor(textColor);
        super.setTextColor(textColor);
    }

    @Override
    public void setBorderColor(Color borderColor)
    {
        if(null != separator)
        {
            separator.setColor(borderColor);
        }
        super.setBorderColor(borderColor);
    }


    @Override
    public boolean addConnection(IEdge e) {
        if (e.getEnd() == null) {
            return false;
        }
        if (this.equals(e.getEnd())) {
            return false;
        }
        return super.addConnection(e);
    }

    /**
     * Sets the name property value.
     *
     * @param newValue the new state name
     */
    public void setName(SingleLineText newValue)
    {
        name.setText(newValue.toEdit());
    }

    /**
     * Gets the name property value.
     */
    public SingleLineText getName()
    {
        return name;
    }

    /**
     * Sets the entry property value.
     *
     * @param newValue the new entry action
     */
    public void setOnEntry(MultiLineText newValue)
    {
        onEntry.setText(newValue.toEdit());
        updateSeparator();
    }

    /**
     * Gets the entry property value.
     *
     * @return the entry action
     */
    public MultiLineText getOnEntry()
    {
        return onEntry;
    }

    /**
     * Sets the exit property value.
     *
     * @param newValue the new exit action
     */
    public void setOnExit(MultiLineText newValue)
    {
        onExit.setText(newValue.toEdit());
        updateSeparator();
    }

    /**
     * Gets the exit action property value.
     *
     * @return the exit action name
     */
    public MultiLineText getOnExit()
    {
        return onExit;
    }

    private void updateSeparator()
    {
        if(onEntry.toEdit().isEmpty() && onExit.toEdit().isEmpty())
        {
            separator.setColor(null);
        }
        else
        {
            separator.setColor(getBorderColor());
        }
    }

    private SingleLineText name;
    private MultiLineText onEntry;
    private MultiLineText onExit;

    private transient Separator separator;

    private static final int ARC_SIZE = 20;
    private static final int DEFAULT_WIDTH = 80;
    private static final int DEFAULT_HEIGHT = 40;
    private static final int DEFAULT_COMPARTMENT_HEIGHT = 5;

    public static final String ENTRY = "entry / ";
    public static final String EXIT = "exit / ";

    private static final LineText.Converter nameConverter = new LineText.Converter(){
        @Override
        public OneLineString toLineString(String text)
        {
            return new BoldDecorator(new OneLineString(text));
        }
    };

    private static final LineText.Converter entryConverter = new LineText.Converter(){
        @Override
        public OneLineString toLineString(String text)
        {
            if(false == text.isEmpty())
            {
                return new PrefixDecorator(new OneLineString(text), ENTRY);
            }
            return new OneLineString(text);
        }
    };

    private static final LineText.Converter exitConverter = new LineText.Converter(){
        @Override
        public OneLineString toLineString(String text)
        {
            if(false == text.isEmpty())
            {
                return new PrefixDecorator(new OneLineString(text), EXIT);
            }
            return new OneLineString(text);
        }
    };
}
