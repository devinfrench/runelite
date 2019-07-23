package net.runelite.client.automation;

import com.github.joonasvali.naturalmouse.api.MouseMotionFactory;
import com.github.joonasvali.naturalmouse.support.DefaultSpeedManager;
import net.runelite.api.Client;
import sun.awt.ComponentFactory;
import sun.java2d.Disposer;
import sun.java2d.DisposerRecord;

import javax.inject.Inject;
import java.awt.AWTException;
import java.awt.Canvas;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.peer.RobotPeer;
import java.util.Random;
import java.util.logging.Logger;

public class Automation extends Robot
{
	@Inject
	private Client client;

	private RobotPeer peer;
	private MouseMotionFactory mouseMotionFactory;

	public Automation() throws AWTException
	{
		if (!GraphicsEnvironment.isHeadless())
		{
			Toolkit toolkit = Toolkit.getDefaultToolkit();
			if (toolkit instanceof ComponentFactory)
			{
				GraphicsDevice device = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
				peer = ((ComponentFactory) toolkit).createRobot(this, device);
				RobotDisposer disposer = new RobotDisposer(peer);
				Disposer.addRecord(new Object(), disposer);
			}
			mouseMotionFactory = MouseMotionFactory.getDefault();
			DefaultSpeedManager speedManager = new DefaultSpeedManager();
			speedManager.setMouseMovementBaseTimeMs(50);
			mouseMotionFactory.setSpeedManager(speedManager);
		}
	}

	private class RobotDisposer implements DisposerRecord
	{
		private final RobotPeer peer;

		private RobotDisposer(RobotPeer peer)
		{
			this.peer = peer;
		}

		public void dispose()
		{
			if (peer != null)
			{
				peer.dispose();
			}
		}
	}

	@Override
	public synchronized void keyPress(int keycode)
	{
		peer.keyPress(keycode);
		delay(random(20, 60));
	}

	@Override
	public synchronized void keyRelease(int keycode)
	{
		peer.keyRelease(keycode);
		delay(random(20, 60));
	}

	@Override
	public synchronized void mouseWheel(int wheelAmt)
	{
		for (int i : new int[wheelAmt])
		{
			peer.mouseWheel(wheelAmt);
			delay(random(20, 60));
		}
	}

	@Override
	public synchronized void mousePress(int buttons)
	{
		peer.mousePress(InputEvent.getMaskForButton(buttons));
		delay(random(20, 60));
	}

	@Override
	public synchronized void mouseRelease(int buttons)
	{
		peer.mouseRelease(InputEvent.getMaskForButton(buttons));
		delay(random(20, 60));
	}

	public synchronized void click(int buttons)
	{
		peer.mousePress(InputEvent.getMaskForButton(buttons));
		delay(random(20, 60));
		peer.mouseRelease(InputEvent.getMaskForButton(buttons));
		delay(random(20, 60));
	}

	@Override
	public synchronized void mouseMove(int x, int y)
	{
		Canvas canvas = client.getCanvas();
		if (canvas == null)
		{
			return;
		}

		Point p = canvas.getLocationOnScreen();
		x = p.x + x;
		y = p.y + y;
		try
		{
			mouseMotionFactory.build(x, y).move();
		}
		catch (InterruptedException e)
		{
			peer.mouseMove(x, y);
		}
		delay(random(20, 60));
	}

	public synchronized void mouseMove(Point p)
	{
		mouseMove((int) p.getX(), (int) p.getY());
	}

	public synchronized Point getMousePosition()
	{
		Canvas canvas = client.getCanvas();
		if (canvas == null)
		{
			return new Point(-1, -1);
		}
		return canvas.getMousePosition();
	}

	public synchronized void click(int x, int y, int button)
	{
		mouseMove(x, y);
		click(button);
	}


	public synchronized void click(Point p, int button)
	{
		click(p.x, p.y, button);
	}

	public synchronized void click(Polygon poly, int button)
	{
		click(getCenterPoint(poly), button);
	}

	public synchronized void click(Rectangle rect, int button)
	{
		click(getCenterPoint(rect), button);
	}

	private int random(int min, int max)
	{
		Random r = new Random();
		return r.nextInt(max - min) + min;
	}

	private Point getCenterPoint(Polygon poly)
	{
		double x = 0;
		double y = 0;
		for (int i = 0; i < poly.npoints; i++)
		{
			x += poly.xpoints[i];
			y += poly.ypoints[i];
		}
		Point p;
		do
		{
			p = new Point((int) (x / poly.npoints) + random(-5, 5), (int) (y / poly.npoints) + random(-5, 5));
		}
		while (!poly.contains(p));
		return p;
	}

	private Point getCenterPoint(Rectangle rect)
	{
		double x = rect.getX() + rect.getWidth() / 2;
		double y = rect.getY() + rect.getHeight() / 2;
		Point p;
		do
		{
			p = new Point((int) (x + random(-5, 5)), (int) (y + random(-5, 5)));
		}
		while (!rect.contains(p));
		return p;
	}
}