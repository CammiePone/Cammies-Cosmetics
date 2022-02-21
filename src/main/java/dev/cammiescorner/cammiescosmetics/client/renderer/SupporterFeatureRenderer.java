package dev.cammiescorner.cammiescosmetics.client.renderer;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.UUID;

public class SupporterFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
	public static final Set<UUID> SUPPORTERS = new HashSet<>();
	private static final String SUPPORTER_URL = "https://raw.githubusercontent.com/CammiePone/Cammies-Cosmetics/HEAD/supporters.properties";
	private static boolean init = false;

	public SupporterFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
		if(!init) {
			Thread loader = new SupporterListLoaderThread();
			loader.start();
			init = true;
		}
		else {

		}
	}

	/**
	 * This code is MoriyaShiine's, it's ARR I just have his permission to use it.
	 *
	 * DO NOT STEAL!!1!11!!1
	 *
	 * https://github.com/MoriyaShiine/bewitchment/blob/1deb5c6bd7273b88bb7160172ada5e4ef30b23fa/src/main/java/moriyashiine/bewitchment/client/renderer/ContributorHornsFeatureRenderer.java
	 */
	private static class SupporterListLoaderThread extends Thread {
		public SupporterListLoaderThread() {
			setName("Bewitchment Contributor List Loader Thread");
			setDaemon(true);
		}

		@Override
		public void run() {
			try(BufferedInputStream stream = IOUtils.buffer(new URL(SUPPORTER_URL).openStream())) {
				Properties properties = new Properties();
				properties.load(stream);

				synchronized(SUPPORTERS) {
					SUPPORTERS.clear();

					for(String key : properties.stringPropertyNames())
						SUPPORTERS.add(UUID.fromString(properties.getProperty(key)));
				}
			}
			catch(IOException e) {
				System.out.println("Failed to load supporter list. Supporter cosmetics will not be rendered.");
			}
		}
	}
}
