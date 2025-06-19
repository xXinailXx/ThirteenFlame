package net.xXinailXx.thirteen_flames.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.xXinailXx.enderdragonlib.utils.MathUtils;
import net.xXinailXx.thirteen_flames.init.EntityRegistry;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

public class LivingFleshEntity extends Mob implements IAnimatable {
    private static final EntityDataAccessor<Integer> SIZE = SynchedEntityData.defineId(LivingFleshEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PROC = SynchedEntityData.defineId(LivingFleshEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> PIECE = SynchedEntityData.defineId(LivingFleshEntity.class, EntityDataSerializers.INT);
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private ItemStack stack;
    private boolean isMove;
    private Vec3 lastPos;

    public LivingFleshEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    public LivingFleshEntity(Level level, int size, int procSeparation, int piece, ItemStack stack) {
        super(EntityRegistry.LIVING_FLESH.get(), level);
        setSize(size * 10);
        setProcSeparation(procSeparation);
        setPiece(piece);
    }

    public void die(DamageSource source) {
        if (getSize() / 10 > 1) {
            if (this.getSize() > 1 && MathUtils.isRandom(this.level, this.getProcSeparation())) {
                for (int i = 0; i < this.level.getRandom().nextInt(1, this.getPiece()); i++) {
                    LivingFleshEntity entity = new LivingFleshEntity(this.level, getSize() / 10 - 1, this.getProcSeparation(), this.getPiece(), this.getStack());
                    entity.setPos(this.position());
                    this.level.addFreshEntity(entity);
                }
            }
        }

        super.die(source);
    }

    public void tick() {
        double moved = Math.sqrt(position().distanceToSqr(xo, yo, zo));

        if (moved > 0)
            this.isMove = true;
        else
            this.isMove = false;

        super.tick();
    }

    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier setAttrebutes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.ATTACK_DAMAGE, 2.5)
                .add(Attributes.ATTACK_SPEED, 0.3)
                .add(Attributes.MOVEMENT_SPEED, 0.1)
                .build();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (isMove)
            event.getController().setAnimation(new AnimationBuilder().addAnimation("move", true));
        else
            event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", true));

        return PlayState.CONTINUE;
    }

    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<LivingFleshEntity>(this, "controller", 0, this::predicate));
    }

    public AnimationFactory getFactory() {
        return factory;
    }

    public int getPiece() {
        return this.getEntityData().get(PIECE);
    }

    public void setPiece(int piece) {
        this.getEntityData().set(PIECE, piece);
    }

    public int getProcSeparation() {
        return this.getEntityData().get(PROC);
    }

    public void setProcSeparation(int procSeparation) {
        this.getEntityData().set(PROC, procSeparation);
    }

    public int getSize() {
        return this.getEntityData().get(SIZE);
    }

    public void setSize(int size) {
        this.getEntityData().set(SIZE, size);
    }

    public ItemStack getStack() {
        return stack;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setSize(compound.getInt("size"));
        this.setProcSeparation(compound.getInt("procent"));
        this.setPiece(compound.getInt("piece"));
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("size", this.getSize());
        compound.putInt("procent", this.getProcSeparation());
        compound.putInt("piece", this.getPiece());
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SIZE, 1);
        this.entityData.define(PROC, 5);
        this.entityData.define(PIECE, 1);
    }

    public Packet<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
