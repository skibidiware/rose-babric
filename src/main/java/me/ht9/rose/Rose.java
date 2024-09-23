package me.ht9.rose;

import me.ht9.rose.event.bus.EventBus;
import me.ht9.rose.event.factory.Factory;
import me.ht9.rose.util.Globals;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Rose implements ClientModInitializer, Globals
{
	private static final Logger logger = LogManager.getLogger("rose");

	private static final EventBus bus = new EventBus();

	private static final ExecutorService asyncExecutor = Executors.newSingleThreadExecutor();
	
	@Override
	public void onInitializeClient()
	{
		long startTime = System.currentTimeMillis();
		logger.info("loading rose...");

		bus.register(Factory.instance());

		double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0D;
		logger.info("successfully loaded rose in {}s.", elapsedTime);
	}

	public static EventBus bus()
	{
		return bus;
	}

	public static Logger logger()
	{
		return logger;
	}
}