package com.doubleos.yd.packet;


import com.doubleos.yd.util.AnimationState;
import com.doubleos.yd.util.Variable;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CPacketFadeEffectAdd implements IMessage, IMessageHandler<CPacketFadeEffectAdd, CPacketFadeEffectAdd>
{


    float m_animationTime = 1;
    float m_animationDelay = 1;

    float m_xPos = 0f;
    float m_yPos = 0f;
    float m_zPos = 0f;

    public CPacketFadeEffectAdd(){}
    public CPacketFadeEffectAdd(float animationTime, float animationDelay, float x, float y, float z)
    {
        m_animationTime = animationTime;
        m_animationDelay = animationDelay;
        m_xPos = x;
        m_yPos = y;
        m_zPos = z;

    }
    @Override
    public void fromBytes(ByteBuf buf)
    {

        m_animationTime = buf.readFloat();
        m_animationDelay = buf.readFloat();
        m_xPos = buf.readFloat();
        m_yPos = buf.readFloat();
        m_zPos = buf.readFloat();


    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeFloat(m_animationTime);
        buf.writeFloat(m_animationDelay);
        buf.writeFloat(m_xPos);
        buf.writeFloat(m_yPos);
        buf.writeFloat(m_zPos);

    }

    @Override
    public CPacketFadeEffectAdd onMessage(CPacketFadeEffectAdd message, MessageContext ctx)
    {
        Variable variable = Variable.Instance();
        AnimationState state = new AnimationState(message.m_animationTime, message.m_animationDelay);
        state.m_teleportXPos = message.m_xPos;
        state.m_teleportYPos = message.m_yPos;
        state.m_teleportZPos = message.m_zPos;

        if(variable.m_animationStateList.size() == 0)
            variable.m_animationStateList.add(state);
        else {
            for(int i = 0; i<variable.m_animationStateList.size(); i++)
            {
                AnimationState checkState = variable.m_animationStateList.get(i);

                if(checkState.m_teleportXPos == message.m_xPos && checkState.m_teleportYPos == message.m_yPos && checkState.m_teleportZPos == message.m_zPos)
                    break;
                if(i == variable.m_animationStateList.size())
                {
                    variable.m_animationStateList.add(state);
                }

            }
        }


        //System.out.println("페이드 인 시작 " + LocalTime.now());
        return null;
    }
}
