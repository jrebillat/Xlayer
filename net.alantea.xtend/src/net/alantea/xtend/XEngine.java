package net.alantea.xtend;

import java.io.Reader;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class XEngine {
	
	private ScriptEngine nashornEngine;
	
	public XEngine()
	{
	    ScriptEngineManager scriptManager = new ScriptEngineManager();
	    nashornEngine = scriptManager.getEngineByName("nashorn");
	}

	public Object eval(String cmd) throws Xception {
		try {
			return nashornEngine.eval(cmd);
		} catch (ScriptException e) {
			throw new Xception("error evaluating script", e);
		}
	}

	public Object eval(Reader stream) throws Xception {
		try {
			return nashornEngine.eval(stream);
		} catch (ScriptException e) {
			throw new Xception("error evaluating script file", e);
		}
	}

	public void put(String name, Object object) {
		nashornEngine.put(name, object);
	}

	public Object get(String name) {
		return nashornEngine.get(name);
	}
}
