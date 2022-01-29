package com.matchewman023.beaconarmor.block;

import com.matchewman023.beaconarmor.BeaconArmor;
import com.matchewman023.beaconarmor.block.entity.ImbuingStationEntity;
import com.matchewman023.beaconarmor.registry.Register;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.stream.Stream;

public class ImbuingStation extends BlockWithEntity {
    public static final IntProperty FRAME = IntProperty.of("frame", 0, 7);

    private static final VoxelShape SHAPE = Stream.of(
            Block.createCuboidShape(5, 1, 5, 6, 11, 6),
            Block.createCuboidShape(6, 1, 4, 10, 11, 5),
            Block.createCuboidShape(10, 1, 5, 11, 11, 6),
            Block.createCuboidShape(11, 1, 6, 12, 11, 10),
            Block.createCuboidShape(10, 1, 10, 11, 11, 11),
            Block.createCuboidShape(6, 1, 11, 10, 11, 12),
            Block.createCuboidShape(5, 1, 10, 6, 11, 11),
            Block.createCuboidShape(4, 1, 6, 5, 11, 10),
            Block.createCuboidShape(5, 13, 1, 11, 14, 15),
            Block.createCuboidShape(1, 13, 5, 2, 14, 11),
            Block.createCuboidShape(2, 13, 4, 3, 14, 12),
            Block.createCuboidShape(3, 13, 3, 4, 14, 13),
            Block.createCuboidShape(4, 13, 2, 5, 14, 14),
            Block.createCuboidShape(14, 13, 5, 15, 14, 11),
            Block.createCuboidShape(11, 13, 2, 12, 14, 14),
            Block.createCuboidShape(12, 13, 3, 13, 14, 13),
            Block.createCuboidShape(13, 13, 4, 14, 14, 12),
            Block.createCuboidShape(13, 12, 6, 14, 13, 10),
            Block.createCuboidShape(12, 12, 4, 13, 13, 12),
            Block.createCuboidShape(10, 12, 3, 12, 13, 13),
            Block.createCuboidShape(6, 12, 2, 10, 13, 14),
            Block.createCuboidShape(4, 12, 3, 6, 13, 13),
            Block.createCuboidShape(3, 12, 4, 4, 13, 12),
            Block.createCuboidShape(2, 12, 6, 3, 13, 10),
            Block.createCuboidShape(5, 14, 0, 11, 16, 1),
            Block.createCuboidShape(10, 14, 1, 12, 16, 2),
            Block.createCuboidShape(12, 14, 1, 13, 16, 3),
            Block.createCuboidShape(13, 14, 2, 14, 16, 4),
            Block.createCuboidShape(14, 14, 3, 15, 16, 6),
            Block.createCuboidShape(15, 14, 5, 16, 16, 11),
            Block.createCuboidShape(14, 14, 10, 15, 16, 12),
            Block.createCuboidShape(13, 14, 12, 15, 16, 13),
            Block.createCuboidShape(12, 14, 13, 14, 16, 14),
            Block.createCuboidShape(10, 14, 14, 13, 16, 15),
            Block.createCuboidShape(4, 14, 14, 6, 16, 15),
            Block.createCuboidShape(5, 14, 15, 11, 16, 16),
            Block.createCuboidShape(3, 14, 13, 4, 16, 15),
            Block.createCuboidShape(2, 14, 12, 3, 16, 14),
            Block.createCuboidShape(1, 14, 10, 2, 16, 13),
            Block.createCuboidShape(0, 14, 5, 1, 16, 11),
            Block.createCuboidShape(1, 14, 4, 2, 16, 6),
            Block.createCuboidShape(1, 14, 3, 3, 16, 4),
            Block.createCuboidShape(2, 14, 2, 4, 16, 3),
            Block.createCuboidShape(3, 14, 1, 6, 16, 2),
            Block.createCuboidShape(6, 11, 3, 10, 12, 13),
            Block.createCuboidShape(12, 11, 6, 13, 12, 10),
            Block.createCuboidShape(4, 11, 4, 6, 12, 12),
            Block.createCuboidShape(3, 11, 6, 4, 12, 10),
            Block.createCuboidShape(10, 11, 4, 12, 12, 12),
            Block.createCuboidShape(6, 0, 12, 10, 1, 13),
            Block.createCuboidShape(6, 0, 3, 10, 1, 4),
            Block.createCuboidShape(4, 0, 4, 12, 1, 6),
            Block.createCuboidShape(3, 0, 6, 13, 1, 10),
            Block.createCuboidShape(4, 0, 10, 12, 1, 12),
            Block.createCuboidShape(1, 14, 6, 2, 15, 10),
            Block.createCuboidShape(2, 14, 4, 3, 15, 12),
            Block.createCuboidShape(3, 14, 3, 4, 15, 13),
            Block.createCuboidShape(4, 14, 2, 6, 15, 14),
            Block.createCuboidShape(6, 14, 1, 10, 15, 15),
            Block.createCuboidShape(10, 14, 2, 12, 15, 14),
            Block.createCuboidShape(12, 14, 3, 13, 15, 13),
            Block.createCuboidShape(13, 14, 4, 14, 15, 12),
            Block.createCuboidShape(14, 14, 6, 15, 15, 10)
    ).reduce((v1, v2) -> VoxelShapes.combineAndSimplify(v1, v2, BooleanBiFunction.OR)).get();

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    public ImbuingStation(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(FRAME, 0));
    }

    @Override
    public BlockEntity createBlockEntity (BlockPos pos, BlockState state) {
        return new ImbuingStationEntity(pos, state);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult blockHitResult) {
        if (!world.isClient) {
            NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);

            if (screenHandlerFactory != null) {
                player.openHandledScreen(screenHandlerFactory);
            }
        }
        return ActionResult.SUCCESS;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FRAME);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return checkType(type, Register.IMBUING_STATION_ENTITY, (world1, pos, state1, be) -> ImbuingStationEntity.tick(world, pos, state, this));
    }

    public static void updateFrame(World world, BlockPos pos, BlockState state, int frame) {
        if (!world.isClient()) world.setBlockState(pos, state.with(FRAME, frame));
    }
}
