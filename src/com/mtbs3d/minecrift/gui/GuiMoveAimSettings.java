/**
 * Copyright 2013 Mark Browning, StellaArtois
 * Licensed under the LGPL 3.0 or later (See LICENSE.md for details)
 */
package com.mtbs3d.minecrift.gui;

import java.util.List;

import com.mtbs3d.minecrift.MCHydra;
import com.mtbs3d.minecrift.api.BasePlugin;
import com.mtbs3d.minecrift.api.IBasePlugin;
import com.mtbs3d.minecrift.api.PluginManager;
import com.mtbs3d.minecrift.settings.VRSettings;

import net.minecraft.src.*;

public class GuiMoveAimSettings extends BaseGuiSettings
{
    /** An array of all of EnumOption's movement options relevant to the hydra. */
    static EnumOptions[] hydraMoveAimOptions = new EnumOptions[] {
        EnumOptions.KEYHOLE_WIDTH,
        EnumOptions.KEYHOLE_HEAD_RELATIVE,
        EnumOptions.DECOUPLE_LOOK_MOVE,
        EnumOptions.JOYSTICK_SENSITIVITY,
        EnumOptions.MOVEAIM_HYDRA_USE_CONTROLLER_ONE,
    };
    /** An array of all of EnumOption's movement options relevant to the mouse. */
    static EnumOptions[] mouseMoveAimOptions = new EnumOptions[] {
        EnumOptions.KEYHOLE_WIDTH,
        EnumOptions.KEYHOLE_HEAD_RELATIVE,
        EnumOptions.DECOUPLE_LOOK_AIM_PITCH,
        EnumOptions.PITCH_AFFECTS_CAMERA,
        EnumOptions.DECOUPLE_LOOK_MOVE,
    };
	private PluginModeChangeButton pluginModeChangeutton;
	private boolean reinit;

