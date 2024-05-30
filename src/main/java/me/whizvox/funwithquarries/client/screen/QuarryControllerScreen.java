package me.whizvox.funwithquarries.client.screen;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.menu.QuarryControllerMenu;
import me.whizvox.funwithquarries.common.util.FWQLang;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class QuarryControllerScreen extends AbstractContainerScreen<QuarryControllerMenu> {

  private static final ResourceLocation BG_TEXTURE = new ResourceLocation(FunWithQuarries.MOD_ID, "textures/gui/quarry_controller.png");

  public QuarryControllerScreen(QuarryControllerMenu menu, Inventory playerInv, Component title) {
    super(menu, playerInv, title);
  }

  @Override
  protected void init() {
    super.init();

    addRenderableWidget(new PowerButton(leftPos + 152, topPos + 62));
  }

  @Override
  protected void renderLabels(GuiGraphics g, int mouseX, int mouseY) {
    // don't render labels
  }

  @Override
  protected void renderBg(GuiGraphics g, float partialTick, int mouseX, int mouseY) {
    g.blit(BG_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
  }

  @Override
  public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
    renderBackground(g);
    super.render(g, mouseX, mouseY, partialTick);
    renderTooltip(g, mouseX, mouseY);
  }

  static class PowerButton extends AbstractButton {

    boolean powered;

    PowerButton(int x, int y) {
      super(x, y, 16, 16, FWQLang.GUI_CONTROLLER_ON);
      powered = false;
    }

    @Override
    public void onPress() {
      powered = !powered;
      setMessage(powered ? FWQLang.GUI_CONTROLLER_OFF : FWQLang.GUI_CONTROLLER_ON);
      // TODO Update controller
    }

    @Override
    public void renderWidget(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
      g.blit(BG_TEXTURE, getX(), getY(), isHovered ? 192 : 176, 0, width, height);
      g.blit(BG_TEXTURE, getX(), getY(), powered ? 192 : 176, 16, width, height);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
      defaultButtonNarrationText(output);
    }

  }

}
