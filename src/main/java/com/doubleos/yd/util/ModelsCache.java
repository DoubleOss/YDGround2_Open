package com.doubleos.yd.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

@SideOnly(Side.CLIENT)
public class ModelsCache implements ISelectiveResourceReloadListener {

    public static final ModelsCache INSTANCE = new ModelsCache();

    private ModelsCache() {
    }

    public static final IModelState DEFAULTMODELSTATE		= part -> java.util.Optional.empty();
    public static final VertexFormat DEFAULTVERTEXFORMAT		= DefaultVertexFormats.BLOCK;
    public static final Function<ResourceLocation, TextureAtlasSprite> DEFAULTTEXTUREGETTER	= texture -> Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(texture.toString());


    private final Map<ResourceLocation, IModel> modelCache	= new HashMap<>();
    private final Map<ResourceLocation, IBakedModel>	bakedCache	= new HashMap<>();

    public IModel getModel(final ResourceLocation location) {
        IModel model = this.modelCache.get(location);
        if (model == null) {
            try {
                model = ModelLoaderRegistry.getModel(location);
            } catch (final Exception e) {
                System.out.println("Error loading model " + location.toString());
                e.printStackTrace();
                model = ModelLoaderRegistry.getMissingModel();
            }
            this.modelCache.put(location, model);
        }
        return model;
    }

    public IBakedModel getBakedModel(final ResourceLocation location) throws Exception {
        return this.getBakedModel(location, DEFAULTMODELSTATE, DEFAULTVERTEXFORMAT, DEFAULTTEXTUREGETTER);
    }

    public IBakedModel getBakedModel(final ResourceLocation location, final IModelState state, final VertexFormat format, final Function<ResourceLocation, TextureAtlasSprite> textureGetter) throws Exception {
        IBakedModel bakedModel = this.bakedCache.get(location);
        if (bakedModel == null) {
            bakedModel = OBJLoader.INSTANCE.loadModel(location).bake(state, format, textureGetter);
            this.bakedCache.put(location, bakedModel);
        }
        return bakedModel;
    }

    @Override
    public void onResourceManagerReload(final IResourceManager resourceManager, final Predicate<IResourceType> resourcePredicate) {
        this.modelCache.clear();
        this.bakedCache.clear();
    }

}