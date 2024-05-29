package me.whizvox.funwithquarries.common.entity.ai.control;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.phys.Vec3;

public class DroneFlyingMoveControl extends MoveControl {

  private static final double
      MIN_DIST = 3,
      MIN_DIST_SQR = MIN_DIST * MIN_DIST;

  public DroneFlyingMoveControl(Mob mob) {
    super(mob);
  }

  @Override
  public void tick() {
    mob.setNoGravity(true);
    if (operation == Operation.MOVE_TO) {
      operation = Operation.WAIT;
      double deltaX = wantedX - mob.getX();
      double deltaY = wantedY - mob.getY();
      double deltaZ = wantedZ - mob.getZ();
      double distSqr = deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ;
      if (distSqr < Mth.EPSILON) {
        mob.setYya(0);
        mob.setZza(0);
      } else {
        float speed;
        // speed gets slower as mob approaches wanted point
        if (distSqr < 0.2) {
          mob.moveTo(wantedX, wantedY, wantedZ);
          mob.setDeltaMovement(0, 0, 0);
          return;
        }
        if (distSqr < MIN_DIST_SQR) {
          speed = (float) ((mob.getAttribute(Attributes.FLYING_SPEED).getValue() / MIN_DIST) * Math.sqrt(distSqr));
        } else {
          speed = (float) mob.getAttribute(Attributes.FLYING_SPEED).getValue();
        }
        mob.setDeltaMovement(new Vec3(deltaX, deltaY, deltaZ).normalize().scale(speed));
      }
    } else if (operation == Operation.WAIT) {
      mob.setYya(0);
      mob.setZza(0);
    }
  }

}
