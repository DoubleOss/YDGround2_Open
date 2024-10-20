package com.doubleos.yd.util;

import net.minecraft.item.ItemStack;

public class AnimationBroadCast
{


    public AnimationBroadCast(String killer, String victim)
    {
        m_killerName = killer;
        m_victimName = victim;
    }
    public String m_killerName = "";
    public String m_victimName = "";


    public ItemStack m_stack = ItemStack.EMPTY;

    public float m_currentAnimationFrame = 0f;
    public float m_maxAnimationFrame = 100f;

    public float m_curreentAniYPosPer = 0f;


    public boolean m_animationShow = false;
    public boolean m_animationPlay = false;

    public boolean m_animationOpen = true;
    public boolean m_animationClose = false;

    public boolean m_animationDelay = false;






}
