package me.whizvox.funwithquarries.common.item;

import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.DiggerItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class UniversalDiggerItem extends DiggerItem {

  public UniversalDiggerItem(Tier tier) {
    super(1, 1, tier, FWQBlocks.TAG_MINEABLE, new Item.Properties().durability(0));
  }

  // skip step of attempting to damage the item

  @Override
  public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
    return true;
  }

  @Override
  public boolean mineBlock(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity miner) {
    return true;
  }

}
