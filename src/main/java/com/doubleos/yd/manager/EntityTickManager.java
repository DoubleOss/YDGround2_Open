package com.doubleos.yd.manager;

import javax.swing.text.html.parser.Entity;
import java.util.ArrayList;
import java.util.HashMap;

public class EntityTickManager
{

    private EntityTickManager(){}

    private static class InnerInstanceGameVariableClazz
    {
        private static final EntityTickManager uniqueEntityTickManager = new EntityTickManager();
    }

    public static EntityTickManager Instance()
    {
        return InnerInstanceGameVariableClazz.uniqueEntityTickManager;
    }


    ArrayList<Entity> m_entityTickList = new ArrayList<Entity>();

    HashMap<String, Entity> m_hashEntity = new HashMap<>();



    public ArrayList<Entity> getEntityList()
    {
        return m_entityTickList;
    }
    public Entity getTickListGetEntity(Entity entity)
    {
        for (Entity list_entity : m_entityTickList)
        {
            if (list_entity.equals(entity))
            {
                return list_entity;
            }
        }
        return null;
    }

    public void addEntityTickList(Entity entity)
    {
        m_entityTickList.add(entity);
    }




}

