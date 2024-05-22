package me.whizvox.funwithquarries.data.client;

import me.whizvox.funwithquarries.FunWithQuarries;
import me.whizvox.funwithquarries.common.block.QuarryControllerBlock;
import me.whizvox.funwithquarries.common.block.QuarryFrameBlock;
import me.whizvox.funwithquarries.common.registry.FWQBlocks;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.HashMap;
import java.util.Map;

public class FWQBlockStateProvider extends BlockStateProvider {

  public FWQBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
    super(output, FunWithQuarries.MOD_ID, exFileHelper);
  }

  private void registerQuarryController() {
    Map<QuarryControllerBlock.State, ModelFile> models = new HashMap<>();
    var builder = getVariantBuilder(FWQBlocks.QUARRY_CONTROLLER.get()).partialState();
    for (QuarryControllerBlock.State state : QuarryControllerBlock.State.values()) {
      for (Direction direction : HorizontalDirectionalBlock.FACING.getPossibleValues()) {
        ModelFile model = models.computeIfAbsent(state, s ->  models().orientable("quarry_controller_" + s.getSerializedName(), modLoc("block/machine_side"), modLoc("block/quarry_controller_" + s.getSerializedName()), modLoc("block/machine_side")));
        builder
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
    registerQuarryFrame();
    simpleBlockWithItem(FWQBlocks.MACHINE_BLOCK.get(), models().cubeAll("machine_block", modLoc("block/machine_side")));
  }

}
