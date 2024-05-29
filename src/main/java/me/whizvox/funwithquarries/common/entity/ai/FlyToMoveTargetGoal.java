package me.whizvox.funwithquarries.common.entity.ai;

import me.whizvox.funwithquarries.common.entity.Drone;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

public class FlyToMoveTargetGoal extends Goal {

  private final Drone drone;

  public FlyToMoveTargetGoal(Drone drone) {
    this.drone = drone;
  }

  @Override
  public boolean canUse() {
    BlockPos targetPos = drone.getTargetPosition();
    return drone.getTargetType() == Drone.TargetType.MOVE && drone.position().distanceToSqr(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5) > 0.1;
  }

  @Override
  public void tick() {
    BlockPos targetPos = drone.getTargetPosition();
    drone.getMoveControl().setWantedPosition(targetPos.getX() + 0.5, targetPos.getY() + 0.1, targetPos.getZ() + 0.5, drone.getAttributeValue(Attributes.FLYING_SPEED));
  }

}
