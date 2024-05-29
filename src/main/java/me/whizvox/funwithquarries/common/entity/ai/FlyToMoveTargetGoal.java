package me.whizvox.funwithquarries.common.entity.ai;

import me.whizvox.funwithquarries.common.entity.Drone;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class FlyToMoveTargetGoal extends Goal {

  private final Drone drone;

  public FlyToMoveTargetGoal(Drone drone) {
    this.drone = drone;
  }

  @Override
  public boolean canUse() {
    return drone.getTargetType() == Drone.TargetType.NONE && drone.shouldMove();
  }

  @Override
  public void tick() {
    if (drone.isAtMovePosition()) {
      drone.removeMovePosition();
    } else {
      Vec3 movePos = drone.getMovePosition().getCenter();
      drone.getMoveControl().setWantedPosition(movePos.x, movePos.y - 0.4, movePos.z, drone.getAttributeValue(Attributes.FLYING_SPEED));
    }
  }

}
