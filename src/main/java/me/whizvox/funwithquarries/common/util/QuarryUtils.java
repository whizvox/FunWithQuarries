package me.whizvox.funwithquarries.common.util;

import net.minecraft.core.BlockPos;

import java.util.Collection;

public class QuarryUtils {

  public static void calculateFramePositions(BlockPos corner1, BlockPos corner2, Collection<BlockPos> output) {
    for (int x = corner1.getX(); x <= corner2.getX(); x++) {
      output.add(new BlockPos(x, corner1.getY(), corner1.getZ()));
      output.add(new BlockPos(x, corner1.getY(), corner2.getZ()));
      output.add(new BlockPos(x, corner2.getY(), corner1.getZ()));
      output.add(new BlockPos(x, corner2.getY(), corner2.getZ()));
    }
    for (int z = corner1.getZ() + 1; z < corner2.getZ(); z++) {
      output.add(new BlockPos(corner1.getX(), corner1.getY(), z));
      output.add(new BlockPos(corner1.getX(), corner2.getY(), z));
      output.add(new BlockPos(corner2.getX(), corner1.getY(), z));
      output.add(new BlockPos(corner2.getX(), corner2.getY(), z));
    }
    for (int y = corner1.getY() + 1; y < corner2.getY(); y++) {
      output.add(new BlockPos(corner1.getX(), y, corner1.getZ()));
      output.add(new BlockPos(corner1.getX(), y, corner2.getZ()));
      output.add(new BlockPos(corner2.getX(), y, corner1.getZ()));
      output.add(new BlockPos(corner2.getX(), y, corner2.getZ()));
    }
  }

}