    public GuiMoveAimSettings(GuiScreen par1GuiScreen,
                            VRSettings par2vrSettings)
    {
    	super( par1GuiScreen, par2vrSettings );
        screenTitle = "Move/Look/Aim Controls Configuration";
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.buttonList.clear();
        this.buttonList.add(new GuiButtonEx(202, this.width / 2 - 100, this.height / 6 + 148, "Reset To Defaults"));
        this.buttonList.add(new GuiButtonEx(200, this.width / 2 - 100, this.height / 6 + 168, stringTranslate.translateKey("gui.done")));
        
        pluginModeChangeutton = new PluginModeChangeButton(201, this.width / 2 - 78, this.height / 6 - 14, (List<IBasePlugin>)(List<?>) PluginManager.thePluginManager.controllerPlugins, this.guivrSettings.controllerPluginID );
        this.buttonList.add(pluginModeChangeutton);

        EnumOptions[] var10 = guivrSettings.controllerPluginID.equals(MCHydra.pluginID)?hydraMoveAimOptions:mouseMoveAimOptions ;
        int var11 = var10.length;

        for (int var12 = 2; var12 < var11 + 2; ++var12)
        {
            EnumOptions var8 = var10[var12 - 2];
            int width = this.width / 2 - 155 + var12 % 2 * 160;
            int height = this.height / 6 + 21 * (var12 / 2) - 10;

            if (var8.getEnumFloat())
            {
                float minValue = 0.0f;
                float maxValue = 1.0f;
                float increment = 0.01f;

                if (var8 == EnumOptions.MOVEMENT_MULTIPLIER)
                {
                    minValue = 0.15f;
                    maxValue = 1.0f;
                    increment = 0.01f;
                }
                else if( var8 == EnumOptions.JOYSTICK_SENSITIVITY )
                {
                	minValue = 0.5f;
                	maxValue = 10.0f;
                	increment= 0.1f;
                }
                else if( var8 == EnumOptions.KEYHOLE_WIDTH)
                {
                	minValue = 0.0f;
                	maxValue = 90f;
                	increment= 1.0f;
                }

                GuiSliderEx slider = new GuiSliderEx(var8.returnEnumOrdinal(), width, height, var8, this.guivrSettings.getKeyBinding(var8), minValue, maxValue, increment, this.guivrSettings.getOptionFloatValue(var8));
                this.buttonList.add(slider);
            }
            else
            {
                String keyText = this.guivrSettings.getKeyBinding(var8);
                GuiSmallButtonEx smallButton = new GuiSmallButtonEx(var8.returnEnumOrdinal(), width, height, var8, keyText);
                this.buttonList.add(smallButton);
            }
        }
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int par1, int par2, float par3)
    {
        if (reinit)
        {
            initGui();
            reinit = false;
        }
        super.drawScreen(par1,par2,par3);
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        EnumOptions num = EnumOptions.getEnumOptions(par1GuiButton.id);

        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id < 200 && par1GuiButton instanceof GuiSmallButtonEx)
            {
                this.guivrSettings.setOptionValue(((GuiSmallButtonEx)par1GuiButton).returnEnumOptions(), 1);
                par1GuiButton.displayString = this.guivrSettings.getKeyBinding(EnumOptions.getEnumOptions(par1GuiButton.id));
            }
            else if (par1GuiButton.id == 200)
            {
                this.mc.vrSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }
            else if (par1GuiButton.id == 201) // Mode Change
            {
            	this.mc.vrSettings.controllerPluginID = pluginModeChangeutton.getSelectedID();
                this.mc.vrSettings.saveOptions();
            	this.mc.lookaimController = PluginManager.configureController(this.mc.vrSettings.controllerPluginID);
            	this.reinit = true;
            }
            else if (par1GuiButton.id == 202) // Defaults
            {
                if (this.guivrSettings.controllerPluginID.equalsIgnoreCase(MCHydra.pluginID))
                {
                    this.guivrSettings.aimKeyholeWidthDegrees = 90f;
                    this.guivrSettings.keyholeHeadRelative = true;
                    this.guivrSettings.lookMoveDecoupled = false;
                    this.guivrSettings.joystickSensitivity = 3f;
                }
                else
                {
                    this.guivrSettings.aimKeyholeWidthDegrees = 0f;
                    this.guivrSettings.keyholeHeadRelative = true;
                    this.guivrSettings.lookMoveDecoupled = false;
                    this.guivrSettings.lookAimPitchDecoupled = false;
                    this.guivrSettings.pitchInputAffectsCamera = true;
                }
                this.mc.vrSettings.saveOptions();
                this.reinit = true;
            }
        }
    }

    @Override
    protected String[] getTooltipLines(String displayString, int buttonId)
    {
        EnumOptions e = EnumOptions.getEnumOptions(buttonId);
        if( e != null )
            switch(e)
            {
                case JOYSTICK_SENSITIVITY:
                    return new String[] {
                            "Joystick Sensitivity",
                            "The higher the value, the more you turn.",
                            "  Doesn't affect forward/backward speed",
                            "  Recommended value: 4.0" } ;
                case DECOUPLE_LOOK_MOVE:
                    return new String[] {
                            "Decouple Movement from Looking - \"Tank mode\"",
                            "  OFF: You always move in the direction you are facing",
                            "  ON: You move in the direction of the GUI - turning",
                            "     your head will not affect movement direction",
                            "  Recommended value: ON" } ;
                case KEYHOLE_WIDTH:
                    return new String[] {
                            "Allows the mouse some flexibility within a \"keyhole\"",
                            "  that doesn't turn the player.",
                            "If set to \"Fully Coupled\", any mouse movement will",
                            "  turn the camera.",
                            "Otherwise, this value is the horizontal width (in degrees)",
                            "  of the keyhole in which the cursor can freely move.",
                            "  Recommended value: > 60°"} ;
                case KEYHOLE_HEAD_RELATIVE:
                    return new String[] {
                            "Determines if the \"keyhole\" used for aiming moves ",
                            "  with your head.",
                            "  If YES, you'll need to move the aiming hydra along",
                            "  with your head in order to prevent counter-rotation.",
                            "  Recommended value: YES"} ;
                case PITCH_AFFECTS_CAMERA:
                    return new String[] {
                            "Adjusts whether the mouse can control the camera pitch",
                            "  OFF: No, the only way to control pitch is your head",
                            "  ON: Yes, moving the mouse up and down will move the",
                            "     camera up and down", };
                case DECOUPLE_LOOK_AIM_PITCH:
                    return new String[] {
                            "Adjusts whether the crosshair is fixed to your head",
                            "  vertically (tilt up and down)",
                            "  OFF: Yes, the only way to aim is with your head",
                            "  ON:  No, the crosshair is free to move"};
                case MOVEAIM_HYDRA_USE_CONTROLLER_ONE:
                    return new String[] {
                            "Sets the controller used for move/aim control.",
                            "  Sets which controller is used. Will be the opposite",
                            "  controller to that used for positional tracking."
                    } ;
                default:
                    return null;
            }
        else
            switch(buttonId)
            {
                case 201:
                    return new String[] {
                            "Changes the method for controlling aiming, looking, ",
                            "  and movement."
                    };
                default:
                    return null;
            }
    }
}
