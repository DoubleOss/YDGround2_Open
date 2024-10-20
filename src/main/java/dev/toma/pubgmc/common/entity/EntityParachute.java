package dev.toma.pubgmc.common.entity;

import dev.toma.pubgmc.DevUtil;
import dev.toma.pubgmc.common.entity.controllable.EntityControllable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class EntityParachute extends EntityControllable {
    float turningSpeed;

    int emptyTicks;

    public EntityParachute(World world) {
        super(world);
    }

    public EntityParachute(World world, EntityLivingBase user) {
        this(world);
        setPosition(user.posX, user.posY, user.posZ);
        setRotation(user.rotationYaw, 0.0F);
        this.motionX = user.motionX;
        this.motionY = user.motionY;
        this.motionZ = user.motionZ;
    }

    public double func_70042_X() {
        return 0.5D;
    }

    public void updatePre() {
        if (!hasTurnInput()) {
            float rotationSpeedDrop = 1.0F;
            this.turningSpeed = (Math.abs(this.turningSpeed - rotationSpeedDrop) <= rotationSpeedDrop) ? 0.0F : ((this.turningSpeed > 0.0F) ? (this.turningSpeed - rotationSpeedDrop) : ((this.turningSpeed < 0.0F) ? (this.turningSpeed + rotationSpeedDrop) : 0.0F));
        }
        if (!hasMovementInput())
            this.rotationPitch = (Math.abs(this.rotationPitch - 1.0F) <= 1.0F) ? 0.0F : ((this.rotationPitch > 0.0F) ? (this.rotationPitch - 1.0F) : ((this.rotationPitch < 0.0F) ? (this.rotationPitch + 1.0F) : 0.0F));
        if (!isBeingRidden() && ++this.emptyTicks >= 100)
            setDead();
        this.rotationYaw += this.turningSpeed;
        if (isBeingRidden()) {
            if (this.onGround || this.collided) {
                removePassengers();
                return;
            }
            if (!isDeployed())
                return;
            this.fallDistance = 0.0F;
            Vec3d look = getLookVec();
            double x = (look.x / 2.0D) * 3.0f;
            double z = (look.z / 2.0D) * 3.0f;
            double speed = (1.0F + this.rotationPitch / 30.0F);
            this.motionX = x;
            this.motionY = (-0.25D * speed) * 0.8f;
            this.motionZ = z;
        } else {
            this.motionX = 0.0D;
            this.motionY = 0.0D;
            this.motionZ = 0.0D;
        }
    }

    public void updatePost() {
        move(MoverType.SELF, this.motionX, this.motionY, this.motionZ);
        if (!isBeingRidden())
            reset();
    }

    public void handleForward() {
        if (isDeployed())
            this.rotationPitch = DevUtil.wrap(this.rotationPitch + 1.5F, -15.0F, 30.0F);
    }

    public void handleBackward() {
        if (isDeployed())
            this.rotationPitch = DevUtil.wrap(this.rotationPitch - 1.0F, -15.0F, 30.0F);
    }

    public void handleRight() {
        if (isDeployed())
            this.turningSpeed = DevUtil.wrap(this.turningSpeed + 1.0F, -5.0F, 5.0F);
    }

    public void handleLeft() {
        if (isDeployed())
            this.turningSpeed = DevUtil.wrap(this.turningSpeed - 1.0F, -5.0F, 5.0F);
    }

    public boolean isInRangeToRenderDist(double distance) {
        return (distance < 256.0D);
    }

    public boolean shouldRiderSit() {
        return false;
    }

    boolean isDeployed() {
        return (this.ticksExisted > 20);
    }

    public int getEmptyTicks() {
        return this.emptyTicks;
    }
}