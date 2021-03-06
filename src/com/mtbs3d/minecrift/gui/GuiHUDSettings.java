package com.mtbs3d.minecrift.gui;

import com.mtbs3d.minecrift.settings.VRSettings;

import net.minecraft.src.*;

public class GuiHUDSettings extends BaseGuiSettings
{
    static EnumOptions[] hudOptions = new EnumOptions[] {
            EnumOptions.HUD_SCALE,
            EnumOptions.HUD_DISTANCE,
            EnumOptions.HUD_OPACITY,
            EnumOptions.HUD_OCCLUSION,
            EnumOptions.CROSSHAIR_SCALE,
            EnumOptions.CROSSHAIR_ALWAYS_SHOW,
            EnumOptions.CROSSHAIR_ROLL,
            EnumOptions.BLOCK_OUTLINE_ALWAYS_SHOW,
    };

    public GuiHUDSettings(GuiScreen guiScreen, VRSettings guivrSettings) {
        super( guiScreen, guivrSettings );
        screenTitle = "HUD / Overlay Settings";
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        StringTranslate stringTranslate = StringTranslate.getInstance();
        this.buttonList.clear();
//        this.buttonList.add(new GuiSmallButtonEx(EnumOptions.USE_VR.returnEnumOrdinal(), this.width / 2 - 78, this.height / 6 - 14, EnumOptions.USE_VR, this.guivrSettings.getKeyBinding(EnumOptions.USE_VR)));
        this.buttonList.add(new GuiButtonEx(201, this.width / 2 - 100, this.height / 6 + 148, "Reset To Defaults"));
        this.buttonList.add(new GuiButtonEx(200, this.width / 2 - 100, this.height / 6 + 168, stringTranslate.translateKey("gui.done")));
        EnumOptions[] buttons = hudOptions;

        for (int var12 = 2; var12 < buttons.length + 2; ++var12)
        {
            EnumOptions var8 = buttons[var12 - 2];
            int width = this.width / 2 - 155 + var12 % 2 * 160;
            int height = this.height / 6 + 21 * (var12 / 2) - 10;

            if (var8.getEnumFloat())
            {
                float minValue = 0.0f;
                float maxValue = 1.0f;
                float increment = 0.01f;

                if (var8 == EnumOptions.HUD_SCALE)
                {
                    minValue = 0.35f;
                    maxValue = 2.5f;
                    increment = 0.01f;
                }
                if (var8 == EnumOptions.HUD_DISTANCE)
                {
                    minValue = 0.25f;
                    maxValue = 5.0f;
                    increment = 0.01f;
                }
                if (var8 == EnumOptions.CROSSHAIR_SCALE)
                {
                    minValue = 0.25f;
                    maxValue = 2.5f;
                    increment = 0.01f;
                }

                this.buttonList.add(new GuiSliderEx(var8.returnEnumOrdinal(), width, height, var8, this.guivrSettings.getKeyBinding(var8), minValue, maxValue, increment, this.guivrSettings.getOptionFloatValue(var8)));
            }
            else
            {
                this.buttonList.add(new GuiSmallButtonEx(var8.returnEnumOrdinal(), width, height, var8, this.guivrSettings.getKeyBinding(var8)));
            }
        }
    }

    /**
     * Fired when a control is clicked. This is the equivalent of ActionListener.actionPerformed(ActionEvent e).
     */
    protected void actionPerformed(GuiButton par1GuiButton)
    {
        if (par1GuiButton.enabled)
        {
            if (par1GuiButton.id < 200 && par1GuiButton instanceof GuiSmallButtonEx)
            {
                EnumOptions num = EnumOptions.getEnumOptions(par1GuiButton.id);
                this.guivrSettings.setOptionValue(((GuiSmallButtonEx)par1GuiButton).returnEnumOptions(), 1);
                par1GuiButton.displayString = this.guivrSettings.getKeyBinding(EnumOptions.getEnumOptions(par1GuiButton.id));
            }
            else if (par1GuiButton.id == 200)
            {
                this.mc.vrSettings.saveOptions();
                this.mc.displayGuiScreen(this.parentGuiScreen);
            }
            else if (par1GuiButton.id == 201)
            {
                this.guivrSettings.hudDistance = 1.0f;
                this.guivrSettings.hudScale = 1.0f;
                this.guivrSettings.useHudOpacity = false;
                this.guivrSettings.hudOcclusion = false;
                this.guivrSettings.crosshairScale = 1.0f;
                this.guivrSettings.alwaysRenderBlockOutline = false;
                this.guivrSettings.alwaysRenderInGameCrosshair = false;
                this.guivrSettings.crosshairRollsWithHead = true;
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
                case HUD_OPACITY:
                    return new String[] {
                            "Whether the in-game HUD and UI are slightly transparent",
                            "  ON: HUD and UI are transparent",
                            "  OFF: HUD and UI are opaque"
                    };
                case HUD_SCALE:
                    return new String[] {
                            "Relative size HUD takes up in field-of-view",
                            "  The units are just relative, not in degrees",
                            "  or a fraction of FOV or anything"
                    };
                case HUD_DISTANCE:
                    return new String[] {
                            "Distance the floating HUD is drawn in front of your body",
                            "  The relative size of the HUD is unchanged by this",
                            "  Distance is in meters (though isn't obstructed by blocks)"
                    };
                case HUD_OCCLUSION:
                    return new String[] {
                            "Specifies whether the HUD is occluded by closer objects.",
                            "  ON:  The HUD will be hidden by closer objects. May",
                            "       be hidden completely in confined environments!",
                            "  OFF: The HUD is always visible. Stereo depth issues",
                            "       may be noticable."
                    };
                case CROSSHAIR_ALWAYS_SHOW:
                    return new String[] {
                            "Set the in-game crosshair display mode",
                            "  Always: The crosshair is always shown even if the",
                            "          HUD is disabled",
                            "  With HUD: The crosshair is only shown when the HUD",
                            "            is enabled"
                    };
                case CROSSHAIR_SCALE:
                    return new String[] {
                            "Sets the size of the in-game crosshair"
                    };
                case BLOCK_OUTLINE_ALWAYS_SHOW:
                    return new String[] {
                            "Sets the in-game block outline display mode.",
                            "  Always: The block outline is always shown even if",
                            "          the HUD is disabled",
                            "  With HUD: The block outline is only shown when the",
                            "           HUD is enabled"
                    };
                case CROSSHAIR_ROLL:
                    return new String[] {
                            "Sets the crosshair roll behaviour.",
                            "  With Head: The crosshair rolls with your head.",
                            "  With HUD:  The crosshair appears to roll, keeping",
                            "             the same orientation as the HUD."
                    };
                default:
                    return null;
            }
        else
            switch(buttonId)
            {
//                case 201:
//                    return new String[] {
//                            "Open this configuration screen to adjust the Head",
//                            "  Tracker orientation (direction) settings. ",
//                            "  Ex: Head Tracking Selection (Hydra/Oculus), Prediction"
//                    };
                default:
                    return null;
            }
    }
}
