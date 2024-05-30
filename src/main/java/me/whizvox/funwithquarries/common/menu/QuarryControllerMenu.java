package me.whizvox.funwithquarries.common.menu;

import me.whizvox.funwithquarries.common.block.entity.QuarryControllerBlockEntity;
import me.whizvox.funwithquarries.common.registry.FWQMenus;
import me.whizvox.funwithquarries.common.util.MenuHelper;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class QuarryControllerMenu extends AbstractContainerMenu {

  @Nullable
  private final QuarryControllerBlockEntity controller;

  public QuarryControllerMenu(int containerId, Inventory playerInv, @Nullable QuarryControllerBlockEntity controller) {
    super(FWQMenus.QUARRY_CONTROLLER.get(), containerId);
    this.controller = controller;

    MenuHelper.addPlayerInventory(playerInv, this::addSlot);
  }

  public QuarryControllerMenu(int containerId, Inventory playerInv) {
    this(containerId, playerInv, null);
  }

  @Override
  public ItemStack quickMoveStack(Player pPlayer, int quickMovedSlotIndex) {
    return ItemStack.EMPTY;
  }

  @Override
  public boolean stillValid(Player player) {
    return controller == null || Container.stillValidBlockEntity(controller, player);
  }

}
