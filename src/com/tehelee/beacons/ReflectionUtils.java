package com.tehelee.beacons;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Set;

public class ReflectionUtils
{

	public static void setStaticFinalField(Field field, Object newValue) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {
		ReflectionUtils.setAccessible(Field.class.getDeclaredField("modifiers")).setInt(field, field.getModifiers() & ~Modifier.FINAL);
		ReflectionUtils.setAccessible(Field.class.getDeclaredField("root")).set(field, null);
		ReflectionUtils.setAccessible(Field.class.getDeclaredField("overrideFieldAccessor")).set(field, null);
		ReflectionUtils.setAccessible(field).set(null, newValue);
	}

	public static <T extends AccessibleObject> T setAccessible(T object) {
		object.setAccessible(true);
		return object;
	}

	public static Field getField(Class<?> clazz, String name) {
		do {
			for (Field field : clazz.getDeclaredFields()) {
				if (field.getName().equals(name)) {
					return setAccessible(field);
				}
			}
		} while ((clazz = clazz.getSuperclass()) != null);
		throw new RuntimeException("Can't find field "+name);
	}

	public static Method getMethod(Class<?> clazz, String name, int paramlength) {
		do {
			for (Method method : clazz.getDeclaredMethods()) {
				if (method.getName().equals(name) && (method.getParameterTypes().length == paramlength)) {
					return setAccessible(method);
				}
			}
		} while ((clazz = clazz.getSuperclass()) != null);
		throw new RuntimeException("Can't find method "+name+" with params length "+paramlength);
	}
	
	public static boolean hasBeenRan()
	{
		boolean isThreadAlive = false;
		
		Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
		
		for(Thread t: threadSet)
		{
			if(t.getName().equals("com-tehelee-beacons") && t.isAlive())
			{
				isThreadAlive = true;
				System.out.println(t.getName());
			}
		}
		
		if(isThreadAlive)
		{
			return true;
		}
		else
		{
			simpleThread t1 = new simpleThread();
			t1.setName("com-tehelee-beacons");
			t1.start();
			return false;
		}
	}

	public static class simpleThread extends Thread
	{
		public void run()
		{
			while(true)
			{
				try{Thread.sleep(5000000);}catch(Exception e){}
			}
		}
	}
}
