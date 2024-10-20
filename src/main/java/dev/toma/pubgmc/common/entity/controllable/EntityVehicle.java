package dev.toma.pubgmc.common.entity.controllable;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import dev.toma.pubgmc.Pubgmc;
import dev.toma.pubgmc.config.common.CFGVehicle;
import dev.toma.pubgmc.init.PMCDamageSources;
import dev.toma.pubgmc.init.PMCSounds;
import io.netty.buffer.ByteBuf;
import java.util.List;
import javax.annotation.Nullable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public abstract class EntityVehicle extends EntityControllable implements IEntityAdditionalSpawnData {
    private static final Predicate<Entity> TARGET = Predicates.and( EntitySelectors.NOT_SPECTATING, EntitySelectors.IS_ALIVE, Entity::canBeCollidedWith);

    private static final AxisAlignedBB BOX = new AxisAlignedBB(-0.5D, 0.0D, -0.5D, 1.5D, 1.0D, 1.5D);

    public float health;

    public float currentSpeed;

    public float turnModifier;

    public float fuel;

    public boolean isBroken = false;

    private short timeInInvalidState;

    public EntityVehicle(World world) {
        super(world);
        this.stepHeight = 1.0F;
        this.preventEntitySpawning = true;
        this.health = (getVehicleConfiguration()).maxHealth.getAsFloat();

        this.fuel = (60 + world.rand.nextInt(40));
    }

    public EntityVehicle(World world, int x, int y, int z) {
        this(world);
        setPosition(x, y, z);
    }

    public abstract int getMaximumCapacity();

    public abstract Vec3d getEnginePosition();

    public abstract Vec3d getExhaustPosition();

    public abstract SoundEvent vehicleSound();

    public abstract CFGVehicle getVehicleConfiguration();

    public void updatePre() {
        handleEmptyInputs();
        Vec3d look = getLookVec();
        this.motionX = look.x * this.currentSpeed;
        this.motionZ = look.z * this.currentSpeed;
        if (this.currentSpeed != 0.0F)
            this.rotationYaw += (this.currentSpeed > 0.0F) ? this.turnModifier : -this.turnModifier;
        if (!isBeingRidden() && (!hasMovementInput() || !hasTurnInput() || !hasFuel() || this.isBroken))
            reset();
        handleEntityCollisions();
        checkState();
        if (this.collidedHorizontally)
            this.currentSpeed = (float)(this.currentSpeed * 0.6D);
    }

    public void updatePost() {
        move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        playSoundAtVehicle();
        spawnParticles();
    }

    protected void handleEmptyInputs() {
        CFGVehicle stats = getVehicleConfiguration();
        if (!hasMovementInput() || !hasFuel()) {
            if (Math.abs(this.currentSpeed) < 0.01D)
                this.currentSpeed = 0.0F;
            if (this.currentSpeed != 0.0F)
                this.currentSpeed = (this.currentSpeed > 0.0F) ? (this.currentSpeed - stats.acceleration.getAsFloat() * 0.1F) : (this.currentSpeed + stats.acceleration.getAsFloat() * 0.1F);
        }
        if (!hasTurnInput()) {
            if (Math.abs(this.turnModifier) <= 0.5F)
                this.turnModifier = 0.0F;
            if (this.turnModifier != 0.0F)
                this.turnModifier = (this.turnModifier > 0.0F) ? (this.turnModifier - 0.5F) : (this.turnModifier + 0.5F);
        }
        if (!this.onGround)
            this.motionY -= 0.1D;
    }

    public void handleEntityCollisions() {
        Vec3d vec1 = new Vec3d(this.posX, this.posY, this.posZ);
        Vec3d vec2 = new Vec3d(vec1.x + this.motionX, vec1.y + this.motionY, vec1.z + this.motionZ);
        Entity e = findEntityInPath(vec1, vec2);
        if (e != null) {
            e.motionX += this.motionX * this.currentSpeed * 3.0D;
            e.motionY += this.currentSpeed;
            e.motionZ += this.motionZ * this.currentSpeed * 3.0D;
            e.attackEntityFrom(PMCDamageSources.VEHICLE, Math.abs(this.currentSpeed) * 15.0F);
        }
    }

    public void handleForward() {
        if (!this.isBroken) {
            CFGVehicle cfg = getVehicleConfiguration();
            float max = cfg.maxSpeed.getAsFloat() * 0.5f;
            if (hasFuel() || this.currentSpeed < 0.0F) {
                burnFuel();
                this.currentSpeed = (this.currentSpeed < max) ? (this.currentSpeed + cfg.acceleration.getAsFloat()) : max;
            }
        }
    }

    public void handleBackward() {
        if (!this.isBroken) {
            CFGVehicle cfg = getVehicleConfiguration();
            if (this.currentSpeed > 0.0F) {
                this.currentSpeed -= cfg.acceleration.getAsFloat();
            } else if (hasFuel()) {
                burnFuel();
                float reverseMax = -cfg.maxSpeed.getAsFloat() * 0.3F;
                this.currentSpeed = (this.currentSpeed > reverseMax) ? (this.currentSpeed - 0.02F) : reverseMax;
            }
        }
    }

    public void handleRight() {
        if (!this.isBroken) {
            CFGVehicle cfg = getVehicleConfiguration();
            float max = cfg.maxTurningAngle.getAsFloat();
            float partial = cfg.turningSpeed.getAsFloat();
            this.turnModifier = (this.turnModifier < max) ? (this.turnModifier + partial) : max;
        }
    }

    public void handleLeft() {
        if (!this.isBroken) {
            CFGVehicle cfg = getVehicleConfiguration();
            float max = cfg.maxTurningAngle.getAsFloat();
            float partial = cfg.turningSpeed.getAsFloat();
            this.turnModifier = (this.turnModifier > -max) ? (this.turnModifier - partial) : -max;
        }
    }

    @Nullable
    protected Entity findEntityInPath(Vec3d start, Vec3d end) {
        Entity e = null;
        List<Entity> entityList = this.world.getEntitiesInAABBexcluding(this, getEntityBoundingBox().expand(this.motionX, this.motionY + 1.0D, this.motionZ).grow(1.0D), TARGET);
        double d0 = 0.0D;
        for (int i = 0; i < entityList.size(); i++) {
            Entity entity = entityList.get(i);
            if (entity != this && !isPassenger(entity)) {
                AxisAlignedBB axisalignedbb = entity.getEntityBoundingBox().grow(0.30000001192092896D);
                RayTraceResult raytraceresult = axisalignedbb.calculateIntercept(start, end);
                if (raytraceresult != null) {
                    double d1 = start.squareDistanceTo(raytraceresult.hitVec);
                    if (d1 < d0 || d0 == 0.0D) {
                        e = entity;
                        d0 = d1;
                    }
                }
            }
        }
        return e;
    }

    public boolean func_184230_a(EntityPlayer player, EnumHand hand) {
        if (!this.world.isRemote &&
                canBeRidden((Entity)player) && func_184219_q((Entity)player))
            player.startRiding(this);
        return true;
    }

    private void explode() {
        if (!this.world.isRemote) {
            this.world.createExplosion(this, this.posX, this.posY, this.posZ, 3.0F, false);
            setDead();
        }
    }

    protected void checkState() {
        if (isInWater() && this.world.getBlockState(getPosition().up()).getMaterial().isLiquid()) {
            this.timeInInvalidState = (short)(this.timeInInvalidState + 1);
            this.motionX *= 0.4D;
            this.motionZ *= 0.4D;
            this.motionY = -0.15D;
        }
        if (this.timeInInvalidState > 30)
            this.isBroken = true;
        if (isInLava() || this.health <= 0.0F)
            explode();
    }

    protected void spawnParticles() {
        float max = (getVehicleConfiguration()).maxHealth.getAsFloat();
        if (this.world.isRemote) {
            if (this.health / max <= 0.35F) {
                Vec3d engineVec = (new Vec3d((getEnginePosition()).x, (getEnginePosition()).y + 0.25D, (getEnginePosition()).z)).rotateYaw(-this.rotationYaw * 0.017453292F - 1.5707964F);
                this.world.spawnParticle(EnumParticleTypes.SMOKE_LARGE, true, this.posX + engineVec.x, this.posY + engineVec.y, this.posZ + engineVec.z, 0.0D, 0.1D, 0.0D, new int[0]);
                if (this.health / max <= 0.15F) {
                    double rngX = (this.rand.nextDouble() - this.rand.nextDouble()) * 0.1D;
                    double rngZ = (this.rand.nextDouble() - this.rand.nextDouble()) * 0.1D;
                    this.world.spawnParticle(EnumParticleTypes.FLAME, true, this.posX + engineVec.x, this.posY + engineVec.y - 0.2D, this.posZ + engineVec.z, rngX, 0.02D, rngZ, new int[0]);
                }
            }
            if (!this.isBroken && hasFuel()) {
                Vec3d exhaustVec = (new Vec3d((getExhaustPosition()).x, (getExhaustPosition()).y + 0.25D, (getExhaustPosition()).z)).rotateYaw(-this.rotationYaw * 0.017453292F - 1.5707964F);
                this.world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, true, this.posX + exhaustVec.x, this.posY + exhaustVec.y, this.posZ + exhaustVec.z, 0.0D, 0.02D, 0.0D, new int[0]);
            }
            if (this.isBroken) {
                Vec3d engine = (new Vec3d((getEnginePosition()).x, (getEnginePosition()).y, (getEnginePosition()).z)).rotateYaw(-this.rotationYaw * 0.017453292F - 1.5707964F);
                this.world.spawnParticle(EnumParticleTypes.CLOUD, true, this.posX + engine.x, this.posY + engine.y, this.posZ + engine.z, 0.0D, 0.05D, 0.0D, new int[0]);
            }
        }
    }

    public boolean func_70097_a(DamageSource source, float amount) {
        if (!getPassengers().contains(source.getTrueSource()))
            this.health -= amount;
        return true;
    }

    public void func_184232_k(Entity passenger) {
        if (isPassenger(passenger)) {
            float x = 0.0F;
            float z = -0.55F;
            float f1 = (float)((this.isDead ? 0.009999999776482582D : getMountedYOffset()) + passenger.getYOffset());
            if (getPassengers().size() > 0) {
                int i = getPassengers().indexOf(passenger);
                x = getPassengerXOffset(i);
                z = getPassengerZOffset(i);
            }
            Vec3d vec3d = (new Vec3d(x, 0.0D, z)).rotateYaw(-this.rotationYaw * 0.017453292F - 1.5707964F);
            passenger.setPosition(this.posX + vec3d.x, this.posY + f1, this.posZ + vec3d.z);
            if (passenger instanceof EntityAnimal && getPassengers().size() > 1) {
                int j = (passenger.getEntityId() % 2 == 0) ? 90 : 270;
                passenger.setRenderYawOffset(((EntityAnimal)passenger).renderYawOffset + j);
                passenger.setRotationYawHead(passenger.getRotationYawHead() + j);
            }
        }
    }

    protected boolean func_184219_q(Entity passenger) {
        return (getPassengers().size() < getMaximumCapacity());
    }

    public boolean func_70112_a(double distance) {
        return true;
    }

    public boolean isPlayerDriver(EntityPlayer player) {
        return (player.isRiding() && player.getRidingEntity() instanceof EntityVehicle && player.getRidingEntity().getPassengers().get(0) == player);
    }

    private boolean isVehicleMoving() {
        return (this.currentSpeed != 0.0F);
    }

    private boolean isVehicleMovingForward() {
        return (this.currentSpeed > 0.0F);
    }

    private boolean isVehicleMovingBackward() {
        return (this.currentSpeed < 0.0F);
    }

    private void playSoundAtVehicle() {
        if (!isVehicleMoving()) {
            if (this.ticksExisted % 5 == 0)
                playSound(PMCSounds.vehicleIdle, 3.0F, 1.0F);
        } else if (this.ticksExisted % 18 == 0) {
            playSound(vehicleSound(), this.currentSpeed * 6.0F, 1.0F);
        }
    }

    protected void func_70088_a() {}

    protected void func_70037_a(NBTTagCompound compound) {
        this.health = compound.getFloat("health");
        this.fuel = compound.getFloat("fuel");
        this.currentSpeed = compound.getFloat("speed");
        this.isBroken = compound.getBoolean("isBroken");
    }

    protected void func_70014_b(NBTTagCompound compound) {
        compound.setFloat("health", this.health);
        compound.setFloat("fuel", this.fuel);
        compound.setFloat("speed", this.currentSpeed);
        compound.setBoolean("isBroken", this.isBroken);
    }

    public boolean func_70067_L() {
        return true;
    }

    protected float getPassengerXOffset(int passengerIndex) {
        return (passengerIndex % 2 == 0) ? 0.5F : -0.5F;
    }

    protected float getPassengerZOffset(int passengerIndex) {
        return (passengerIndex < 2) ? -0.55F : 0.55F;
    }

    protected void applyYawToEntity(Entity entityToUpdate) {
        entityToUpdate.setRenderYawOffset(this.rotationYaw);
        float f = MathHelper.wrapDegrees(entityToUpdate.rotationYaw - this.rotationYaw);
        float f1 = MathHelper.clamp(f, -105.0F, 105.0F);
        entityToUpdate.prevRotationYaw += f1 - f;
        entityToUpdate.rotationYaw += f1 - f;
        entityToUpdate.setRotationYawHead(entityToUpdate.rotationYaw);
    }

    public boolean hasFuel() {
        return (this.fuel > 0.0F);
    }

    public void refill(EntityPlayer source) {
        if (getPassengers().contains(source)) {
            this.fuel = (this.fuel + 30.0F < 100.0F) ? (this.fuel + 30.0F) : 100.0F;
        } else {
            Pubgmc.logger.warn("{} has attempted to refuel vehicle with ID {}, but he wasn't inside the vehicle!", source, Integer.valueOf(getEntityId()));
        }
    }

    public void burnFuel() {
        this.fuel = hasFuel() ? (this.fuel - 0.01F) : 0.0F;
    }

    protected float generateFuel() {
        return 60.0F + this.rand.nextInt(40);
    }

    public void writeSpawnData(ByteBuf buf) {
        buf.writeFloat(this.health);
        buf.writeFloat(this.fuel);
    }

    public void readSpawnData(ByteBuf buf) {
        this.health = buf.readFloat();
        this.fuel = buf.readFloat();
    }
}
