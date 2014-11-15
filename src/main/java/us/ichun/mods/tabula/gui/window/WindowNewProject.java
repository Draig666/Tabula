package us.ichun.mods.tabula.gui.window;

import net.minecraft.util.StatCollector;
import us.ichun.mods.tabula.gui.GuiWorkspace;
import us.ichun.mods.tabula.gui.Theme;
import us.ichun.mods.tabula.gui.window.element.Element;
import us.ichun.mods.tabula.gui.window.element.ElementButton;
import us.ichun.mods.tabula.gui.window.element.ElementTextInput;

public class WindowNewProject extends Window
{
    public WindowNewProject(GuiWorkspace parent, int x, int y, int w, int h, int minW, int minH)
    {
        super(parent, x, y, w, h, minW, minH, "window.newProject.title", true);

        elements.add(new ElementTextInput(this, 10, 30, width - 20, 12, 1, "window.newProject.projName"));
        elements.add(new ElementTextInput(this, 10, 65, width - 20, 12, 2, "window.newProject.authName"));
        elements.add(new ElementButton(this, width - 140, height - 30, 60, 16, 3, false, 1, 1, "element.button.ok"));
        elements.add(new ElementButton(this, width - 70, height - 30, 60, 16, 0, false, 1, 1, "element.button.cancel"));
    }

    @Override
    public void draw(int mouseX, int mouseY)
    {
        super.draw(mouseX, mouseY);
        if(!minimized)
        {
            workspace.getFontRenderer().drawString(StatCollector.translateToLocal("window.newProject.projName"), posX + 11, posY + 20, Theme.getAsHex(Theme.font), false);
            workspace.getFontRenderer().drawString(StatCollector.translateToLocal("window.newProject.authName"), posX + 11, posY + 55, Theme.getAsHex(Theme.font), false);
        }
    }

    @Override
    public void elementTriggered(Element element)
    {
        if(element.id == 0)
        {
            workspace.removeWindow(this, true);
        }
        if(element.id >= 0)
        {
            //DO STUFF.
            workspace.removeWindow(this, true);
        }
    }
}