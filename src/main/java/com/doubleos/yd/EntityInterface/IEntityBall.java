package com.doubleos.yd.EntityInterface;

public interface IEntityBall
{

    default void addList()
    {
        /*
        boolean checkSide = Minecraft.getMinecraft().isCallingFromMinecraftThread();
        EntityTickManager manager = EntityTickManager.Instance();
        ArrayList<Entity> entityList = manager.getEntityList();

        boolean check = false;

        if(checkSide)
        {
            for(int i = 0; i<entityList.size(); i++)
            {
                if (entityList.get(i) == this)
                {
                    check = true;
                }
            }
            if(!check)
            {
                entityList.add((Entity) this);
                System.out.println("@ " + entityList.size());
            }
        }

         */
    }
}
