package de.arcane_artistry.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.Direction;

public abstract class CornerBlock extends Block {
  public static final BooleanProperty UP = Properties.UP;
  public static final BooleanProperty NORTH = Properties.NORTH;
  public static final BooleanProperty EAST = Properties.EAST;

  private static final int FACING_PROPERTIES_START_INDEX = 3;
  private static final int FACING_PROPERTIES_END_INDEX = 6;

  public CornerBlock(Settings settings) {
    super(settings);
    setDefaultState(stateManager.getDefaultState().with(UP, true).with(NORTH, true).with(EAST, true));
  }

  @Override
  protected void appendProperties(Builder<Block, BlockState> builder) {
    builder.add(UP, NORTH, EAST);
  }

  @Override
  public BlockState getPlacementState(ItemPlacementContext ctx) {
    Direction[] facingDirections = Direction.getEntityFacingOrder(ctx.getPlayer());
    BlockState state = getDefaultState();
    for (int i = FACING_PROPERTIES_START_INDEX; i < FACING_PROPERTIES_END_INDEX; i++) {
      state = updateCornerState(state, facingDirections[i]);
    }
    return state;
  }

  @Override
  public BlockState rotate(BlockState state, BlockRotation rotation) {
    return switch (rotation) {
      case NONE -> state;
      case CLOCKWISE_90 -> state.get(NORTH) && state.get(EAST) ? state.with(NORTH, !state.get(NORTH))
          : state.with(EAST, !state.get(EAST));
      case COUNTERCLOCKWISE_90 -> state.get(NORTH) && state.get(EAST) ? state.with(EAST, !state.get(EAST))
          : state.with(NORTH, !state.get(NORTH));
      case CLOCKWISE_180 -> state.with(NORTH, !state.get(NORTH)).with(EAST, !state.get(EAST));
      default -> state;
    };
  }

  @Override
  public BlockState mirror(BlockState state, BlockMirror mirror) {
    return switch (mirror) {
      case NONE -> state;
      case FRONT_BACK -> state.with(EAST, !state.get(EAST));
      case LEFT_RIGHT -> state.with(NORTH, !state.get(NORTH));
      default -> state;
    };
  }

  private static BlockState updateCornerState(BlockState state, Direction facing) {
    return switch (facing) {
      case NORTH -> state.with(NORTH, true);
      case SOUTH -> state.with(NORTH, false);
      case EAST -> state.with(EAST, true);
      case WEST -> state.with(EAST, false);
      case UP -> state.with(UP, true);
      case DOWN -> state.with(UP, false);
      default -> state;
    };
  }
}
