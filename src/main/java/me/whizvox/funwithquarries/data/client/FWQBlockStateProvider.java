package me.whizvox.funwithquarries.data.client;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.block.DroneStationBlock;
import me.whizvox.funwithquarries.common.block.QuarryControllerBlock;
import me.whizvox.funwithquarries.common.block.QuarryFrameBlock;
import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.Map;

public class FWQBlockStateProvider extends BlockStateProvider {

  private final ResourceLocation machineSide;

  public FWQBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, FunWithQuarries.MOD_ID, exFileHelper);

    machineSide = modLoc("block/machine_side");
  }

  private void registerQuarryController() {
    Map<QuarryControllerBlock.State, ModelFile> models = new HashMap<>();
    var partialState = getVariantBuilder(FWQBlocks.QUARRY_CONTROLLER.get()).partialState();
    for (QuarryControllerBlock.State state : QuarryControllerBlock.State.values()) {
      for (Direction direction : HorizontalDirectionalBlock.FACING.getPossibleValues()) {
        ModelFile model = models.computeIfAbsent(state, s ->  models().orientable("quarry_controller_" + s.getSerializedName(), machineSide, modLoc("block/quarry_controller_" + s.getSerializedName()), machineSide));
        partialState
            .with(QuarryControllerBlock.STATE, state)
            .with(QuarryControllerBlock.FACING, direction.getOpposite())
            .modelForState()
            .modelFile(model)
            .rotationY((int) direction.toYRot())
            .addModel();
      }
    }
    simpleBlockItem(FWQBlocks.QUARRY_CONTROLLER.get(), models.get(QuarryControllerBlock.State.OFF));
  }

  private void registerDroneStation() {
    ModelFile closed = models().orientable("drone_station_closed", machineSide, modLoc("block/drone_station_closed"), machineSide);
    ModelFile open = models().orientable("drone_station_open", machineSide, modLoc("block/drone_station_open"), machineSide);
    var partialState = getVariantBuilder(FWQBlocks.DRONE_STATION.get()).partialState();
    for (Direction direction : Direction.values()) {
      int xRot, yRot;
      switch (direction) {
        case UP -> {
          xRot = 90;
          yRot = 0;
        }
        case DOWN -> {
          xRot = 270;
          yRot = 0;
        }
        default -> {
          xRot = 0;
          yRot = (int) direction.toYRot();
        }
      }
      partialState
          .with(DroneStationBlock.OPEN, false)
          .with(DroneStationBlock.FACING, direction.getOpposite())
          .modelForState()
          .modelFile(closed)
          .rotationX(xRot)
          .rotationY(yRot)
          .addModel();
      partialState
          .with(DroneStationBlock.OPEN, true)
          .with(DroneStationBlock.FACING, direction.getOpposite())
          .modelForState()
          .modelFile(open)
          .rotationX(xRot)
          .rotationY(yRot)
          .addModel();
    }
    simpleBlockItem(FWQBlocks.DRONE_STATION.get(), closed);
  }

  private void registerQuarryFrame() {
    ModelFile center = models().getExistingFile(modLoc("block/quarry_frame/center"));
    ModelFile connectorX = models().getExistingFile(modLoc("block/quarry_frame/connector_x"));
    ModelFile connectorY = models().getExistingFile(modLoc("block/quarry_frame/connector_y"));
    ModelFile connectorZ = models().getExistingFile(modLoc("block/quarry_frame/connector_z"));
    getMultipartBuilder(FWQBlocks.QUARRY_FRAME.get())
        .part().modelFile(center).addModel().end()
        .part().modelFile(connectorZ).rotationY(0).addModel().condition(QuarryFrameBlock.NORTH, true).end()
        .part().modelFile(connectorZ).rotationY(180).addModel().condition(QuarryFrameBlock.SOUTH, true).end()
        .part().modelFile(connectorX).rotationY(180).addModel().condition(QuarryFrameBlock.EAST, true).end()
        .part().modelFile(connectorX).rotationY(0).addModel().condition(QuarryFrameBlock.WEST, true).end()
        .part().modelFile(connectorY).rotationX(0).addModel().condition(QuarryFrameBlock.UP, true).end()
        .part().modelFile(connectorY).rotationX(180).addModel().condition(QuarryFrameBlock.DOWN, true).end();
    simpleBlockItem(FWQBlocks.QUARRY_FRAME.get(), center);
  }

  @Override
  protected void registerStatesAndModels() {
    registerQuarryController();
    registerDroneStation();
    registerQuarryFrame();
    simpleBlockWithItem(FWQBlocks.MACHINE_BLOCK.get(), models().cubeAll("machine_block", modLoc("block/machine_side")));
  }

}
