package com.doubleos.yd.util;


import java.util.ArrayList;
import java.util.HashMap;

public class Variable {

    public int m_gamePoliceCount = 1;

    //싱글톤
    private Variable() {

    }

    private static class InnerInstanceGameVariableClazz {
        private static final Variable uniqueGameVariable = new Variable();
    }

    public static Variable Instance() {
        return InnerInstanceGameVariableClazz.uniqueGameVariable;
    }

    public ArrayList<String> m_gamePlayList = new ArrayList<>();


    public ArrayList<AnimationBroadCast> m_animationBroadcastList = new ArrayList<>();


    public ArrayList<Ping> m_teamNames = new ArrayList<>();


    public ArrayList<AnimationState> m_animationStateList = new ArrayList<>();

    public ArrayList<String> m_shopNameList = new ArrayList<>();

    public HashMap<String, Shop> m_shopNameToShopData = new HashMap<>();


    public String m_barType = "blue";
    public int m_mainTimer_min = 90;
    public int m_mainTimer_sec = 1;

    public int m_subTimer_min = 0;
    public int m_subTimer_sec = 0;

    public int m_money = 0;

    public int m_sur = 0;

    public boolean m_beeHitActive = false;

    public int mapNumber = 0;

    public float m_borderMiddlePosX = 0;
    public float m_borderMiddlePosZ = 0;

    public float m_borderSize = 0;

    public int m_KillAmount = 0;

    public float m_shrinkBorderSize = 0;




    public boolean checkShopNameExist(String name)
    {
        for(int i = 0; i<this.m_shopNameList.size(); i++)
        {
            if(this.m_shopNameList.get(i).equals(name))
                return true;
        }
        return false;
    }


}
