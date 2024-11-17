package me.ht9.rose.util.misc;

import net.minecraft.src.GuiButton;
import net.minecraft.src.GuiScreen;
import net.minecraft.src.GuiTextField;
import org.lwjgl.input.Keyboard;

public final class GuiUsername extends GuiScreen
{
    private final GuiScreen parentScreen;
    private GuiTextField textField;

    public GuiUsername(GuiScreen parentScreen)
    {
        this.parentScreen = parentScreen;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @SuppressWarnings("unchecked")
    @Override
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        controlList.clear();
        controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 2 - 10, "Change Username"));
        controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 2 + 12, "Cancel"));

        this.textField = new GuiTextField(this, this.fontRenderer, this.width / 2 - 100, this.height / 2 - 32, 200, 20, mc.session.username);
        textField.isFocused = true;
        textField.setMaxStringLength(30);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton guiButton) {
        if (guiButton.enabled)
        {
            if (guiButton.id == 0)
            {
                mc.session.username = textField.getText();
                mc.displayGuiScreen(parentScreen);
            }
            else if (guiButton.id == 1)
            {
                mc.displayGuiScreen(parentScreen);
            }
        }
    }

    @Override
    protected void keyTyped(char c, int i) {
        this.textField.textboxKeyTyped(c, i);
        if (c == '\r')
        {
            this.actionPerformed((GuiButton) this.controlList.get(0));
        }

        ((GuiButton) this.controlList.get(0)).enabled = !this.textField.getText().isEmpty();
    }

    @Override
    protected void mouseClicked(int i, int j, int k) {
        super.mouseClicked(i, j, k);
        this.textField.mouseClicked(i, j, k);
    }

    @Override
    public void drawScreen(int i, int j, float f) {
        drawDefaultBackground();
        drawCenteredString(fontRenderer, "Change Username", width / 2, 10, -1);
        this.textField.drawTextBox();
        super.drawScreen(i, j, f);
    }
}