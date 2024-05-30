package me.whizvox.funwithquarries.common.util;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.function.Consumer;

public class MenuHelper {

  public static void addPlayerInventory(Inventory playerInv, Consumer<Slot> addSlotFunc, int yOffset) {
    for(int y = 0; y < 3; ++y) {
      for(int x = 0; x < 9; ++x) {
        addSlotFunc.accept(new Slot(playerInv, x + y * 9 + 9, 8 + x * 18, yOffset + y * 18));
      }
    }
    for(int x = 0; x < 9; ++x) {
      addSlotFunc.accept(new Slot(playerInv, x, 8 + x * 18, yOffset + 58));
    }
  }

  public static void addPlayerInventory(Inventory playerInv, Consumer<Slot> addSlotFunc) {
    addPlayerInventory(playerInv, addSlotFunc, 84);
  }

}
